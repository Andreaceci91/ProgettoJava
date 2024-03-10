package model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Semaphore;


/**
 * The MatchManager class handles the logic of the game, including player management, deck initialization,
 * game flow, and interaction between players and the game.
 */
public class MatchManager extends Observable {

    /** The list of players participating in the game. */
    private List<Player> playerList;

    /** The main deck of the game. */
    private Deck gameDeck;

    /** The deck of discarded cards. */
    private Deck discardedCards;

    /** The current card in the hand of a player. */
    private Card cardInHand;

    /** The index of the current player in the player list. */
    private int playerIndex;

    /** The total number of players in the game. */
    private int numberOfPlayer;

    /** The file path for storing the statistics of winning players. */
    private String giocatoriPathVinte = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/ProgettoJava/src/GiocatoriVinte.txt";

    /** The file path for storing the statistics of losing players. */
    private String giocatoriPathPerse = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/ProgettoJava/src/GiocatoriPerse.txt";

    /** The list of players who have won the game. */
    private List<Player> winnerPlayerList;

    /** The index of the first player who won in the game. */
    private int firtPlayerIndexWinner;

    /** Flag indicating whether it's the last round of the game. */
    private boolean ultimoGiro;

    // Enable interaction on deck
    private static boolean interactionOnDeck = true;

    // Disable interaction on board
    private static boolean interactionOnBoard = false;

    /** Semaphore for controlling interaction on the game deck. */
    public Semaphore semaphoreInteractionOnDeck = new Semaphore(0);

    /** Semaphore for controlling interaction on the game board. */
    public Semaphore semaphoreInteractionOnBoard = new Semaphore(0);

    /** Flag indicating whether it's the first play of the game. */
    private boolean firstPlay = true;

    /** The choice of drawing a card from the deck or discarded pile. */
    private String sceltaPescata;

    /** The singleton instance of MatchManager. */
    private static MatchManager matchManager;

    /**
     * Private with no parameters constructor
     */
    private MatchManager() {
    }

    /**
     * Gets the singleton instance of MatchManager.
     *
     * @return The singleton instance of MatchManager.
     */
    public static MatchManager getInstance(){
        if(matchManager == null)
            matchManager = new MatchManager();

        return matchManager;
    }

    /**
     * Starts the game by initializing players and the game deck.
     */
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

