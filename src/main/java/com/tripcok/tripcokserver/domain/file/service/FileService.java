package com.tripcok.tripcokserver.domain.file.service;

import com.tripcok.tripcokserver.domain.file.FileDto;
import lombok.extern.slf4j.Slf4j;
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
public class FileService {

    /* 파일 저장 */
    public List<FileDto> saveFiles(List<MultipartFile> files, String savePath) {
        List<FileDto> fileDtoList = new ArrayList<>();

        // 저장 경로가 존재하지 않으면 디렉토리 생성
        File directory = new File(savePath);
        if (!directory.exists()) {
            directory.mkdirs();
            log.info(savePath + " <- 폴더 생성");
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            try {
                String originalFilename = file.getOriginalFilename();
                String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;

                String filePath = savePath + File.separator + uniqueFilename;

                file.transferTo(new File(filePath));

                // 저장된 파일 경로를 리스트에 추가
                FileDto fileDto = new FileDto();
                fileDto.setName(originalFilename);
                fileDto.setPath(filePath);
                fileDtoList.add(fileDto);

            } catch (IOException e) {
                throw new RuntimeException("파일 저장 중 오류 발생: " + file.getOriginalFilename(), e);
            }
        }

        return fileDtoList;
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
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);

        if (file.exists()) {
            if (file.delete()) {
                log.info("파일이 성공적으로 삭제되었습니다: " + filePath);
                return true;
            } else {
                log.error("파일 삭제에 실패했습니다: " + filePath);
                return false;
            }
        } else {
            log.warn("삭제하려는 파일이 존재하지 않습니다: " + filePath);
            return false;
        }
    }
}

