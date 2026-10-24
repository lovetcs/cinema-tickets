package uk.gov.dwp.uc.pairtest.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import uk.gov.dwp.uc.pairtest.TicketPurchaseValidator;
import org.junit.jupiter.api.function.Executable;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException.InvalidPurchaseReason;

public class RulesValidatorTest {

    private final TicketPurchaseValidator rulesValidator = new RulesValidator();

    private void assertRejectedWith(InvalidPurchaseReason expected, Executable action) {
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, action);
        assertEquals(expected, exception.getReason());
    }

    @Test
    void shouldThrowWhenTicketCountIsOverLimit() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 26);

        assertRejectedWith(InvalidPurchaseReason.TOO_MANY_TICKETS,
                () -> rulesValidator.validate(accountId, adult));
    }

    @Test
    void shouldAcceptWhenTicketCountIsAtBoundary() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 25);

        assertDoesNotThrow(() -> rulesValidator.validate(accountId, adult));
    }

    @Test
    void shouldThrowWhenTotalAcrossSeparateRequestsIsOverLimit() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 13);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 13);

        assertRejectedWith(InvalidPurchaseReason.TOO_MANY_TICKETS,
                () -> rulesValidator.validate(accountId, adult, child));
    }

    @Test
    void shouldCountInfantsTowardsTheMaximumNumberOfTickets() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 13);
        TicketTypeRequest infantsOverLimit = new TicketTypeRequest(Type.INFANT, 13);
        TicketTypeRequest infantsAtLimit = new TicketTypeRequest(Type.INFANT, 12);

        assertRejectedWith(InvalidPurchaseReason.TOO_MANY_TICKETS,
                () -> rulesValidator.validate(accountId, adult, infantsOverLimit));

        assertDoesNotThrow(() -> rulesValidator.validate(accountId, adult, infantsAtLimit));
    }

    @Test
    void shouldThrowWhenTicketCountsWouldOverflowTheTotal() {
        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, Integer.MAX_VALUE);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, Integer.MAX_VALUE);
        TicketTypeRequest moreChildren = new TicketTypeRequest(Type.CHILD, 5);

        assertRejectedWith(InvalidPurchaseReason.TOO_MANY_TICKETS,
                () -> rulesValidator.validate(accountId, adult, child, moreChildren));

    }

    @Test
    void shouldThrowWhenChildrenAndInfantsAreRequestedWithoutAdults() {

        Long accountId = 1L;

        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 1);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 1);

        assertRejectedWith(InvalidPurchaseReason.ADULT_REQUIRED,
                () -> rulesValidator.validate(accountId, child, infant));

    }

    @Test
    void shouldThrowWhenOnlyChildrenAreRequested() {

        Long accountId = 1L;

        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 1);

        assertRejectedWith(InvalidPurchaseReason.ADULT_REQUIRED,
                () -> rulesValidator.validate(accountId, child));
    }

    @Test
    void shouldThrowWhenOnlyInfantsAreRequested() {

        Long accountId = 1L;

        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 1);

        assertRejectedWith(InvalidPurchaseReason.ADULT_REQUIRED,
                () -> rulesValidator.validate(accountId, infant));
    }

    @Test
    void shouldThrowWhenInfantsOutnumberAdults() {

        Long accountId = 1L;

        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 2);
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 1);

        assertRejectedWith(InvalidPurchaseReason.INFANTS_EXCEED_ADULTS,
                () -> rulesValidator.validate(accountId, adult, infant));

    }

    @Test
    void shouldAcceptInfantsEqualToAdults() {
        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 2);

        assertDoesNotThrow(() -> rulesValidator.validate(accountId, adult, infant));
    }
}
