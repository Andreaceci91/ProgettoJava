package model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Semaphore;

public class MatchManager extends Observable {

    List<Player> playerList;
    Deck gameDeck;
    Deck discardedCards;
    Card cardInHand;
    int playerIndex;
    int numberOfPlayer;
    String giocatoriPathVinte = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/ProgettoJava/src/GiocatoriVinte.txt";
    String giocatoriPathPerse = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/ProgettoJava/src/GiocatoriPerse.txt";
    List<Player> winnerPlayerList;
    int firtPlayerIndexWinner;
    boolean ultimoGiro;

    private static boolean interactionOnDeck = true;
    private static boolean interactionOnBoard = false;

    public Semaphore semaphoreInteractionOnDeck = new Semaphore(0);
    public Semaphore semaphoreInteractionOnBoard = new Semaphore(0);

    boolean firstPlay = true;
    String sceltaPescata;

    private static MatchManager matchManager;

    private MatchManager() {
    }

    public static MatchManager getInstance(){
        if(matchManager == null)
            matchManager = new MatchManager();

        return matchManager;
    }

    public void avviaGioco(){
        //Da reinserire
//        if(firstPlay == true) {
            setChanged();
            notifyObservers(Arrays.asList(9));
//        }

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
        notifyObservers(Arrays.asList(0, playerList, playerIndex, discardedCards, gameDeck));

    }

