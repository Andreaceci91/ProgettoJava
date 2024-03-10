package model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class that represent deck cards, which can be used as the main deck cards or deck of discarded cards.
 * The deck contains method for creating deck, distributing cards and other related methods
 */
public class Deck {
    private LinkedList<Card> deck;

    /**
     * *Constructor with no parameters to create a deck of discarded cards.
     */
    public Deck() {
        this.deck = new LinkedList<>();
    }


    /**
     * Constructor to create main deck of cards
     * @param numberOfPlayer for the number of players in the game
     */
    public Deck(int numberOfPlayer) {
        deck = new LinkedList<>();
        createDeck(numberOfPlayer);
    }

    /**
     * Method to create deck of cards base on the number of player in the game
     * @param numberOfPlayer for the number of players in the game
     */
    public void createDeck(int numberOfPlayer) {
        int nDeck = getNDeck(numberOfPlayer);

        //use stream to create deck excluding Joker card rank
        this.deck = IntStream.range(0, nDeck)
                .mapToObj(i -> Arrays.stream(CardRank.values())
                        .filter(cr -> !cr.equals(CardRank.JOLLY))
                        .flatMap(cr -> Arrays.stream(CardSeed.values())
                                .map(cs -> new Card(cs, cr))))
                .flatMap(s -> s)
                .collect(Collectors.toCollection(LinkedList::new));

        //adding Joker based on the number of player
        addJokersByPlayerCount(numberOfPlayer);
        Collections.shuffle(this.deck);
    }

    /**
     * Method to add joker to deck based on the number of players
     * @param numberOfPlayer for the number of players in the game
     */
    private void addJokersByPlayerCount(int numberOfPlayer) {
        int jokerCount = switch (numberOfPlayer) {
            case 2 -> 2;
            case 3, 4 -> 4;
            default -> throw new IllegalArgumentException("Numero di giocatori non supportato: " + numberOfPlayer);
        };

        for (int i = 0; i < jokerCount; i++)
            this.deck.add(new Card(CardRank.JOLLY));
    }

    /**
     * Method used to get the number of cards deck based on the number of players in game.
     * @param numberOfPlayer for the number of player in the game
     * @return numbers of decks of cards necessary
     */
    private static int getNDeck(int numberOfPlayer) {
        if (numberOfPlayer < 2 || numberOfPlayer > 4) {
            throw new IllegalArgumentException("Il numero di giocatori deve essere compreso tra 2 e 4.");
        }

        return numberOfPlayer == 2 ? 1 : 2;
    }

    /**
     * Method to give a card of deck
     * @return single card
     */
    public Card giveCard() {
        return this.deck.pollFirst();
    }

    /**
     * Method to add a card to the deck
     * @param card card to add
     */
    public void addCard(Card card) {
        this.deck.add(card);
    }

    /**
     * Method to get the number of remaining cards in the deck
     * @return the number of remaining cards
     */
    public int getRemainingCardOfDeck() {
        return this.deck.size();
    }

    /**
     * Method to get the last card in the deck
     * @return the last card in the deck
     */
    public Card getLast() {
        return this.deck.getLast();
    }

    /**
     * Method to get the second to last card in the deck
     * @return the second to last card in the deck
     */
    public Card getSecondToLastCard(){
        int deckDim = this.deck.size();
        return this.deck.get(deckDim-2);
    }

    /**
     * Method to get the first card of the deck
     * @return the first card of the deck
     */
    public Card getFirst() {
        return this.deck.getFirst();
    }

    /**
     * Method to get and remove the last card in the deck.
     * @return the last card in the deck, removed
     */
    public Card getLastERemove() {
        return this.deck.pollLast();
    }

    /**
     * Method to get the dimension of the deck.
     * @return the dimension of the deck
     */
    public int getDimension() {
        return this.deck.size();
    }

    /**
     * Method to check if the deck is empty.
     * @return true if the deck is empty, otherwise false
     */
    public boolean isEmpty() {
        return this.deck.isEmpty();
    }

    /**
     * Static method to shuffle a deck of cards.
     * @param cards the deck of cards to shuffle
     * @return the shuffled deck of cards
     */
    public static Deck mischiaMazzo(Deck cards) {
        Collections.shuffle(cards.deck);
        return cards;
    }
}
