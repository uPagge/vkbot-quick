package org.sadtech.vk.bot.scheduler;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sadtech.vk.bot.sdk.service.listener.EventListenerVk;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventListenerScheduler {

    private final EventListenerVk eventListenerVk;

    @Scheduled(fixedRate = 1000)
    public void start() {
        try {
            eventListenerVk.listen();
        } catch (ClientException | ApiException e) {
            log.error(e.getMessage());
        }
    }

}

