package model;

import java.util.*;

public class MatchManager extends Observable {

    List<Player> playerList;
    Deck gameDeck;
    Deck discardedCards;
    Card cardInHand;
    int playerIndex;
    int numberOfPlayer;

    public MatchManager() {
    }

    public void avviaGioco() {
        System.out.println("Inserisci numero giocatori");
        Scanner scanner = new Scanner(System.in);
        setNumberOfPlayer(scanner.nextInt());
        inizializzaGiocatori();
        inizializzaGioco();

    }

    //inizializzo il gioco di carte
    public void inizializzaGioco() {

        //genero un gamedeck
        this.gameDeck = new Deck(numberOfPlayer);
        this.discardedCards = new Deck();

        for (Player p : playerList) {
            p.initializeBoardCard();
            p.initializaRemainingCard();
        }

        for (Player p : playerList) {
            assegnaCarte(p, this.gameDeck);
        }

        Card cartaGirata = this.gameDeck.giveCard();

        //Giro prima carta sul tavolo
        discardedCards.addCard(cartaGirata);

//        this.playerIndex = 0;

        setChanged();
        notifyObservers(Arrays.asList(0, playerList));

        setChanged();
        notifyObservers(Arrays.asList(1, cartaGirata));

    }

    public void inizializzaGiocatori() {
        Scanner scanner2 = new Scanner(System.in);
        //Inizializzo lista giocatori
        playerList = new ArrayList<>();
        String nicknameApp;

        //Inserisco Player Umano
        System.out.println("Inserisci nome del Player Umano");
        nicknameApp = scanner2.nextLine();
//        nicknameApp = "Andrea";
        playerList.add(new Player(nicknameApp));

        //Inserisco Player Computer
        for (int i = 1; i < numberOfPlayer; i++) {
            System.out.println("Inserisci nome del Player Computer");
            nicknameApp = scanner2.nextLine();
//            nicknameApp = "Francesco";
            playerList.add(new Player(nicknameApp));
        }

    }

    public static void assegnaCarte(Player player, Deck gameDeck) {
        for (int i = 0; i < player.getboardCardDimension(); i++) {
            player.takeCardToBoard(gameDeck.giveCard());
        }
    }

    private boolean controlloCartaJackDonna(Card card) {
        if (card.getRank() == CardRank.JACK || card.getRank() == CardRank.DONNA)
            return true;

        return false;
    }

    private boolean controlloCartaJollyRe(Card card) {
        if (card.getRank() == CardRank.JOLLY || card.getRank() == CardRank.RE)
            return true;

        return false;
    }

    private void movimentoComputer(Player player) {

        System.out.println("Entrato in movimento computer");

        boolean sostituito = false;
        Card appCard = null;
        while (!sostituito) {
            //scelgo un numero a caso tra 0 e dimensione del board
            int randomIndex = (int) (Math.random() * player.getboardCardDimension());
            //prendo in mano carta che era a terra
            appCard = player.getBoardCards().get(randomIndex);

            if (!appCard.getFaceUp()) {
                player.showCard();
                System.out.print(" * Scambio: " + cardInHand + " con " + appCard + "  111" + "\n");
                System.out.println(" * Indici: " + cardInHand.getRank().rankToValue() + " con " + (randomIndex + 1));
                //posiziono la carta
                player.getBoardCards().set(randomIndex, cardInHand);

                cardInHand = appCard;
                //Ristampo la scacchiera
                setChanged();
                notifyObservers(Arrays.asList(4, playerList));

                if(!discardedCards.isEmpty()) {
                    //Aggiorno carta sul tavolo
                    setChanged();
                    notifyObservers(Arrays.asList(1, discardedCards.getLast()));
                }

                sostituito = true;
            }
        }


        //Aggiorno visualizzazione carta vicino a giocatore dal giocatore
        setChanged();
        notifyObservers(Arrays.asList(2, cardInHand, playerIndex));

        //controllo termine partita
//        controlloTerminePartita(player);

        System.out.println("Uscito da movimento computer");
    }

    private void movimentoUmanoTemporaneo(Player player) {
        boolean sostituito = false;
        Card appCard = null;
        int selectedIndex;

        Scanner scanner = new Scanner(System.in);

        while (!sostituito) {
            System.out.println("Inserisci indice carta da sostituire: ");
            selectedIndex = scanner.nextInt() - 1;

            //prendo in mano carta che era a terra
            appCard = player.getBoardCards().get(selectedIndex);

            if (!appCard.getFaceUp()) {
                //posiziono la carta
                player.getBoardCards().set(selectedIndex, cardInHand);
                sostituito = true;
            }
        }
        cardInHand = appCard;
        //controllo termine partita
        controlloTerminePartita(player);
    }

