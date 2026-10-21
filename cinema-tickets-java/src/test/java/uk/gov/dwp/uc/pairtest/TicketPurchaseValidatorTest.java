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

        Integer accountId = 1;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 1);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 1);

        assertDoesNotThrow(() -> ticketPurchaseValidator.validate(accountId, adult, child, infant));

    }

    @Test
    void shouldThrowWhenAccountIdIsNull() {

        Integer accountId = null;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);

        assertThrows(InvalidPurchaseException.class, () -> ticketPurchaseValidator.validate(accountId, adult));
    }

    @Test
    void shouldThrowWhenAccountIdIsEqualsOrLessThanZero() {

        Integer accountId = 0;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);

        assertThrows(InvalidPurchaseException.class, () -> ticketPurchaseValidator.validate(accountId, adult));
    }
}
