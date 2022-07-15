package com.proglint.camundaquickstart;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
@EnableZeebeClient
@RestController
public class CamundaQuickstartApplication {

    private final ApplicationRepository applicationRepository;
    private final UnderwriterRepository underwriterRepository;

    private final ZeebeClient zeebeClient;

    @Autowired
    public CamundaQuickstartApplication(ApplicationRepository applicationRepository,
                                        UnderwriterRepository underwriterRepository, ZeebeClient zeebeClient) {
        this.applicationRepository = applicationRepository;
        this.underwriterRepository = underwriterRepository;;
        this.zeebeClient = zeebeClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(CamundaQuickstartApplication.class, args);
    }

    @PostMapping("/application")
    public void createApplication(@RequestBody CreateApplicationRequest createApplicationRequest) {
        Application application = new Application(createApplicationRequest.getCarType(),
                createApplicationRequest.getSumInsured());
        application = applicationRepository.save(application);

        Map<String, String> variables = new HashMap<>();
        variables.put("applicationId", application.getId().toString());
        variables.put("carType", application.getCarType());
        variables.put("sumInsured", application.getSumInsured().toString());
        this.zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("Process_1dpktni")
                .latestVersion()
                .variables(variables)
                .send()
                .join();
    }

    @ZeebeWorker(type = "CheckUnderwriterNeeded", autoComplete = true)
    public void checkUnderwriterNeeded(ActivatedJob job) {
        boolean underwriterNeeded = false;
        Application application = job.getVariablesAsType(Application.class);
        if (application.getSumInsured().compareTo(new BigDecimal(50_000)) > 0
                || application.getCarType().equals("electric")) {
            underwriterNeeded = true;
        }
        Map<String, String> variables = new HashMap<>();
        variables.put("underwriterNeeded", Boolean.toString(underwriterNeeded));
        this.zeebeClient.newSetVariablesCommand(job.getElementInstanceKey())
                .variables(variables)
                .send()
                .join();
    }

    @ZeebeWorker(type = "AssignToUnderwriter", autoComplete = true)
    public void assignToUnderwriter(ActivatedJob job) {
        Long applicationId = Long.parseLong(job.getVariablesAsMap().get("applicationId").toString());
        Optional<Application> applicationOptional = applicationRepository.findById(applicationId);
        if (!applicationOptional.isPresent()) {
            throw new IllegalStateException("Application not found");
        }
        Application application = applicationOptional.get();
        Optional<Underwriter> underwriterOptional = underwriterRepository.findById(1L);
        if (!underwriterOptional.isPresent()) {
            throw new IllegalStateException("Underwriter not found");
        }
        Underwriter underwriter = underwriterOptional.get();
        application.setUnderwriter(underwriter);
        applicationRepository.save(application);
    }
}
