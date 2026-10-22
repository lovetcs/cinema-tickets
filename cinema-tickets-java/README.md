# Cinema Tickets

A ticket purchasing service for a cinema. It validates a purchase request, calculates the price and the number of seats, takes payment through `TicketPaymentService` and reserves seats through `SeatReservationService`.

The solution is written in Java and lives in `cinema-tickets-java`.

## Requirements

- Java 21. (Mockito 5.14 does not support newer JDKs out of the box, so please run the tests on JDK 21).
- Maven 3.x

## How to run the tests

```bash
cd cinema-tickets-java
mvn test
```

## Design

All collaborators are passed in through the constructor, so each piece can be tested on its own.

- **`TicketServiceImpl`**: coordinates a purchase: validate, calculate the price and seats, take payment, then reserve seats. It contains no business rules itself.
- **`TicketPurchaseValidator`**: rejects invalid purchases by throwing `InvalidPurchaseException`. It has no side effects.
- **`TicketCalculator`**: works out the total price and the number of seats. It does no validation.
- **`TicketTypeRequest`**: immutable pair of ticket type and number of tickets.

Prices: Infant £0, Child £15, Adult £25. Infants do not need a seat.

## Testing approach

The solution was developed test-first (TDD), in small steps.

- **Unit tests:** each class is tested on its own.
  - **Validator:** one test per rule, including the boundary at 25 tickets.
  - **Calculator:** each ticket type, and mixed purchases, for both price and seats.
  - **Service:** the validator, calculator, payment service and seat service are all mocked (Mockito). These tests check that the service calls each of them correctly, and that nothing is paid or reserved when a request is invalid.

- **Integration-style tests:** these use the real validator and calculator, and mock only the two external services (`TicketPaymentService` and `SeatReservationService`). They check complete purchase scenarios with real figures.

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

## Notes

- Invalid requests are rejected before any payment or reservation call is made.
- The `thirdparty` packages and the `TicketService` interface were left unchanged.