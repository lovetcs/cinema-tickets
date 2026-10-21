package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketPurchaseValidator {
    
    public void validate(Integer accountId, TicketTypeRequest... ticketTypeRequests) {

        if (accountId == null || accountId <= 0
        ) {
            throw new InvalidPurchaseException();
        }

    }

}
