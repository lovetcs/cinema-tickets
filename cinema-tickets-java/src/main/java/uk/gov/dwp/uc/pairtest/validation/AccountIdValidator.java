package uk.gov.dwp.uc.pairtest.validation;

import uk.gov.dwp.uc.pairtest.TicketPurchaseValidator;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException.InvalidPurchaseReason;

public class AccountIdValidator implements TicketPurchaseValidator{

     /** Rejects a null, zero or negative account ID. */
    @Override 
    public void validate(Long accountId, TicketTypeRequest... ticketTypeRequests) {
     
        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException(InvalidPurchaseReason.INVALID_ACCOUNT_ID);
        }    
    }
}
