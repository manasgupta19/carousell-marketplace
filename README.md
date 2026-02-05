# Carousell Marketplace CLI Implementation

## **Overview**
A high-performance, thread-safe, and extensible CLI application for managing a marketplace. Built strictly with **Java 8 (Standard Library)**, this project demonstrates production-grade software engineering principles including SOLID design, the Command Pattern, and defensive programming.

---

## **Architectural Design Patterns**

To ensure the system is maintainable and resilient to changing requirements, the following patterns were implemented:

* **Command Pattern:** Each operation (e.g., `NotesING`) is encapsulated as a standalone object. This decouples the CLI invoker from the business logic, allowing new commands to be added with zero changes to the core loop (**Open/Closed Principle**).
* **Strategy & Factory Patterns:** Sorting logic for `GET_CATEGORY` is handled via a `ListingSortStrategy` interface. A `SortStrategyFactory` determines the strategy at runtime, making the sorting logic easily extensible.
* **Repository Pattern with Interface Segregation:** Data persistence is managed by a `MarketplaceRepository`. Splitting the repository into `MarketplaceReader` and `MarketplaceWriter` interfaces ensures that read-only commands cannot accidentally modify state (**Interface Segregation Principle**).
* **Dependency Injection:** All commands receive their dependencies (Reader/Writer) via constructor injection. This makes the application fully decoupled and straightforward to unit test.
* **Domain Modeling:** Core concepts like `User`, `Listing`, and `Category` are modeled as formal entities rather than simple primitives, providing a foundation for future metadata and business rules.

---

## **Technical Optimizations & Rigor**

### **1. O(1) Read-Heavy Optimization**
As per the requirement that `GET_TOP_CATEGORY` is a read-heavy operation, I implemented an **Eager Caching Strategy**:
* **Mechanism:** The repository maintains an internal frequency map of categories. A `volatile cachedTopCategory` field is updated only during write operations.
* **Benefit:** Complexity is shifted to the write path, allowing frequent read operations to execute in constant time $O(1)$.

### **2. Concurrency & Race Condition Prevention**
* **Atomic Operations:** Used `putIfAbsent` for user registration to prevent race conditions during concurrent account creation.
* **Thread-Safe Storage:** Utilized `ConcurrentHashMap` for storage and `AtomicInteger` for sequential 6-digit ID generation.

### **3. Defensive Programming & Validation**
* **Input Validation:** The application strictly validates business constraints, rejecting empty titles, descriptions, or categories, and enforcing positive pricing.
* **Resource Management:** Implemented **try-with-resources** for system scanners to ensure proper resource cleanup and prevent memory leaks.
* **Dynamic Data:** Replaced hardcoded constants with dynamic timestamp generation to reflect real-time marketplace activity.

---

## **How to Build and Run**

### **Build**
```bash
chmod +x build.sh
./build.sh
```

### **Run**
```bash
chmod +x run.sh
./run.sh
```

### **Automated Testing**
The submission includes a custom test suite using Java standard assertions. Run it with the -ea (enable assertions) flag:
```bash
javac -d out $(find src -name "*.java")
java -ea -cp out com.carousell.marketplace.MarketplaceTest
```
---

## **Git History**

The submission includes the .git directory. The history shows a professional, iterative development flow:

* **Initial Scaffolding:** Interface definitions and basic CLI loop.
* **Core Feature Set:** Implementation of the data layer and basic commands.
* **SOLID Refactor:** Decoupling logic through interfaces and Dependency Injection.
* **Production Hardening:** Addressing race conditions, resource management, and input validation..

---
