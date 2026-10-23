package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

/**
 * Default {@link TicketCalculator}. Each ticket type supplies its own price and
 * whether it needs a seat (see {@link Type}), so this class holds no pricing
 * rules of its own.
 * <p>
 * Does no validation. A caller that skips the validator would get a
 * {@link NullPointerException} for a null request, and could overflow
 * {@code int} for absurdly large counts.
 */
public class TicketCalculatorImpl implements TicketCalculator {

    @Override
    public int calculateTotalPrice(TicketTypeRequest... ticketTypeRequests) {

        int totalPrice = 0;

        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {

            int typePrice = ticketTypeRequest.getTicketType().getPrice();
            int noOfTickets = ticketTypeRequest.getNoOfTickets();

            totalPrice += typePrice * noOfTickets;
        }

        return totalPrice;
    }

    @Override
    public int calculateTotalSeats(TicketTypeRequest... ticketTypeRequests) {
        int totalSeats = 0;

        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {

            Type type = ticketTypeRequest.getTicketType();
            int noOfTickets = ticketTypeRequest.getNoOfTickets();

            if (type.requiresSeat()) {
                totalSeats += noOfTickets;
            }
        }

        return totalSeats;
    }

}