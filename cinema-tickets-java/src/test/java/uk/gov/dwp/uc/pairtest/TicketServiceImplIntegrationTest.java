package uk.gov.dwp.uc.pairtest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
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

        TicketCalculatorImpl ticketCalculator = new TicketCalculatorImpl();
        TicketPurchaseValidatorImpl ticketPurchaseValidator = new TicketPurchaseValidatorImpl();

        ticketServiceImpl = new TicketServiceImpl(ticketPurchaseValidator, ticketCalculator, ticketPaymentService,
                seatReservationService);
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

        assertThrows(InvalidPurchaseException.class,
                () -> ticketServiceImpl.purchaseTickets(accountId, adult, child, infant));

        verifyNoInteractions(ticketPaymentService, seatReservationService);
    }

    @Test
    void throwsWhenNoAdultsInRequest() {

        Long accountId = 1L;

        TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 11);
        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 5);

        assertThrows(InvalidPurchaseException.class,
                () -> ticketServiceImpl.purchaseTickets(accountId, child, infant));

        verifyNoInteractions(ticketPaymentService, seatReservationService);
    }

}
