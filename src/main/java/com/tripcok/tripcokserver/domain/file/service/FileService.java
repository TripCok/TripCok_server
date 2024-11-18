package com.tripcok.tripcokserver.domain.file.service;

import com.tripcok.tripcokserver.domain.file.FileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
}
