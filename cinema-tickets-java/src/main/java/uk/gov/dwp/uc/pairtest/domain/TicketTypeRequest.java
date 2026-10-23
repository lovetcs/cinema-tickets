package uk.gov.dwp.uc.pairtest.domain;

/**
 * A request for a number of tickets of a single {@link Type}.
 * <p>
 * Immutable: the class is final and both fields are final with no setters,
 * so an instance can be shared safely.
 * <p>
 * The constructor does not validate its arguments. Rules such as "the count
 * must be positive" are enforced by the purchase validator.
 */
public final class TicketTypeRequest {

    private final int noOfTickets;
    private final Type type;

    /**
     * @param type        the kind of ticket requested
     * @param noOfTickets how many tickets of that kind are requested
     */
    public TicketTypeRequest(Type type, int noOfTickets) {
        this.type = type;
        this.noOfTickets = noOfTickets;
    }

    /**
     * @return the number of tickets requested, as supplied by the caller
     */
    public int getNoOfTickets() {
        return noOfTickets;
    }

    /**
     * @return the type of ticket requested, as supplied by the caller
     */
    public Type getTicketType() {
        return type;
    }

    /**
     * The kinds of ticket that can be bought. Each type knows its own price
     * and whether it needs a seat, so a new type cannot be added without both.
     */
    public enum Type {

        /** Costs £25 and is allocated a seat. */
        ADULT(25, true),
        /** Costs £15 and is allocated a seat. */
        CHILD(15, true),
        /** Costs £0 and is not allocated a seat. (Infants sit on Adult's lap) */
        INFANT(0, false);

        private final int price;
        private final boolean requiresSeat;

        Type(int price, boolean requiresSeat) {
            this.price = price;
            this.requiresSeat = requiresSeat;
        }

        /**
         * @return the price of one ticket of this type, in whole pounds
         */
        public int getPrice() {
            return price;
        }

        /**
         * @return {@code true} if a ticket of this type is allocated a seat
         */
        public boolean requiresSeat() {
            return requiresSeat;
        }
    }

}
