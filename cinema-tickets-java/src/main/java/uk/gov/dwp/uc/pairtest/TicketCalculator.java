package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public interface TicketCalculator {

    int calculateTotalPrice(TicketTypeRequest... ticketTypeRequests);

    int calculateTotalSeats(TicketTypeRequest... ticketTypeRequests);
}