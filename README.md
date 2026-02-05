# Carousell Marketplace CLI Implementation

## **Overview**
A high-performance, thread-safe, and extensible CLI application for managing a marketplace. Built strictly with **Java 8 (Standard Library)**, this project demonstrates advanced software engineering principles including SOLID design, the Command Pattern, and read-heavy data optimization.

---

## **Architectural Design Patterns**

To ensure the system is maintainable and resilient to changing requirements, the following patterns were implemented:

* **Command Pattern:** Each operation (e.g., `NotesING`) is encapsulated as a standalone object. This decouples the CLI invoker from the business logic, allowing new commands to be added with zero changes to the core loop (**Open/Closed Principle**).
* **Strategy & Factory Patterns:** Sorting logic for `GET_CATEGORY` is handled via a `ListingSortStrategy` interface. A `SortStrategyFactory` determines the strategy at runtime based on user input, making the sorting logic easily extensible.
* **Repository Pattern with Interface Segregation:** Data persistence is managed by a `MarketplaceRepository`. I split the repository into `MarketplaceReader` and `MarketplaceWriter` interfaces. This ensures that a read-only command (like `GET_TOP_CATEGORY`) cannot accidentally modify state (**Interface Segregation Principle**).
* **Dependency Injection:** All commands receive their dependencies (Reader/Writer) via constructor injection. This makes the application fully decoupled and straightforward to unit test using mocks (**Dependency Inversion Principle**).

---

## **Technical Optimizations**

### **1. O(1) Read-Heavy Optimization**
As per the requirement that `GET_TOP_CATEGORY` is a read-heavy operation, I implemented an **Eager Caching Strategy**:
* **Mechanism:** The repository maintains an internal frequency map of categories. A `volatile cachedTopCategory` field is updated only during write operations (`CREATE` or `DELETE`).
* **Benefit:** The complexity is shifted to the write path ($O(N)$ where N is the number of categories), allowing the frequent read operations to execute in constant time $O(1)$.

### **2. Incumbency Tie-Breaking ("King of the Hill")**
The system implements a sophisticated tie-breaking logic for the top category to match the behavioral requirements:
* **Incumbent Stability:** A category remains the "Top Category" even if a new category reaches the same count.
* **Strict Dominance:** A leader is only replaced if a challenger's count becomes **strictly greater** than the leader's current count.
* **Resilient Leadership:** If the leader’s count decreases (due to a deletion) but remains tied for the highest count with another category, it retains its leadership position.

---

## **Implementation Details**
* **Thread Safety:** Used `ConcurrentHashMap` for storage and `AtomicInteger` for sequential 6-digit ID generation starting at `100001`.
* **Input Parsing:** A specialized `Parser` utility uses regex to handle multi-word quoted strings (e.g., `'Phone model 8'`) without splitting them.
* **Case Insensitivity:** All user and category lookups are normalized to lowercase to ensure consistent behavior regardless of input casing.
* **Standard Compliance:** Output follows the pipe-separated format (`|`) exactly, including formatting prices as integers (e.g., `1000` instead of `1000.0`) using `%.0f`.

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
```bash
./run.sh < input.txt
```
---

## **Git History**

The submission includes the .git directory. The history shows a professional, iterative development flow:

* **Initial Scaffolding:** Interface definitions and basic CLI loop.
* **Core Feature Set:** Implementation of the data layer and basic commands.
* **SOLID Refactor:** Decoupling logic through interfaces and Dependency Injection.
* **Optimization:** Finalizing the incumbency logic and $O(1)$ read-path cache.

---