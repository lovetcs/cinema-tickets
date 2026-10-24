package uk.gov.dwp.uc.pairtest.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import org.junit.jupiter.api.function.Executable;
import uk.gov.dwp.uc.pairtest.TicketPurchaseValidator;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException.InvalidPurchaseReason;

public class AccountIdValidatorTest {

    private final TicketPurchaseValidator accountIdValidator = new AccountIdValidator();

    private void assertRejectedWith(InvalidPurchaseReason expected, Executable action) {
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, action);
        assertEquals(expected, exception.getReason());
    }

    @Test
    void shouldThrowWhenAccountIdIsNull() {

        Long accountId = null;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);

        assertRejectedWith(InvalidPurchaseReason.INVALID_ACCOUNT_ID,
                () -> accountIdValidator.validate(accountId, adult));
    }

    @Test
    void shouldThrowWhenAccountIdIsZero() {

        Long accountId = 0L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);

        assertRejectedWith(InvalidPurchaseReason.INVALID_ACCOUNT_ID,
                () -> accountIdValidator.validate(accountId, adult));
    }

    @Test
    void shouldThrowWhenAccountIdIsLessThanZero() {

        Long accountId = -1L;
        TicketTypeRequest adult = new TicketTypeRequest(Type.ADULT, 2);

        assertRejectedWith(InvalidPurchaseReason.INVALID_ACCOUNT_ID,
                () -> accountIdValidator.validate(accountId, adult));
    }
}
