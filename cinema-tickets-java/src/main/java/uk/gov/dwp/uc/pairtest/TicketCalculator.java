package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public class TicketCalculator {


    public int calculateTotalPrice(TicketTypeRequest... ticketTypeRequests) {

        int totalPrice = 0;

        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {

            int typePrice = ticketTypeRequest.getTicketType().getPrice();
            int noOfTickets = ticketTypeRequest.getNoOfTickets();

            totalPrice += typePrice * noOfTickets;
        }

        return totalPrice;
    }

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