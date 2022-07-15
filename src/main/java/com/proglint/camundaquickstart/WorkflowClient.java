package com.proglint.camundaquickstart;

import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WorkflowClient {
    @Bean
    public ZeebeClient zeebeClient() {
        return ZeebeClient.newClientBuilder().gatewayAddress("localhost:26500").usePlaintext().build();
    }
}
