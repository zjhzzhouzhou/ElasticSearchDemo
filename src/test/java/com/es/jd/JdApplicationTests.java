package com.es.jd;

import com.es.jd.repository.EsUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JdApplicationTests {

    @Autowired
    private EsUserRepository esUserRepository;

    @Test
    void contextLoads() {
        esUserRepository.deleteAll();
    }

}
