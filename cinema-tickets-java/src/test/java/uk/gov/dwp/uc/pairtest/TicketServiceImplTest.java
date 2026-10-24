package uk.gov.dwp.uc.pairtest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException.InvalidPurchaseReason;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    @Mock
    private TicketPaymentService ticketPaymentService;
    @Mock
    private TicketCalculator ticketCalculator;
    @Mock
    private TicketPurchaseValidator ticketPurchaseValidator;
    @Mock
    private SeatReservationService seatReservationService;

    private TicketServiceImpl ticketServiceImpl;

    private final Long accountId = 1L;

    private final TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 5);
    private final TicketTypeRequest child = new TicketTypeRequest(Type.CHILD, 2);
    private final TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 1);
    private final TicketTypeRequest infantInvalid = new TicketTypeRequest(Type.INFANT, 6);

    @BeforeEach
    void setUp() {
        ticketServiceImpl = new TicketServiceImpl(ticketPurchaseValidator, ticketCalculator, ticketPaymentService,
                seatReservationService);

    }

    @Test
    void givenAPurchase_shouldValidateTheRequest() {

        ticketServiceImpl.purchaseTickets(accountId, adult, child, infant);

        verify(ticketPurchaseValidator).validate(accountId, adult, child, infant);
    }

    @Test
    void givenAPurchase_shouldPayTheCalculatedPrice() {

        when(ticketCalculator.calculateTotalPrice(adult, child, infant)).thenReturn(100);

        ticketServiceImpl.purchaseTickets(accountId, adult, child, infant);

        verify(ticketPaymentService).makePayment(accountId, 100);
    }

    @Test
    void givenAPurchase_shouldReserveTheCalculatedSeats() {

        when(ticketCalculator.calculateTotalSeats(adult, child, infant)).thenReturn(10);

        ticketServiceImpl.purchaseTickets(accountId, adult, child, infant);

        verify(seatReservationService).reserveSeat(accountId, 10);
    }

    @Test
    void givenInvalidRequest_shouldNotPayOrReserveSeats() {

        doThrow(new InvalidPurchaseException(InvalidPurchaseReason.INFANTS_EXCEED_ADULTS))
                .when(ticketPurchaseValidator)
                .validate(accountId, adult, child, infantInvalid);

        assertThrows(InvalidPurchaseException.class,
                () -> ticketServiceImpl.purchaseTickets(accountId, adult, child, infantInvalid));

        verifyNoInteractions(ticketPaymentService, seatReservationService);
    }

    @Test
    void givenPaymentFails_shouldNotReserveSeats() {

        doThrow(new RuntimeException("Payment failed"))
                .when(ticketPaymentService)
                .makePayment(accountId, 100);

        when(ticketCalculator.calculateTotalPrice(adult, child, infant)).thenReturn(100);

        assertThrows(RuntimeException.class,
                () -> ticketServiceImpl.purchaseTickets(accountId, adult, child, infant));

        verify(ticketPaymentService).makePayment(accountId, 100);
        verifyNoInteractions(seatReservationService);
    }

    @Test
    void givenAValidPurchase_shouldPayBeforeReservingSeats() {

        ticketServiceImpl.purchaseTickets(accountId, adult, child, infant);

        InOrder inOrder = inOrder(ticketPaymentService, seatReservationService);
        inOrder.verify(ticketPaymentService).makePayment(eq(accountId), anyInt());
        inOrder.verify(seatReservationService).reserveSeat(eq(accountId), anyInt());
    }

}
