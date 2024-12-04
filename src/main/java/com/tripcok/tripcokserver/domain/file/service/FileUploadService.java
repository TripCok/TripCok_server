package com.tripcok.tripcokserver.domain.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 파일 업로드
     *
     * @param file 업로드할 파일
     * @return 파일의 URL
     * @throws IOException
     */
    public String uploadFile(MultipartFile file) throws IOException {
        // 파일을 InputStream으로 변환
        InputStream inputStream = file.getInputStream();
        System.out.println(inputStream);

        // ObjectMetadata를 사용해 파일의 메타데이터 설정 (선택사항)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // 파일 업로드 요청 생성
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, file.getOriginalFilename(), inputStream, metadata);

        // 파일 S3에 업로드
        amazonS3.putObject(putObjectRequest);

        // 업로드된 파일의 URL 반환 (공개된 파일 URL)
        return amazonS3.getUrl(bucket, file.getOriginalFilename()).toString();
    }
}
