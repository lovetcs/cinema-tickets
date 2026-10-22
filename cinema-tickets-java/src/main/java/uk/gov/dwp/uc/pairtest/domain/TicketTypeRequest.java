package uk.gov.dwp.uc.pairtest.domain;

/**
 * Immutable Object
 */

public final class TicketTypeRequest {

    private final int noOfTickets;
    private final Type type;

    public TicketTypeRequest(Type type, int noOfTickets) {
        this.type = type;
        this.noOfTickets = noOfTickets;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    public enum Type {
        ADULT(25, true),
        CHILD(15, true),
        INFANT(0, false);

        private final int price;
        private final boolean requiresSeat;

        Type(int price, boolean requiresSeat) {
            this.price = price;
            this.requiresSeat = requiresSeat;
        }

        public int getPrice() {
            return price;
        }

        public boolean requiresSeat() {
            return requiresSeat;
        }
    }

}
