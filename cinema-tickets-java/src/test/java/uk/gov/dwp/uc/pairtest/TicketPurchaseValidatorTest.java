package uk.gov.dwp.uc.pairtest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException.InvalidPurchaseReason;

public class TicketPurchaseValidatorTest {

    private final TicketPurchaseValidatorImpl ticketPurchaseValidator = new TicketPurchaseValidatorImpl();

    private final TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);

    private void assertRejectedWith(InvalidPurchaseReason expected, Executable action) {
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, action);
        assertEquals(expected, exception.getReason());
    }

    @Test
    void shouldAcceptValidRequest() {

        Long accountId = 1L;
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 1);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 1);

        assertDoesNotThrow(() -> ticketPurchaseValidator.validate(accountId, adult, child, infant));
    }

    // Each default validator is registered

    @Test
    void givenInvalidAccountId_shouldRejectWithInvalidAccountId() {

        assertRejectedWith(InvalidPurchaseReason.INVALID_ACCOUNT_ID,
                () -> ticketPurchaseValidator.validate(0L, adult));
    }

    @Test
    void givenNoRequests_shouldRejectWithNoTicketsRequested() {

        assertRejectedWith(InvalidPurchaseReason.NO_TICKETS_REQUESTED,
                () -> ticketPurchaseValidator.validate(1L));
    }

    @Test
    void givenMoreThanTwentyFiveTickets_shouldRejectWithTooManyTickets() {

        TicketTypeRequest tooMany = new TicketTypeRequest(Type.ADULT, 26);

        assertRejectedWith(InvalidPurchaseReason.TOO_MANY_TICKETS,
                () -> ticketPurchaseValidator.validate(1L, tooMany));
    }

    @Test
    void givenNoAdult_shouldRejectWithAdultRequired() {

        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 1);

        assertRejectedWith(InvalidPurchaseReason.ADULT_REQUIRED,
                () -> ticketPurchaseValidator.validate(1L, child));
    }

    // Order of the default validators

    @Test
    void givenBadAccountIdAndNoRequests_shouldReportTheAccountIdFirst() {

        assertRejectedWith(InvalidPurchaseReason.INVALID_ACCOUNT_ID,
                () -> ticketPurchaseValidator.validate(null));
    }

    @Test
    void givenANullRequest_shouldRejectCleanlyBeforeTheTicketRulesRun() {

        assertRejectedWith(InvalidPurchaseReason.INVALID_TICKET_REQUEST,
                () -> ticketPurchaseValidator.validate(1L, adult, null));
    }

    // The composite itself

    @Test
    void givenCustomValidators_shouldRunThemInOrderAndStopAtTheFirstFailure() {

        List<String> calls = new ArrayList<>();

        TicketPurchaseValidator first = (accountId, requests) -> calls.add("first");
        TicketPurchaseValidator failing = (accountId, requests) -> {
            calls.add("failing");
            throw new InvalidPurchaseException(InvalidPurchaseReason.ADULT_REQUIRED);
        };
        TicketPurchaseValidator never = (accountId, requests) -> calls.add("never");

        TicketPurchaseValidatorImpl composite = new TicketPurchaseValidatorImpl(List.of(first, failing, never));

        assertRejectedWith(InvalidPurchaseReason.ADULT_REQUIRED, () -> composite.validate(1L, adult));
        assertEquals(List.of("first", "failing"), calls);
    }
}