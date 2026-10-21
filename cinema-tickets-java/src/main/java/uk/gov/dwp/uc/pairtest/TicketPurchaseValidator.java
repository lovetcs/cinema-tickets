package uk.gov.dwp.uc.pairtest;

import java.util.Arrays;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketPurchaseValidator {
    
    public void validate(Integer accountId, TicketTypeRequest... ticketTypeRequests) {

        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException();
        }

        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException();
        }

        int totalTicketCount = 0;

        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {

            if (ticketTypeRequest.getNoOfTickets() <= 0) {
                throw new InvalidPurchaseException();
            } 
            
            totalTicketCount += ticketTypeRequest.getNoOfTickets();
        }

        if (totalTicketCount > 25) {
            throw new InvalidPurchaseException();
        }

        boolean hasAdult = Arrays.stream(ticketTypeRequests) .anyMatch(request -> request.getTicketType() == Type.ADULT);

        if (!hasAdult) {
            throw new InvalidPurchaseException();
        }

        /*if (!Arrays.asList(ticketTypeRequests).contains(Type.ADULT)) {
            throw new InvalidPurchaseException();
        }
            */

    }

}
