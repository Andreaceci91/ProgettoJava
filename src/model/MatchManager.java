package model;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class MatchManager extends Observable {

    List<Player> playerList;
    Deck gameDeck;
    Deck discardedCards;
    Card cardInHand;
    int playerIndex;
    int numberOfPlayer;
    String giocatoriPath = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/ProgettoJava/src/Giocatori.txt";

    List<Player> winnerPlayerList;
    int firtPlayerIndexWinner;
    boolean ultimoGiro;

    private static boolean interactionOnDeck = true;
    private static boolean interactionOnBoard = false;

    public Semaphore semaphoreInteractionOnDeck = new Semaphore(0);
    public Semaphore semaphoreInteractionOnBoard = new Semaphore(0);

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

        this.winnerPlayerList = new ArrayList<>();

        firtPlayerIndexWinner = -1;
        ultimoGiro = false;

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


        setChanged();
        notifyObservers(Arrays.asList(0, playerList, playerIndex, discardedCards));

//        setChanged();
//        notifyObservers(Arrays.asList(1, discardedCards.getLast(), playerIndex));

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

        try(BufferedReader br = new BufferedReader(new FileReader(giocatoriPath))){
            String line;
            while((line = br.readLine()) != null){
                String[] statisticPlayer = line.split(" ");

                String nomePlayer = statisticPlayer[0];
                int lvPlayer = Integer.parseInt(statisticPlayer[1]);

                for(Player p: playerList){
                    if(p.getNickname().equals(nomePlayer))
                        p.setPartiteVinte(lvPlayer);
                }

            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        boolean sostituito = false;
        Card appCard = null;
        while (!sostituito) {

            //scelgo un numero a caso tra 0 e dimensione del board
            int randomIndex = (int) (Math.random() * player.getboardCardDimension());
            //prendo in mano carta che era a terra
            appCard = player.getBoardCards().get(randomIndex);

            if (!appCard.getFaceUp()) {
                player.showCard();
//                System.out.print(" * Scambio: " + cardInHand + " con " + appCard + "  111" + "\n");
//                System.out.println(" * Indici: " + cardInHand.getRank().rankToValue() + " con " + (randomIndex + 1));

                //posiziono la carta
                player.getBoardCards().set(randomIndex, cardInHand);

                cardInHand = appCard;
                //Ristampo la scacchiera
                setChanged();
                notifyObservers(Arrays.asList(4, playerList, playerIndex, discardedCards));

                setChanged();
                notifyObservers(Arrays.asList(6, this.playerIndex));

                //In revisione, si può rimuovere questo controllo è anche nel ComposeGampanel from matrix
                if(!discardedCards.isEmpty()) {
                    //Aggiorno carta sul tavolo
                    setChanged();
                    notifyObservers(Arrays.asList(1, discardedCards.getLast(), playerIndex));
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

    public void movimentoUmanoPescaTerra() {
        if(interactionOnDeck == true){
//            interactionOnDeck = false;
            cardInHand = discardedCards.getLastERemove();
            System.out.println("Carta Pescata da terra: " + cardInHand);
            semaphoreInteractionOnDeck.release();
        }
    }

    public void movimentoUmanoPescaMazzo(){
        if(interactionOnDeck == true) {
//            interactionOnDeck = false;
            cardInHand = gameDeck.giveCard();
            System.out.println("Carta pescata dal mazzo: " + cardInHand);
            semaphoreInteractionOnDeck.release();
        }
    }

    public void turnoDiGioco() {

        if(ultimoGiro == true && playerIndex == firtPlayerIndexWinner) {
            System.out.println("Sono qui");
            if (termineRoundOTermineGioco() == true) {
                throw new RuntimeException("Gioco Terminato");
            } else {
                //Elimino carta visualizzata a terra
                setChanged();
                notifyObservers(Arrays.asList(5));
            }
        }

        System.out.println("********************************");
        if (playerIndex != -1) {
            System.out.println("Turno di: " + playerList.get(playerIndex).getNickname());
            System.out.println(" * Carta coperta: " + cardInHand);
        }

        //Visualizzazione pedina
//        setChanged();
//        notifyObservers(Arrays.asList(6, this.playerIndex));

        setChanged();
        notifyObservers(Arrays.asList(4, playerList, playerIndex, discardedCards));

        sleep(300);

        //Controllo se carte del mazzo sono terminate
        if (gameDeck.getRemainingCardOfDeck() == 0) {
            Deck.mischiaMazzo(discardedCards);
            this.gameDeck = discardedCards;
            discardedCards = new Deck();
        }

        Card appCard = null;
        Player playerInRound = playerList.get(playerIndex);

        if(playerIndex == 0){
            System.out.println("Fa la tua scelta");
            System.out.println(gameDeck.getFirst());

            try {
                interactionOnDeck = true;
                semaphoreInteractionOnDeck.acquire();
                interactionOnDeck = false;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else{
            //Se le scartate sono un Re o Jolly
            if(discardedCards.getLast().getRank() == CardRank.RE || discardedCards.getLast().getRank() == CardRank.JOLLY){
                cardInHand = discardedCards.getLastERemove();
                
            //Se il rank carta > 10 o maggiore della dimensione board giocatore o la faceUp non è un Wildcard
            } else if (discardedCards.getLast().getRank().rankToValue() > 10 || discardedCards.getLast().getRank().rankToValue() > playerInRound.getboardCardDimension() || (playerInRound.getBoardCards().get(discardedCards.getLast().getRank().rankToValue() - 1).getFaceUp() && (playerInRound.getBoardCards().get(discardedCards.getLast().getRank().rankToValue() - 1).getRank() != CardRank.JOLLY && playerInRound.getBoardCards().get(discardedCards.getLast().getRank().rankToValue() - 1).getRank() != CardRank.RE))) {
                cardInHand = this.gameDeck.giveCard();
            }
            //Ottimizzazione scelta giocatore con carte < 5
            else if(!playerInRound.getBoardCards().get(discardedCards.getLast().getRank().rankToValue()-1).getFaceUp() && playerInRound.getRemainingCards()< 5){
                cardInHand = discardedCards.getLastERemove();
            }
            //scelgo casualmente se pescare dal mazzo o da terra
            else{
                if(casuale0e1() == 0)
                    cardInHand = this.gameDeck.giveCard();
                else {
                    cardInHand = discardedCards.getLastERemove();
                }
            }
        }

//        In revisione
//        //Visualizzazione carte scartate
//        if(discardedCards.isEmpty()) {
//            setChanged();
//            notifyObservers(Arrays.asList(5));
//        }
//        else {
//            setChanged();
//            notifyObservers(Arrays.asList(1, discardedCards.getLast(), playerIndex));
//        }

        setChanged();
        notifyObservers(Arrays.asList(4, playerList, playerIndex, discardedCards));

        sleep(300);

        //Visualizzazione carta pescata
        setChanged();
        notifyObservers(Arrays.asList(2, cardInHand, playerIndex));

        sleep(200);

        sleep(1000);

        do {
            sleep(1000);

            cardInHand.setFaceUpTrue();
            int cardInHandIndex = cardInHand.getRank().rankToValue() - 1;

            if (controlloCartaJackDonna(cardInHand)) {
                //Rimuovo visualizzazione carta pescata dal giocatore
                setChanged();
                notifyObservers(Arrays.asList(3, playerIndex));

                discardedCards.addCard(cardInHand);
                System.out.println(" * Scarto: " + cardInHand);

//                In revisione
//                //Aggiorno visualizzazione scarto la carta e la posiziono sul tavolo
//                setChanged();
//                notifyObservers(Arrays.asList(1, discardedCards.getLast(), playerIndex));
                setChanged();
                notifyObservers(Arrays.asList(4, playerList, playerIndex, discardedCards));

                break;

            //Controllo se la carta in mano è una WildCard
            } else if (controlloCartaJollyRe(cardInHand)) {
                if (playerIndex != 0)
                    movimentoComputer(playerInRound);
                else {
                    interactionOnBoard = true;

                    try {
                        semaphoreInteractionOnBoard.acquire();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    interactionOnBoard = false;
                }

                sleep(500);
                playerInRound.reduceRemainingCards();

                if (controlloJtrash()) {
                    //Rimuovo visualizzazione carta pescata dal giocatore
                    setChanged();
                    notifyObservers(Arrays.asList(3, playerIndex));
                    System.out.println(" * Scarto: " + cardInHand);
                    discardedCards.addCard(cardInHand);
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

//                In revisione
//                setChanged();
//                notifyObservers(Arrays.asList(1, discardedCards.getLast(), playerIndex));
                setChanged();
                notifyObservers(Arrays.asList(4, playerList, playerIndex, discardedCards));

                break;
            }
            //Se la carta non è una figura
            else {
                //controllo posizione già occupata\scoperta
                if (playerInRound.getBoardCards().get(cardInHandIndex).getFaceUp()) {
                    //se occupata da Jolly o Re
                    if (playerInRound.getBoardCards().get(cardInHandIndex).getRank() == CardRank.JOLLY || playerInRound.getBoardCards().get(cardInHandIndex).getRank() == CardRank.RE) {

                        //Carta appoggio per WildCards che era sul board
                        appCard = playerInRound.getBoardCards().get(cardInHandIndex);

//                       Stampe
                        //sostituisco WildCard sul board con carta in mano
                        playerInRound.showCard();
                        System.out.println(" * Scambio: " + cardInHand + " con " + appCard + "  256");
                        System.out.println(" * Indici: " + cardInHand.getRank().rankToValue() + " con " + cardInHandIndex);
                        playerInRound.getBoardCards().set(cardInHandIndex, cardInHand);

                        setChanged();
                        notifyObservers(Arrays.asList(4, playerList, playerIndex, discardedCards));

//                        setChanged();
//                        notifyObservers(Arrays.asList(6, this.playerIndex));

//                        In revisione
//                        if(!discardedCards.isEmpty()) {
//                            //Aggiorno carta sul tavolo
//                            setChanged();
//                            notifyObservers(Arrays.asList(1, discardedCards.getLast(), playerIndex));
//                        }

                        //Aggiorno visualizzazione carta vicino al giocatore
                        cardInHand = appCard;
                        setChanged();
                        notifyObservers(Arrays.asList(2, cardInHand, playerIndex));

//                        playerInRound.reduceRemainingCards();

                        if (controlloJtrash()) {
                            //Rimuovo visualizzazione carta pescata dal giocatore
                            setChanged();
                            notifyObservers(Arrays.asList(3, playerIndex));
                            System.out.println(" * Scarto: " + cardInHand);
                            discardedCards.addCard(cardInHand);
                            break;
                        }
                    }

                    //altrimenti
                    else {
                        discardedCards.addCard(cardInHand);
                        System.out.println(" * Scarto: " + cardInHand);
//                        In revisione
//                        setChanged();
//                        notifyObservers(Arrays.asList(1, discardedCards.getLast(), playerIndex));

                        setChanged();
                        notifyObservers(Arrays.asList(4, playerList, playerIndex, discardedCards));
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
                    notifyObservers(Arrays.asList(4, playerList, playerIndex, discardedCards));

//                    In revisione
//                    setChanged();
//                    notifyObservers(Arrays.asList(6, this.playerIndex));

                    if(!discardedCards.isEmpty()) {
//                        In revisione
                        //Aggiorno carta sul tavolo
//                        setChanged();
//                        notifyObservers(Arrays.asList(1, discardedCards.getLast(), playerIndex));
                    }

                    cardInHand = appCard;
                    setChanged();
                    notifyObservers(Arrays.asList(2, cardInHand, playerIndex));

                    playerInRound.reduceRemainingCards();

                    if (controlloJtrash()) {
                        //Rimuovo visualizzazione carta pescata dal giocatore
                        setChanged();
                        notifyObservers(Arrays.asList(3, playerIndex));
                        System.out.println(" * Scarto: " + cardInHand);

                        discardedCards.addCard(cardInHand);
                        break;
                    }
                }
            }
        } while (true);

        setChanged();
        notifyObservers(Arrays.asList(3, playerIndex));
        sleep(200);

//        In revisione
//        setChanged();
//        notifyObservers(Arrays.asList(7, this.playerIndex));
        setChanged();
        notifyObservers(Arrays.asList(4, playerList, playerIndex, discardedCards));

        sleep(1500);

        calcolaTurno();
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean termineRoundOTermineGioco() {
        boolean giocoFinito = false;

        for(Player p: winnerPlayerList)
            p.reduceBoardCardDimension();

        for (Player p : this.playerList) {
            System.out.println("Dimensione Board: " + p.getNickname() + " " + p.getboardCardDimension());
        }

        for(Player p: winnerPlayerList){
            if (p.getboardCardDimension() == 0) {
                giocoFinito = true;
                p.incrementaPartiteVinte();
                System.out.println("Gioco terminato da: " + p.getNickname());
            }
        }

        this.playerIndex = 0;
        inizializzaGioco();

        if(giocoFinito)
            return true;
        else
            return false;
    }

    private boolean controlloJtrash() {
        if (playerList.get(playerIndex).getRemainingCards() == 0) {
            System.out.println(playerList.get(playerIndex).getNickname() + ": *** JTrash ***");

            if (ultimoGiro == false){
                firtPlayerIndexWinner = playerIndex;
                ultimoGiro = true;
                System.out.println("ULTIMOOOOO GIROOOOOOOOO");
            }

            winnerPlayerList.add(playerList.get(playerIndex));

            return true;
        }
        else
            return false;
    }

    public void calcolaTurno() {
        if(numberOfPlayer <4) {
            this.playerIndex++;
            if (this.playerIndex == numberOfPlayer) {
                this.playerIndex = 0;
            }
        }
        else {
            switch (playerIndex) {
                case -1:
                    this.playerIndex = 0;
                    break;

                case 0:
                    this.playerIndex = 3;
                    break;

                case 3:
                    this.playerIndex = 1;
                    break;

                case 1:
                    this.playerIndex = 2;
                    break;

                case 2:
                    this.playerIndex = 0;
                    break;
            }
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

    public void movimentoUmanoPescaBoardIndex(int cardIndex) {

        if(interactionOnBoard == true) {
            System.out.println("Sto scambiando una carta");

            if(playerList.get(playerIndex).getBoardCards().get(cardIndex-1).getFaceUp() == false){
                Card app = playerList.get(playerIndex).getBoardCards().get(cardIndex-1);
                playerList.get(playerIndex).getBoardCards().set(cardIndex-1,cardInHand);
                cardInHand = app;
                semaphoreInteractionOnBoard.release();
                }
            }

        }

}

