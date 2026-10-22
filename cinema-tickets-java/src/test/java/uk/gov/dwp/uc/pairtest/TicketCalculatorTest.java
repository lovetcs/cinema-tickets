package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

import static org.junit.jupiter.api.Assertions.*;

class TicketCalculatorTest {

    private final TicketCalculatorImpl ticketCalculator = new TicketCalculatorImpl();

    //  Price Calculation Tests
    @Test
    void calculatesPriceForOneAdultTicketType() {

        TicketTypeRequest adultTicketTypeRequest = new TicketTypeRequest(Type.ADULT, 1);

        int calculatedPrice = ticketCalculator.calculateTotalPrice(adultTicketTypeRequest);

        assertEquals(25, calculatedPrice);
    }

    @Test
    void calculatesPriceForOneChildTicketType() {

        TicketTypeRequest childTicketTypeRequest = new TicketTypeRequest(Type.CHILD, 1);

        int calculatedPrice = ticketCalculator.calculateTotalPrice(childTicketTypeRequest);

        assertEquals(15, calculatedPrice);
    }

    @Test
    void calculatesPriceForOneInfantTicketType() {

        TicketTypeRequest infantTicketTypeRequest = new TicketTypeRequest(Type.INFANT, 1);

        int calculatedPrice = ticketCalculator.calculateTotalPrice(infantTicketTypeRequest);

        assertEquals(0, calculatedPrice);
    }

    @Test
    void calculatesPriceForMultipleAdultTicketTypes() {

        TicketTypeRequest adultTicketTypeRequest = new TicketTypeRequest(Type.ADULT, 3);

        int calculatedPrice = ticketCalculator.calculateTotalPrice(adultTicketTypeRequest);

        assertEquals(75, calculatedPrice);
    }

    @Test
    void calculatesPriceForMixOfTicketTypes() {

        TicketTypeRequest adultTicketTypeRequest = new TicketTypeRequest(Type.ADULT, 9);
        TicketTypeRequest childTicketTypeRequest = new TicketTypeRequest(Type.CHILD, 2);
        TicketTypeRequest infantTicketTypeRequest = new TicketTypeRequest(Type.INFANT, 3);

        int calculatedPrice = ticketCalculator.calculateTotalPrice(adultTicketTypeRequest, childTicketTypeRequest, infantTicketTypeRequest);

        assertEquals(255, calculatedPrice);
    }

    @Test
    void calculatesPriceWhenSameTypeAppearsInSeparateRequests() {

        TicketTypeRequest adultTicketTypeRequestOne = new TicketTypeRequest(Type.ADULT, 2);
        TicketTypeRequest adultTicketTypeRequestTwo = new TicketTypeRequest(Type.ADULT, 3);

        int calculatedPrice = ticketCalculator.calculateTotalPrice(adultTicketTypeRequestOne, adultTicketTypeRequestTwo);

        assertEquals(125, calculatedPrice);
    }

    //  Seat Calculation Tests
    @Test
    void calculatesTotalSeatsForOneAdultTicketType() {

        TicketTypeRequest adultTicketTypeRequest = new TicketTypeRequest(Type.ADULT, 1);

        int noOfSeats = ticketCalculator.calculateTotalSeats(adultTicketTypeRequest);

        assertEquals(1, noOfSeats);
    }

    @Test
    void calculatesTotalSeatsForOneChildTicketType() {

        TicketTypeRequest childTicketTypeRequest = new TicketTypeRequest(Type.CHILD, 1);

        int noOfSeats = ticketCalculator.calculateTotalSeats(childTicketTypeRequest);

        assertEquals(1, noOfSeats);
    }

    @Test
    void calculatesTotalSeatsForOneInfantTicketType() {

        TicketTypeRequest infantTicketTypeRequest = new TicketTypeRequest(Type.INFANT, 1);

        int noOfSeats = ticketCalculator.calculateTotalSeats(infantTicketTypeRequest);

        assertEquals(0, noOfSeats);
    }

    @Test
    void calculatesTotalSeatsForMultipleAdultTicketType() {

        TicketTypeRequest adultTicketTypeRequest = new TicketTypeRequest(Type.ADULT, 15);

        int noOfSeats = ticketCalculator.calculateTotalSeats(adultTicketTypeRequest);

        assertEquals(15, noOfSeats);
    }

    @Test
    void calculatesTotalSeatsForMixOfTicketType() {

        TicketTypeRequest adultTicketTypeRequest = new TicketTypeRequest(Type.ADULT, 10);
        TicketTypeRequest childTicketTypeRequest = new TicketTypeRequest(Type.CHILD, 3);
        TicketTypeRequest infantTicketTypeRequest = new TicketTypeRequest(Type.INFANT, 6);

        int noOfSeats = ticketCalculator.calculateTotalSeats(adultTicketTypeRequest, childTicketTypeRequest, infantTicketTypeRequest);

        assertEquals(13, noOfSeats);
    }

    @Test
    void calculatesTotalSeatsWhenSameTypeAppearsInSeparateRequests() {

        TicketTypeRequest adultTicketTypeRequestOne = new TicketTypeRequest(Type.ADULT, 2);
        TicketTypeRequest adultTicketTypeRequestTwo = new TicketTypeRequest(Type.ADULT, 3);

        int noOfSeats = ticketCalculator.calculateTotalSeats(adultTicketTypeRequestOne, adultTicketTypeRequestTwo);

        assertEquals(5, noOfSeats);
    }

}