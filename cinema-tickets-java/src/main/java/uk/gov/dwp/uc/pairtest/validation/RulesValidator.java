package uk.gov.dwp.uc.pairtest.validation;

import uk.gov.dwp.uc.pairtest.TicketPurchaseValidator;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException.InvalidPurchaseReason;


/**
 * Enforces the rules about how many tickets can be bought and in what mix:
 * <ul>
 *   <li>at most 25 tickets in total, infants included;</li>
 *   <li>at least one adult ticket;</li>
 *   <li>no more infants than adults, as each infant sits on an adult's lap.</li>
 * </ul>
 * The checks run in that order, so the first rule broken decides the
 * {@link InvalidPurchaseException} thrown.
 * <p>
 * Assumes the requests are well formed (none null, each with a type and a
 * positive count), which {@link RequestsValidator} checks, so it must be
 * run after that validator. Each request's count is checked on its own first,
 * so a huge count is rejected before it is added to the running total.
 */
public class RulesValidator implements TicketPurchaseValidator {

    /** The most tickets, infants included, that one purchase may contain. */
    private static final int MAX_TICKETS = 25;


    /**
     * Rejects a purchase that has too many tickets, no adult, or more infants
     * than adults.
     *
     * @param accountId          not used by this rule
     * @param ticketTypeRequests the tickets requested, assumed to be well formed
     * @throws InvalidPurchaseException with reason {@code TOO_MANY_TICKETS},
     *         {@code ADULT_REQUIRED} or {@code INFANTS_EXCEED_ADULTS}
     */
    @Override
    public void validate(Long accountId, TicketTypeRequest... ticketTypeRequests) {

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
