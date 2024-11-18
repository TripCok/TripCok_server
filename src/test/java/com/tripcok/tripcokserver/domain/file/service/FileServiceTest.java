package com.tripcok.tripcokserver.domain.file.service;

import com.tripcok.tripcokserver.domain.file.FileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FileServiceTest {

    @Autowired
    private FileService fileService;

    String savePath = System.getProperty("user.home") + "/test-files/";

    @Test
    @DisplayName("파일 저장 성공 테스트")
    void saveFiles_Success() {
        // 테스트 파일 생성
        MockMultipartFile file1 = new MockMultipartFile(
                "file",
                "test1.txt",
                "text/plain",
                "테스트 파일 1".getBytes()
        );

        MockMultipartFile file2 = new MockMultipartFile(
                "file",
                "test2.txt",
                "text/plain",
                "테스트 파일 2".getBytes()
        );

        System.out.println("Home Directory: " + savePath);


        // 파일 저장
        List<FileDto> fileDtoList = fileService.saveFiles(List.of(file1, file2), savePath);

        // 검증
        assertNotNull(fileDtoList);
        assertEquals(2, fileDtoList.size());

    }

    @Test
    @DisplayName("빈 파일 무시 테스트")
    void saveFiles_EmptyFileIgnored() {
        // 테스트 파일 생성
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.txt",
                "text/plain",
                new byte[0]
        );

        // 파일 저장
        List<FileDto> fileDtoList = fileService.saveFiles(List.of(emptyFile), savePath);

        // 검증
        assertNotNull(fileDtoList);
        assertEquals(0, fileDtoList.size());

        // 저장 디렉토리 삭제
        new File(savePath).delete();
    }

    @Test
    @DisplayName("저장 경로가 존재하지 않을 때 디렉토리 생성 테스트")
    void saveFiles_DirectoryCreation() {
        // 테스트 파일 생성
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Directory creation test".getBytes()
        );

        // 파일 저장
        List<FileDto> fileDtoList = fileService.saveFiles(List.of(file), savePath);

        // 검증
        assertTrue(new File(savePath).exists(), "저장 디렉토리가 생성되지 않았습니다.");

        // 저장된 파일 삭제 (테스트 환경 정리)
        for (FileDto fileDto : fileDtoList) {
            new File(fileDto.getPath()).delete();
        }

        // 저장 디렉토리 삭제
        new File(savePath).delete();
    }
}
