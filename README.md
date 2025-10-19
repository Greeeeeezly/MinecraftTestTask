# Тестовое задание

Fabric мод для Minecraft 1.21.8, который позволяет игроку отправлять произвольный текст с клиента на сервер, кодируя сообщение в Protobuf и сохраняя его в PostgreSQL через Hibernate.

## Возможности
- Клиентский экран (`M`) с полем ввода и кнопкой `Send`.
- Сериализация сообщения `Message{text}` в Protobuf 3 и отправка через Fabric Networking API.
- Сервер принимает payload, декодирует, валидирует и пишет в таблицу `messages` (PostgreSQL) с помощью Hibernate/JPA.
- Конфигурация подключения к БД через файл `config/modid-postgres.properties`.

## Требования
- JDK 21
- Gradle 8.14
- Minecraft 1.21.8 + Fabric Loader 0.17.3
- PostgreSQL 14+ (проверено с 17)

## Подготовка PostgreSQL
1. Создайте базу данных, например `messages`.
2. Выполните:
   ```sql
   CREATE TABLE messages (
       id BIGSERIAL PRIMARY KEY,
       uuid UUID NOT NULL,
       text VARCHAR(256) NOT NULL
   );
   ```
3. При первом запуске мода в папке `config/` появится файл `modid-postgres.properties`. Укажите реальные `jdbcUrl`, `username`, `password`.

## Сборка и запуск
```bash
./gradlew build           # сборка мода
./gradlew runServer       # dev-сервер (при первой попытке отключите online-mode в run/server/server.properties
./gradlew runClient       # dev-клиент
```

После подключения к серверу нажмите `M`, введите текст и нажмите `Send`. В логах сервера появится сообщение, а в таблице `messages` — новая запись.

## Архитектура
- `com.example.client.gui.MessageScreen` — экран ввода.
- `com.example.network` — регистрация каналов и обработка protobuf payload.
- `com.example.proto.MessageProtos` — сгенерированный класс из `src/main/proto/message.proto`.
- `com.example.service` — DTO (`PlayerMessage`) и сервис `MessageService`.
- `com.example.database` — конфигурация PostgreSQL, Hibernate `MessageRepository`, JPA-сущность `MessageEntity`.

