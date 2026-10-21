package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketPurchaseValidator {
    
    public void validate(Long accountId, TicketTypeRequest... ticketTypeRequests) {

        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException();
        }

        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException();
        }

        int adultCount = 0;
        int infantCount = 0;
        int totalTickets = 0;

        for (TicketTypeRequest request : ticketTypeRequests) {
            
            if (request == null) {
                throw new InvalidPurchaseException();
            }

            if (request.getTicketType() == null) {
                throw new InvalidPurchaseException();
            }

            int count = request.getNoOfTickets();
            
            if (count <= 0) {
                throw new InvalidPurchaseException();
            }
            
            totalTickets += count;

            switch (request.getTicketType()) {
                
                case ADULT -> adultCount += count;
                case INFANT -> infantCount += count;
            }
        }

        if (totalTickets > 25) {
            throw new InvalidPurchaseException();
        }

        if (adultCount == 0) {
            throw new InvalidPurchaseException();
        }

        if (infantCount > adultCount) {
            throw new InvalidPurchaseException();
        }

    }

}
