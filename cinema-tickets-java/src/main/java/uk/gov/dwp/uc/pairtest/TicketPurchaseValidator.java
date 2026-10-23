package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

/**
 * Decides whether a ticket purchase is allowed.
 * <p>
 * Implementations have no side effects: they either return normally or throw
 * {@link InvalidPurchaseException}. Nothing should be paid for or reserved
 * until validation has passed.
 */
public interface TicketPurchaseValidator {

    /**
     * Checks a purchase against the business rules.
     *
     * @param accountId          the purchaser's account, must be greater than zero
     * @param ticketTypeRequests the tickets requested, at least one request and
     *                           at least one adult ticket in total
     * @throws InvalidPurchaseException if any rule is broken; its
     *         {@link InvalidPurchaseException#getReason() reason} says which one
     */
    void validate(Long accountId, TicketTypeRequest... ticketTypeRequests);
}