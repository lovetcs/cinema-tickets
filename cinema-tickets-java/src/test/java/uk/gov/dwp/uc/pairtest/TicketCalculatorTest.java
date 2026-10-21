package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

import static org.junit.jupiter.api.Assertions.*;

class TicketCalculatorTest {

    @Test
    void oneAdultTicketCosts25Pounds() {

        TicketTypeRequest adultTicketTypeRequest = new TicketTypeRequest(Type.ADULT, 1);

        TicketCalculator ticketCalculator = new TicketCalculator();

        int calculatedPrice = ticketCalculator.calculateTotalPrice(adultTicketTypeRequest);

        assertEquals(25, calculatedPrice);
    }

}