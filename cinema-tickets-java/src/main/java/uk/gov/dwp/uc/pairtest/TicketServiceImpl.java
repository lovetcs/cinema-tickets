package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

/**
 * Default {@link TicketService}. Coordinates a purchase but contains no
 * business rules of its own:
 * <ol>
 *   <li>validates the request, which rejects invalid purchases before anything
 *       is paid for or reserved;</li>
 *   <li>calculates the total price and the number of seats;</li>
 *   <li>takes payment through {@link TicketPaymentService};</li>
 *   <li>reserves seats through {@link SeatReservationService}.</li>
 * </ol>
 * Payment is requested before seats. If payment throws, no seats are reserved.
 * If seat reservation fails after payment succeeds, nothing is rolled back,
 * because both external services are assumed to always succeed.
 */
public class TicketServiceImpl implements TicketService {

    private final TicketPurchaseValidator ticketPurchaseValidator;
    private final TicketCalculator ticketCalculator;
    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;

    /**
     * Creates a service from its collaborators.
     *
     * @param ticketPurchaseValidator checks that a purchase is allowed
     * @param ticketCalculator        works out the price and number of seats
     * @param ticketPaymentService    external service that takes payment
     * @param seatReservationService  external service that reserves seats
     */
    public TicketServiceImpl(TicketPurchaseValidator ticketPurchaseValidator, TicketCalculator ticketCalculator,
            TicketPaymentService ticketPaymentService, SeatReservationService seatReservationService) {

        this.ticketPurchaseValidator = ticketPurchaseValidator;
        this.ticketCalculator = ticketCalculator;
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
    }

    /**
     * Validates the purchase, then pays for it and reserves the seats.
     * Nothing is paid for or reserved if validation fails.
     *
     * @param accountId          the purchaser's account, must be greater than zero
     * @param ticketTypeRequests the tickets requested, at most 25 in total and
     *                           including at least one adult
     * @throws InvalidPurchaseException if the purchase breaks a business rule
     */
    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests)
            throws InvalidPurchaseException {

        this.ticketPurchaseValidator.validate(accountId, ticketTypeRequests);

        int totalAmount = this.ticketCalculator.calculateTotalPrice(ticketTypeRequests);
        int totalSeats = this.ticketCalculator.calculateTotalSeats(ticketTypeRequests);

        this.ticketPaymentService.makePayment(accountId, totalAmount);
        this.seatReservationService.reserveSeat(accountId, totalSeats);

    }

}
