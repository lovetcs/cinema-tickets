package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validation.AccountIdValidator;
import uk.gov.dwp.uc.pairtest.validation.RequestsValidator;
import uk.gov.dwp.uc.pairtest.validation.RulesValidator;

/**
 * Default {@link TicketPurchaseValidator}. Rejects a purchase with an
 * {@link InvalidPurchaseException} carrying the first rule that fails.
 * <p>
 * Checks run in this order, and the order matters because each step relies on
 * the ones before it:
 * <ol>
 *   <li>the account ID is not null and is greater than zero;</li>
 *   <li>there is at least one request, and every request is non-null with a
 *       ticket type and a positive count;</li>
 *   <li>there are at most 25 tickets in total, infants included, at least one
 *       adult ticket, and no more infants than adults.</li>
 * </ol>
 * Totals are summed as {@code long} so very large counts cannot overflow past
 * the 25-ticket limit.
 */
public class TicketPurchaseValidatorImpl implements TicketPurchaseValidator {

    
    private final TicketPurchaseValidator accountIdValidator = new AccountIdValidator();
    private final TicketPurchaseValidator requestsValidator = new RequestsValidator();
    private final TicketPurchaseValidator rulesValidator = new RulesValidator();

    @Override 
    public void validate(Long accountId, TicketTypeRequest... ticketTypeRequests) {

        accountIdValidator.validate(accountId, ticketTypeRequests);

        requestsValidator.validate(accountId, ticketTypeRequests);

        rulesValidator.validate(accountId, ticketTypeRequests);

    }


}
