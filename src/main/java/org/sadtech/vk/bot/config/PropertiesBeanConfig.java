package org.sadtech.vk.bot.config;

import org.sadtech.vk.bot.sdk.config.VkConfigGroup;
import org.sadtech.vk.bot.sdk.config.VkConfigService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
public class PropertiesBeanConfig {

    @Bean
    public VkConfigGroup vkConfigGroup(Environment environment) {
        return VkConfigGroup.builder()
                .groupToken(environment.getProperty("sadtech.vk.config.group.token"))
                .groupId(Integer.valueOf(Objects.requireNonNull(environment.getProperty("sadtech.vk.config.group.id"))))
                .build();
    }

    @Bean
    public VkConfigService vkConfigService(Environment environment) {
        return VkConfigService.builder()
                .serviceToken(environment.getProperty("sadtech.vk.config.service.token"))
                .appId(Integer.valueOf(Objects.requireNonNull(environment.getProperty("sadtech.vk.config.service.app-id"))))
                .build();
    }

}
