# 🏨 Hotel Room Management System

A Java console application for managing hotel rooms using OOP principles and full CRUD functionality.

## 📁 Project Structure

```
hotel-management/
├── src/
│   ├── Main.java                      ← Entry point, menu loop
│   ├── model/
│   │   ├── HotelRoom.java             ← Primary entity (CRUD target)
│   │   ├── Customer.java              ← Symbolic entity
│   │   └── Booking.java               ← Symbolic entity
│   ├── service/
│   │   └── HotelRoomService.java      ← All CRUD business logic
│   └── util/
│       ├── Validator.java             ← Input validation methods
│       └── ConsoleHelper.java         ← Formatted output / ANSI colors
└── out/                               ← Compiled .class files (after build)
```

## 🗂️ Entity: HotelRoom

| Field          | Type   | Constraints                          |
|----------------|--------|--------------------------------------|
| roomId         | String | Format: R### (e.g. R001), unique     |
| roomType       | String | SINGLE / DOUBLE / SUITE / DELUXE     |
| pricePerNight  | double | > 0                                  |
| capacity       | int    | 1 – 10                               |
| status         | String | AVAILABLE / OCCUPIED / MAINTENANCE   |
| description    | String | Non-empty                            |

## ⚙️ How to Compile & Run

### Requirements
- Java JDK 11 or higher

### Compile
```bash
javac -d out -sourcepath src src/Main.java src/model/HotelRoom.java src/model/Customer.java src/model/Booking.java src/service/HotelRoomService.java src/util/Validator.java src/util/ConsoleHelper.java
```

### Run
```bash
java -cp out Main
```

## 📋 Menu Options

| # | Feature          | Description                                         |
|---|------------------|-----------------------------------------------------|
| 1 | Add New Room     | Prompt all fields with validation before inserting  |
| 2 | Display All      | Print all rooms in a formatted table                |
| 3 | Update a Room    | Find by ID, press ENTER to keep current values      |
| 4 | Delete a Room    | Find by ID, confirm before removing                 |
| 5 | Search           | By Room ID, Room Type, or Status                    |
| 6 | Sort             | By ID, Price (asc/desc), or Room Type               |
| 7 | Save to File     | Exports all rooms to `hotel_rooms.csv`              |
| 8 | Load from File   | Imports rooms from `hotel_rooms.csv`                |
| 0 | Exit             | Quit the program                                    |

## ✅ Validation Rules

| Field         | Rule                                                   |
|---------------|--------------------------------------------------------|
| Room ID       | Regex `R\d{3}` — must be unique                       |
| Room Type     | Enum: SINGLE, DOUBLE, SUITE, DELUXE                   |
| Price         | Must be > 0 (double)                                   |
| Capacity      | Integer between 1 and 10                              |
| Status        | Enum: AVAILABLE, OCCUPIED, MAINTENANCE                 |
| Description   | Must not be blank                                      |

Input errors are caught with try-catch blocks; the user is re-prompted until valid input is given (max 3 attempts for Room ID).

## 💾 File Storage

Rooms can be saved to / loaded from **hotel_rooms.csv** (comma-separated):
```
roomId,roomType,pricePerNight,capacity,status,description
R001,SINGLE,89.99,1,AVAILABLE,Cozy single room with city view
...
```

## 🏗️ OOP Design

- **Encapsulation** — all fields are `private`, accessed via getters/setters
- **Separation of Concerns** — Model / Service / Util layers
- **Single Responsibility** — each class has a clear, focused role
- **Validation Layer** — `Validator` class centralises all rules

## 📊 Sample Data (pre-loaded)

| Room ID | Type   | Price  | Capacity | Status      |
|---------|--------|--------|----------|-------------|
| R001    | SINGLE |  89.99 | 1        | AVAILABLE   |
| R002    | DOUBLE | 129.99 | 2        | OCCUPIED    |
| R003    | SUITE  | 299.99 | 4        | AVAILABLE   |
| R004    | DELUXE | 199.99 | 3        | MAINTENANCE |
| R005    | SINGLE |  75.00 | 1        | AVAILABLE   |
| R006    | DOUBLE | 149.99 | 2        | OCCUPIED    |
| R007    | SUITE  | 399.99 | 6        | AVAILABLE   |