    public void inizializzaGiocatori() {
        Scanner scanner2 = new Scanner(System.in);
        //Inizializzo lista giocatori
        playerList = new ArrayList<>();
        String nicknameApp;

        //Inserisco Player Umano
        System.out.println("Inserisci nome del Player Umano");
        nicknameApp = scanner2.nextLine();
        playerList.add(new Player(nicknameApp));

        //Inserisco Player Computer
        for (int i = 1; i < numberOfPlayer; i++) {
            System.out.println("Inserisci nome del Player Computer");
            nicknameApp = scanner2.nextLine();
            playerList.add(new Player(nicknameApp));
        }

//        try(BufferedReader br = new BufferedReader(new FileReader(giocatoriPath))){
//            String line;
//            while((line = br.readLine()) != null){
//                String[] statisticPlayer = line.split(" ");
//
//                String nomePlayer = statisticPlayer[0];
//                int lvPlayer = Integer.parseInt(statisticPlayer[1]);
//
//                for(Player p: playerList){
//                    if(p.getNickname().equals(nomePlayer))
//                        p.setPartiteVinte(lvPlayer);
//                }
//
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        try {
            Files.lines(Paths.get(giocatoriPathVinte)).forEach(line -> {
                String[] statisticPlayer = line.split(" ");
                String nomePlayer = statisticPlayer[0];
                int lvPlayer = Integer.parseInt(statisticPlayer[1]);
                playerList.stream()
                        .filter(p -> p.getNickname().equals(nomePlayer))
                        .findFirst()
                        .ifPresent(p -> p.setPartiteVinte(lvPlayer));
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Files.lines(Paths.get(giocatoriPathPerse)).forEach(line -> {
                String[] statisticPlayer = line.split(" ");
                String nomePlayer = statisticPlayer[0];
                int pPerse = Integer.parseInt(statisticPlayer[1]);
                playerList.stream()
                        .filter(p -> p.getNickname().equals(nomePlayer))
                        .findFirst()
                        .ifPresent(p -> p.setPartitePerse(pPerse));
            });
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
        return card.getRank() == CardRank.JACK || card.getRank() == CardRank.DONNA;
    }

    private boolean controlloCartaJollyRe(Card card) {
        return card.getRank() == CardRank.JOLLY || card.getRank() == CardRank.RE;
    }

    private void movimentoComputer(Player player) {
        boolean sostituito = false;
        Card appCard;
        while (!sostituito) {

            //scelgo un numero a caso tra 0 e dimensione del board
            int randomIndex = (int) (Math.random() * player.getboardCardDimension());
            //prendo in mano carta che era a terra
            appCard = player.getBoardCards().get(randomIndex);

            if (!appCard.getFaceUp()) {
//                player.showCard();

                //posiziono la carta
                player.getBoardCards().set(randomIndex, cardInHand);

                cardInHand = appCard;

                setChanged();
                notifyObservers(Arrays.asList(12, playerIndex, randomIndex, cardInHand, appCard, sceltaPescata));

                sostituito = true;
            }
        }

//        System.out.println("Uscito da movimento computer");
    }

    public void movimentoUmanoPescaTerra() {
//        System.out.println("Sono in movimento umano pescaterra");
        if(interactionOnDeck){
            cardInHand = discardedCards.getLastERemove();
//            System.out.println("Carta Pescata da terra: " + cardInHand);
            this.sceltaPescata = "terra";
            semaphoreInteractionOnDeck.release();
        }
    }

    public void movimentoUmanoPescaMazzo(){
//        System.out.println("Sono in movimento umano pescamazzo");
        if(interactionOnDeck) {
            cardInHand = gameDeck.giveCard();
//            System.out.println("Carta pescata dal mazzo: " + cardInHand);
            this.sceltaPescata = "mazzo";
            semaphoreInteractionOnDeck.release();
        }
    }

    public void turnoDiGioco() {
        sceltaPescata = "";

        if(ultimoGiro && playerIndex == firtPlayerIndexWinner) {
//            System.out.println("Sono qui");
            if (termineRoundOTermineGioco()) {
                System.out.println("Gioco Terminato");

                System.out.println("Vuoi giocare nuovamente?: ");
                Scanner scanner = new Scanner(System.in);
                String scelta = scanner.nextLine();

                if(scelta.equals("no"))
                    throw new RuntimeException("Gioco Terminato");
                else if (scelta.equals("si")) {
                    firstPlay = false;
                    avviaGioco();
                }
                else
                    throw new RuntimeException("Scelta non valida!");


            } else {
                //Elimino carta visualizzata a terra
                setChanged();
                notifyObservers(Arrays.asList(0, playerList, playerIndex, discardedCards, gameDeck));
            }
        }

//        System.out.println("********************************");
//        if (playerIndex != -1) {
////            System.out.println("Turno di: " + playerList.get(playerIndex).getNickname());
////            System.out.println(" * Carta coperta: " + cardInHand);
//        }

        //Controllo se carte del mazzo sono terminate
        if (gameDeck.getRemainingCardOfDeck() == 0) {
            Deck.mischiaMazzo(discardedCards);
            this.gameDeck = discardedCards;
            discardedCards = new Deck();
        }

        Card appCard;
        Player playerInRound = playerList.get(playerIndex);

        //Turno Umano
        if(playerIndex == 0){
//            System.out.println("Fa la tua scelta");
            System.out.println(gameDeck.getFirst());


            setChanged();
            notifyObservers(Arrays.asList(14, gameDeck, discardedCards));

            try {
                interactionOnDeck = true;
//                System.out.println("Acquisito");
                semaphoreInteractionOnDeck.acquire();
//                System.out.println("Rilasciato");
                interactionOnDeck = false;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println("Sono qui 1");
        }
        //Turno Computer
        else{
            setChanged();
            notifyObservers(Arrays.asList(15));
            //Se le scartate sono un Re o Jolly
            if(discardedCards.getLast().getRank() == CardRank.RE || discardedCards.getLast().getRank() == CardRank.JOLLY){
                cardInHand = discardedCards.getLastERemove();
                
            //Se il rank carta > 10 o maggiore della dimensione board giocatore o la faceUp non è un Wildcard
            } else if (discardedCards.getLast().getRank().rankToValue() > 10 || discardedCards.getLast().getRank().rankToValue() > playerInRound.getboardCardDimension() || (playerInRound.getBoardCards().get(discardedCards.getLast().getRank().rankToValue() - 1).getFaceUp() && (playerInRound.getBoardCards().get(discardedCards.getLast().getRank().rankToValue() - 1).getRank() != CardRank.JOLLY && playerInRound.getBoardCards().get(discardedCards.getLast().getRank().rankToValue() - 1).getRank() != CardRank.RE))) {
                cardInHand = this.gameDeck.giveCard();
                sceltaPescata = "mazzo";
            }
            //Ottimizzazione scelta giocatore con carte < 5
            else if(!playerInRound.getBoardCards().get(discardedCards.getLast().getRank().rankToValue()-1).getFaceUp() && playerInRound.getRemainingCards()< 5){
                cardInHand = discardedCards.getLastERemove();
                sceltaPescata = "terra";
            }
            //scelgo casualmente se pescare dal mazzo o da terra
            else{
                if(casuale0e1() == 0) {
                    cardInHand = this.gameDeck.giveCard();
                    sceltaPescata = "mazzo";
                }
                else {
                    cardInHand = discardedCards.getLastERemove();
                    sceltaPescata = "terra";
                }
            }
        }

        //Movimento carta vicino al Player di turno e la Ruoto
        setChanged();
        notifyObservers(Arrays.asList(11, playerIndex, sceltaPescata));

        do {
            sleep(1000);

            cardInHand.setFaceUpTrue();
            int cardInHandIndex = cardInHand.getRank().rankToValue() - 1;

            if (controlloCartaJackDonna(cardInHand)) {
                discardedCards.addCard(cardInHand);

                setChanged();
                notifyObservers(Arrays.asList(13, sceltaPescata, discardedCards, gameDeck, cardInHand));

                break;

            //Controllo se la carta in mano è una WildCard
            } else if (controlloCartaJollyRe(cardInHand)) {
                if (playerIndex != 0)
                    movimentoComputer(playerInRound);
                else {
                    setChanged();
                    notifyObservers(Arrays.asList(16));
                    interactionOnBoard = true;

                    try {
                        semaphoreInteractionOnBoard.acquire();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    interactionOnBoard = false;
                }

                playerInRound.reduceRemainingCards();

                if (controlloJtrash()) {
                    //Rimuovo visualizzazione carta pescata dal giocatore

//                    System.out.println(" * Scarto: " + cardInHand);
                    discardedCards.addCard(cardInHand);

                    setChanged();
                    notifyObservers(Arrays.asList(13, sceltaPescata, discardedCards, gameDeck, cardInHand));
                    break;
                }
            }

            //se indice carta è maggiore di dimensione del board del giocatore e non è una WildCard
            else if (cardInHandIndex >= playerInRound.getboardCardDimension()) {
                discardedCards.addCard(cardInHand);

                setChanged();
                notifyObservers(Arrays.asList(13, sceltaPescata, discardedCards, gameDeck, cardInHand));
//                System.out.println(" * Scarto: " + cardInHand);

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

                       //Stampe
//                        playerInRound.showCard();
//                        System.out.println(" * Scambio: " + cardInHand + " con " + appCard + "  256");
//                        System.out.println(" * Indici: " + cardInHand.getRank().rankToValue() + " con " + cardInHandIndex);

                        //sostituisco WildCard sul board con carta in mano
                        playerInRound.getBoardCards().set(cardInHandIndex, cardInHand);

                        setChanged();
                        notifyObservers(Arrays.asList(12, playerIndex, cardInHandIndex, cardInHand, appCard, sceltaPescata));

                        //Aggiorno visualizzazione carta vicino al giocatore
                        cardInHand = appCard;

                        if (controlloJtrash()) {
                            //Rimuovo visualizzazione carta pescata dal giocatore

//                            System.out.println(" * Scarto: " + cardInHand);
                            discardedCards.addCard(cardInHand);

                            setChanged();
                            notifyObservers(Arrays.asList(13, sceltaPescata, discardedCards, gameDeck, cardInHand));

                            break;
                        }
                    }

                    //altrimenti
                    else {
                        discardedCards.addCard(cardInHand);
//                        System.out.println(" * Scarto: " + cardInHand);

                        setChanged();
                        notifyObservers(Arrays.asList(13, sceltaPescata, discardedCards, gameDeck, cardInHand));

//                        In revisione
                        break;
                    }
                }

                //se posizione non è occupata
                else {
                    //prendo in mano carta coperta del board giocatore
                    appCard = playerInRound.getBoardCards().get(cardInHandIndex);

                    //posiziono la carta
//                    playerInRound.showCard();
//                    System.out.println(" * Scambio: " + cardInHand + " con " + appCard + "  299");
//                    System.out.println(" * Indici: " + cardInHand.getRank().rankToValue() + " con " + cardInHandIndex);

                    setChanged();
                    notifyObservers(Arrays.asList(8, playerIndex, cardInHandIndex));

                    sleep(2000);

                    //Scambio le carte
                    playerInRound.getBoardCards().set(cardInHandIndex, cardInHand);
                    setChanged();
                    notifyObservers(Arrays.asList(12, playerIndex, cardInHandIndex, cardInHand, appCard, sceltaPescata));

//                    cardInHand = appCard;

//                    if(!discardedCards.isEmpty()) {
////                        In revisione
//                        //Aggiorno carta sul tavolo
//                    }

                    cardInHand = appCard;

                    playerInRound.reduceRemainingCards();

                    if (controlloJtrash()) {
                        //Rimuovo visualizzazione carta pescata dal giocatore
//                        System.out.println(" * Scarto: " + cardInHand);

                        discardedCards.addCard(cardInHand);

                        setChanged();
                        notifyObservers(Arrays.asList(13, sceltaPescata, discardedCards, gameDeck, cardInHand));
                        break;
                    }
                }
            }

        } while (true);

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
//            System.out.println("Dimensione Board: " + p.getNickname() + " " + p.getboardCardDimension());
        }

        for(Player p: playerList){
            if (p.getboardCardDimension() == 0) {
                giocoFinito = true;
                p.incrementaPartiteVinte();
                System.out.println("Gioco terminato da: " + p.getNickname());
            }
            else
                p.incrementaPartitePerse();
        }


        if(giocoFinito){
            salvataggioPartite(giocatoriPathVinte);
            salvataggioPartite(giocatoriPathPerse);
        }


        this.playerIndex = 0;
        inizializzaGioco();

        return giocoFinito;
    }

    private void salvataggioPartite(String fileDaUsarePath) {
        File file = new File(fileDaUsarePath);

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        HashMap<String, Integer> statisticheGiocatoriLette = new HashMap<>();

        try(BufferedReader br = new BufferedReader(new FileReader(fileDaUsarePath))){
            String line;

            while((line = br.readLine()) != null){
                String[] app = line.split(" ");

                statisticheGiocatoriLette.put(app[0], Integer.parseInt(app[1]));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(fileDaUsarePath.equals(giocatoriPathVinte)) {
            for (Player p : playerList) {
                statisticheGiocatoriLette.computeIfPresent(p.getNickname(), (key, value) -> Math.max(value, p.getPartiteVinte()));
                statisticheGiocatoriLette.computeIfAbsent(p.getNickname(), key -> p.getPartiteVinte());
            }
        }
        else{
            for(Player p: playerList){
                statisticheGiocatoriLette.computeIfPresent(p.getNickname(), (key, value) -> Math.max(value, p.getPartitePerse()));
                statisticheGiocatoriLette.computeIfAbsent(p.getNickname(), key-> p.getPartitePerse());
            }
        }

        FileWriter fw;

        try {
            fw = new FileWriter(file.getAbsoluteFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedWriter bw = new BufferedWriter(fw);

        for(String key: statisticheGiocatoriLette.keySet()) {
            try {
                bw.write(key + " " + statisticheGiocatoriLette.get(key) + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean controlloJtrash() {
        if (playerList.get(playerIndex).getRemainingCards() == 0) {
//            System.out.println(playerList.get(playerIndex).getNickname() + ": *** JTrash ***");

            if (!ultimoGiro){
                firtPlayerIndexWinner = playerIndex;
                ultimoGiro = true;
//                System.out.println("ULTIMOOOOO GIROOOOOOOOO");
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

//    public List<Player> getPlayerList() {
//        return this.playerList;
//    }

    public void setNumberOfPlayer(int numberOfPlayer) {
        this.numberOfPlayer = numberOfPlayer;
    }

    public void movimentoUmanoPescaBoardIndex(int cardIndex) {

        if(interactionOnBoard) {

            if(!playerList.get(playerIndex).getBoardCards().get(cardIndex - 1).getFaceUp()){
                Card app = playerList.get(playerIndex).getBoardCards().get(cardIndex-1);
                playerList.get(playerIndex).getBoardCards().set(cardIndex-1,cardInHand);

                setChanged();
                notifyObservers(Arrays.asList(12, playerIndex, cardIndex-1, cardInHand, app, sceltaPescata));

                cardInHand = app;
                semaphoreInteractionOnBoard.release();
                }
            }

    }

    public void comandoAvviaGioco(){
//        System.out.println("Premuto il pulsante avvia gioco");
        setChanged();
        notifyObservers(Arrays.asList(99));
    }
}

