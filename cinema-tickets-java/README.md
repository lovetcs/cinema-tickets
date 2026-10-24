# Cinema Tickets

A ticket purchasing service for a cinema. It validates a purchase request, calculates the price and the number of seats, takes payment through `TicketPaymentService` and reserves seats through `SeatReservationService`.

The solution is written in Java and lives in `cinema-tickets-java`.

## Requirements

- Java 21
- Maven 3.x

## How to run the tests

```bash
cd cinema-tickets-java
mvn test
```

## Design

All collaborators are passed in through the constructor and are depended on as interfaces, so each piece can be tested on its own.

- **`TicketServiceImpl`**: coordinates a purchase: validate, calculate the price and seats, take payment, then reserve seats. It contains no business rules itself.
- **`TicketPurchaseValidator`** (interface) / **`TicketPurchaseValidatorImpl`**: rejects invalid purchases by throwing `InvalidPurchaseException`. It has no side effects. The implementation is a composite: it runs an ordered list of small validators and stops at the first one that fails.
- **Validators** (`validation` package): each one owns one group of rules, so a rule can be changed or added without editing the others.
  - **`AccountIdValidator`**: the account ID is not null and is greater than zero.
  - **`RequestsValidator`**: there is at least one request, and each request is non-null with a ticket type and a positive count.
  - **`RulesValidator`**: at most 25 tickets in total (infants included), at least one adult, and no more infants than adults. It assumes the requests are well formed, so it runs after `RequestsValidator`.
- **`TicketCalculator`** (interface) / **`TicketCalculatorImpl`**: works out the total price and the number of seats. It does no validation.
- **`TicketTypeRequest`**: immutable pair of ticket type and number of tickets.
- **`TicketTypeRequest.Type`**: each ticket type holds its own price and whether it needs a seat (Adult £25, seat; Child £15, seat; Infant £0, no seat). Adding a new type means adding one enum constant, so a type can't be added without a price.

## Testing approach

The logic was developed largely test-first (TDD), in small steps.

- **Unit tests:** each class is tested on its own.
  - **Validators:** each validator has its own test class, with one test per rule including the boundary at 25 tickets and very large counts. `TicketPurchaseValidatorTest` checks the composite: every default rule is registered, the rules run in order, and it stops at the first failure.
  - **Calculator:** each ticket type, and mixed purchases, for both price and seats.
  - **Ticket types:** the price and seat requirement of each type.
  - **Service:** the validator and calculator interfaces and the two external services are mocked (Mockito). These tests check that the service calls each of them correctly, and that nothing is paid or reserved when a request is invalid.
  - **Integration-style tests:** these use the real validator and calculator, and mock only the two external services (`TicketPaymentService` and `SeatReservationService`). They check complete purchase scenarios with real figures.
  - **Style:** tests are named `given<Scenario>_should<Outcome>` (or plain `should<Outcome>` for simple cases). Parameterised tests are used in `TicketCalculatorTest`, where only the data differs.

## Assumptions

The brief left some rules open. These are the choices made:

- **Account ID:** must be greater than zero. A null, zero or negative ID is invalid.
- **Requests:** a null or empty set of requests, a null entry, a request with no ticket type, or a request with a count of zero or less is invalid.
- **Maximum tickets:** 25 per purchase, and infants count towards it. 1 to 25 tickets is valid, 26 or more is invalid. The limit is applied to the total across all requests.
- **Adult required:** at least one adult ticket is needed, so child-only, infant-only and child-plus-infant purchases are invalid.
- **Infants vs adults:** each infant sits on an adult's lap, so there cannot be more infants than adults.
- **Multiple requests of the same type:** allowed, and added together.
- **Order of payment and seat reservation:** payment is requested first, then seats. Both happen only if the request is valid.
- **`TicketTypeRequest` immutability:** it is a final class with final fields and no setters.
- **Failure after payment:** if payment succeeds but seat reservation fails, nothing is rolled back. Both external services are assumed to always succeed.

## Notes

- Public classes and methods have Javadoc. Run `mvn javadoc:javadoc` to generate it.
- Invalid requests are rejected before any payment or reservation call is made.
- The `thirdparty` packages and the `TicketService` interface were left unchanged.
- `TicketServiceImpl` depends only on interfaces, with real implementations passed in.