    public void turnoDiGioco() {

        //se finite carte del mazzo
        if (gameDeck.getRemainingCardOfDeck() == 0) {
            Deck.mischiaMazzo(discardedCards);
            this.gameDeck = discardedCards;
            discardedCards = new Deck();
        }

        Card appCard = null;
        Player playerInRound = playerList.get(playerIndex);

        if(discardedCards.getLast().getRank() == CardRank.RE ||
                discardedCards.getLast().getRank() == CardRank.JOLLY){
            cardInHand = discardedCards.getLastERemove();

            if(discardedCards.isEmpty()) {
                setChanged();
                notifyObservers(Arrays.asList(5));
            }
            else {
                setChanged();
                notifyObservers(Arrays.asList(1, discardedCards.getLast()));
            }
        }
        else if( discardedCards.getLast().getRank().rankToValue() > 10 ||
                discardedCards.getLast().getRank().rankToValue() > playerInRound.getboardCardDimension() ||
                playerInRound.getBoardCards().get(discardedCards.getLast().getRank().rankToValue()-1).getFaceUp()) {
            //pesco dal mazzo
            cardInHand = this.gameDeck.giveCard();
        }
        else if(!playerInRound.getBoardCards().get(discardedCards.getLast().getRank().rankToValue()-1).getFaceUp() &&
        playerInRound.getRemainingCards()< 5){
            cardInHand = discardedCards.getLastERemove();

            if(discardedCards.isEmpty()) {
                setChanged();
                notifyObservers(Arrays.asList(5));
            }
            else {
                setChanged();
                notifyObservers(Arrays.asList(1, discardedCards.getLast()));
            }
        }
        //altrimenti scelgo casualmente se pescare dal mazzo o da terra
        else{
            if(casuale0e1() == 0)
                cardInHand = this.gameDeck.giveCard();
            else {
                cardInHand = discardedCards.getLastERemove();

                if(discardedCards.isEmpty()) {
                    setChanged();
                    notifyObservers(Arrays.asList(5));
                }
                else {
                    setChanged();
                    notifyObservers(Arrays.asList(1, discardedCards.getLast()));
                }
            }
        }


        //Aggiorno visualizzazione carta pescata dal giocatore
        setChanged();
        notifyObservers(Arrays.asList(2, cardInHand, playerIndex));

//        //Rimuovo visualizzazione carta a terra
//        setChanged();
//        notifyObservers(Arrays.asList(5));

        System.out.println("********************************");
        System.out.println("Turno di: " + playerInRound.getNickname());
        System.out.println(" * Carta pescata: " + cardInHand);

        do {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            cardInHand.setFaceUpTrue();
            int cardInHandIndex = cardInHand.getRank().rankToValue() - 1;

            if (controlloCartaJackDonna(cardInHand)) {
                //Rimuovo visualizzazione carta pescata dal giocatore
                setChanged();
                notifyObservers(Arrays.asList(3, playerIndex));

                discardedCards.addCard(cardInHand);
                System.out.println(" * Scarto: " + cardInHand);

                //Aggiorno visualizzazione scarto la carta e la posiziono sul tavolo
                setChanged();
                notifyObservers(Arrays.asList(1, cardInHand));
                break;

            } else if (controlloCartaJollyRe(cardInHand)) {
                if (playerIndex != 0)
                    movimentoComputer(playerInRound);
                else
                    movimentoComputer(playerInRound);

                playerInRound.reduceRemainingCards();

                if (controlloTerminePartita(playerInRound)) {
                    //Elimino carta visualizzata a terra
                    setChanged();
                    notifyObservers(Arrays.asList(5));
                    break;
                }
            }

            //se indice carta è maggiore di dimensione del board del giocatore e non è una WildCard
            else if (cardInHandIndex >= playerInRound.getboardCardDimension()) {
                discardedCards.addCard(cardInHand);
                System.out.println(" * Scarto: " + cardInHand);

                //Rimuovo visualizzazione carta pescata dal giocatore
                setChanged();
                notifyObservers(Arrays.asList(3, playerIndex));

                setChanged();
                notifyObservers(Arrays.asList(1, cardInHand));

                break;
            }
            //Se la carta non è una figura
            else {

                //controllo posizione già occupata\scoperta
                if (playerInRound.getBoardCards().get(cardInHandIndex).getFaceUp()) {

                    //se occupata da Jolly o Re
                    if (playerInRound.getBoardCards().get(cardInHandIndex).getRank() == CardRank.JOLLY ||
                            playerInRound.getBoardCards().get(cardInHandIndex).getRank() == CardRank.RE) {

                        //Carta appoggio per WildCards che era sul board
                        appCard = playerInRound.getBoardCards().get(cardInHandIndex);

                        //sostituisco WildCard sul board con carta in mano
                        playerInRound.showCard();
                        System.out.println(" * Scambio: " + cardInHand + " con " + appCard + "  256");
                        System.out.println(" * Indici: " + cardInHand.getRank().rankToValue() + " con " + cardInHandIndex);
                        playerInRound.getBoardCards().set(cardInHandIndex, cardInHand);

                        setChanged();
                        notifyObservers(Arrays.asList(4, playerList));

                        if(!discardedCards.isEmpty()) {
                            //Aggiorno carta sul tavolo
                            setChanged();
                            notifyObservers(Arrays.asList(1, discardedCards.getLast()));
                        }

                        //Aggiorno visualizzazione carta vicino al giocatore
                        cardInHand = appCard;
                        setChanged();
                        notifyObservers(Arrays.asList(2, cardInHand, playerIndex));

//                        playerInRound.reduceRemainingCards();

                        if (controlloTerminePartita(playerInRound)) {
                            //Elimino carta visualizzata a terra
                            setChanged();
                            notifyObservers(Arrays.asList(5));
                            break;
                        }
                    }

                    //altrimenti
                    else {
                        discardedCards.addCard(cardInHand);
                        System.out.println(" * Scarto: " + cardInHand);
                        setChanged();
                        notifyObservers(Arrays.asList(1, cardInHand));
                        break;
                    }
                }

                //se posizione non è occupata
                else {
                    //prendo in mano carta coperta
                    appCard = playerInRound.getBoardCards().get(cardInHandIndex);

                    //posiziono la carta
                    playerInRound.showCard();
                    System.out.println(" * Scambio: " + cardInHand + " con " + appCard + "  299");
                    System.out.println(" * Indici: " + cardInHand.getRank().rankToValue() + " con " + cardInHandIndex);
                    playerInRound.getBoardCards().set(cardInHandIndex, cardInHand);

                    //Aggiorno visualizzazione scacchiera giocatori
                    setChanged();
                    notifyObservers(Arrays.asList(4, playerList));


                    if(!discardedCards.isEmpty()) {
                        //Aggiorno carta sul tavolo
                        setChanged();
                        notifyObservers(Arrays.asList(1, discardedCards.getLast()));
                    }

                    cardInHand = appCard;
                    setChanged();
                    notifyObservers(Arrays.asList(2, cardInHand, playerIndex));

                    playerInRound.reduceRemainingCards();

                    if (controlloTerminePartita(playerInRound)) {
                        //Elimino carta visualizzata a terra
                        setChanged();
                        notifyObservers(Arrays.asList(5));
                        break;
                    }
                }
            }
        } while (true);

        setChanged();
        notifyObservers(Arrays.asList(3, playerIndex));

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        calcolaTurno();
    }

