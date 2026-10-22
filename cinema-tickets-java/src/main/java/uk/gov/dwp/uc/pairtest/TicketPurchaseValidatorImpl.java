package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException.InvalidPurchaseReason;

public class TicketPurchaseValidatorImpl implements TicketPurchaseValidator {

    private static final int MAX_TICKETS = 25;

    public void validate(Long accountId, TicketTypeRequest... ticketTypeRequests) {

        validateAccountId(accountId);

        validateRequests(ticketTypeRequests);

        validateTicketRules(ticketTypeRequests);

    }

    private void validateAccountId(Long accountId) {

        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException(InvalidPurchaseReason.INVALID_ACCOUNT_ID);
        }
    }

    private void validateRequests(TicketTypeRequest... ticketTypeRequests) {

        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException(InvalidPurchaseReason.NO_TICKETS_REQUESTED);
        }

        for (TicketTypeRequest request : ticketTypeRequests) {

            if (request == null || request.getTicketType() == null) {
                throw new InvalidPurchaseException(InvalidPurchaseReason.INVALID_TICKET_REQUEST);
            }

            if (request.getNoOfTickets() <= 0) {
                throw new InvalidPurchaseException(InvalidPurchaseReason.INVALID_TICKET_COUNT);
            }
        }

    }

    private void validateTicketRules(TicketTypeRequest... ticketTypeRequests) {

        int adultCount = 0;
        int infantCount = 0;
        long totalTickets = 0L;

        for (TicketTypeRequest request : ticketTypeRequests) {

            int count = request.getNoOfTickets();

            if (count > MAX_TICKETS) {
                throw new InvalidPurchaseException(InvalidPurchaseReason.TOO_MANY_TICKETS);
            }

            totalTickets += count;

            if (totalTickets > MAX_TICKETS) {
                throw new InvalidPurchaseException(InvalidPurchaseReason.TOO_MANY_TICKETS);
            }

            Type type = request.getTicketType();

            if (type == Type.ADULT) {
                adultCount += count;
            } else if (type == Type.INFANT) {
                infantCount += count;
            }

        }

        if (adultCount == 0) {
            throw new InvalidPurchaseException(InvalidPurchaseReason.ADULT_REQUIRED);
        }

        if (infantCount > adultCount) {
            throw new InvalidPurchaseException(InvalidPurchaseReason.INFANTS_EXCEED_ADULTS);
        }
    }

}
