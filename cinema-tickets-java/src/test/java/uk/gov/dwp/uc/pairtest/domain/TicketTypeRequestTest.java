package uk.gov.dwp.uc.pairtest.domain;

import org.junit.jupiter.api.Test;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

import static org.junit.jupiter.api.Assertions.*;

class TicketTypeRequestTest {

    


    @Test
    void getTicketTypeReturnsTheTypeProvided() {

        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(Type.ADULT, 3);

        Type ticketType = ticketTypeRequest.getTicketType();

        assertEquals(Type.ADULT, ticketType);

    }

    @Test 
    void getNoOfTicketsReturnsTheCountProvided() {

        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(Type.CHILD, 5);

        int noOfTickets = ticketTypeRequest.getNoOfTickets();

        assertEquals(5, noOfTickets);
    }

}