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

    @BeforeEach 
    void setUp() {
        ticketServiceImpl = new TicketServiceImpl(ticketPurchaseValidator, ticketCalculator, ticketPaymentService, seatReservationService);
    }

    @Test 
    void validatesTheRequestBeforeDoingAnythingElse(){

        Long accountId = 1L;

        TicketTypeRequest infant = new TicketTypeRequest(Type.INFANT, 2);
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 1);

        ticketServiceImpl.purchaseTickets(accountId, adult, infant);

        verify(ticketPurchaseValidator).validate(accountId, adult, infant);

    }


}
