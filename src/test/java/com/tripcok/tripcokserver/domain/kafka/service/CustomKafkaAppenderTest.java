package com.tripcok.tripcokserver.domain.kafka.service;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class CustomKafkaAppenderTest {

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void SetUp(){
        //로그인하기
        //
    }

    @Test
    void testAppend_SuccessfulSend() throws Exception {

        //given

        //when
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(get("/member"))
                .andExpect(status().isOk());  // 상태 코드가 200 OK인지 확인

        //then
    }

}
