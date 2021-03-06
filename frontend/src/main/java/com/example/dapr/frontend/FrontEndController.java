package com.example.dapr.frontend;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.HttpExtension;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FrontEndController {

    private static String SERVICE_APP_ID = "backend";

    @GetMapping("/")
    public String root() {
        log.info("OK");
        return "OK";
    }

    @GetMapping("/test")
    public String test(@RequestParam(required = false) Optional<String> name) {
        log.info("/test");
        try (DaprClient client = (new DaprClientBuilder()).build()) {
            byte[] response = client.invokeMethod(SERVICE_APP_ID,
                "say",
                name.orElse("anon"),
                HttpExtension.POST,
                null,
                byte[].class)
                .block();

            return new String(response);
        } catch (Exception e) {
            log.error("catch exception", e);
            throw new RuntimeException(e);
        }
    }
}
