package model;

/**
 * Enum that represents the rank of a playing card.
 */
public enum CardRank {
    ASSO(1),
    DUE(2),
    TRE(3),
    QUATTRO(4),
    CINQUE(5),
    SEI(6),
    SETTE(7),
    OTTO(8),
    NOVE(9),
    DIECI(10),
    JACK(11),
    DONNA(12),
    RE(13), //WildCard
    JOLLY(14); //WildCard

    private final int value;

    /**
     * Constructs a CardRank with the specified value.
     * @param value the numerical value associated with the rank
     */
    CardRank(int value) {
        this.value = value;
    }

    /**
     * Returns the numerical value associated with the rank.
     * @return the value associated with the rank
     */
    public int rankToValue() {
        return value;
    }
}
