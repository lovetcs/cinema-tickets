package uk.gov.dwp.uc.pairtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.function.Executable;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException.InvalidPurchaseReason;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplIntegrationTest {

    @Mock
    private TicketPaymentService ticketPaymentService;

    @Mock
    private SeatReservationService seatReservationService;

    private TicketServiceImpl ticketServiceImpl;

    @BeforeEach
    void setUp() {

        TicketCalculator ticketCalculator = new TicketCalculatorImpl();
        TicketPurchaseValidator ticketPurchaseValidator = new TicketPurchaseValidatorImpl();

        ticketServiceImpl = new TicketServiceImpl(ticketPurchaseValidator, ticketCalculator, ticketPaymentService,
                seatReservationService);
    }

    private void assertRejectedWith(InvalidPurchaseReason expected, Executable action) {
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, action);
        assertEquals(expected, exception.getReason());
    }

    @Test
    void paysAndReservesSeatsForAMixOfTicketTypes() {

        Long accountId = 1L;

        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 5);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 2);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 1);

        ticketServiceImpl.purchaseTickets(accountId, adult, child, infant);

        verify(ticketPaymentService).makePayment(accountId, 155);
        verify(seatReservationService).reserveSeat(accountId, 7);
    }

    @Test
    void paysAndReservesSeatsForOneAdultWithInfant() {

        Long accountId = 1L;

        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 1);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 1);

        ticketServiceImpl.purchaseTickets(accountId, adult, infant);

        verify(ticketPaymentService).makePayment(accountId, 25);
        verify(seatReservationService).reserveSeat(accountId, 1);
    }

    @Test
    void paysAndReservesSeatsForExactly25Tickets() {

        Long accountId = 1L;

        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 10);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 10);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 5);

        ticketServiceImpl.purchaseTickets(accountId, adult, child, infant);

        verify(ticketPaymentService).makePayment(accountId, 400);
        verify(seatReservationService).reserveSeat(accountId, 20);
    }

    @Test
    void throwsWhenTicketsAreAboveTheMaximum() {

        Long accountId = 1L;

        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 10);
        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 11);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 5);

        assertRejectedWith(InvalidPurchaseReason.TOO_MANY_TICKETS,
                () -> ticketServiceImpl.purchaseTickets(accountId, adult, child, infant));
        verifyNoInteractions(ticketPaymentService, seatReservationService);
    }

    @Test
    void throwsWhenNoAdultsInRequest() {

        Long accountId = 1L;

        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 11);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 5);

        assertRejectedWith(InvalidPurchaseReason.ADULT_REQUIRED,
                () -> ticketServiceImpl.purchaseTickets(accountId, child, infant));

        verifyNoInteractions(ticketPaymentService, seatReservationService);
    }

}