    public boolean controlloTerminePartita(Player player) {
        if (player.getRemainingCards() == 0) {
            System.out.println(player.getNickname() + ": *** JTrash ***");
            player.reduceBoardCardDimension();
            this.playerIndex = -1;
            inizializzaGioco();

            for (Player p : this.playerList) {
                System.out.println("Dimensione Board: " + p.getNickname() + " " + p.getboardCardDimension());
            }

//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }

            if (player.getboardCardDimension() == 0) {
                player.incrementaPartiteVinte();

                for (Player p : this.playerList) {
                    if (p != player)
                        p.incrementaPartitePerse();
                }

                throw new RuntimeException("Gioco finito, ha vinto: " + player.getNickname());
            } else
                return true;
        } else
            return false;
    }

    public void calcolaTurno() {
        this.playerIndex++;
        if (this.playerIndex == numberOfPlayer) {
            this.playerIndex = 0;
        }
    }

    public int casuale0e1(){
        double appNum =  Math.random();

        if(appNum >= 0.5)
            return 1;
        else
            return 0;
//        return 1;

    }

    public List<Player> getPlayerList() {
        return this.playerList;
    }

    public void setNumberOfPlayer(int numberOfPlayer) {
        this.numberOfPlayer = numberOfPlayer;
    }

}
