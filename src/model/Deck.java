package model;

import java.util.*;

public class Deck {

    private List<Card> deck;
    private int nJolly;

    //Costruttore vuoto per Scarti da terra
    public Deck(){
        this.deck = new ArrayList<>();
    }

    //Costruttore Mazzo buono
    public Deck(int numberOfPlayer){
        deck = new ArrayList<>();
        createDeck(numberOfPlayer);
    }

    public void createDeck(int numberOfPlayer){

        List<Integer> listNumber = new ArrayList<>();
        int nDeck = 0;

        nDeck = getNDeck(numberOfPlayer);
        composeSeedRankCards(nDeck);
        addJokersByPlayerCount(numberOfPlayer);
        Collections.shuffle(this.deck);
    }

    private void addJokersByPlayerCount(int numberOfPlayer) {
        switch (numberOfPlayer) {
            case 2:
                this.deck.add(new Card(CardRank.JOLLY));
                this.deck.add(new Card(CardRank.JOLLY));
                break;
            case 3:
                this.deck.add(new Card(CardRank.JOLLY));
                this.deck.add(new Card(CardRank.JOLLY));
                this.deck.add(new Card(CardRank.JOLLY));
                this.deck.add(new Card(CardRank.JOLLY));
                break;
            case 4:
                this.deck.add(new Card(CardRank.JOLLY));
                this.deck.add(new Card(CardRank.JOLLY));
                this.deck.add(new Card(CardRank.JOLLY));
                this.deck.add(new Card(CardRank.JOLLY));
                break;
        }
    }

    private static int getNDeck(int numberOfPlayer) {
        int nDeck;

        switch ((numberOfPlayer)){
            case 0:
                throw new IllegalArgumentException("Devono esserci dei giocatori");
            case 1:
                throw new IllegalArgumentException("Non puoi giocare da solo");
            case 2:
                nDeck = 1;
                break;
            case 3:
                nDeck = 2;
                break;
            case 4:
                nDeck = 2;
                break;
            default:
                throw new IllegalArgumentException("Puoi giocare massimo con 3 giocatori virtuali");
        }
        return nDeck;
    }

    private void composeSeedRankCards(int nDeck) {
        //itero sui valori
        for(int i = 0; i < nDeck; i++){
            for(CardRank cr : CardRank.values()) {

                if (!cr.equals(CardRank.JOLLY)) {
                    //itero su tutti i semi
                    for (CardSeed cs : CardSeed.values()) {
                        //compongo carta
                        Card compositeCard = new Card(cs, cr);

                        //aggiungo carta composta a deck
                        this.deck.add(compositeCard);
                    }
                }
            }
        }
    }

    private void composeSeedRankCardsTarocco(int nDeck) {
        //itero sui valori
        for(int i = 0; i < nDeck; i++){
            for(CardRank cr : CardRank.values()) {

                if (!cr.equals(CardRank.JOLLY) && !cr.equals(CardRank.RE) ) {
                    //itero su tutti i semi
                    for (CardSeed cs : CardSeed.values()) {
                        //compongo carta
                        Card compositeCard = new Card(cs, cr);

                        //aggiungo carta composta a deck
                        this.deck.add(compositeCard);
                    }
                }
            }
        }
    }

    public Card giveCard(){
        Card givedCard = this.deck.getFirst();
        this.deck.removeFirst();

        return givedCard;
    }

    public void addCard(Card card){
        this.deck.add(card);
    }

    public void printDeck(){
        int conta = 0;
        for(Card c : this.deck) {
            System.out.println(c);
            conta++;
        }
        System.out.println("Numero carte nel mazzo: " + conta);
    }

    public int getRemainingCardOfDeck(){
        return this.deck.size();
    }

    public Card getLast(){
        return this.deck.getLast();
    }
    public Card getFirst(){
        return this.deck.getFirst();
    }

    public Card getSecondToLastCard(){
        int deckDim = this.deck.size();

        return this.deck.get(deckDim-1);
    }
    public Card getLastERemove(){
        Card lastCard = this.deck.getLast();
        this.deck.removeLast();

        return lastCard;
    }

    public Boolean isEmpty(){
        return this.deck.isEmpty();
    }

    public static Deck mischiaMazzo(Deck cards){
        Collections.shuffle(cards.deck);

        return cards;
    }

}
