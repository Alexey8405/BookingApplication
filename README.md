# Booking Application

REST API для управления отелями, номерами, бронированиями и сбором статистики.

Проект построен на `Spring Boot` и использует несколько хранилищ и брокер сообщений:
- `PostgreSQL` для основной бизнес-логики (пользователи, отели, номера, брони)
- `MongoDB` для хранения событий статистики
- `Kafka` для доставки событий (`user-registered`, `room-booked`)

## Возможности

- Регистрация и управление пользователями (`USER`, `ADMIN`)
- CRUD для отелей и номеров
- Поиск отелей и номеров с фильтрами и пагинацией
- Бронирование номеров с валидацией дат и доступности
- Экспорт статистики в CSV (только для администратора)
- Базовая безопасность через `HTTP Basic` + роли

## Технологии

- `Java 17`
- `Spring Boot 3.2`
- `Spring Web`
- `Spring Data JPA` + `PostgreSQL`
- `Spring Data MongoDB`
- `Spring Security`
- `Spring for Apache Kafka`
- `MapStruct`, `Lombok`
- `Gradle Kotlin DSL`

## Структура данных

- Основные сущности в PostgreSQL: `User`, `Hotel`, `Room`, `Booking`
- Статистика в MongoDB: `StatisticsEvent`
- События в Kafka:
  - `user-registered`
  - `room-booked`

## Быстрый старт

### 1) Требования

- Установленные:
  - `Java 17`
  - `Docker` и `Docker Compose`

### 2) Поднять инфраструктуру

Из корня проекта:

```bash
  docker compose up -d
```

Поднимутся сервисы:
- PostgreSQL (`localhost:5432`)
- MongoDB (`localhost:27017`)
- Zookeeper (`localhost:2181`)
- Kafka (`localhost:9092`)

### 3) Запустить приложение

```bash
  ./gradlew bootRun
```

Приложение будет доступно на:
- `http://localhost:8080`

## Конфигурация

Основные настройки находятся в `src/main/resources/application.yml`:

- PostgreSQL:
  - URL: `jdbc:postgresql://localhost:5432/booking_db`
  - user/password: `booking_user` / `booking_password`
- MongoDB:
  - URI: `mongodb://localhost:27017/booking_stats`
- Kafka:
  - bootstrap servers: `localhost:9092`
- Порт приложения:
  - `8080`

> По умолчанию `spring.jpa.hibernate.ddl-auto: create-drop`, то есть схема пересоздается при старте и удаляется при остановке.

## Аутентификация и роли

API защищено через `HTTP Basic`.

- Без авторизации доступен только:
  - `POST /api/users/register`
- Роль `USER`:
  - чтение сущностей, поиск, создание брони, просмотр своих бронирований
- Роль `ADMIN`:
  - полный CRUD пользователей/отелей/номеров
  - просмотр всех бронирований
  - экспорт статистики

Пример заголовка Basic Auth:

```http
Authorization: Basic base64(username:password)
```

## Начальные данные

При старте применяется `src/main/resources/data.sql`:
- создаются тестовые пользователи, отели, номера и бронирования
- сбрасываются последовательности ID

Если нужно использовать собственных пользователей, зарегистрируйте их через API.

## Основные API-эндпоинты

### Пользователи (`/api/users`)

- `POST /register` - регистрация
- `GET /{id}` - получить пользователя по ID (`USER`, `ADMIN`)
- `GET /username/{username}` - получить по username (`ADMIN`)
- `PUT /{id}` - обновить пользователя (`ADMIN`)
- `DELETE /{id}` - удалить пользователя (`ADMIN`)

### Отели (`/api/hotels`)

- `GET /{id}` - получить отель
- `POST /` - создать отель (`ADMIN`)
- `PUT /{id}` - обновить отель (`ADMIN`)
- `DELETE /{id}` - удалить отель (`ADMIN`)
- `POST /{id}/rating` - изменить рейтинг (`USER`, `ADMIN`)
- `GET /` - список отелей (пагинация)
- `GET /search` - поиск отелей по фильтрам

### Номера (`/api/rooms`)

- `GET /{id}` - получить номер
- `POST /` - создать номер (`ADMIN`)
- `PUT /{id}` - обновить номер (`ADMIN`)
- `DELETE /{id}` - удалить номер (`ADMIN`)
- `GET /search` - поиск номеров по фильтрам

### Бронирования (`/api/bookings`)

- `POST /` - создать бронь (`USER`, `ADMIN`)
- `GET /` - все брони (`ADMIN`)
- `GET /my` - брони текущего пользователя (`USER`, `ADMIN`)

### Статистика (`/api/statistics`)

- `GET /export/csv` - выгрузка статистики в CSV (`ADMIN`)

## Примеры запросов

### Регистрация пользователя

```bash
  curl -X POST http://localhost:8080/api/users/register \
    -H "Content-Type: application/json" \
    -d '{
      "username": "newuser",
      "password": "secret123",
      "email": "newuser@example.com",
      "role": "USER"
    }'
```

### Поиск отелей по городу

```bash
  curl -u newuser:secret123 \
    "http://localhost:8080/api/hotels/search?city=Москва&page=0&size=10"
```

### Создание бронирования

```bash
  curl -X POST http://localhost:8080/api/bookings \
    -u newuser:secret123 \
    -H "Content-Type: application/json" \
    -d '{
      "checkInDate": "2026-07-10",
      "checkOutDate": "2026-07-15",
      "roomId": 1
    }'
```

### Экспорт статистики (ADMIN)

```bash
  curl -u admin:admin_password \
    -OJ "http://localhost:8080/api/statistics/export/csv"
```

> Укажите актуальные учетные данные администратора.

## Логи

Логи пишутся в директорию `logs/`.

## Тесты

```bash
  ./gradlew test
```

## Дальнейшие улучшения

- Добавить `springdoc-openapi` (Swagger UI) для автодокументации API
- Перейти с `HTTP Basic` на `JWT`/OAuth2 для production-сценария
- Ужесточить CORS и вынести секреты в переменные окружения/секрет-хранилище
- Разделить consumer group Kafka для независимой обработки событий
