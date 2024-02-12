package model;

import model.CardRank;
import model.CardSeed;

public class Card {
    private CardSeed seed;
    private CardRank rank; //impostato a String e non numero per gestire anche Jolly, J, K, Q
    private boolean faceUp;

    public Card(CardSeed seed, CardRank rank){
        this.seed = seed;
        this.rank = rank;
        this.faceUp = false;
    }

    public Card(CardRank rank){
        this.seed = null;
        this.rank = rank;
    }

    public CardSeed getSeed(){
        return this.seed;
    }

    public CardRank getRank(){
        return this.rank;
    }

    public boolean getFaceUp(){ return this.faceUp;}

    public void setFaceUpTrue(){this.faceUp = true;}

    @Override
    public String toString(){
        if(this.seed != null)
            return this.getSeed() + " " + this.getRank();
        else
            return "" + this.getRank();
    }
}
