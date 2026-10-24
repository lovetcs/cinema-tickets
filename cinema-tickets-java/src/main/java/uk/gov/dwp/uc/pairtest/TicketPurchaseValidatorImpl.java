package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException.InvalidPurchaseReason;
import uk.gov.dwp.uc.pairtest.validation.AccountIdValidator;
import uk.gov.dwp.uc.pairtest.validation.RequestsValidator;

/**
 * Default {@link TicketPurchaseValidator}. Rejects a purchase with an
 * {@link InvalidPurchaseException} carrying the first rule that fails.
 * <p>
 * Checks run in this order, and the order matters because each step relies on
 * the ones before it:
 * <ol>
 *   <li>the account ID is not null and is greater than zero;</li>
 *   <li>there is at least one request, and every request is non-null with a
 *       ticket type and a positive count;</li>
 *   <li>there are at most 25 tickets in total, infants included, at least one
 *       adult ticket, and no more infants than adults.</li>
 * </ol>
 * Totals are summed as {@code long} so very large counts cannot overflow past
 * the 25-ticket limit.
 */
public class TicketPurchaseValidatorImpl implements TicketPurchaseValidator {

    /** The most tickets, infants included, that one purchase may contain. */
    private static final int MAX_TICKETS = 25;
    private final TicketPurchaseValidator accountIdValidator = new AccountIdValidator();
    private final TicketPurchaseValidator requestsValidator = new RequestsValidator();

    @Override 
    public void validate(Long accountId, TicketTypeRequest... ticketTypeRequests) {

        accountIdValidator.validate(accountId, ticketTypeRequests);

        requestsValidator.validate(accountId, ticketTypeRequests);

        validateTicketRules(ticketTypeRequests);

    }


    /**
     * Rejects a missing or empty set of requests, and any request that is null,
     * has no type or has a non-positive count. Must run before
     * {@link #validateTicketRules}, which assumes every request is well formed.
     */
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

    /** Enforces the 25-ticket limit, the adult requirement and the infant limit. */
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
