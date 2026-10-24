package uk.gov.dwp.uc.pairtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

public class TicketCalculatorTest {

    private final TicketCalculatorImpl ticketCalculator = new TicketCalculatorImpl();

    // Price

    @ParameterizedTest
    @CsvSource({
            "ADULT,  1, 25",
            "ADULT,  3, 75",
            "CHILD,  1, 15",
            "INFANT, 1, 0"
    })
    void givenASingleTicketType_shouldCalculateThePrice(Type type, int noOfTickets, int expectedPrice) {

        int price = ticketCalculator.calculateTotalPrice(new TicketTypeRequest(type, noOfTickets));

        assertEquals(expectedPrice, price);
    }

    @Test
    void givenAMixOfTicketTypes_shouldCalculateTheTotalPrice() {

        TicketTypeRequest adults = new TicketTypeRequest(Type.ADULT, 9);
        TicketTypeRequest children = new TicketTypeRequest(Type.CHILD, 2);
        TicketTypeRequest infants = new TicketTypeRequest(Type.INFANT, 3);

        int price = ticketCalculator.calculateTotalPrice(adults, children, infants);

        assertEquals(255, price);
    }

    @Test
    void givenTheSameTypeInSeparateRequests_shouldAddThemTogetherForThePrice() {

        TicketTypeRequest first = new TicketTypeRequest(Type.ADULT, 2);
        TicketTypeRequest second = new TicketTypeRequest(Type.ADULT, 3);

        int price = ticketCalculator.calculateTotalPrice(first, second);

        assertEquals(125, price);
    }

    // Seats

    @ParameterizedTest
    @CsvSource({
            "ADULT,  1,  1",
            "ADULT,  15, 15",
            "CHILD,  1,  1",
            "INFANT, 1,  0"
    })
    void givenASingleTicketType_shouldCalculateTheSeats(Type type, int noOfTickets, int expectedSeats) {

        int seats = ticketCalculator.calculateTotalSeats(new TicketTypeRequest(type, noOfTickets));

        assertEquals(expectedSeats, seats);
    }

    @Test
    void givenAMixOfTicketTypes_shouldNotCountInfantsAsSeats() {

        TicketTypeRequest adults = new TicketTypeRequest(Type.ADULT, 10);
        TicketTypeRequest children = new TicketTypeRequest(Type.CHILD, 3);
        TicketTypeRequest infants = new TicketTypeRequest(Type.INFANT, 6);

        int seats = ticketCalculator.calculateTotalSeats(adults, children, infants);

        assertEquals(13, seats);
    }

    @Test
    void givenTheSameTypeInSeparateRequests_shouldAddThemTogetherForTheSeats() {

        TicketTypeRequest first = new TicketTypeRequest(Type.ADULT, 2);
        TicketTypeRequest second = new TicketTypeRequest(Type.ADULT, 3);

        int seats = ticketCalculator.calculateTotalSeats(first, second);

        assertEquals(5, seats);
    }
}