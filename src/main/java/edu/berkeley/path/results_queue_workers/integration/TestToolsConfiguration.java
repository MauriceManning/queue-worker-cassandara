package edu.berkeley.path.results_queue_workers.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.CountDownLatch;

/**
 *
 */

@Configuration
@Profile("dev")
public class TestToolsConfiguration {

    private int NUMBER_OF_LINKS = 10;

    @Bean
    public CountDownLatch latch(){
        return new CountDownLatch(NUMBER_OF_LINKS);
    }

}
