# Customer Inquiry Tracker

Учебный REST API для учёта обращений клиентов в техподдержку. Поддерживает создание, просмотр, редактирование и удаление обращений, смену статусов, фильтрацию и пагинацию.

## Стек

Kotlin, Java 21, Spring Boot, Spring MVC, Spring Data JPA / Hibernate, PostgreSQL 17, Flyway, Gradle Kotlin DSL, Docker Compose и springdoc OpenAPI. Тесты написаны на JUnit 5 с Mockito.

## Запуск

Понадобятся JDK 21, Docker с поддержкой Compose и доступ к интернету для первой загрузки зависимостей и образа PostgreSQL.

1. Запустите Docker Desktop и дождитесь готовности движка.
2. В корне проекта выполните команду.

```bash
./gradlew bootRun
```

Spring Boot использует `compose.yaml` для запуска PostgreSQL и получения параметров подключения. Docker назначает свободный порт хоста для порта контейнера `5432`. Данные хранятся в именованном томе `pgdata`.

Приложение доступно по адресу `http://localhost:8080`. Для остановки нажмите `Ctrl+C` в терминале с `bootRun`.

Проверка состояния приложения выполняется отдельным запросом.

```bash
curl -i http://localhost:8080/actuator/health
```

## Документация API

- [Swagger UI](http://localhost:8080/swagger-ui.html) позволяет просмотреть операции и отправить запросы.
- [OpenAPI JSON](http://localhost:8080/v3/api-docs) содержит машиночитаемое описание API.

## Эндпоинты

Базовый путь — `/api/v1/inquiries`. Тела запросов и ответов передаются в JSON.

| Метод | Путь | Назначение | Успешный статус |
|---|---|---|---|
| POST | `/api/v1/inquiries` | Создать обращение | 201 и заголовок `Location` |
| GET | `/api/v1/inquiries` | Получить страницу обращений | 200 |
| GET | `/api/v1/inquiries/{id}` | Получить обращение по ID | 200 |
| PUT | `/api/v1/inquiries/{id}` | Обновить редактируемые поля | 200 |
| PATCH | `/api/v1/inquiries/{id}/status` | Сменить статус | 200 |
| DELETE | `/api/v1/inquiries/{id}` | Удалить обращение | 204 без тела |

### Создание обращения

```bash
curl -i http://localhost:8080/api/v1/inquiries \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "Не приходит письмо",
    "customerName": "Анна",
    "customerEmail": "anna@example.com",
    "description": "Проверила папку Спам",
    "priority": "HIGH"
  }'
```

`title`, `customerName` и `customerEmail` обязательны. При отсутствии `description` используется `null`, при отсутствии `priority` — `MEDIUM`. Начальный статус — `NEW`. В ответе возвращаются все поля обращения, включая ID и даты. Заголовок `Location` содержит путь к созданному обращению.

Текущая валидация создания ограничивает заголовок длиной 3–200 символов, имя клиента — максимум 100 символов, email — 3–254 символа с проверкой формата, описание — 5000 символами. Заголовок, имя и email должны содержать непробельные символы.

### Обновление и смена статуса

Для `PUT` передаются `title`, `customerName`, `customerEmail`, `description` и `priority`. Значение `description: null` очищает описание. При обновлении имя клиента ограничено 100 символами и должно содержать непробельные символы. Статус изменяется отдельным `PATCH`.

```json
{
  "status": "IN_PROGRESS"
}
```

Разрешены переходы `NEW → IN_PROGRESS`, `IN_PROGRESS → RESOLVED`, `RESOLVED → CLOSED` и переоткрытие `RESOLVED → IN_PROGRESS`. Из `CLOSED` переходов нет. Запрос текущего статуса повторно также отклоняется с `409`.

### Фильтры, страницы и сортировка

| Параметр | Назначение | Значение по умолчанию |
|---|---|---|
| `status` | Фильтр по `NEW`, `IN_PROGRESS`, `RESOLVED`, `CLOSED` | Любой статус |
| `priority` | Фильтр по `LOW`, `MEDIUM`, `HIGH` | Любой приоритет |
| `page` | Номер страницы с нуля | 0 |
| `size` | Размер страницы, максимум 100 | 20 |
| `sort` | Поле и направление сортировки | `createdAt,desc` |

Оба фильтра применяются одновременно, если переданы вместе. Размер свыше 100 ограничивается до 100.

```bash
curl -i 'http://localhost:8080/api/v1/inquiries?status=NEW&priority=HIGH&page=0&size=10&sort=createdAt,desc'
```

Ответ содержит `content` со списком обращений, `page`, `size`, `totalElements` и `totalPages`. Общее количество учитывает фильтры. Страница за пределами результата возвращает пустой `content`.

## Ошибки

Ошибки возвращаются в формате `ProblemDetail` с типом содержимого `application/problem+json`.

| Статус | Причина |
|---|---|
| 400 | Нарушение ограничений DTO, некорректные значения параметров или JSON |
| 404 | Обращение не найдено |
| 405 | HTTP-метод не поддерживается для пути |
| 409 | Запрещённый переход статуса |
| 500 | Непредвиденная ошибка, подробности записываются в серверный лог |

При ошибках Bean Validation ответ дополнительно содержит массив `errors` с полями `field` и `message`. Текст сообщений может зависеть от локали.

## Схема данных

Таблица `inquiries` хранит обращения.

| Колонка | Тип PostgreSQL | Ограничения |
|---|---|---|
| `id` | BIGINT | PRIMARY KEY, генерация через IDENTITY |
| `title` | VARCHAR(200) | NOT NULL |
| `description` | VARCHAR(5000) | Допускает NULL |
| `status` | VARCHAR(100) | NOT NULL, DEFAULT `NEW`, CHECK допустимых статусов |
| `priority` | VARCHAR(100) | NOT NULL, DEFAULT `MEDIUM`, CHECK допустимых приоритетов |
| `customer_name` | VARCHAR(100) | NOT NULL |
| `customer_email` | VARCHAR(254) | NOT NULL |
| `created_at` | TIMESTAMPTZ | NOT NULL, DEFAULT CURRENT_TIMESTAMP |
| `updated_at` | TIMESTAMPTZ | NOT NULL, DEFAULT CURRENT_TIMESTAMP |

Миграции расположены в `src/main/resources/db/migration`. `V1` сохранена как пустая применённая миграция, `V2` создаёт таблицу. Flyway ведёт историю в `flyway_schema_history`. Hibernate проверяет схему через `ddl-auto: validate`. В приложении даты заполняются Hibernate через `@CreationTimestamp` и `@UpdateTimestamp`.

## Структура

```text
src/main/kotlin/com/rakhman/customerinquirytracker/
├── CustomerInquiryTrackerApplication.kt
├── inquiry/
│   ├── Inquiry.kt, InquiryStatus.kt, InquiryPriority.kt
│   ├── InquiryController.kt
│   ├── InquiryService.kt
│   ├── InquiryRepository.kt
│   ├── InquiryMapper.kt
│   ├── InquiryNotFoundException.kt
│   ├── InvalidStatusTransitionException.kt
│   └── dto/
└── common/
    ├── config/OpenApiConfig.kt
    └── error/GlobalExceptionHandler.kt

src/main/resources/
├── application.yaml
└── db/migration/

src/test/kotlin/com/rakhman/customerinquirytracker/
├── CustomerInquiryTrackerApplicationTests.kt
└── inquiry/InquiryServiceTest.kt
```

Контроллер принимает HTTP и формирует ответы, сервис выполняет бизнес-правила в транзакциях, репозиторий обращается к базе. DTO задают формат обмена, маппер преобразует сущность в DTO, общий обработчик формирует ответы об ошибках. `open-in-view` отключён.

## Тесты

Для полного набора требуется запущенный Docker. Тест контекста использует PostgreSQL из того же `compose.yaml`, что и локальный запуск приложения.

```bash
./gradlew test
```

Три unit-теста сервиса проверяют `NEW → IN_PROGRESS`, запрет `NEW → CLOSED` с сохранением прежнего статуса и переоткрытие `RESOLVED → IN_PROGRESS`. Они используют mock репозитория и запускаются отдельно без PostgreSQL.

```bash
./gradlew test --tests '*InquiryServiceTest'
```

`contextLoads()` проверяет запуск Spring-контекста с подключением к PostgreSQL, миграциями и JPA.

```bash
./gradlew test --tests '*CustomerInquiryTrackerApplicationTests'
```

HTML-отчёт после запуска находится в `build/reports/tests/test/index.html`.
