package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MatchManager {

    List<Player> playerList;
    Deck gameDeck;
    Deck discardedCards;
    Card cardInHand;
    int playerIndex;
    int numberOfPlayer;

    public MatchManager(int numberOfPlayer){
        this.numberOfPlayer = numberOfPlayer;
        inizializzaGiocatori(numberOfPlayer);
        inizializzaGioco();
    }

    public static void assegnaCarte(Player player, Deck gameDeck){
        for(int i = 0; i < player.getboardCardDimension(); i++){
            player.takeCardToBoard(gameDeck.giveCard());
        }
    }

    //inizializzo il gioco di carte
    public void inizializzaGioco(){

        //genero un gamedeck
        this.gameDeck = new Deck(numberOfPlayer);
        this.discardedCards = new Deck();

        for(Player p: playerList){
            p.initializeBoardCard();
            p.initializaRemainingCard();
        }

        for(Player p: playerList){
            assegnaCarte(p, this.gameDeck);
        }

        //Giro prima carta sul tavolo
        discardedCards.addCard(this.gameDeck.giveCard());

    }

    private void inizializzaGiocatori(int numberOfPlayer) {
        //Inizializzo lista giocatori
        playerList = new ArrayList<>();
        String nicknameApp;
        Scanner scanner = new Scanner(System.in);

        //Inserisco Player Umano
        System.out.println("Inserisci nome del Player Umano");
        //nicknameApp = scanner.nextLine();
        nicknameApp = "Andrea";
        playerList.add(new Player(nicknameApp));

        //Inserisco Player Computer
        for(int i = 1; i < numberOfPlayer; i++) {
            nicknameApp = "";

            System.out.println("Inserisci nome del Player Computer");
            //nicknameApp = scanner.nextLine();
            nicknameApp = "Francesco";
            playerList.add(new Player(nicknameApp));
        }
    }

    private boolean controlloCartaJackDonna(Card card){
        if(card.getRank() == CardRank.JACK || card.getRank() == CardRank.DONNA)
            return true;

        return false;
    }

    private boolean controlloCartaJollyRe(Card card){
        if(card.getRank() == CardRank.JOLLY || card.getRank() == CardRank.RE)
            return true;

        return false;
    }

    private void movimentoComputer(Player player){
        boolean sostituito = false;
        Card appCard = null;

        while(!sostituito) {
            //scelgo un numero a caso tra 0 e dimensione del board
            int randomIndex = (int) (Math.random() * player.getboardCardDimension());
            //prendo in mano carta che era a terra
            appCard = player.getBoardCards().get(randomIndex);

            if(!appCard.getFaceUp()) {
                //posiziono la carta
                player.getBoardCards().set(randomIndex, cardInHand);
                sostituito = true;
            }
        }
        cardInHand = appCard;
        //controllo termine partita
        controlloTerminePartita(player);
    }

    private void movimentoUmanoTemporaneo(Player player){
        boolean sostituito = false;
        Card appCard = null;
        int selectedIndex;

        Scanner scanner = new Scanner(System.in);

        while(!sostituito) {
            System.out.println("Inserisci indice carta da sostituire: ");
            selectedIndex = scanner.nextInt() -1;

            //prendo in mano carta che era a terra
            appCard = player.getBoardCards().get(selectedIndex);

            if(!appCard.getFaceUp()) {
                //posiziono la carta
                player.getBoardCards().set(selectedIndex, cardInHand);
                sostituito = true;
            }
        }
        cardInHand = appCard;
        //controllo termine partita
        controlloTerminePartita(player);
    }

    public void turnoDiGioco(){

        //se finite carte del mazzo
        if(gameDeck.getRemainingCardOfDeck() == 0){
            Deck.mischiaMazzo(discardedCards);
            this.gameDeck = discardedCards;
            discardedCards = new Deck();
        }

        cardInHand = this.gameDeck.giveCard();
        Card appCard = null;
        Player playerInRound = playerList.get(playerIndex);
        System.out.println("Turno di: " + playerInRound.getNickname());

        do{
            cardInHand.setFaceUpTrue();
            int cardInHandIndex = cardInHand.getRank().rankToValue() - 1;

            if(controlloCartaJackDonna(cardInHand)){
                discardedCards.addCard(cardInHand);
                break;
            }

            else if(controlloCartaJollyRe(cardInHand)) {
                if(playerIndex != 0)
                    movimentoComputer(playerInRound);
                else
                    movimentoComputer(playerInRound);

                playerInRound.reduceRemainingCards();
                controlloTerminePartita(playerInRound);

            }

            //se indice carta è maggiore di dimensione del board del giocatore e non è una WildCard
            else if(cardInHandIndex >= playerInRound.getboardCardDimension()){
                discardedCards.addCard(cardInHand);
                break;
            }

            //Se la carta non è una figura
            else{

                //controllo posizione già occupata
                if (playerInRound.getBoardCards().get(cardInHandIndex).getFaceUp()) {

                    //se occupata da Jolly o Re
                    if (playerInRound.getBoardCards().get(cardInHandIndex).getRank() == CardRank.JOLLY || playerInRound.getBoardCards().get(cardInHandIndex).getRank() == CardRank.RE) {

                        //Carta appoggio per WildCards che era sul board
                        appCard = playerInRound.getBoardCards().get(cardInHandIndex);

                        //sostituisco WildCard sul board con carta in mano
                        playerInRound.getBoardCards().set(cardInHandIndex, cardInHand);
                        cardInHand = appCard;

                        playerInRound.reduceRemainingCards();
                        controlloTerminePartita(playerInRound);
                    }

                    //altrimenti
                    else{
                        discardedCards.addCard(cardInHand);
                        break;
                    }
                }

                //se posizione non è occupata
                else {
                    //prendo in mano carta che era a terra
                    appCard = playerInRound.getBoardCards().get(cardInHandIndex);

                    //posiziono la carta
                    playerInRound.getBoardCards().set(cardInHandIndex, cardInHand);
                    cardInHand = appCard;

                    playerInRound.reduceRemainingCards();
                    controlloTerminePartita(playerInRound);
                }
            }

        }while(true);

        calcolaTurno();
    }

    public void controlloTerminePartita(Player player){
        if(player.getRemainingCards() == 0) {
            System.out.println(player.getNickname() + ": *** JTrash ***");
            player.reduceBoardCardDimension();
            inizializzaGioco();

            for(Player p: this.playerList){
                System.out.println(p.getNickname() + " " +p.getboardCardDimension());
            }

            if(player.getboardCardDimension() == 0) {
                player.incrementaPartiteVinte();

                for(Player p: this.playerList){
                    if(p != player)
                        p.incrementaPartitePerse();
                }

                throw new RuntimeException("Gioco finito, ha vinto: " + player.getNickname());
            }
        }
    }

    public void calcolaTurno(){
        this.playerIndex++;
        if(this.playerIndex == numberOfPlayer){
            this.playerIndex = 0;
        }
    }

    public List<Player> getPlayerList(){
        return this.playerList;
    }



}
