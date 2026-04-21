# Advanced Test Automation (Java) with Selenium
## Notification System / Система сповіщень - Sprint-01

[English version](#english-version) | [Українська версія](#українська-версія)

---

## English Version

### Project Overview
This project implements a multi-channel notification system (Email, SMS, Push) using Java. It demonstrates core Object-Oriented Programming (OOP) principles such as inheritance, polymorphism, encapsulation, and the Template Method design pattern.

### Tech Stack
- **Java 21**
- **Maven** (Build tool)
- **JUnit 5** (Testing)
- **Lombok** (Boilerplate reduction)

### Key Features

#### 1. Core Logic (`Notification` class)
- **Template Method Pattern**: The `send()` method defines the high-level workflow, while specific delivery logic is delegated to subclasses via `performSend()`.
- **Validation**: Strict constructor-level validation for recipient, message, and priority.
- **Priority System**: Notifications have priority levels (1-5). Higher values (4-5) are marked as high priority.
- **Sorting**: Implements `Comparable` to allow automatic sorting by priority in descending order.

#### 2. Notification Channels

| Type | Additional Fields | Validation Logic (`isDeliverable`) | Formatting |
| :--- | :--- | :--- | :--- |
| **Email** | `senderEmail`, `subject`, `hasAttachment` | Checks for `@` and `.` in email. | `Subject: {subject}\n{message}` |
| **SMS** | `phoneNumber`, `isFlash` | Starts with `+`, length 10-15 chars. | Truncates message to 160 chars. |
| **Push** | `deviceToken`, `iconUrl` | Token length > 10 and not blank. | Prepends 🔔 emoji. |

#### 3. Error Handling
- `InvalidNotificationException`: Thrown during object creation if parameters are invalid.
- `NotDeliverableException`: Thrown if the `send()` method is called on a notification that fails its `isDeliverable()` check.

### Getting Started

#### Prerequisites
- JDK 21 or higher
- Maven 3.8+

#### Build & Test
```bash
mvn clean compile
mvn test
```

---

## Українська версія

### Огляд проєкту
Цей проєкт реалізує багатоканальну систему сповіщень (Email, SMS, Push) на мові Java. Він демонструє ключові принципи об'єктно-орієнтованого програмування (ООП), такі як успадкування, поліморфізм, інкапсуляція та шаблон проектування "Шаблонний метод" (Template Method).

### Технологічний стек
- **Java 21**
- **Maven** (інструмент збірки)
- **JUnit 5** (тестування)
- **Lombok** (скорочення шаблонного коду)

### Ключові можливості

#### 1. Основна логіка (клас `Notification`)
- **Паттерн "Шаблонний метод"**: Метод `send()` визначає загальний алгоритм відправки, тоді як конкретна логіка доставки делегується підкласам через `performSend()`.
- **Валідація**: Сувора перевірка отримувача, повідомлення та пріоритету безпосередньо в конструкторі.
- **Система пріоритетів**: Сповіщення мають рівні від 1 до 5. Рівні 4 та 5 вважаються високим пріоритетом.
- **Сортування**: Реалізація інтерфейсу `Comparable` дозволяє автоматично сортувати сповіщення за пріоритетом (від найвищого до найнижчого).

#### 2. Канали сповіщень

| Тип | Додаткові поля | Логіка валідації (`isDeliverable`) | Форматування |
| :--- | :--- | :--- | :--- |
| **Email** | `senderEmail`, `subject`, `hasAttachment` | Перевірка наявності `@` та `.` в адресі. | `Subject: {subject}\n{message}` |
| **SMS** | `phoneNumber`, `isFlash` | Починається з `+`, довжина 10-15 символів. | Обрізає текст до 160 символів. |
| **Push** | `deviceToken`, `iconUrl` | Токен не пустий, довжина > 10. | Додає емодзі 🔔 до тексту. |

#### 3. Обробка помилок
- `InvalidNotificationException`: Виникає під час створення об'єкта, якщо параметри не відповідають вимогам.
- `NotDeliverableException`: Виникає при спробі відправити сповіщення, яке не пройшло перевірку `isDeliverable()`.

### Як почати роботу

#### Попередні вимоги
- JDK 21 або вище
- Maven 3.8+

#### Збірка та тестування
```bash
mvn clean compile
mvn test
```
