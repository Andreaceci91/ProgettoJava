package model;

/**
 * Represents a playing card with a seed, rank, and face-up status.
 */
public class Card {
    private CardSeed seed;
    private CardRank rank;
    private boolean faceUp;

    /**
     * Constructs a Card with the specified seed and rank.
     * @param seed the seed of the card
     * @param rank the rank of the card
     */
    public Card(CardSeed seed, CardRank rank){
        this.seed = seed;
        this.rank = rank;
        this.faceUp = false;
    }

    /**
     * Constructs a Card with only the specified rank.
     * @param rank the rank of the card
     */
    public Card(CardRank rank){
        this.seed = null;
        this.rank = rank;
    }

    /**
     * Gets the seed of the card.
     * @return the seed of the card
     */
    public CardSeed getSeed(){
        return this.seed;
    }

    /**
     * Gets the rank of the card.
     * @return the rank of the card
     */
    public CardRank getRank(){
        return this.rank;
    }

    /**
     * Gets the face-up status of the card.
     * @return true if the card is face-up, false otherwise
     */
    public boolean getFaceUp(){ return this.faceUp;}

    /**
     * Sets the face-up status of the card to true.
     */
    public void setFaceUpTrue(){this.faceUp = true;}

    /**
     * Returns a string representation of the card.
     * If the card has a seed, returns the seed followed by the rank.
     * If the card does not have a seed, returns only the rank.
     * @return a string representation of the card
     */
    @Override
    public String toString(){
        if(this.seed != null)
            return this.getSeed() + " " + this.getRank();
        else
            return "" + this.getRank();
    }
}
