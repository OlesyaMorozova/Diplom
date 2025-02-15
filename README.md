# Бизнес-часть
Приложение — это веб-сервис, который предлагает купить тур по определённой цене двумя способами:
1. Обычная оплата по дебетовой карте.
2. Уникальная технология: выдача кредита по данным банковской карты.

# Программное обеспечение
1. Docker Desktop
2. IntelliJ IDEA Community Edition 2024.1.4
3. Git
4. Google Chrome

# Запуск автотестов
1. Клонировать репозиторий командой в терминале git clone git@github.com:OlesyaMorozova/Diplom.git
2. Запустить Docker Desktop
3. Открыть проект в IntelliJ IDEA Community Edition 2024.1.4
4. Запустить три контейнера (MySQL, PostgreSQL, эмулятор банковских сервисов) с помощью команды в терминале docker-compose up
5. Запустить приложение с помощью команды в терминале java -jar aqa-shop.jar -Dspring.datasource.url=jdbc:postgresql://localhost:5432/app (при подключении SUT к PostgreSQL),  java -jar aqa-shop.jar -Dspring.datasource.url=jdbc:mysql://localhost:3306/app (при подключение SUT к MySQL)
   на порту 8080
6. Запустить автотесты с помощью команды в терминале .\gradlew clean test -DdbUrl=jdbc:postgresql://localhost:5432/app (при подключении SUT к PostgreSQL), .\gradlew clean test -DdbUrl=jdbc:mysql://localhost:3306/app (при подключение SUT к MySQL)
7. Создать отчёт Allure с помощью команды в терминале ./gradlew allureServe (отчет автоматически откроется в браузере)
8. Остановить приложение командой CTRL + C
9. Остановить контейнеры командой docker-compose down