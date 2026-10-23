package uk.gov.dwp.uc.pairtest.exception;
/**
 * Thrown when a ticket purchase breaks a business rule and so cannot be
 * processed.
 * <p>
 * Unchecked. The {@link #getReason() reason} identifies which rule was broken,
 * so callers and tests can tell failures apart without parsing the message.
 */
public class InvalidPurchaseException extends RuntimeException {

    /**
     * The business rules a purchase can break. Each reason carries a
     * human-readable message.
     */
    public enum InvalidPurchaseReason {
        /** The account ID is null, zero or negative. */
        INVALID_ACCOUNT_ID("Account ID must be greater than zero"),

        /** The purchase contains no ticket requests (null or empty). */
        NO_TICKETS_REQUESTED("At least one ticket request is required"),

        /** A ticket request is null or has no ticket type. */
        INVALID_TICKET_REQUEST("Ticket requests must not be null and must have a ticket type"),

        /** A ticket request asks for zero or a negative number of tickets. */
        INVALID_TICKET_COUNT("Number of tickets must be greater than zero"),

        /** The purchase has more than 25 tickets in total, infants included. */
        TOO_MANY_TICKETS("A maximum of 25 tickets can be purchased at a time"),

        /** The purchase has no adult ticket, so children and infants cannot be bought. */
        ADULT_REQUIRED("At least one adult ticket is required"),

         /** The purchase has more infants than adults; each infant sits on an adult's lap. */
        INFANTS_EXCEED_ADULTS("There cannot be more infants than adults");

        private final String message;

        InvalidPurchaseReason(String message) {
            this.message = message;
        }

         /**
         * Returns the human-readable description of this reason.
         *
         * @return the message explaining the rule that was broken
         */
        public String getMessage() {
            return message;
        }
    }

    /** The rule that was broken. */
    private final InvalidPurchaseReason reason;

    /**
     * Creates an exception for a broken rule. The exception's message is the
     * reason's {@link InvalidPurchaseReason#getMessage() message}.
     *
     * @param reason the rule that was broken, must not be {@code null}
     */
    public InvalidPurchaseException(InvalidPurchaseReason reason) {
        super(reason.getMessage());
        this.reason = reason;
    }

    /**
     * Returns the rule that was broken.
     *
     * @return the reason this purchase was rejected
     */
    public InvalidPurchaseReason getReason() {
        return reason;
    }
}