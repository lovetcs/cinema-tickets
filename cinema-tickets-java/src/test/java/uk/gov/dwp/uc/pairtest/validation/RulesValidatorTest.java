package uk.gov.dwp.uc.pairtest.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import uk.gov.dwp.uc.pairtest.TicketPurchaseValidator;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException.InvalidPurchaseReason;

public class RulesValidatorTest {

    private final TicketPurchaseValidator rulesValidator = new RulesValidator();

    private void assertRejectedWith(InvalidPurchaseReason expected, Executable action) {
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, action);
        assertEquals(expected, exception.getReason());
    }

    // Maximum number of tickets

    @Test
    void givenMoreThanTwentyFiveTickets_shouldRejectWithTooManyTickets() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 26);

        assertRejectedWith(InvalidPurchaseReason.TOO_MANY_TICKETS,
                () -> rulesValidator.validate(accountId, adult));
    }

    @Test
    void givenExactlyTwentyFiveTickets_shouldAccept() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 25);

        assertDoesNotThrow(() -> rulesValidator.validate(accountId, adult));
    }

    @Test
    void givenATotalOverTheLimitAcrossSeparateRequests_shouldRejectWithTooManyTickets() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 13);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 13);

        assertRejectedWith(InvalidPurchaseReason.TOO_MANY_TICKETS,
                () -> rulesValidator.validate(accountId, adult, child));
    }

    @Test
    void givenInfantsAlongsideAdults_shouldCountInfantsTowardsTheMaximum() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 13);
        TicketTypeRequest infantsOverLimit = new TicketTypeRequest(Type.INFANT, 13);
        TicketTypeRequest infantsAtLimit = new TicketTypeRequest(Type.INFANT, 12);

        assertRejectedWith(InvalidPurchaseReason.TOO_MANY_TICKETS,
                () -> rulesValidator.validate(accountId, adult, infantsOverLimit));

        assertDoesNotThrow(() -> rulesValidator.validate(accountId, adult, infantsAtLimit));
    }

    @Test
    void givenVeryLargeTicketCounts_shouldRejectWithTooManyTickets() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, Integer.MAX_VALUE);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, Integer.MAX_VALUE);
        TicketTypeRequest moreChildren = new TicketTypeRequest(Type.CHILD, 5);

        assertRejectedWith(InvalidPurchaseReason.TOO_MANY_TICKETS,
                () -> rulesValidator.validate(accountId, adult, child, moreChildren));
    }

    // Adult required

    @Test
    void givenChildrenAndInfantsWithoutAdults_shouldRejectWithAdultRequired() {

        Long accountId = 1L;
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 1);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 1);

        assertRejectedWith(InvalidPurchaseReason.ADULT_REQUIRED,
                () -> rulesValidator.validate(accountId, child, infant));
    }

    @Test
    void givenOnlyChildren_shouldRejectWithAdultRequired() {

        Long accountId = 1L;
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 1);

        assertRejectedWith(InvalidPurchaseReason.ADULT_REQUIRED,
                () -> rulesValidator.validate(accountId, child));
    }

    @Test
    void givenOnlyInfants_shouldRejectWithAdultRequired() {

        Long accountId = 1L;
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 1);

        assertRejectedWith(InvalidPurchaseReason.ADULT_REQUIRED,
                () -> rulesValidator.validate(accountId, infant));
    }

    // Infants versus adults

    @Test
    void givenMoreInfantsThanAdults_shouldRejectWithInfantsExceedAdults() {

        Long accountId = 1L;
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 2);
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 1);

        assertRejectedWith(InvalidPurchaseReason.INFANTS_EXCEED_ADULTS,
                () -> rulesValidator.validate(accountId, adult, infant));
    }

    @Test
    void givenAsManyInfantsAsAdults_shouldAccept() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 2);

        assertDoesNotThrow(() -> rulesValidator.validate(accountId, adult, infant));
    }
}