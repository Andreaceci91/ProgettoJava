package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a player in game.
 */
public class Player {
    
    private String nickname;
    private int partiteVinte;
    private int partitePerse;
    private int boardCardDimension;
    private List<Card> boardCard;
    private int remainingCards;

    /**
     * Constructor for the Player class.
     *
     * @param nickname The player's nickname.
     */
    public Player(String nickname){
        this.nickname = nickname;
        this.boardCardDimension = 10;
        boardCard = new ArrayList<>();
        this.remainingCards = this.boardCardDimension;
    }

    /**
     * Adds a card to the player's board.
     *
     * @param c The card to be added to the board.
     */
    public void takeCardToBoard(Card c){
        this.boardCard.add(c);
    }

    /**
     * Displays the cards currently on the player's board.
     */
    public void showCard(){
        for(Card c : this.boardCard ){
            System.out.println(c + " " + c.getFaceUp());
        }
    }


    /**
     * Reduces the dimension of the player's board.
     */
    public void reduceBoardCardDimension(){
        this.boardCardDimension --;
    }

    /**
     * Reduces the number of remaining cards for the player.
     */
    public void reduceRemainingCards(){this.remainingCards-- ;}

    /**
     * Initializes the player's board.
     */
    public void initializeBoardCard(){
        this.boardCard = new ArrayList<>();
    }

    /**
     * Initializes the number of remaining cards for the player.
     */
    public void initializaRemainingCard(){this.remainingCards = this.boardCardDimension;}

    /**
     * Increments the number of games won by the player.
     */
    public void incrementaPartiteVinte(){this.partiteVinte++;}

    /**
     * Increments the number of games lost by the player.
     */
    public void incrementaPartitePerse(){this.partitePerse++;}


    /**
     * Gets the player's nickname.
     *
     * @return The player's nickname.
     */
    public String getNickname(){return this.nickname;}

    /**
     * Gets the dimension of the player's board.
     *
     * @return The dimension of the player's board.
     */
    public int getboardCardDimension(){
        return this.boardCardDimension;
    }

    /**
     * Sets the number of games won by the player.
     *
     * @param nPartiteVinte The number of games won.
     */
    public void setPartiteVinte(int nPartiteVinte){
        this.partiteVinte = nPartiteVinte;
    }

    /**
     * Sets the number of games lost by the player.
     *
     * @param nPartitePerse The number of games lost.
     */
    public void setPartitePerse(int nPartitePerse){
        this.partitePerse = nPartitePerse;
    }

    /**
     * Gets the list of cards on the player's board.
     *
     * @return The list of cards on the player's board.
     */
    public  List<Card> getBoardCards(){return this.boardCard;}

    /**
     * Gets the number of remaining cards for the player.
     *
     * @return The number of remaining cards for the player.
     */

    public int getRemainingCards(){return this.remainingCards;};

    /**
     * Gets the number of games won by the player.
     *
     * @return The number of games won by the player.
     */
    public int getPartiteVinte(){
        return this.partiteVinte;
    }

    /**
     * Gets the number of games lost by the player.
     *
     * @return The number of games lost by the player.
     */
    public int getPartitePerse(){
        return this.partitePerse;
    }

    /**
     * Gets the card from the player's board based on the specified index.
     *
     * @param index The index of the card to retrieve.
     * @return The card from the player's board.
     */
    public Card getCardFromIndex(int index){
        return this.boardCard.get(index-1);
    }

}
