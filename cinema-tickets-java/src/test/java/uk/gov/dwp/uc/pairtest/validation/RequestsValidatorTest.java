package uk.gov.dwp.uc.pairtest.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import uk.gov.dwp.uc.pairtest.TicketPurchaseValidator;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException.InvalidPurchaseReason;

public class RequestsValidatorTest {

    private final TicketPurchaseValidator requestsValidator = new RequestsValidator();

    private void assertRejectedWith(InvalidPurchaseReason expected, Executable action) {
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, action);
        assertEquals(expected, exception.getReason());
    }

    @Test
    void givenNoRequests_shouldRejectWithNoTicketsRequested() {

        Long accountId = 1L;

        assertRejectedWith(InvalidPurchaseReason.NO_TICKETS_REQUESTED,
                () -> requestsValidator.validate(accountId));
    }

    @Test
    void givenNullRequests_shouldRejectWithNoTicketsRequested() {

        Long accountId = 1L;

        assertRejectedWith(InvalidPurchaseReason.NO_TICKETS_REQUESTED,
                () -> requestsValidator.validate(accountId, (TicketTypeRequest[]) null));
    }

    @Test
    void givenZeroTicketCount_shouldRejectWithInvalidTicketCount() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 0);

        assertRejectedWith(InvalidPurchaseReason.INVALID_TICKET_COUNT,
                () -> requestsValidator.validate(accountId, adult));
    }

    @Test
    void givenNegativeTicketCount_shouldRejectWithInvalidTicketCount() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, -1);

        assertRejectedWith(InvalidPurchaseReason.INVALID_TICKET_COUNT,
                () -> requestsValidator.validate(accountId, adult));
    }

    @Test
    void givenNullTicketType_shouldRejectWithInvalidTicketRequest() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);
        TicketTypeRequest noType = new TicketTypeRequest(null, 1);

        assertRejectedWith(InvalidPurchaseReason.INVALID_TICKET_REQUEST,
                () -> requestsValidator.validate(accountId, adult, noType));
    }

    @Test
    void givenNullRequestInTheArray_shouldRejectWithInvalidTicketRequest() {

        Long accountId = 1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);

        assertRejectedWith(InvalidPurchaseReason.INVALID_TICKET_REQUEST,
                () -> requestsValidator.validate(accountId, adult, null));
    }

}