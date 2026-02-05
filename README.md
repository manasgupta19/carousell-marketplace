# Carousell Marketplace CLI Implementation (Spring Boot Edition)

## **Overview**
A production-grade, thread-safe CLI application for managing a marketplace. This implementation has been refactored from a standard Java 8 project to a modern **Spring Boot** application. It leverages **Spring Shell** for CLI lifecycle management and **Lombok** to ensure a clean, boilerplate-free codebase.

---

## **Architectural Design Patterns**

* **Command Pattern:** Each marketplace operation (e.g., `NotesING`) is a Spring-managed `@Component`. This decouples the CLI controller from business logic, allowing for easy extensibility without modifying existing code.
* **Dependency Injection (IoC):** Manual factories have been replaced by Spring’s Inversion of Control. Components are wired together via constructor injection, facilitating better testability and modularity.
* **Strategy Pattern:** Sorting logic is decoupled into specific strategies (Price vs. Time), managed by a factory to ensure the system can support new sorting requirements with minimal changes.
* **Interface Segregation:** The data layer is accessed through specific `MarketplaceReader` and `MarketplaceWriter` interfaces, ensuring that commands only have access to the operations they require.
* **Domain Modeling:** Core concepts are represented as formal entities (`User`, `Listing`, `Category`) rather than primitives, providing a robust foundation for future business rules.

---

## **Technical Optimizations & Rigor**

### **1. Concurrency & Race Condition Prevention**
* **Atomic Registration:** The user registration logic uses `ConcurrentHashMap.putIfAbsent()` to ensure that concurrent attempts to register the same username are handled atomically, resolving previous race condition concerns.
* **Thread-Safe Storage:** All in-memory data structures utilize `ConcurrentHashMap` and `AtomicInteger` for thread-safe operations in a multi-threaded environment.

### **2. O(1) Read-Heavy Optimization**
* **Eager Caching:** The repository maintains an internal frequency map and a `volatile` cache for the top category.
* **Performance:** Calculations are performed during write operations (Add/Delete), allowing the read-heavy `GET_TOP_CATEGORY` command to execute in constant time $O(1)$.
* **Tie-Breaking:** The system implements "Incumbent Stability," where a leader is only replaced if a challenger's volume is strictly greater.

### **3. Defensive Programming & Validation**
* **Input Sanitization:** The application rejects empty titles, descriptions, or categories and enforces positive pricing.
* **Modern Resource Management:** Spring Shell handles the STDIN stream, ensuring resources are managed by the framework and resolving issues related to unclosed Scanners.
* **Dynamic Data:** Timestamps are generated dynamically using `java.time` APIs at the moment of creation.

---

## **How to Build and Run**

### **Prerequisites**
* Java 17 or higher
* Maven

### **Build**
```bash
mvn clean install
```

### **Run**
```bash
mvn spring-boot:run
```

### **Automated Testing**
The project includes a suite of integration tests that validate business logic, concurrency, and validation rules. These tests are configured to run in non-interactive mode to support CI/CD pipelines.
```bash
mvn test
```
---

## **Development History**

The project evolution demonstrates a shift from basic functional requirements to architectural excellence:

* **Standard Java Implementation:** Established core logic and patterns.
* **Concurrency Hardening:** Resolved race conditions and optimized read performance.
* **SOLID Refactor:** Decoupling logic through interfaces and Dependency Injection.
* **Production Hardening:** Addressing race conditions, resource management, and input validation.
* **Spring Boot & Lombok Refactor:** Integrated professional frameworks for dependency injection, boilerplate reduction, and robust CLI management.

---
