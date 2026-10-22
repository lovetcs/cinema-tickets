package uk.gov.dwp.uc.pairtest.exception;

public class InvalidPurchaseException extends RuntimeException {

    public enum InvalidPurchaseReason {
        INVALID_ACCOUNT_ID("Account ID must be greater than zero"),
        NO_TICKETS_REQUESTED("At least one ticket request is required"),
        INVALID_TICKET_REQUEST("Ticket requests must not be null and must have a ticket type"),
        INVALID_TICKET_COUNT("Number of tickets must be greater than zero"),
        TOO_MANY_TICKETS("A maximum of 25 tickets can be purchased at a time"),
        ADULT_REQUIRED("At least one adult ticket is required"),
        INFANTS_EXCEED_ADULTS("There cannot be more infants than adults");

        private final String message;

        InvalidPurchaseReason(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    private final InvalidPurchaseReason reason;

    public InvalidPurchaseException(InvalidPurchaseReason reason) {
        super(reason.getMessage());
        this.reason = reason;
    }

    public InvalidPurchaseReason getReason() {
        return reason;
    }
}