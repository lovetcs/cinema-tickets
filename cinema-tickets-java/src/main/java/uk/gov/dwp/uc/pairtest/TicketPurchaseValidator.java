package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public interface TicketPurchaseValidator {

    /**
     * @throws InvalidPurchaseException if the purchase breaks any business rule
     */
    void validate(Long accountId, TicketTypeRequest... ticketTypeRequests);
}