package com.tripcok.tripcokserver.domain.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tripcok.tripcokserver.domain.file.FileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 파일 업로드
    public List<FileDto> uploadFile(List<MultipartFile> files, String folder) {

        /* Path List */
        List<FileDto> fileDtos = new ArrayList<>();

        for (MultipartFile multipartFile : files) {
            String originalFilename = multipartFile.getOriginalFilename();
            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
            String key = folder + "/" + uniqueFilename;

            try {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(multipartFile.getSize()); // 파일 크기를 설정

                amazonS3.putObject(new PutObjectRequest(bucketName, key, multipartFile.getInputStream(), metadata));


                String path = amazonS3.getUrl(bucketName, key).toString();

                fileDtos.add(new FileDto(uniqueFilename, path));
            } catch (IOException e) {
                throw new RuntimeException("S3에 파일 업로드 중 오류 발생: " + multipartFile.getOriginalFilename(), e);
            }

        }

        return fileDtos;

    }

    // 파일 삭제
    public boolean deleteFile(String fileFullPath) {
        try {
            String key = fileFullPath.replace(amazonS3.getUrl(bucketName, "").toString(), "");
            System.out.println(key);
            amazonS3.deleteObject(bucketName, key);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /* 이미지 파일 로드 */
    public Resource loadImageAsResource(String filePath) {
        Path path = Paths.get(filePath);

        try {
            // Normalize the path to prevent path traversal issues
            Path normalizedPath = path.normalize();

            // Convert the path to a Resource
            Resource resource = new UrlResource(normalizedPath.toUri());

            // Check if the resource exists and is readable
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("이미지 파일을 찾을 수 없거나 읽을 수 없습니다: " + path);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("파일 URL이 유효하지 않습니다: " + path, e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /* 파일 삭제 */
//    public boolean deleteFile(String filePath) {
//        File file = new File(filePath);
//
//        if (file.exists()) {
//            if (file.delete()) {
//                log.info("파일이 성공적으로 삭제되었습니다: " + filePath);
//                return true;
//            } else {
//                log.error("파일 삭제에 실패했습니다: " + filePath);
//                return false;
//            }
//        } else {
//            log.warn("삭제하려는 파일이 존재하지 않습니다: " + filePath);
//            return false;
//        }
//    }
}

