package uk.gov.dwp.uc.pairtest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException.InvalidPurchaseReason;

public class TicketPurchaseValidatorTest {

    private final TicketPurchaseValidator ticketPurchaseValidator = new TicketPurchaseValidator();

    private void assertRejectedWith(InvalidPurchaseReason expected, Executable action) {
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, action);
        assertEquals(expected, exception.getReason());
    }

    @Test
    void shouldAcceptValidRequest() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 1);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 1);

        assertDoesNotThrow(() -> ticketPurchaseValidator.validate(accountId, adult, child, infant));

    }

    @Test
    void shouldThrowWhenAccountIdIsNull() {

        Long accountId = null;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);

        assertRejectedWith(InvalidPurchaseReason.INVALID_ACCOUNT_ID,
                () -> ticketPurchaseValidator.validate(accountId, adult));
    }

    @Test
    void shouldThrowWhenAccountIdIsZero() {

        Long accountId = 0L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);

        assertRejectedWith(InvalidPurchaseReason.INVALID_ACCOUNT_ID,
                () -> ticketPurchaseValidator.validate(accountId, adult));
    }

    @Test
    void shouldThrowWhenAccountIdIsLessThanZero() {

        Long accountId = -1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);

        assertRejectedWith(InvalidPurchaseReason.INVALID_ACCOUNT_ID,
                () -> ticketPurchaseValidator.validate(accountId, adult));
    }

    @Test
    void shouldThrowWhenTicketRequestsIsEmpty() {

        Long accountId = 1L;

        assertRejectedWith(InvalidPurchaseReason.NO_TICKETS_REQUESTED,
                () -> ticketPurchaseValidator.validate(accountId));
    }

    @Test
    void shouldThrowWhenTicketRequestsIsNull() {

        Long accountId = 1L;

        assertRejectedWith(InvalidPurchaseReason.NO_TICKETS_REQUESTED,
                () -> ticketPurchaseValidator.validate(accountId, (TicketTypeRequest[]) null));
    }

    @Test
    void shouldThrowWhenTicketCountIsZero() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 0);

        assertRejectedWith(InvalidPurchaseReason.INVALID_TICKET_COUNT,
                () -> ticketPurchaseValidator.validate(accountId, adult));
    }

    @Test
    void shouldThrowWhenTicketCountIsLessThanZero() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, -1);

        assertRejectedWith(InvalidPurchaseReason.INVALID_TICKET_COUNT,
                () -> ticketPurchaseValidator.validate(accountId, adult));
    }

    @Test
    void shouldThrowWhenTicketCountIsOverLimit() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 26);

        assertRejectedWith(InvalidPurchaseReason.TOO_MANY_TICKETS,
                () -> ticketPurchaseValidator.validate(accountId, adult));
    }

    @Test
    void shouldAcceptWhenTicketCountIsAtBoundary() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 25);

        assertDoesNotThrow(() -> ticketPurchaseValidator.validate(accountId, adult));
    }

    @Test
    void shouldThrowWhenChildrenAndInfantsAreRequestedWithoutAdults() {

        Long accountId = 1L;

        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 1);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 1);

        assertRejectedWith(InvalidPurchaseReason.ADULT_REQUIRED,
                () -> ticketPurchaseValidator.validate(accountId, child, infant));

    }

    @Test
    void shouldThrowWhenOnlyChildrenAreRequested() {

        Long accountId = 1L;

        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 1);

        assertRejectedWith(InvalidPurchaseReason.ADULT_REQUIRED,
                () -> ticketPurchaseValidator.validate(accountId, child));
    }

    @Test
    void shouldThrowWhenOnlyInfantsAreRequested() {

        Long accountId = 1L;

        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 1);

        assertRejectedWith(InvalidPurchaseReason.ADULT_REQUIRED,
                () -> ticketPurchaseValidator.validate(accountId, infant));
    }

    @Test
    void shouldThrowWhenInfantsOutnumberAdults() {

        Long accountId = 1L;

        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 2);
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 1);

        assertRejectedWith(InvalidPurchaseReason.INFANTS_EXCEED_ADULTS,
                () -> ticketPurchaseValidator.validate(accountId, adult, infant));

    }

    @Test
    void shouldThrowWhenTicketTypeIsNull() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);
        TicketTypeRequest noType = new TicketTypeRequest(null, 1);

        assertRejectedWith(InvalidPurchaseReason.INVALID_TICKET_REQUEST,
                () -> ticketPurchaseValidator.validate(accountId, adult, noType));

    }

    @Test
    void shouldThrowWhenARequestInTheArrayIsNull() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);

        assertRejectedWith(InvalidPurchaseReason.INVALID_TICKET_REQUEST,
                () -> ticketPurchaseValidator.validate(accountId, adult, null));

    }

    @Test
    void shouldThrowWhenTotalAcrossSeparateRequestsIsOverLimit() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 13);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 13);

        assertRejectedWith(InvalidPurchaseReason.TOO_MANY_TICKETS,
                () -> ticketPurchaseValidator.validate(accountId, adult, child));
    }

    @Test
    void shouldCountInfantsTowardsTheMaximumNumberOfTickets() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 13);
        TicketTypeRequest infantsOverLimit = new TicketTypeRequest(Type.INFANT, 13);
        TicketTypeRequest infantsAtLimit = new TicketTypeRequest(Type.INFANT, 12);

        assertRejectedWith(InvalidPurchaseReason.TOO_MANY_TICKETS,
                () -> ticketPurchaseValidator.validate(accountId, adult, infantsOverLimit));

        assertDoesNotThrow(() -> ticketPurchaseValidator.validate(accountId, adult, infantsAtLimit));
    }

    @Test
    void shouldThrowWhenTicketCountsWouldOverflowTheTotal() {
        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, Integer.MAX_VALUE);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, Integer.MAX_VALUE);
        TicketTypeRequest moreChildren = new TicketTypeRequest(Type.CHILD, 5);

        assertRejectedWith(InvalidPurchaseReason.TOO_MANY_TICKETS,
                () -> ticketPurchaseValidator.validate(accountId, adult, child, moreChildren));

    }

    @Test
    void shouldAcceptInfantsEqualToAdults() {
        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 2);

        assertDoesNotThrow(() -> ticketPurchaseValidator.validate(accountId, adult, infant));
    }

}
