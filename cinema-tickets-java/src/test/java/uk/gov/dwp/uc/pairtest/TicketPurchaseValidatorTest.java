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

    private final TicketPurchaseValidatorImpl ticketPurchaseValidator = new TicketPurchaseValidatorImpl();

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

 

    

   

   

   


}
