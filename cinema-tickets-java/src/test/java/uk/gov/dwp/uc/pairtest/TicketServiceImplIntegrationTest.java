package uk.gov.dwp.uc.pairtest;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
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

        TicketCalculator ticketCalculator = new TicketCalculator();
        TicketPurchaseValidator ticketPurchaseValidator = new TicketPurchaseValidator();

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
}
