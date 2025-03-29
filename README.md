# Expense Sharing Application - Technical Design

This document outlines the technical design, architecture patterns, and OOP principles implemented in the Expense Sharing Application.

## System Overview

The Expense Sharing Application applies Domain-Driven Design (DDD) principles to model a real-world expense-sharing scenario. The application follows SOLID principles and incorporates several design patterns to achieve maintainability, extensibility, and separation of concerns.

## Architecture

The application follows a layered architecture:

- **Domain Layer**: Contains the core business entities and logic
- **Service Layer**: Contains application services that orchestrate operations
- **Infrastructure Layer**: Handles persistence and external concerns (simplified in this implementation)

## Design Patterns Implemented

### 1. Strategy Pattern
Used for implementing different splitting strategies (Equal, Exact, Percentage). The `Split` interface acts as the strategy interface, with concrete implementations:
- `EqualSplit`: Distributes expenses equally
- `ExactSplit`: Assigns exact amounts to participants
- `PercentSplit`: Distributes by percentage

This pattern allows the application to interchange splitting algorithms without modifying the client code that uses them.

### 2. Factory Method Pattern
Implemented in the `ExpenseService` for creating different types of expense transactions based on the split type. This encapsulates the creation logic and insulates the client from implementation details.

### 3. Command Pattern
The input commands (`EXPENSE`, `SHOW`) function as commands that are executed against the application. Each command triggers specific operations in the appropriate services.

### 4. Observer Pattern (Implicit)
Balance updates function in an observer-like pattern where transactions (observables) trigger updates to user balances (observers). When an expense is recorded, all involved users' balances are automatically updated.

### 5. Facade Pattern
The `ExpenseSharingApp` class serves as a facade, providing a simplified interface to the complex subsystems, hiding the details of transaction creation and balance management.

## SOLID Principles Applied

### Single Responsibility Principle (SRP)
- Each class has a single responsibility:
    - `UserService`: Managing users
    - `BalanceService`: Tracking financial balances
    - `ExpenseService`: Processing expenses
    - `GroupService`: Managing groups

### Open/Closed Principle (OCP)
- The application is open for extension but closed for modification:
    - New split types can be added by implementing the `Split` interface
    - New transaction types can be added by extending the `Transaction` class

### Liskov Substitution Principle (LSP)
- Subtypes are substitutable for their base types:
    - `ExpenseTransaction` and `SettlementTransaction` can be used anywhere a `Transaction` is expected
    - Different `Split` implementations can be used interchangeably

### Interface Segregation Principle (ISP)
- Interfaces are client-specific rather than general-purpose:
    - `Split` interface contains only methods relevant to splitting expenses

### Dependency Inversion Principle (DIP)
- High-level modules depend on abstractions, not details:
    - Services depend on interfaces rather than concrete implementations
    - Balance calculations depend on the `Split` interface, not specific implementations

## Low-Level Design Components

### 1. Entity Structure
- **Immutable Core**: Entities have immutable identifiers (UUIDs) for reference stability
- **Rich Domain Model**: Entities encapsulate both state and behavior
- **Value Objects**: Money amounts are represented as BigDecimal for precision

### 2. Concurrency Considerations
- Thread-safety in balance updates with synchronized methods
- Defensive copying of collections to prevent concurrent modification issues

### 3. Exception Handling
- Custom exception hierarchy for domain-specific errors
- Validation at appropriate layers to prevent invalid states

### 4. Memory Management
- Efficient use of hash-based collections for O(1) lookups
- Proper encapsulation to control object lifecycles

## Data Structures

### 1. Balance Tracking
- Double-indexed map structure: `Map<String, Map<String, BigDecimal>>`
- Provides O(1) lookups for user-to-user balances
- Space complexity: O(n²) where n is the number of users

### 2. Transaction History
- Chronological list: `List<Transaction>`
- Allows O(n) scan for transaction history
- Supports filtering by user, group, or date

### 3. Group Membership
- Set-based structure: `Set<User>` within Group
- O(1) membership checks

## Algorithmic Considerations

### Split Calculations
- **Equal Split**: O(n) time complexity, where n is the number of participants
- **Percentage Split**: O(n) time complexity with additional validation
- **Exact Split**: O(n) time complexity with sum validation

### Balance Simplification
- The application supports balance simplification using a graph-based approach
- Reduces the number of transactions needed to settle all debts

## Extensibility Points

The system is designed to be extended in the following ways:

1. **New Split Types**: Implement the `Split` interface for new splitting strategies
2. **New Transaction Types**: Extend the `Transaction` class for new financial activities
3. **Additional Services**: Add new services that use the existing domain model
4. **Persistence Layer**: Add implementations for storing data persistently
5. **User Interface**: Integrate with different UI technologies through the service layer

## Performance Considerations

- BigDecimal used for financial calculations to prevent floating-point errors
- Efficient data structures for common operations (balance lookups, user retrieval)
- Minimal object allocation during transaction processing
- Constant-time lookups for most operations through hash-based collections

## Testing Strategy

- **Unit Tests**: For individual components (especially split calculations)
- **Integration Tests**: For service interactions
- **System Tests**: For end-to-end expense scenarios (using the examples provided)

## Design Trade-offs

1. **Immutability vs. Mutability**: Immutable IDs with mutable state for practical balance tracking
2. **Memory vs. Speed**: In-memory data structures optimize for speed at the cost of memory usage
3. **Simplicity vs. Features**: Core functionality prioritized over advanced features for maintainability

## UML Class Diagram (Simplified)

```
┌───────────┐         ┌─────────────┐         ┌────────────┐
│   User    │         │ Transaction │         │   Group    │
└───────────┘         └─────────────┘         └────────────┘
      ▲                     ▲                        ▲
      │                     │                        │
      │                 ┌───┴────┐                   │
┌──────────┐   ┌────────┴─────┐  │  ┌───────────┐   │
│UserService│   │ExpenseService│  │  │GroupService│  │
└──────────┘   └──────────────┘  │  └───────────┘   │
      │               │          │        │         │
      │               │          │        │         │
      │         ┌─────┴──────┐   │        │         │
      │         │BalanceService│  │        │         │
      │         └─────────────┘   │        │         │
      │                           │        │         │
      │                    ┌──────┴─────┐  │         │
      │                    │ExpenseTransaction│     │
      │                    └──────────────┘  │      │
      │                           │          │      │
      │                           ▼          │      │
      │                      ┌────────┐      │      │
      └─────────────────────▶│ Split  │◀─────┘      │
                             └────────┘              │
                                 ▲                   │
                          ┌──────┼──────┐            │
                          │      │      │            │
                    ┌─────┴──┐ ┌─┴────┐ ┌┴─────────┐ │
                    │EqualSplit│ │ExactSplit│ │PercentSplit│ │
                    └────────┘ └────────┘ └──────────┘ │
                          │      │      │            │
                          └──────┼──────┘            │
                                 │                   │
                                 └───────────────────┘
```

## Conclusion

This expense sharing application demonstrates the application of numerous design patterns, SOLID principles, and architectural best practices. The modular design allows for future extensions while maintaining a clean separation of concerns and adhering to OOP principles.