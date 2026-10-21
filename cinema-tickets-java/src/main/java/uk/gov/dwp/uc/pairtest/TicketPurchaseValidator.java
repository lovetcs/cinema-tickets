package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketPurchaseValidator {
    
    public void validate(Integer accountId, TicketTypeRequest... ticketTypeRequests) {

        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException();
        }

        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException();
        }

        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {

            if (ticketTypeRequest.getNoOfTickets() <= 0) {
                throw new InvalidPurchaseException();
            }

        }

    }

}
