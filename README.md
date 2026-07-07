# Онлайн магази

REST API интернет-магазина, разработанное на **Java** с использованием **Spring Boot**. Проект реализует аутентификацию пользователей, управление товарами, категориями и заказами, а также предоставляет REST API для взаимодействия с клиентскими приложениями.

## Технологии

* Java
* Spring Boot
* Spring Security
* Spring Data JPA (Hibernate)
* PostgreSQL
* Docker
* Gradle
* REST API
* Swagger (OpenAPI)
* Postman

## Архитектура проекта

Проект построен с использованием многослойной архитектуры.

```text
Client
   │
REST Controller
   │
Service
   │
Repository
   │
PostgreSQL
```

Основные слои:

* **Controller** — обработка HTTP-запросов;
* **Service** — бизнес-логика;
* **Repository** — взаимодействие с базой данных;
* **Entity** — модели базы данных;
* **DTO** — объекты передачи данных между клиентом и сервером.

## Реализованный функционал

* регистрация пользователей;
* аутентификация и авторизация;
* разграничение прав доступа;
* работа с товарами;
* работа с категориями;
* создание и управление заказами;
* обработка ошибок через Global Exception Handler;
* безопасное хранение паролей с использованием BCrypt.

## Запуск проекта

### 1. Клонировать репозиторий

```bash
git clone https://github.com/USERNAME/onlineStore.git
```

### 2. Запустить PostgreSQL

```bash
docker compose up -d
```

3. Запустите класс `OnlineStoreApplication`.

### Запуск через Gradle

```bash
./gradlew bootRun
```

или через IntelliJ IDEA.

## Документация API

После запуска приложения Swagger доступен по адресу:

http://localhost:8080/swagger-ui/index.html#/

Через Swagger можно:

* просматривать все REST-эндпоинты;
* тестировать запросы;
* смотреть модели запросов и ответов.

## Postman

Для удобства тестирования проекта в репозитории находится коллекция Postman.

Импортируйте файл коллекции в Postman и выполните запросы без дополнительной настройки.

## Используемые технологии

| Технология      | Назначение                   |
| --------------- | ---------------------------- |
| Spring Boot     | создание REST API            |
| Spring Security | аутентификация и авторизация |
| Spring Data JPA | работа с базой данных        |
| Hibernate       | ORM                          |
| PostgreSQL      | база данных                  |
| Docker          | контейнеризация PostgreSQL   |
| Gradle          | система сборки               |
| Swagger/OpenAPI | документация API             |
| Postman         | тестирование API             |

## Структура проекта

```text
src
 ├── controller
 ├── service
 ├── repository
 ├── entity
 ├── dto
 ├── config
 ├── exception
 └── security
```

## Планы по развитию

* Добавить работу с кэшем через Redis;
* Сделать нагрузочное тестирование локально и удаленно;

## Автор

Разработчик: **[Илья]**

GitHub: https://github.com/samarityanin-hq
