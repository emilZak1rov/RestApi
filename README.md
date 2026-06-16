# REST API (GET/POST)

Работа с REST API с использование RestAssured, gson, TestNG

## Описание

Этот проект содержит тест для сайта https://jsonplaceholder.typicode.com/

## Как запустить проект

### Требования

- JDK 18
- Maven 4.0
- TestNG (latest)
- gson (latest)
- slf4j-api (2.0.9)
- logback-classic (1.4.11)
- lomback(RELEASE)
- rest-assured(5.5.0)

### Сборка проекта

1. Клонируйте репозиторий: https://github.com/tquality-education/e.zakirov.git
2. Перейдите на ветку REST_API
3. Установка зависимостей из файла pom.xml
4. Для запуска теста, запустите файл RestApiTest.java

## Описание ключевых компонентов

### 1. RestAssured (Взаимодействие с REST API)

### 2. Тестовый фреймворк

- Использование `testNG` для организации и запуска тестов

### 4. Конфигурирование

- Хранение параметров окружения в `settings.json` и `config.json`

### 5. Тестовые данные

- Хранение данных в 'restapi.json'

## Примечания

- Проект предназначен для учебных целей