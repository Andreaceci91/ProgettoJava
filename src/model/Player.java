package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    
    private String nickname;
    private Object avatar; //da implementare
    private int partiteGiocate;
    private int partiteVinte;
    private int partitePerse;
    private int lvPlayer;
    private int boardCardDimension;
    private List<Card> boardCard;
    private int remainingCards;

    public Player(String nickname){
        this.nickname = nickname;
        this.boardCardDimension = 2;
        boardCard = new ArrayList<>();
        this.remainingCards = this.boardCardDimension;
    }

    public void takeCardToBoard(Card c){
        this.boardCard.add(c);
    }

    public void showCard(){
//        System.out.println(" ");
//        System.out.println("Carte in mano del player " + this.nickname);
//        this.boardCard.forEach(System.out::println);
        for(Card c : this.boardCard ){
            System.out.println(c + " " + c.getFaceUp());
        }
    }

    public void showFirst(){ System.out.println(boardCard.getFirst()); }

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

    public  List<Card> getBoardCards(){return this.boardCard;}

    public int getRemainingCards(){return this.remainingCards;};

    public int getPartiteVinte(){
        return this.partiteVinte;
    }

    public Card getCardFromIndex(int index){
//        System.out.println(" ");
//        System.out.println(this.boardCard.get(index-1));
        return this.boardCard.get(index-1);
    }

}
