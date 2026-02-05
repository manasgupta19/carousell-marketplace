# Carousell Marketplace CLI Implementation

## **Overview**
A high-performance, thread-safe, and extensible CLI application for managing a marketplace. [cite_start]Built strictly with **Java 8 (Standard Library)**, this project demonstrates production-grade software engineering principles including SOLID design, the Command Pattern, and defensive programming.

---

## **Architectural Design Patterns**

To ensure the system is maintainable and resilient to changing requirements, the following patterns were implemented:

* [cite_start]**Command Pattern:** Each operation (e.g., `NotesING`) is encapsulated as a standalone object. [cite_start]This decouples the CLI invoker from the business logic, allowing new commands to be added with zero changes to the core loop (**Open/Closed Principle**).
* [cite_start]**Strategy & Factory Patterns:** Sorting logic for `GET_CATEGORY` is handled via a `ListingSortStrategy` interface. [cite_start]A `SortStrategyFactory` determines the strategy at runtime, making the sorting logic easily extensible.
* [cite_start]**Repository Pattern with Interface Segregation:** Data persistence is managed by a `MarketplaceRepository`. [cite_start]Splitting the repository into `MarketplaceReader` and `MarketplaceWriter` interfaces ensures that read-only commands cannot accidentally modify state (**Interface Segregation Principle**).
* [cite_start]**Dependency Injection:** All commands receive their dependencies (Reader/Writer) via constructor injection. [cite_start]This makes the application fully decoupled and straightforward to unit test.
* [cite_start]**Domain Modeling:** Core concepts like `User`, `Listing`, and `Category` are modeled as formal entities rather than simple primitives, providing a foundation for future metadata and business rules.

---

## **Technical Optimizations & Rigor**

### **1. O(1) Read-Heavy Optimization**
[cite_start]As per the requirement that `GET_TOP_CATEGORY` is a read-heavy operation, I implemented an **Eager Caching Strategy**:
* [cite_start]**Mechanism:** The repository maintains an internal frequency map of categories. [cite_start]A `volatile cachedTopCategory` field is updated only during write operations.
* [cite_start]**Benefit:** Complexity is shifted to the write path, allowing frequent read operations to execute in constant time $O(1)$.

### **2. Concurrency & Race Condition Prevention**
* [cite_start]**Atomic Operations:** Used `putIfAbsent` for user registration to prevent race conditions during concurrent account creation.
* [cite_start]**Thread-Safe Storage:** Utilized `ConcurrentHashMap` for storage and `AtomicInteger` for sequential 6-digit ID generation.

### **3. Defensive Programming & Validation**
* [cite_start]**Input Validation:** The application strictly validates business constraints, rejecting empty titles, descriptions, or categories, and enforcing positive pricing.
* [cite_start]**Resource Management:** Implemented **try-with-resources** for system scanners to ensure proper resource cleanup and prevent memory leaks.
* [cite_start]**Dynamic Data:** Replaced hardcoded constants with dynamic timestamp generation to reflect real-time marketplace activity.

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
