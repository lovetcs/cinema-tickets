package uk.gov.dwp.uc.pairtest.validation;

import uk.gov.dwp.uc.pairtest.TicketPurchaseValidator;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException.InvalidPurchaseReason;

/**
 * Checks that the purchaser's account ID is usable: it must not be null and
 * must be greater than zero.
 * <p>
 * Says nothing about the ticket requests themselves. That is left to the other
 * validators.
 */
public class AccountIdValidator implements TicketPurchaseValidator {

    /**
     * Rejects a null, zero or negative account ID.
     *
     * @param accountId          the purchaser's account ID
     * @param ticketTypeRequests not used by this rule
     * @throws InvalidPurchaseException with reason {@code INVALID_ACCOUNT_ID}
     *         if the account ID is null, zero or negative
     */
    @Override
    public void validate(Long accountId, TicketTypeRequest... ticketTypeRequests) {

        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException(InvalidPurchaseReason.INVALID_ACCOUNT_ID);
        }
    }
}