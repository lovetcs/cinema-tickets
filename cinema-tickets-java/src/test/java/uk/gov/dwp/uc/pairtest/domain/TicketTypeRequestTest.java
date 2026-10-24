package uk.gov.dwp.uc.pairtest.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

public class TicketTypeRequestTest {

    @Test
    void givenATypeAndCount_shouldReturnTheType() {

        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(Type.ADULT, 3);

        Type ticketType = ticketTypeRequest.getTicketType();

        assertEquals(Type.ADULT, ticketType);
    }

    @Test
    void givenATypeAndCount_shouldReturnTheCount() {

        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(Type.CHILD, 5);

        int noOfTickets = ticketTypeRequest.getNoOfTickets();

        assertEquals(5, noOfTickets);
    }

    @Test
    void shouldPriceAdultAtTwentyFivePoundsWithASeat() {
        assertEquals(25, Type.ADULT.getPrice());
        assertTrue(Type.ADULT.requiresSeat());
    }

    @Test
    void shouldPriceChildAtFifteenPoundsWithASeat() {
        assertEquals(15, Type.CHILD.getPrice());
        assertTrue(Type.CHILD.requiresSeat());
    }

    @Test
    void shouldMakeInfantFreeWithoutASeat() {
        assertEquals(0, Type.INFANT.getPrice());
        assertFalse(Type.INFANT.requiresSeat());
    }

}