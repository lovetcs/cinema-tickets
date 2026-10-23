package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

/**
 * Works out what a purchase costs and how many seats it needs.
 * <p>
 * Performs no validation: it assumes the requests have already passed a
 * {@link TicketPurchaseValidator}. Requests of the same type are added
 * together.
 */
public interface TicketCalculator {

    /**
     * Calculates the total price of the requested tickets.
     *
     * @param ticketTypeRequests the tickets requested, assumed to be valid
     * @return the total price in whole pounds; infant tickets cost nothing
     */
    int calculateTotalPrice(TicketTypeRequest... ticketTypeRequests);

    /**
     * Calculates the number of seats to reserve for the requested tickets.
     *
     * @param ticketTypeRequests the tickets requested, assumed to be valid
     * @return the number of seats needed; infants are not counted because
     *         they sit on an adult's lap
     */
    int calculateTotalSeats(TicketTypeRequest... ticketTypeRequests);
}