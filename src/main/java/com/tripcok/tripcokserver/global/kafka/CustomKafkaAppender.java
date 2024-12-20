package com.tripcok.tripcokserver.global.kafka;

import ch.qos.logback.core.AppenderBase;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

@Component
@Slf4j
public class CustomKafkaAppender<E> extends AppenderBase<E> {

    private KafkaProducer<String, String> producer;

    @Setter
    private int maxRetries = 3; // 최대 재시도 횟수
    private Queue<String> retryQueue = new LinkedList<>();

    @Setter
    private String fallbackFilePath = "./failed_logs.txt";

    @Setter
    private String kafkaBootstrapServers;

    @Setter
    private String topic;

    @Override
    public void start() {
        super.start();

        // KafkaProducer 설정
        if (kafkaBootstrapServers == null || kafkaBootstrapServers.isEmpty()) {
            addError("Kafka bootstrap servers are not configured.");
            return;
        }
        log.info("Kafka bootstrap address: {}", kafkaBootstrapServers);

        // KafkaProducer 설정
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaBootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //멱등성 보장
        props.put("acks", "all");  // 모든 ISR 복제본이 수신해야 성공으로 간주
        props.put("enable.idempotence", "true");  // 중복 방지를 위한 멱등성 설정

        // 배치 설정
        props.put("batch.size", 16384);  // 배치 크기 설정
        props.put("linger.ms", 5);  // 배치 대기 시간 설정

        // 타임아웃 설정
        props.put("request.timeout.ms", 500);  // 요청 타임아웃 (밀리초)
        props.put("delivery.timeout.ms", 1000);  // 배달 타임아웃 (밀리초)

        //메타데이터 갱신
        props.put("metadata.max.age.ms", "10000"); // 메타데이터를 1초마다 갱신

        producer = new KafkaProducer<>(props);
    }

    @Override
    protected void append(E eventObject) {
        if (!isStarted()) {
            return;
        }

        String message = eventObject.toString(); // 로그 메시지 변환

        message = message.replaceAll("^\\[\\w+\\]\\s*", ""); // [INFO], [ERROR] 등 로그 레벨 제거

        System.out.println("CustomAppender invoked with event: " + message);

        boolean success = sendWithRetry(message, maxRetries);

        System.out.println("***************************" + success);

        // 전송 실패 시 임시 큐에 저장
        if (!success) {
            retryQueue.offer(message);
        }
    }

    private boolean sendWithRetry(String message, int retries) {
        for (int i = 0; i < retries; i++) {
            try {
                producer.send(new ProducerRecord<>(topic, message)).get(); // 동기 전송
                return true; // 전송 성공
            } catch (Exception e) {
                System.err.println("Retry " + (i + 1) + " failed: " + e.getMessage());
            }
        }
        return false; // 전송 실패
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void processRetryQueue() {
        while (!retryQueue.isEmpty()) {
            String message = retryQueue.poll();
            boolean success = sendWithRetry(message, maxRetries);
            if (!success) {
                writeToFallbackFile(message); // 실패 시 파일에 기록
            }
        }
    }

    private void writeToFallbackFile(String message) {
        //현재 날짜 가지고 오는 방법
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        fallbackFilePath = "./failed_logs/" + currentDateTime; // 실패한 로그 파일 경로
        try (FileWriter writer = new FileWriter(fallbackFilePath, true)) {
            writer.write(message + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("Failed to write to fallback file: " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        super.stop();
        if (producer != null) {
            producer.close();
        }
    }

}
