package model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Deck {

    private LinkedList<Card> deck;

    // Costruttore vuoto per Scarti da terra
    public Deck() {
        this.deck = new LinkedList<>();
    }

    // Costruttore Mazzo buono
    public Deck(int numberOfPlayer) {
        deck = new LinkedList<>();
        createDeck(numberOfPlayer);
    }

    public void createDeck(int numberOfPlayer) {
        int nDeck = getNDeck(numberOfPlayer);

        this.deck = IntStream.range(0, nDeck)
                .mapToObj(i -> Arrays.stream(CardRank.values())
                        .filter(cr -> !cr.equals(CardRank.JOLLY))
                        .flatMap(cr -> Arrays.stream(CardSeed.values())
                                .map(cs -> new Card(cs, cr))))
                .flatMap(s -> s)
                .collect(Collectors.toCollection(LinkedList::new));

        addJokersByPlayerCount(numberOfPlayer);
        Collections.shuffle(this.deck);
    }

    private void addJokersByPlayerCount(int numberOfPlayer) {
        int jokerCount = switch (numberOfPlayer) {
            case 2 -> 2;
            case 3, 4 -> 4;
            default -> throw new IllegalArgumentException("Numero di giocatori non supportato: " + numberOfPlayer);
        };

        for (int i = 0; i < jokerCount; i++) {
            this.deck.add(new Card(CardRank.JOLLY));
        }
    }

    private static int getNDeck(int numberOfPlayer) {
        if (numberOfPlayer < 2 || numberOfPlayer > 4) {
            throw new IllegalArgumentException("Il numero di giocatori deve essere compreso tra 2 e 4.");
        }

        return numberOfPlayer == 2 ? 1 : 2;
    }

    public Card giveCard() {
        return this.deck.pollFirst();
    }

    public void addCard(Card card) {
        this.deck.add(card);
    }

    public int getRemainingCardOfDeck() {
        return this.deck.size();
    }

    public Card getLast() {
        return this.deck.getLast();
    }
    public Card getSecondToLastCard(){
        int deckDim = this.deck.size();
        return this.deck.get(deckDim-2);
    }

    public Card getFirst() {
        return this.deck.getFirst();
    }

    public Card getLastERemove() {
        return this.deck.pollLast();
    }

    public int getDimension() {
        return this.deck.size();
    }

    public boolean isEmpty() {
        return this.deck.isEmpty();
    }

    public static Deck mischiaMazzo(Deck cards) {
        Collections.shuffle(cards.deck);
        return cards;
    }
}
