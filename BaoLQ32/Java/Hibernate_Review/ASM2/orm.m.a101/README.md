# orm.m.a101 — Movie Theater Application (Hibernate ORM Assignment 02)

## Tech Stack
| Tool | Version |
|---|---|
| Java | 8+ |
| Hibernate | 5.6.15.Final |
| H2 Database | 2.2.224 (in-memory, no install needed) |
| Maven | 3+ |

---

## Project Structure

```
orm.m.a101/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/fa/training/
    │   │   ├── Main.java                        ← Scenario simulation + switch/lambda menu
    │   │   ├── entities/
    │   │   │   ├── CinemaRoom.java              ← @Entity CINEMA_ROOM (OneToMany Seat, OneToOne Detail)
    │   │   │   ├── CinemaRoomDetail.java        ← @Entity CINEMA_ROOM_DETAIL (OneToOne owning)
    │   │   │   └── Seat.java                    ← @Entity SEAT (ManyToOne owning)
    │   │   ├── dao/
    │   │   │   ├── RoomDao.java                 ← CRUD for CinemaRoom
    │   │   │   ├── RoomDetailDao.java           ← CRUD for CinemaRoomDetail
    │   │   │   └── SeatDao.java                 ← CRUD for Seat + extra queries
    │   │   └── utils/HibernateUtil.java         ← SessionFactory singleton
    │   └── resources/hibernate.cfg.xml         ← H2 + show_sql + hbm2ddl.auto=update
    └── test/java/fa/training/dao/
        ├── RoomDaoTest.java
        ├── RoomDetailDaoTest.java
        └── SeatDaoTest.java
```

---

## Entity Relationships (ERD → Java)

```
CinemaRoom  ──OneToOne──►  CinemaRoomDetail   (room owns FK in CINEMA_ROOM_DETAIL)
CinemaRoom  ──OneToMany──► Seat               (seat owns FK CINEMA_ROOM_ID in SEAT)
```

Many-to-many is **intentionally avoided** per assignment requirements.

---

## hbm2ddl.auto: create vs update

| Value | Behaviour | When to use |
|---|---|---|
| `create` | Drops + recreates all tables on every startup | Fresh dev, wiping data is OK |
| `update` | Creates missing tables/columns, never drops | Normal dev & production |

This project uses **`update`** so data survives restarts.

---

## How to Run

```bash
# Download dependencies & compile
mvn clean install

# Run all unit tests
mvn test

# Run the application (simulates scenario then opens menu)
mvn exec:java -Dexec.mainClass="fa.training.Main"
```

---

## Design Notes (Avoiding Common AI Pitfalls)

| Pitfall | Solution |
|---|---|
| **Connection Leak** | Every `Session` opened via `try-with-resources` — auto-closed even on exception |
| **Layer Violation** | `Scanner` / `System.out` only in `Main.java`; DAOs are pure data-access |
| **Fake unit tests** | All assertions are real (`assertEquals`, `assertTrue(condition)`, `assertFalse`) |
| **Input crash** | `readInt()` validates with Regex before parsing |
| **Java 8 avoidance** | `Consumer<Scanner>` lambdas for menu actions; `Optional` for nullable returns; `Stream` + method references in tests |
| **Many-to-many** | Modelled as OneToOne + OneToMany only |
| **Date accuracy** | `LocalDate` used for `ACTIVE_DATE` column (as per assignment guideline) |
