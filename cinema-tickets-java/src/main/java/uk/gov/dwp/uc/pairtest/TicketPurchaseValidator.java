package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketPurchaseValidator {

    private static final int MAX_TICKETS = 25;

    public void validate(Long accountId, TicketTypeRequest... ticketTypeRequests) {

        validateAccountId(accountId);

        validateRequests(ticketTypeRequests);

        validateTicketRules(ticketTypeRequests);

    }

    private void validateAccountId(Long accountId) {

        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException();
        }
    }

    private void validateRequests(TicketTypeRequest... ticketTypeRequests) {

        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException();
        }

        for (TicketTypeRequest request : ticketTypeRequests) {

            if (request == null) {
                throw new InvalidPurchaseException();
            }

            if (request.getTicketType() == null) {
                throw new InvalidPurchaseException();
            }

            if (request.getNoOfTickets() <= 0) {
                throw new InvalidPurchaseException();
            }
        }

    }

    private void validateTicketRules(TicketTypeRequest... ticketTypeRequests) {

        int adultCount = 0;
        int infantCount = 0;
        int totalTickets = 0;

        for (TicketTypeRequest request : ticketTypeRequests) {

            int count = request.getNoOfTickets();

            totalTickets += count;

            Type type = request.getTicketType();

            if (type == Type.ADULT) {
                adultCount += count;
            } else if (type == Type.INFANT) {
                infantCount += count;
            }

        }

        if (totalTickets > MAX_TICKETS) {
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
