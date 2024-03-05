package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    
    private String nickname;
    private int partiteVinte;
    private int partitePerse;
    private int boardCardDimension;
    private List<Card> boardCard;
    private int remainingCards;

    public Player(String nickname){
        this.nickname = nickname;
        this.boardCardDimension = 1;
        boardCard = new ArrayList<>();
        this.remainingCards = this.boardCardDimension;
    }

    public void takeCardToBoard(Card c){
        this.boardCard.add(c);
    }

    public void showCard(){
        for(Card c : this.boardCard ){
            System.out.println(c + " " + c.getFaceUp());
        }
    }

    //Metodi Setter
    public void reduceBoardCardDimension(){
        this.boardCardDimension --;
    }
    public void reduceRemainingCards(){this.remainingCards-- ;}
    public void initializeBoardCard(){
        this.boardCard = new ArrayList<>();
    }

    public void initializaRemainingCard(){this.remainingCards = this.boardCardDimension;}

    public void incrementaPartiteVinte(){this.partiteVinte++;}
    public void incrementaPartitePerse(){this.partitePerse++;}
    
    //Metodi Getter
    public String getNickname(){return this.nickname;}

    public int getboardCardDimension(){
        return this.boardCardDimension;
    }

    //Metodi Setter
    public void setPartiteVinte(int nPartiteVinte){
        this.partiteVinte = nPartiteVinte;
    }
    public void setPartitePerse(int nPartitePerse){
        this.partitePerse = nPartitePerse;
    }

    public  List<Card> getBoardCards(){return this.boardCard;}

    public int getRemainingCards(){return this.remainingCards;};

    public int getPartiteVinte(){
        return this.partiteVinte;
    }
    public int getPartitePerse(){
        return this.partitePerse;
    }

    public Card getCardFromIndex(int index){
        return this.boardCard.get(index-1);
    }

}