    /**
     * Initializes the game by generating the main deck, initializing player boards,
     * and distributing cards among players.
     */
    public void inizializzaGioco() {
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

    /**
     * Initializes players by prompting user input for player names and try loading player statistics.
     */
    public void inizializzaGiocatori() {
        Scanner scanner2 = new Scanner(System.in);
        playerList = new ArrayList<>();
        String nicknameApp;

        // Entering the name of Human Player
        System.out.println("Inserisci nome del Player Umano");
        nicknameApp = scanner2.nextLine();
        playerList.add(new Player(nicknameApp));

        // Entering the nam of Computer Players
        for (int i = 1; i < numberOfPlayer; i++) {
            System.out.println("Inserisci nome del Player Computer");
            nicknameApp = scanner2.nextLine();
            playerList.add(new Player(nicknameApp));
        }

        // Attempting to read each line from a file specified by the path stored in 'giocatoriPathVinte', which contains
        // the number of games won for each player.
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

        // Attempting to read each line from a file specified by the path stored in 'giocatoriPathPerse', which contains
        // the number of lost games for each player.
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

    /**
     * Assigns cards to a player from the game deck.
     *
     * @param player The player to which the cards will be assigned.
     * @param gameDeck The game deck from which the cards will be drawn.
     */
    public static void assegnaCarte(Player player, Deck gameDeck) {
        for (int i = 0; i < player.getboardCardDimension(); i++)
            player.takeCardToBoard(gameDeck.giveCard());
    }

    /**
     * Method to check if a given card is Jack or Queen.
     *
     * @param card The card to evaluate.
     * @return True if the given card is Jack or Queen, false otherwise.
     */
    private boolean controlloCartaJackDonna(Card card) {
        return card.getRank() == CardRank.JACK || card.getRank() == CardRank.DONNA;
    }

    /**
     * Method to check if a given card is Joker or King.
     *
     * @param card The card to evaluate.
     * @return True if the given card is Joker or King, false otherwise.
     */
    private boolean controlloCartaJollyRe(Card card) {
        return card.getRank() == CardRank.JOLLY || card.getRank() == CardRank.RE;
    }


    /**
     * Method to implement the computer's movement during the game.
     *
     * @param player The player currently in turn.
     */
    private void movimentoComputer(Player player) {
        boolean sostituito = false;
        Card appCard;
        while (!sostituito) {

            // Choose a random index between 0 and the player's board size
            int randomIndex = (int) (Math.random() * player.getboardCardDimension());
            // Take the card from the board
            appCard = player.getBoardCards().get(randomIndex);
            // If the card is face down
            if (!appCard.getFaceUp()) {
                // Place the card
                player.getBoardCards().set(randomIndex, cardInHand);
                // Set the current card in hand
                cardInHand = appCard;
                // Notify the observers about the move
                setChanged();
                notifyObservers(Arrays.asList(12, playerIndex, randomIndex, cardInHand, appCard, sceltaPescata));
                sostituito = true;
            }
        }
    }

    /**
     * Implements the player's action of drawing a card from the discarded pile during their turn.
     * If the interaction with the discarded pile is enabled, the player draws a card from it.
     * The drawn card becomes the current card in hand.
     */
    public void movimentoUmanoPescaTerra() {
        if(interactionOnDeck){
            // Draw a card from the discarded pile
            cardInHand = discardedCards.getLastERemove();
            // Mark the draw type as from the discarded pile
            this.sceltaPescata = "terra";
            // Release the semaphore
            semaphoreInteractionOnDeck.release();
        }
    }

    /**
     * Implements the player's action of drawing a card from the deck pile during their turn.
     * If the interaction with the deck pile is enabled, the player draws a card from it.
     * The drawn card becomes the current card in hand.
     */
    public void movimentoUmanoPescaMazzo(){
        if(interactionOnDeck) {
            // Draw a card from the deck pile
            cardInHand = gameDeck.giveCard();
            // Mark the draw type as from the deck pile
            this.sceltaPescata = "mazzo";
            // Release the semaphore
            semaphoreInteractionOnDeck.release();
        }
    }


    /**
     * Executes the game logic for a player's turn, including drawing a card and making moves.
     * It handles various game scenarios such as game termination, drawing from the deck or the discarded pile,
     * making moves on the player's board, and updating the game state accordingly.
     * This method covers the human player's turn and the computer player's turn.
     */
    public void turnoDiGioco() {
        // Reset the draw choice
        sceltaPescata = "";

        // Check if it's the last round and the current player is the first player who won the last round
        if(ultimoGiro && playerIndex == firtPlayerIndexWinner) {
            // Check if the game should be terminated
            if (termineRoundOTermineGioco()) {
                System.out.println("Gioco Terminato");

                // Ask if the player wants to play again
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
                // Notify the start of the game
                setChanged();
                notifyObservers(Arrays.asList(0, playerList, playerIndex, discardedCards, gameDeck));
            }
        }

        // Check if the deck is empty
        if (gameDeck.getRemainingCardOfDeck() == 0) {
            // Reshuffle the discarded pile into the deck
            Deck.mischiaMazzo(discardedCards);
            this.gameDeck = discardedCards;
            discardedCards = new Deck();
        }

        Card appCard;
        // Get the current player in the round
        Player playerInRound = playerList.get(playerIndex);

        // Human Player's Turn
        if(playerIndex == 0){

            // Notify observers about the interaction
            setChanged();
            notifyObservers(Arrays.asList(14, gameDeck, discardedCards));

            try {
                interactionOnDeck = true;
                semaphoreInteractionOnDeck.acquire();

                interactionOnDeck = false;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Computer Player's Turn
        else{
            setChanged();
            notifyObservers(Arrays.asList(15));

            // If the last discarded card is a King or a Joker
            if(discardedCards.getLast().getRank() == CardRank.RE || discardedCards.getLast().getRank() == CardRank.JOLLY){
                cardInHand = discardedCards.getLastERemove();//
            }
            //If the value of the last discarded card is greater than 10 or exceeds the dimensions of the player board card,
            //or if the corresponding card is face-up and not a wildcard.
            else if (discardedCards.getLast().getRank().rankToValue() > 10 ||
                    discardedCards.getLast().getRank().rankToValue() > playerInRound.getboardCardDimension() ||
                    (playerInRound.getBoardCards().get(discardedCards.getLast().getRank().rankToValue() - 1).getFaceUp() &&
                            (playerInRound.getBoardCards().get(discardedCards.getLast().getRank().rankToValue() - 1).getRank() != CardRank.JOLLY && playerInRound.getBoardCards().get(discardedCards.getLast().getRank().rankToValue() - 1).getRank() != CardRank.RE)
                    )) {
                cardInHand = this.gameDeck.giveCard();
                sceltaPescata = "mazzo";
            }
            // Optimization of player choice with cards < 5
            else if(!playerInRound.getBoardCards().get(discardedCards.getLast().getRank().rankToValue()-1).getFaceUp() && playerInRound.getRemainingCards()< 5){
                cardInHand = discardedCards.getLastERemove();
                sceltaPescata = "terra";
            }
            // Random decision regarding whether to take a card from the deck or from the board
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

        // Move and rotate the card near the current player
        setChanged();
        notifyObservers(Arrays.asList(11, playerIndex, sceltaPescata));

        do {
            sleep(1000);

            cardInHand.setFaceUpTrue();

            // Taking cardInHand index using card value
            int cardInHandIndex = cardInHand.getRank().rankToValue() - 1;

            // Check if cardInHand is Jack or Queen
            if (controlloCartaJackDonna(cardInHand)) {
                discardedCards.addCard(cardInHand);

                // Notify observers about the change
                setChanged();
                notifyObservers(Arrays.asList(13, sceltaPescata, discardedCards, gameDeck, cardInHand));

                break;

            // Check in cardInHand is a Wildcard
            } else if (controlloCartaJollyRe(cardInHand)) {
                // If it's the computer's turn
                if (playerIndex != 0)
                    movimentoComputer(playerInRound);
                // If it's the human player turn
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

                // Reduce number of face down card
                playerInRound.reduceRemainingCards();

                // Check if the current player has no remaining face-down cards
                if (controlloJtrash()) {
                    discardedCards.addCard(cardInHand);
                    // Notify observers about the change
                    setChanged();
                    notifyObservers(Arrays.asList(13, sceltaPescata, discardedCards, gameDeck, cardInHand));
                    break;
                }
            }

            // If cardInHand value is greater than the dimension of board card
            else if (cardInHandIndex >= playerInRound.getboardCardDimension()) {
                // Discard the card
                discardedCards.addCard(cardInHand);
                // Notify observers about the change
                setChanged();
                notifyObservers(Arrays.asList(13, sceltaPescata, discardedCards, gameDeck, cardInHand));
                break;
            }
            // If the card has no figure
            else {
                // Check if corresponding card in board position is face up
                if (playerInRound.getBoardCards().get(cardInHandIndex).getFaceUp()) {
                    // If it's a wildcard
                    if (playerInRound.getBoardCards().get(cardInHandIndex).getRank() == CardRank.JOLLY ||
                            playerInRound.getBoardCards().get(cardInHandIndex).getRank() == CardRank.RE) {

                        //Backup card for WildCards that were on the board
                        appCard = playerInRound.getBoardCards().get(cardInHandIndex);

                        // Replace WildCard on board with cardInHand
                        playerInRound.getBoardCards().set(cardInHandIndex, cardInHand);

                        // Notify observers about the change
                        setChanged();
                        notifyObservers(Arrays.asList(12, playerIndex, cardInHandIndex, cardInHand, appCard, sceltaPescata));

                        // Backup card is the new cardInHand
                        cardInHand = appCard;

                        // Check if the current player has no remaining face-down cards
                        if (controlloJtrash()) {
                            discardedCards.addCard(cardInHand);

                            // Notify observers about the change
                            setChanged();
                            notifyObservers(Arrays.asList(13, sceltaPescata, discardedCards, gameDeck, cardInHand));
                            break;
                        }
                    }

                    // If it is not a wildcard
                    else {
                        discardedCards.addCard(cardInHand);

                        // Notify observers about the change
                        setChanged();
                        notifyObservers(Arrays.asList(13, sceltaPescata, discardedCards, gameDeck, cardInHand));
                        break;
                    }
                }

                // Check if corresponding card in board position is face DOWN
                else {
                    //Backup card for card that were on the board
                    appCard = playerInRound.getBoardCards().get(cardInHandIndex);

                    // Notify observers about the change
                    setChanged();
                    notifyObservers(Arrays.asList(8, playerIndex, cardInHandIndex));

                    sleep(2000);

                    // Exchange the cards and notify observers about the change
                    playerInRound.getBoardCards().set(cardInHandIndex, cardInHand);
                    setChanged();
                    notifyObservers(Arrays.asList(12, playerIndex, cardInHandIndex, cardInHand, appCard, sceltaPescata));

                    cardInHand = appCard;

                    // Reduce number of face down card
                    playerInRound.reduceRemainingCards();

                    // Check if the current player has no remaining face-down cards
                    if (controlloJtrash()) {
                        discardedCards.addCard(cardInHand);

                        // Notify observers about the change
                        setChanged();
                        notifyObservers(Arrays.asList(13, sceltaPescata, discardedCards, gameDeck, cardInHand));
                        break;
                    }
                }
            }

        } while (true);

        // Calculate the next player in turn
        calcolaTurno();
    }

    /**
     * A utility method to pause the current thread for a specified amount of time.
     *
     * @param millis The number of milliseconds to sleep
     */
    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the current round or the game has ended.
     *
     * @return {@code true} if the game has ended, {@code false} otherwise
     */
    public boolean termineRoundOTermineGioco() {
        // Initialize flag indicating whether the game has ended
        boolean giocoFinito = false;

        // Reduce board card dimension for all winner players
        for(Player p: winnerPlayerList)
            p.reduceBoardCardDimension();

        // Check each player's board card dimension
        for(Player p: playerList){
            // If a player has no remaining board cards, the game is finished
            if (p.getboardCardDimension() == 0) {
                giocoFinito = true; // Set game finished flag to true
                p.incrementaPartiteVinte(); // Increment the number of games won by the player
                System.out.println("Gioco terminato da: " + p.getNickname()); // Print a message indicating the game has ended for the player
            }
            else
                p.incrementaPartitePerse(); // Increment the number of games lost by the player
        }

        // If the game has finished, save game statistics
        if(giocoFinito){
            salvataggioPartite(giocatoriPathVinte); // Save statistics for games won
            salvataggioPartite(giocatoriPathPerse); // Save statistics for games lost
        }

        // Reset player index and initialize a new game
        this.playerIndex = 0;
        inizializzaGioco();

        // Return whether the game has ended
        return giocoFinito;
    }

    /**
     * Saves game statistics to a file.
     *
     * @param fileDaUsarePath The path of the file to save the statistics
     */
    private void salvataggioPartite(String fileDaUsarePath) {
        // Create a File object representing the file path
        File file = new File(fileDaUsarePath);

        // If the file does not exist, create a new file
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Create a HashMap to store player statistics read from the file
        HashMap<String, Integer> statisticheGiocatoriLette = new HashMap<>();

        // Read player statistics from the file and populate the HashMap
        try(BufferedReader br = new BufferedReader(new FileReader(fileDaUsarePath))){
            String line;

            // Read each line from the file
            while((line = br.readLine()) != null){
                // Split the line into nickname and number of games played
                String[] app = line.split(" ");
                // Store the player's nickname and the number of games played in the HashMap
                statisticheGiocatoriLette.put(app[0], Integer.parseInt(app[1]));
            }

        } catch (IOException e) {
            // Throw a runtime exception if an IOException occurs during file reading
            throw new RuntimeException(e);
        }

        // Update player statistics in the HashMap based on the file being processed
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

        // Write updated player statistics to the file
        FileWriter fw;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedWriter bw = new BufferedWriter(fw);

        // Iterate through the HashMap and write each player's statistics to the file
        for(String key: statisticheGiocatoriLette.keySet()) {
            try {
                bw.write(key + " " + statisticheGiocatoriLette.get(key) + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Close the BufferedWriter
        try {
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the current player's remaining cards are zero, indicating JTrash.
     *
     * @return {@code true} if JTrash condition is met, {@code false} otherwise
     */

    private boolean controlloJtrash() {
        // Check if the current player has no remaining cards
        if (playerList.get(playerIndex).getRemainingCards() == 0) {
            // If it's the first time encountering JTrash in the game, update the first player index winner and set the flag to true
            if (!ultimoGiro){
                firtPlayerIndexWinner = playerIndex;
                ultimoGiro = true;
            }
            // Add the current player to the list of winner players
            winnerPlayerList.add(playerList.get(playerIndex));

            // Return true to indicate that the condition is met
            return true;
        }
        else
            return false; // If the condition is not met, return false
    }

    /**
     * Calculates the next player's turn based on the number of players.
     */
    public void calcolaTurno() {

        // If there are less than 4 players, increment the player index.
        // If the player index reaches the number of players, reset it to 0.

        if(numberOfPlayer <4) {
            this.playerIndex++;
            if (this.playerIndex == numberOfPlayer) {
                this.playerIndex = 0;
            }
        }
        //If there are 4 players, use a switch statement to calculate the next player index.
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

    /**
     * Generates a random number (0 or 1) to decide whether to draw a card from the discard pile or the main deck.
     *
     * @return 1 or 0, randomly generated
     */
    public int casuale0e1(){
        double appNum =  Math.random();

        if(appNum >= 0.5)
            return 1;
        else
            return 0;
    }

    /**
     * Sets the number of players.
     *
     * @param numberOfPlayer The number of players in the game
     */
    public void setNumberOfPlayer(int numberOfPlayer) {
        this.numberOfPlayer = numberOfPlayer;
    }

    /**
     * Moves the card from the player's hand to the board at the specified index.
     *
     * @param cardIndex The index on the board where the card will be moved
     */
    public void movimentoUmanoPescaBoardIndex(int cardIndex) {

        // Check if there is an interaction on the board
        if(interactionOnBoard) {

            // Check if the card at the specified index is face down
            if(!playerList.get(playerIndex).getBoardCards().get(cardIndex - 1).getFaceUp()){
                // Store the card at the specified index in a temporary variable
                Card app = playerList.get(playerIndex).getBoardCards().get(cardIndex-1);
                // Replace the card at the specified index with the card in hand
                playerList.get(playerIndex).getBoardCards().set(cardIndex-1,cardInHand);

                // Notify observers about the move
                setChanged();
                notifyObservers(Arrays.asList(12, playerIndex, cardIndex-1, cardInHand, app, sceltaPescata));

                // Update the card in hand with the stored card
                cardInHand = app;
                // Release the semaphore for interaction on the board
                semaphoreInteractionOnBoard.release();
                }
            }

    }

    /**
     * Notifies observers to start the game.
     */
    public void comandoAvviaGioco(){
        // Notify the observers with the command code for starting the game
        setChanged();
        notifyObservers(Arrays.asList(99));
    }
}

