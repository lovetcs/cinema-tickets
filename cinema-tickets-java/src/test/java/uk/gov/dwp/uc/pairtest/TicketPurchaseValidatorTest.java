package uk.gov.dwp.uc.pairtest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketPurchaseValidatorTest {

    private final TicketPurchaseValidator ticketPurchaseValidator = new TicketPurchaseValidator();

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

        assertThrows(InvalidPurchaseException.class, () -> ticketPurchaseValidator.validate(accountId, adult));
    }

    @Test
    void shouldThrowWhenAccountIdIsZero() {

        Long accountId = 0L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);

        assertThrows(InvalidPurchaseException.class, () -> ticketPurchaseValidator.validate(accountId, adult));
    }

    @Test
    void shouldThrowWhenAccountIdIsLessThanZero() {

        Long accountId = -1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);

        assertThrows(InvalidPurchaseException.class, () -> ticketPurchaseValidator.validate(accountId, adult));
    }

    @Test
    void shouldThrowWhenTicketRequestsIsEmpty() {

        Long accountId = 1L;

        assertThrows(InvalidPurchaseException.class, () -> ticketPurchaseValidator.validate(accountId));
    }

    @Test
    void shouldThrowWhenTicketRequestsIsNull() {

        Long accountId = 1L;

        assertThrows(InvalidPurchaseException.class,
                () -> ticketPurchaseValidator.validate(accountId, (TicketTypeRequest[]) null));
    }

    @Test
    void shouldThrowWhenTicketCountIsZero() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 0);

        assertThrows(InvalidPurchaseException.class, () -> ticketPurchaseValidator.validate(accountId, adult));
    }

    @Test
    void shouldThrowWhenTicketCountIsLessThanZero() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, -1);

        assertThrows(InvalidPurchaseException.class, () -> ticketPurchaseValidator.validate(accountId, adult));
    }

    @Test
    void shouldThrowWhenTicketCountIsOverLimit() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 26);

        assertThrows(InvalidPurchaseException.class, () -> ticketPurchaseValidator.validate(accountId, adult));
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

        assertThrows(InvalidPurchaseException.class, () -> ticketPurchaseValidator.validate(accountId, child, infant));
    }

    @Test
    void shouldThrowWhenOnlyChildrenAreRequested() {

        Long accountId = 1L;

        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 1);

        assertThrows(InvalidPurchaseException.class, () -> ticketPurchaseValidator.validate(accountId, child));
    }

    @Test
    void shouldThrowWhenOnlyInfantsAreRequested() {

        Long accountId = 1L;

        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 1);

        assertThrows(InvalidPurchaseException.class, () -> ticketPurchaseValidator.validate(accountId, infant));
    }

    @Test
    void shouldThrowWhenInfantsOutnumberAdults() {

        Long accountId = 1L;

        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 2);
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 1);

        assertThrows(InvalidPurchaseException.class, () -> ticketPurchaseValidator.validate(accountId, adult, infant));
    }

    @Test
    void shouldThrowWhenTicketTypeIsNull() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);
        TicketTypeRequest noType = new TicketTypeRequest(null, 1);

        assertThrows(InvalidPurchaseException.class, () -> ticketPurchaseValidator.validate(accountId, adult, noType));
    }

    @Test
    void shouldThrowWhenARequestInTheArrayIsNull() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);

        assertThrows(InvalidPurchaseException.class,
                () -> ticketPurchaseValidator.validate(accountId, adult, null));
    }

    @Test
    void shouldThrowWhenTotalAcrossSeparateRequestsIsOverLimit() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 13);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 13);

        assertThrows(InvalidPurchaseException.class,
                () -> ticketPurchaseValidator.validate(accountId, adult, child));
    }

    @Test
    void shouldCountInfantsTowardsTheMaximumNumberOfTickets() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 13);
        TicketTypeRequest infantsOverLimit = new TicketTypeRequest(Type.INFANT, 13);
        TicketTypeRequest infantsAtLimit = new TicketTypeRequest(Type.INFANT, 12);

        assertThrows(InvalidPurchaseException.class,
                () -> ticketPurchaseValidator.validate(accountId, adult, infantsOverLimit));
        assertDoesNotThrow(() -> ticketPurchaseValidator.validate(accountId, adult, infantsAtLimit));
    }

    @Test
    void shouldThrowWhenTicketCountsWouldOverflowTheTotal() {
        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, Integer.MAX_VALUE);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, Integer.MAX_VALUE);
        TicketTypeRequest moreChildren = new TicketTypeRequest(Type.CHILD, 5);

        assertThrows(InvalidPurchaseException.class,
                () -> ticketPurchaseValidator.validate(accountId, adult, child, moreChildren));
    }

}
