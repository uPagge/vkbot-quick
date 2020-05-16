package org.sadtech.vk.bot.config;

import org.sadtech.autoresponder.repository.UnitPointerRepository;
import org.sadtech.autoresponder.repository.UnitPointerRepositoryMap;
import org.sadtech.social.bot.domain.unit.MainUnit;
import org.sadtech.social.core.repository.impl.local.MailRepositoryList;
import org.sadtech.social.core.service.MailService;
import org.sadtech.social.core.service.impl.MailServiceImpl;
import org.sadtech.social.core.service.sender.Sending;
import org.sadtech.vk.bot.sdk.MessageAutoresponderVk;
import org.sadtech.vk.bot.sdk.config.VkConfig;
import org.sadtech.vk.bot.sdk.config.VkConfigGroup;
import org.sadtech.vk.bot.sdk.config.VkConfigService;
import org.sadtech.vk.bot.sdk.config.VkConnect;
import org.sadtech.vk.bot.sdk.repository.impl.RawEventRepositorySet;
import org.sadtech.vk.bot.sdk.sender.MailSenderVk;
import org.sadtech.vk.bot.sdk.service.RawEventService;
import org.sadtech.vk.bot.sdk.service.distribution.EventDistributor;
import org.sadtech.vk.bot.sdk.service.distribution.subscriber.MailSubscriber;
import org.sadtech.vk.bot.sdk.service.impl.RawEventServiceImpl;
import org.sadtech.vk.bot.sdk.service.listener.EventListenerVk;
import org.sadtech.vk.bot.sdk.utils.VkApi;
import org.sadtech.vk.bot.utils.UnitUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class BeanConfig {

    @Bean
    public ConversionService conversionService(Converter... converters) {
        final DefaultConversionService defaultConversionService = new DefaultConversionService();
        Arrays.stream(converters).forEach(defaultConversionService::addConverter);
        return defaultConversionService;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(3);
        return taskScheduler;
    }

    @Bean
    public VkConfig vkConfig(
            VkConfigGroup vkConfigGroup,
            VkConfigService vkConfigService
    ) {
        return VkConfig.builder()
                .configGroup(vkConfigGroup)
                .configService(vkConfigService)
                .build();
    }

    @Bean
    public VkConnect vkConnect(VkConfig vkConfig) {
        return new VkConnect(vkConfig);
    }

    @Bean(name = "menuUnits")
    public Set<MainUnit> startUnit(ListableBeanFactory listableBeanFactory) {
        final Collection<MainUnit> units = listableBeanFactory.getBeansOfType(MainUnit.class).entrySet().stream()
                .filter(stringMainUnitEntry -> !stringMainUnitEntry.getKey().equalsIgnoreCase("defaultunit"))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
        final Set<String> nextUnitId = units.stream().flatMap(unit -> UnitUtils.nextUnits(unit).stream())
                .distinct()
                .map(MainUnit::getUuid)
                .collect(Collectors.toSet());
        return units.stream()
                .filter(mainUnit -> !nextUnitId.contains(mainUnit.getUuid()))
                .collect(Collectors.toSet());
    }

    @Bean
    public MessageAutoresponderVk messageAutoresponderVk(
            @Qualifier("menuUnits") Set<MainUnit> startUnit,
            Sending sending,
            MailService mailService,
            UnitPointerRepository unitPointerRepository,
            MainUnit defaultUnit
    ) {
        final MessageAutoresponderVk messageAutoresponderVk = new MessageAutoresponderVk(
                startUnit,
                sending,
                mailService,
                unitPointerRepository
        );
        messageAutoresponderVk.setDefaultUnit(defaultUnit);
        return messageAutoresponderVk;
    }

    @Bean
    public UnitPointerRepository unitPointerRepository() {
        return new UnitPointerRepositoryMap();
    }

    @Bean
    public Sending sending(VkConnect vkConnect, ConversionService conversionService) {
        final MailSenderVk mailSenderVk = new MailSenderVk(vkConnect);
        mailSenderVk.setConversionService(conversionService);
        return mailSenderVk;
    }

    @Bean
    public MailService mailService() {
        return new MailServiceImpl(new MailRepositoryList());
    }

    @Bean
    public RawEventService rawEventService() {
        return new RawEventServiceImpl(new RawEventRepositorySet());
    }

    @Bean
    public EventListenerVk eventListenerVk(
            VkConnect vkConnect,
            RawEventService rawEventService
    ) {
        return new EventListenerVk(vkConnect, rawEventService);
    }

    @Bean
    public EventDistributor eventDistributor(
            RawEventService rawEventService,
            MailService mailService,
            ConversionService conversionService
    ) {
        final MailSubscriber mailSubscriber = new MailSubscriber(mailService, conversionService);
        return new EventDistributor(rawEventService, Collections.singleton(mailSubscriber));
    }

    @Bean
    public VkApi vkApi(
            VkConnect vkConnect
    ) {
        return new VkApi(vkConnect);
    }

}
