package org.sadtech.vk.bot.scheduler;

import lombok.RequiredArgsConstructor;
import org.sadtech.vk.bot.sdk.service.distribution.EventDistributor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventDistributorScheduler {

    private final EventDistributor eventDistributor;

    @Scheduled(fixedRate = 1000)
    public void run() {
        eventDistributor.run();
    }


}
