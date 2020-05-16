# VK Bot Quick
Модуль позволяет в пару файлов запустить своего полноценного ВК бота. Сам проект использует 

## Dependency
```
<dependency>
    <groupId>org.sadtech.vk.bot</groupId>
    <artifactId>vkbot-quick</artifactId>
    <version>1.0.0-RELEASE</version>
</dependency>
```

## Quick Start

- Создайте новый Spring Boot проект
- Добавьте зависимость этого проекта
- Добавьте следующую анотацию в класс конфигурации Spring
```
@ComponentScan(basePackages = {"org.sadtech.vk.bot", "com.example.yourproject"})
```
- Добавьте в application.yaml:

```
sadtech:
  vkbot:
    config:
      group:
        id: идентификатор_вашей_группы
        token: токен_для_взаимодействия_с_сообщениями
      service:
        app-id: идентификатор_вашего_приложения
        token: токен_для_сервисного_доступа
```

- Создайте новый класс конфигурации и приступайте к настройкам бинов юнитов. [Как настраивать?]()

Пример

```
@Bean
public AnswerText hello(AnswerValidity map) {
    return AnswerText.builder()
        .boxAnswer(
            BoxAnswer.builder()
                .message("Привет, %firstname%! Чем могу помочь")
                .stickerId(21)
                .build()
        )
        .build();
}
```

- Запустите проект

## Подробное описание работы проекта

Весь проект состоит из нескольких модулей, для понимания работы всей системы необходимо прочитать документацию каждого 
модуля в следующем порядке.

- [Autoresponder](https://github.com/uPagge/autoresponder) - отвечает за базовую логику выбора юнита.
- [Social-Core](https://github.com/uPagge/social-core) - содержит абстрактные классы сущностей социальных сетей и месенджеров (Сообщения, комментарии и проч.)
- [Social-Bot](https://github.com/uPagge/social-bot) - содержит абстрактную реализацию бота, которая не привязана ни к какой социально сети.
- [VKBOT-SDK](https://github.com/uPagge/vk-bot) - содержит реализацию бота для ВКонтакте, которая позволяет более гибкие настройки, например заменять
слой репозитория и прочее.