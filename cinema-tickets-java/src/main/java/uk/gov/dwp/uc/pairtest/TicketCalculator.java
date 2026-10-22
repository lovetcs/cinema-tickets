package uk.gov.dwp.uc.pairtest;

import java.util.Map;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public class TicketCalculator {

    private static final Map<Type, Integer> TICKET_PRICES = Map.of(
            Type.ADULT, 25,
            Type.CHILD, 15,
            Type.INFANT, 0);

    public int calculateTotalPrice(TicketTypeRequest... ticketTypeRequests) {

        int totalPrice = 0;

        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {

            Type type = ticketTypeRequest.getTicketType();
            int noOfTickets = ticketTypeRequest.getNoOfTickets();

            totalPrice += (TICKET_PRICES.get(type) * noOfTickets);
        }

        return totalPrice;
    }

    public int calculateTotalSeats(TicketTypeRequest... ticketTypeRequests) {
        int totalSeats = 0;

        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {

            Type type = ticketTypeRequest.getTicketType();
            int noOfTickets = ticketTypeRequest.getNoOfTickets();

            if (type == Type.ADULT || type == Type.CHILD) {
                totalSeats += noOfTickets;
            }
        }

        return totalSeats;
    }

}