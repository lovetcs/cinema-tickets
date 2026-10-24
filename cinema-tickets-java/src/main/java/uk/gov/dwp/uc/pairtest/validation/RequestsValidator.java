package uk.gov.dwp.uc.pairtest.validation;

import uk.gov.dwp.uc.pairtest.TicketPurchaseValidator;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException.InvalidPurchaseReason;

/**
 * Checks that the ticket requests themselves are well formed: there is at
 * least one, none is null, each has a ticket type, and each asks for a
 * positive number of tickets.
 * <p>
 * Says nothing about how many tickets may be bought or in what mix. That is
 * left to the other validators, which assume this one has already run.
 */
public class RequestsValidator implements TicketPurchaseValidator {

    /**
     * Rejects a missing or empty set of requests, and any request that is null,
     * has no type or has a non-positive count.
     *
     * @param accountId          not used by this rule
     * @param ticketTypeRequests the tickets requested
     * @throws InvalidPurchaseException with reason {@code NO_TICKETS_REQUESTED},
     *         {@code INVALID_TICKET_REQUEST} or {@code INVALID_TICKET_COUNT}
     */
    @Override
    public void validate(Long accountId, TicketTypeRequest... ticketTypeRequests) {

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
}
