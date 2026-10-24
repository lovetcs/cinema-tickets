package uk.gov.dwp.uc.pairtest;

import java.util.List;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validation.AccountIdValidator;
import uk.gov.dwp.uc.pairtest.validation.RequestsValidator;
import uk.gov.dwp.uc.pairtest.validation.RulesValidator;

/**
 * Default {@link TicketPurchaseValidator}. A composite: it holds an ordered
 * list of validators and runs each one in turn, so the first rule that fails
 * decides the {@link InvalidPurchaseException} thrown.
 * <p>
 * The default validators run in this order, which matters because each one
 * assumes the ones before it have passed:
 * <ol>
 *   <li>{@link AccountIdValidator}: the account ID is not null and is greater
 *       than zero;</li>
 *   <li>{@link RequestsValidator}: there is at least one request, and every
 *       request is non-null with a ticket type and a positive count;</li>
 *   <li>{@link RulesValidator}: at most 25 tickets in total, infants
 *       included, at least one adult ticket, and no more infants than
 *       adults.</li>
 * </ol>
 * Adding a rule means writing one new validator and adding it to the list.
 */
public class TicketPurchaseValidatorImpl implements TicketPurchaseValidator {

    private final List<TicketPurchaseValidator> validators;

    /**
     * Creates a validator with the standard rules.
     */
    public TicketPurchaseValidatorImpl() {
        this(List.of(
                new AccountIdValidator(),
                new RequestsValidator(),
                new RulesValidator()));
    }

    /**
     * Creates a validator with the given rules.
     *
     * @param validators the validators to run, in the order they should run
     */
    public TicketPurchaseValidatorImpl(List<TicketPurchaseValidator> validators) {
        this.validators = List.copyOf(validators);
    }

    @Override
    public void validate(Long accountId, TicketTypeRequest... ticketTypeRequests) {
        for (TicketPurchaseValidator validator : validators) {
            validator.validate(accountId, ticketTypeRequests);
        }
    }
}