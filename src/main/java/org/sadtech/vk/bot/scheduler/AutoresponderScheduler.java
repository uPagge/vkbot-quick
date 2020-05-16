package org.sadtech.vk.bot.scheduler;

import lombok.RequiredArgsConstructor;
import org.sadtech.vk.bot.sdk.MessageAutoresponderVk;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutoresponderScheduler {

    private final MessageAutoresponderVk messageAutoresponderVk;

    @Scheduled(fixedRate = 1000)
    public void start() {
        messageAutoresponderVk.checkNewMessage();
    }

}
