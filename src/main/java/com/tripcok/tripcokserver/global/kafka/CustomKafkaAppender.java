package com.tripcok.tripcokserver.global.kafka;

import ch.qos.logback.core.AppenderBase;
import lombok.Setter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

public class CustomKafkaAppender<E> extends AppenderBase<E> {

    private KafkaProducer<String, String> producer;
    @Setter
    private String topic;
    @Setter
    private int maxRetries = 3; // 최대 재시도 횟수
    private Queue<String> retryQueue = new LinkedList<>(); // 임시 큐
    @Setter
    private String fallbackFilePath = "failed_logs.txt"; // 실패한 로그 파일 경로

    @Override
    public void start() {
        super.start();
        // KafkaProducer 설정
        Properties props = new Properties();
        props.put("bootstrap.servers", "172.18.0.1:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all");  // 멱등성 보장을 위해 'all'로 설정
        producer = new KafkaProducer<>(props);
    }

    @Override
    protected void append(E eventObject) {
        if (!isStarted()) {
            return;
        }

        String message = eventObject.toString(); // 로그 메시지 변환

        boolean success = sendWithRetry(message, maxRetries);

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