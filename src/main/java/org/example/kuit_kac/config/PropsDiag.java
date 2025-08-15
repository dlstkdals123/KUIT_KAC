package org.example.kuit_kac.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class PropsDiag {
    private final Environment env;
    private final AuthLoginSuccessProperties props;

    @EventListener(ApplicationReadyEvent.class)
    void ready() {
        log.info("Active profiles={}", Arrays.toString(env.getActiveProfiles()));
        log.info("login.mode={}, deepLink={}", props.getMode(), props.getDeepLink());
    }
}

