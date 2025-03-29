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
