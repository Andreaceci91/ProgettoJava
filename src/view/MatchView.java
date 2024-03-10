package view;

import controller.PersonalMouseListeners;
import model.Card;
import model.Player;
import model.Deck;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import java.util.List;

/**
 * Represents the view for a match in JTrash game.
 */
public class MatchView implements Observer {

    /** The width of a card. */
    final int lCard = 76;

    /** The height of a card. */
    final int hCard = 88;

    /** The width of the game panel. */
    private int widthPanel = 77 * 9;

    /** The height of the game panel. */
    private int heightPanel = 88 * 9;

    /** The number of rows on the game board. */
    private int nRighe = 9;

    /** The number of columns on the game board. */
    private int nColonne = 9;

    /** The matrix representing the game board. */
    private int[][] matriceScacchiera;

    /** Text area for player 1 details. */
    private JTextArea player1TextArea = new JTextArea();

    /** Text area for player 2 details. */
    private JTextArea player2TextArea = new JTextArea();

    /** Text area for player 3 details. */
    private JTextArea player3TextArea = new JTextArea();

    /** Text area for player 4 details. */
    private JTextArea player4TextArea = new JTextArea();

    private JTextArea empty1TextArea = new JTextArea();

    /** Panel for the initial screen. */
    private MyPanel schermataInizialePanel;

    /** Label for the trash area. */
    private JLabel trashLabel;

    /** Button to start the game. */
    private JButton startGameButton;

    /** Panel for the game screen. */
    private MyPanel gamePanel;

    /** The main frame of the application. */
    private JFrame mainFrame;

    /** Array of custom buttons representing player 1's cards. */
    private CartaPersonalizzataButton[] listaCartaPersonalizzataButton0;

    /** Array of custom buttons representing player 2's cards. */
    private CartaPersonalizzataButton[] listaCartaPersonalizzataButton1;

    /** Array of custom buttons representing player 3's cards. */
    private CartaPersonalizzataButton[] listaCartaPersonalizzataButton2;

    /** Array of custom buttons representing player 4's cards. */
    private CartaPersonalizzataButton[] listaCartaPersonalizzataButton3;

    /** Custom button representing the discarded cards pile. */
    private CartaPersonalizzataButton discardedCardsButton;

    /** Custom button representing the deck of cards. */
    private CartaPersonalizzataButton deckButton;

    /** Custom button representing the second to last discarded card. */
    private CartaPersonalizzataButton secondoToLastDiscardedCardButton;

    /** Path to the image file of a covered card. */
    private String cartaCopertaPath = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/" + "SpriteProgetto/CartaCoperta.png";

    /** Path to the image file of the second to last discarded card. */
    private String secondoToLastDiscardedCardPath = "";

    /** Listener for drawing a card from the deck. */
    private PersonalMouseListeners.PescaTerraListeners pescaTerraListeners = new PersonalMouseListeners.PescaTerraListeners();

    /** Listener for drawing a card from the deck. */
    private PersonalMouseListeners.PescaMazzoListeners pescaMazzoListeners = new PersonalMouseListeners.PescaMazzoListeners();

    /**
     * Constructs a MatchView object.
     */
    public MatchView() {
    }

    /**
     * Creates the match board based on the given parameters.
     *
     * @param listaPlayer      The list of players.
     * @param playerIndex      The index of the current player.
     * @param discardedCards   The deck of discarded cards.
     * @param deckCard         The deck of cards.
     */
    public void creaScacchieraPixel(List<Player> listaPlayer, int playerIndex, Deck discardedCards, Deck deckCard) {
        if (schermataInizialePanel != null)
            mainFrame.remove(schermataInizialePanel);

        if (gamePanel != null)
            mainFrame.remove(gamePanel);
        else {
            // Set main frame for the first time
            mainFrame.setLayout(new GridBagLayout());
        }

        gamePanel = new MyPanel("/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/SpriteProgetto/BackGround_Resized.png");
        gamePanel.setPreferredSize(new Dimension(widthPanel, heightPanel));
        gamePanel.setLayout(null);

        matriceScacchiera = new int[nRighe][nColonne];
        impostaMatriceIniziale(listaPlayer);

        composeGamePanelFromMatrix(listaPlayer, gamePanel, playerIndex, discardedCards, deckCard);
        addingMainElementToFrame(mainFrame, gamePanel);
        setPlayerBoxDetails(listaPlayer, player1TextArea, player2TextArea, player3TextArea, player4TextArea);
        repaintPanelFrame();
    }

    /**
     * Composes the game panel based on the matrix and player information.
     *
     * @param listaPlayer      The list of players.
     * @param gamePanel        The game panel.
     * @param playerIndex      The index of the current player.
     * @param discardedCards   The deck of discarded cards.
     * @param deckCard         The deck of cards.
     */
    public void composeGamePanelFromMatrix(List<Player> listaPlayer, MyPanel gamePanel, int playerIndex, Deck discardedCards, Deck deckCard) {
        int playerIndexMatrix;
        CartaPersonalizzataButton cartaPersonalizzataButton;

        listaCartaPersonalizzataButton0 = new CartaPersonalizzataButton[10];
        listaCartaPersonalizzataButton1 = new CartaPersonalizzataButton[10];
        listaCartaPersonalizzataButton2 = new CartaPersonalizzataButton[10];
        listaCartaPersonalizzataButton3 = new CartaPersonalizzataButton[10];

        for (int i = 0; i < matriceScacchiera.length; i++) {
            for (int j = 0; j < matriceScacchiera[0].length; j++) {

                if (matriceScacchiera[i][j] == 0) {
//                    JLabel emptyLabel = new JLabel("" + indiceCarteScacchiera + "  i:" + i + "  j:" + j);
                    JLabel emptyLabel = new JLabel();
                    emptyLabel.setPreferredSize(new Dimension(lCard, hCard));
//                    emptyLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    emptyLabel.setBounds(j * lCard, i * hCard, lCard, hCard);
                    gamePanel.add(emptyLabel);

                } else {
                    //Player Up
                    if (i == 0 || i == 1) {
                        playerIndexMatrix = 0;

                        cartaPersonalizzataButton = generazioneCarteGiocatori2(listaPlayer, playerIndexMatrix, i, j);
                        cartaPersonalizzataButton.addMouseListener(new PersonalMouseListeners.PescaBoardIndex(matriceScacchiera[i][j], cartaPersonalizzataButton));
                        listaCartaPersonalizzataButton0[matriceScacchiera[i][j] - 1] = cartaPersonalizzataButton;
                        cartaPersonalizzataButton.setSize(new Dimension(lCard, hCard));
                        gamePanel.add(cartaPersonalizzataButton);
                    }

                    //Player Down
                    if (i == matriceScacchiera.length - 1 || i == matriceScacchiera.length - 2) {
                        playerIndexMatrix = 1;

                        cartaPersonalizzataButton = generazioneCarteGiocatori2(listaPlayer, playerIndexMatrix, i, j);
                        listaCartaPersonalizzataButton1[matriceScacchiera[i][j] - 1] = cartaPersonalizzataButton;
                        cartaPersonalizzataButton.setSize(new Dimension(lCard, hCard));
                        gamePanel.add(cartaPersonalizzataButton);
                    }

                    //Player Sx
                    if (i > 1 && j <= 1) {
                        playerIndexMatrix = 2;

                        cartaPersonalizzataButton = generazioneCarteGiocatori2(listaPlayer, playerIndexMatrix, i, j);
                        listaCartaPersonalizzataButton2[matriceScacchiera[i][j] - 1] = cartaPersonalizzataButton;
                        cartaPersonalizzataButton.setSize(new Dimension(lCard, hCard));
                        gamePanel.add(cartaPersonalizzataButton);
                    }

                    //Player Dx
                    if (i >= 1 && j >= matriceScacchiera[0].length - 2) {
                        playerIndexMatrix = 3;
                        cartaPersonalizzataButton = generazioneCarteGiocatori2(listaPlayer, playerIndexMatrix, i, j);
                        listaCartaPersonalizzataButton3[matriceScacchiera[i][j] - 1] = cartaPersonalizzataButton;
                        cartaPersonalizzataButton.setSize(new Dimension(lCard, hCard));
                        gamePanel.add(cartaPersonalizzataButton);
                    }
                }
//                indiceCarteScacchiera++;
            }
        }

        //Deck card
        String deckCardPath = getStringPathFromCard(deckCard.getFirst());
        deckButton = new CartaPersonalizzataButton(5 * lCard, 4 * hCard, cartaCopertaPath, deckCardPath);

        if (playerIndex == 0)
            deckButton.addMouseListener(pescaMazzoListeners);

        gamePanel.add(deckButton);

        // Hidden card to simulate cards remaining on the ground of the deck
        CartaPersonalizzataButton deckButtonVisualizzataCoperta = new CartaPersonalizzataButton(5 * lCard, 4 * hCard, cartaCopertaPath, cartaCopertaPath);
        gamePanel.add(deckButtonVisualizzataCoperta);


        // Discarded card
        if (!discardedCards.isEmpty()) {
            Card lastCardDiscarded = discardedCards.getLast();
            String pathDiscardedCars = getStringPathFromCard(lastCardDiscarded);
            discardedCardsButton = new CartaPersonalizzataButton(3 * lCard, 4 * hCard, pathDiscardedCars, pathDiscardedCars);

            if (playerIndex == 0)
                discardedCardsButton.addMouseListener(pescaTerraListeners);

            gamePanel.add(discardedCardsButton);
        }
    }

    /**
     * Generates a custom button representing a player's card.
     *
     * @param listaPlayer The list of players.
     * @param playerIndex The index of the player.
     * @param i           The row index of the card.
     * @param j           The column index of the card.
     * @return The generated custom button.
     */

    private CartaPersonalizzataButton generazioneCarteGiocatori2(List<Player> listaPlayer, int playerIndex, int i, int j) {
        CartaPersonalizzataButton cardButton;
        String pathPlayerCardVisionata;
        String pathPlayerCardRetro;
        Card playerCardVisionata;
        Player player = listaPlayer.get(playerIndex);
        playerCardVisionata = player.getCardFromIndex(matriceScacchiera[i][j]);

        // Setting the path based on whether the card is face up or face down
        if (playerCardVisionata.getFaceUp()) {
            pathPlayerCardVisionata = getStringPathFromCard(playerCardVisionata);
            pathPlayerCardRetro = getStringPathFromCard(playerCardVisionata);
        } else {
            pathPlayerCardVisionata = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/SpriteProgetto/CartaCoperta.png";
            pathPlayerCardRetro = getStringPathFromCard(playerCardVisionata);
        }

        // Creating the custom button
        cardButton = new CartaPersonalizzataButton(j * lCard, i * hCard, pathPlayerCardVisionata, pathPlayerCardRetro);

        // Setting border
        cardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return cardButton;
    }

    /**
     * Adds main elements to the frame using GridBagConstraints for layout.
     *
     * @param mainFrame  The main frame.
     * @param gamePanel  The game panel.
     */
    private void addingMainElementToFrame(JFrame mainFrame, MyPanel gamePanel) {
        GridBagConstraints gbcEmpty1TextArea = new GridBagConstraints();
        gbcEmpty1TextArea.weighty = 0.1;
        gbcEmpty1TextArea.weightx = 0.1;
        gbcEmpty1TextArea.gridx = 0;
        gbcEmpty1TextArea.gridy = 0;

        mainFrame.add(empty1TextArea, gbcEmpty1TextArea);

        GridBagConstraints gbcPlayer1TextArea = new GridBagConstraints();
        gbcPlayer1TextArea.weighty = 0.1;
        gbcPlayer1TextArea.weightx = 0.1;
        gbcPlayer1TextArea.gridx = 1;
        gbcPlayer1TextArea.gridy = 0;
        mainFrame.add(player1TextArea, gbcPlayer1TextArea);

        GridBagConstraints gbcEmpty2TextArea = new GridBagConstraints();
        gbcEmpty2TextArea.weighty = 0.1;
        gbcEmpty2TextArea.weightx = 0.1;
        gbcEmpty2TextArea.gridx = 2;
        gbcEmpty2TextArea.gridy = 0;
        mainFrame.add(empty1TextArea, gbcEmpty1TextArea);

        GridBagConstraints gbcPlayer3TextArea = new GridBagConstraints();
        gbcPlayer3TextArea.weighty = 0.1;
        gbcPlayer3TextArea.weightx = 0.1;
        gbcPlayer3TextArea.gridx = 0;
        gbcPlayer3TextArea.gridy = 1;
        mainFrame.add(player3TextArea, gbcPlayer3TextArea);

        GridBagConstraints gbcMyPanel = new GridBagConstraints();
        gbcMyPanel.weightx = 0.1;
        gbcMyPanel.weighty = 0.1;
        gbcMyPanel.gridx = 1;
        gbcMyPanel.gridy = 1;

        mainFrame.add(gamePanel, gbcMyPanel);

        GridBagConstraints gbcPlayer4TextArea = new GridBagConstraints();
        gbcPlayer4TextArea.weighty = 0.1;
        gbcPlayer4TextArea.weightx = 0.1;
        gbcPlayer4TextArea.gridx = 2;
        gbcPlayer4TextArea.gridy = 1;
        mainFrame.add(player4TextArea, gbcPlayer4TextArea);

        GridBagConstraints gbcEmpty3TextArea = new GridBagConstraints();
        gbcEmpty3TextArea.weighty = 0.1;
        gbcEmpty3TextArea.weightx = 0.1;
        gbcEmpty3TextArea.gridx = 0;
        gbcEmpty3TextArea.gridy = 2;
        mainFrame.add(empty1TextArea, gbcEmpty1TextArea);

        GridBagConstraints gbcPlayer2TextArea = new GridBagConstraints();
        gbcPlayer2TextArea.weighty = 0.1;
        gbcPlayer2TextArea.weightx = 0.1;
        gbcPlayer2TextArea.gridx = 1;
        gbcPlayer2TextArea.gridy = 2;
        mainFrame.add(player2TextArea, gbcPlayer2TextArea);

        GridBagConstraints gbcEmpty4TextArea = new GridBagConstraints();
        gbcEmpty4TextArea.weighty = 0.1;
        gbcEmpty4TextArea.weightx = 0.1;
        gbcEmpty4TextArea.gridx = 2;
        gbcEmpty4TextArea.gridy = 2;
        mainFrame.add(empty1TextArea, gbcEmpty1TextArea);
    }

    /**
     * Converts the card object to its corresponding path string.
     *
     * @param card The card object.
     * @return The path string for the card image.
     */
    private static String getStringPathFromCard(Card card) {
        String path;
        String cardRank = card.getRank().toString().toLowerCase();
        String normalizedCardRank = Character.toUpperCase(cardRank.charAt(0)) + cardRank.substring(1);

        String normalizedCardSeed = "";

        if (card.getSeed() != null) {
            String cardSeed = card.getSeed().toString().toLowerCase();
            normalizedCardSeed = Character.toUpperCase(cardSeed.charAt(0)) + cardSeed.substring(1);
        }

        path = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/SpriteProgetto" +
                "/" + normalizedCardSeed + normalizedCardRank + ".png";

        return path;
    }

    /**
     * Sets up the initial matrix based on the player list.
     *
     * @param listaPlayer The list of players.
     */
    private void impostaMatriceIniziale(List<Player> listaPlayer) {
        impostaMatricePlayerUp(listaPlayer);
        impostaMatricePlayerDown(listaPlayer);

        switch (listaPlayer.size()) {
            case 3:
                impostaMatricePlayerLeft(listaPlayer);
                break;
            case 4:
                impostaMatricePlayerLeft(listaPlayer);
                impostaMatricePlayerRight(listaPlayer);
                break;
        }
    }

    /**
     * Sets up the player matrix on the right side.
     *
     * @param listaPlayer The list of players.
     */
    private void impostaMatricePlayerRight(List<Player> listaPlayer) {
        int nFileCarta;
        int numeroCartePlayer = (listaPlayer.get(3)).getboardCardDimension();
        int indice = numeroCartePlayer;

        if (numeroCartePlayer <= 5)
            nFileCarta = 1;
        else
            nFileCarta = 2;

        if (nFileCarta == 1) {
            for (int i = 2; i < 3 + numeroCartePlayer; i++) {
                matriceScacchiera[i][nColonne - 2] = indice--;
            }
        }

        if (nFileCarta == 2) {
            for (int i = 2 + (10 - numeroCartePlayer); i < 7; i++)
                matriceScacchiera[i][nColonne - 1] = indice--;

            for (int i = 2; i < 7; i++)
                matriceScacchiera[i][nColonne - 2] = indice--;
        }
    }

    /**
     * Sets up the player matrix on the left side.
     *
     * @param listaPlayer The list of players.
     */

    private void impostaMatricePlayerLeft(List<Player> listaPlayer) {
        int indice = 1;
        int nCol;
        int numeroCartePlayer = (listaPlayer.get(2)).getboardCardDimension();

        // Determine the number of columns based on the number of player cards
        if (numeroCartePlayer <= 5)
            nCol = 1;
        else
            nCol = 2;

        // Populate the matrix
        if (nCol == 1) {
            for (int i = 2; i < 2 + numeroCartePlayer; i++)
                matriceScacchiera[i][1] = indice++;
        }

        if (nCol == 2) {
            for (int i = 2; i < 7; i++)
                matriceScacchiera[i][1] = indice++;

            for (int i = 2; i < 7 - (10 - numeroCartePlayer); i++)
                matriceScacchiera[i][0] = indice++;
        }
    }

    /**
     * Sets up the player matrix upwards.
     *
     * @param listaPlayer The list of players.
     */
    private void impostaMatricePlayerUp(List<Player> listaPlayer) {
        int nFileCarte;
        int numeroCartePlayer = (listaPlayer.getFirst()).getboardCardDimension();
        int indice = numeroCartePlayer;

        // Determine the number of rows based on the number of player cards
        if (numeroCartePlayer <= 5)
            nFileCarte = 1;
        else
            nFileCarte = 2;

        // Populate the matrix
        if (nFileCarte == 1) {
            for (int j = 2; j < 2 + numeroCartePlayer; j++) {
                matriceScacchiera[1][j] = indice--;
            }
        }

        if (nFileCarte == 2) {
            for (int j = 2 + (10 - numeroCartePlayer); j < 7; j++) {
                matriceScacchiera[0][j] = indice--;
            }

            for (int j = 2; j < 7; j++) {
                matriceScacchiera[1][j] = indice--;
            }
        }
    }

    /**
     * Sets up the player matrix downwards.
     *
     * @param listaPlayer The list of players.
     */
    private void impostaMatricePlayerDown(List<Player> listaPlayer) {
        int indice = 1;

        int nFileCarte;
        int numeroCartePlayer = (listaPlayer.get(1)).getboardCardDimension();

        // Determine the number of rows based on the number of player cards
        if (numeroCartePlayer <= 5)
            nFileCarte = 1;
        else
            nFileCarte = 2;

        // Populate the matrix
        if (nFileCarte == 1) {
            for (int j = 2; j < 2 + numeroCartePlayer; j++) {
                matriceScacchiera[nRighe - 2][j] = indice++;
            }
        }

        if (nFileCarte == 2) {
            for (int j = 2; j < 7; j++)
                matriceScacchiera[nRighe - 2][j] = indice++;

            for (int j = 2; j < 7 - (10 - numeroCartePlayer); j++)
                matriceScacchiera[nRighe - 1][j] = indice++;
        }
    }

    /**
     * Sets the details of player boxes.
     *
     * @param playerList       The list of players.
     * @param player1TextArea The text area for player 1.
     * @param player2TextArea The text area for player 2.
     * @param player3TextArea The text area for player 3.
     * @param player4TextArea The text area for player 4.
     */
    private void setPlayerBoxDetails(List<Player> playerList, JTextArea player1TextArea, JTextArea player2TextArea,
                                     JTextArea player3TextArea, JTextArea player4TextArea) {

        // Set details for player 1
        player1TextArea.setText("");
        player1TextArea.append("Nickname: " + playerList.get(0).getNickname() + "\n");
        player1TextArea.append("BoardCardDimension: " + playerList.get(0).getboardCardDimension() + "\n");
        player1TextArea.append("Lv: " + playerList.get(0).getPartiteVinte() + " - ");
        player1TextArea.append("Perse: " + playerList.get(0).getPartitePerse());

        // Set details for player 2
        player2TextArea.setText("");
        player2TextArea.append("Nickname: " + playerList.get(1).getNickname() + "\n");
        player2TextArea.append("BoardCardDimension: " + playerList.get(1).getboardCardDimension() + "\n");
        player2TextArea.append("Lv: " + playerList.get(1).getPartiteVinte()+ " - ");
        player2TextArea.append("Perse: " + playerList.get(1).getPartitePerse());

        // Set details for player 3
        if (playerList.size() >= 3) {
            player3TextArea.setText("");
            player3TextArea.append("Nickname: " + playerList.get(2).getNickname() + "\n");
            player3TextArea.append("BoardCardDimension: " + playerList.get(2).getboardCardDimension() + "\n");
            player3TextArea.append("Lv: " + playerList.get(2).getPartiteVinte() +" - ");
            player3TextArea.append("Perse: " + playerList.get(2).getPartitePerse());
        }

        // Set details for player 4
        if (playerList.size() >= 4) {
            player4TextArea.setText("");
            player4TextArea.append("Nickname: " + playerList.get(3).getNickname() + "\n");
            player4TextArea.append("BoardCardDimension: " + playerList.get(3).getboardCardDimension() + "\n");
            player4TextArea.append("Lv: " + playerList.get(3).getPartiteVinte() + " - ");
            player4TextArea.append("Perse: " + playerList.get(3).getPartitePerse());
        }
    }

    /**
     * Invalidates and repaints the panel frame.
     */
    public void repaintPanelFrame() {
        mainFrame.invalidate();
        gamePanel.invalidate();
        mainFrame.repaint();
        gamePanel.repaint();
        mainFrame.revalidate();
        gamePanel.revalidate();
    }

    /**
     * Gets the position of the card near the player.
     *
     * @param playerIndex The index of the player.
     * @return The position of the card.
     */
    private Result getPosizioneCartaVicinoPlayer(int playerIndex) {
        int posX = 0;
        int posY = 0;

        // Determine the position based on the player index
        if (playerIndex == 0) {
            posX = lCard;
            posY = 0 * hCard;
        } else if (playerIndex == 1) {
            posX = 7 * lCard;
            posY = 8 * hCard;
        } else if (playerIndex == 2) {
            posX = 0 * lCard;
            posY = 7 * hCard;
        } else if (playerIndex == 3) {
            posX = 8 * lCard;
            posY = 1 * hCard;
        }
        Result result = new Result(posX, posY);
        return result;
    }

    /**
     * Swaps drawn cards between player's board and hand.
     *
     * @param playerIndex     The index of the player.
     * @param cardInHandIndex The index of the card in hand.
     * @param sceltaPescata   The choice of the drawn card.
     */
    public void scambiaCartePescataEBoard(int playerIndex, int cardInHandIndex, String sceltaPescata) {

        // Get position of card near player
        Result result = getPosizioneCartaVicinoPlayer(playerIndex);
        Point point = new Point();
        point.x = result.posX;
        point.y = result.posY;
        Point cardBoardPoint;

        CartaPersonalizzataButton appCard;

        // Move the card from the player's board to the position near the player
        if (playerIndex == 0) {
            listaCartaPersonalizzataButton0[cardInHandIndex].avviaRotazione();
            SLEEP(500);
            listaCartaPersonalizzataButton0[cardInHandIndex].avviaMovimento(result.posX, result.posY);
            cardBoardPoint = listaCartaPersonalizzataButton0[cardInHandIndex].getLocation();
        } else if (playerIndex == 1) {
            listaCartaPersonalizzataButton1[cardInHandIndex].avviaRotazione();
            SLEEP(500);
            listaCartaPersonalizzataButton1[cardInHandIndex].avviaMovimento(result.posX, result.posY);
            cardBoardPoint = listaCartaPersonalizzataButton1[cardInHandIndex].getLocation();
        } else if (playerIndex == 2) {
            listaCartaPersonalizzataButton2[cardInHandIndex].avviaRotazione();
            SLEEP(500);
            listaCartaPersonalizzataButton2[cardInHandIndex].avviaMovimento(result.posX, result.posY);
            cardBoardPoint = listaCartaPersonalizzataButton2[cardInHandIndex].getLocation();
        } else {
            listaCartaPersonalizzataButton3[cardInHandIndex].avviaRotazione();
            SLEEP(500);
            listaCartaPersonalizzataButton3[cardInHandIndex].avviaMovimento(result.posX, result.posY);
            cardBoardPoint = listaCartaPersonalizzataButton3[cardInHandIndex].getLocation();
        }

        // Move the drawn card from deck to player's board or discard pile
        if (sceltaPescata.equals("mazzo")) {
            /// Move card from deck to player's board
            deckButton.avviaMovimento(cardBoardPoint.x, cardBoardPoint.y);
            SLEEP(1000);

            // Swap the cards
            if (playerIndex == 0) {
                listaCartaPersonalizzataButton0[cardInHandIndex].avviaRotazione();
                SLEEP(500);
                appCard = listaCartaPersonalizzataButton0[cardInHandIndex];
                listaCartaPersonalizzataButton0[cardInHandIndex] = deckButton;
            } else if (playerIndex == 1) {
                listaCartaPersonalizzataButton1[cardInHandIndex].avviaRotazione();
                SLEEP(500);
                appCard = listaCartaPersonalizzataButton1[cardInHandIndex];
                listaCartaPersonalizzataButton1[cardInHandIndex] = deckButton;
            } else if (playerIndex == 2) {
                listaCartaPersonalizzataButton2[cardInHandIndex].avviaRotazione();
                SLEEP(500);
                appCard = listaCartaPersonalizzataButton2[cardInHandIndex];
                listaCartaPersonalizzataButton2[cardInHandIndex] = deckButton;
            } else {
                listaCartaPersonalizzataButton3[cardInHandIndex].avviaRotazione();
                SLEEP(500);
                appCard = listaCartaPersonalizzataButton3[cardInHandIndex];
                listaCartaPersonalizzataButton3[cardInHandIndex] = deckButton;
            }
            deckButton = appCard;

        } else {
            // Move card from discard pile to player's board
            discardedCardsButton.avviaMovimento(cardBoardPoint.x, cardBoardPoint.y);
            SLEEP(100);

            // Swap the cards
            if (playerIndex == 0) {
                appCard = listaCartaPersonalizzataButton0[cardInHandIndex];
                listaCartaPersonalizzataButton0[cardInHandIndex] = discardedCardsButton;
            } else if (playerIndex == 1) {
                appCard = listaCartaPersonalizzataButton1[cardInHandIndex];
                listaCartaPersonalizzataButton1[cardInHandIndex] = discardedCardsButton;
            } else if (playerIndex == 2) {
                appCard = listaCartaPersonalizzataButton2[cardInHandIndex];
                listaCartaPersonalizzataButton2[cardInHandIndex] = discardedCardsButton;
            } else {
                appCard = listaCartaPersonalizzataButton3[cardInHandIndex];
                listaCartaPersonalizzataButton3[cardInHandIndex] = discardedCardsButton;
            }

            discardedCardsButton = appCard;
        }
    }

    /**
     * A record representing a result with X and Y coordinates.
     */

    private record Result(int posX, int posY) {
    }

    /**
     * Updates the view based on changes in the observable.
     *
     * @param o   The observable object.
     * @param arg The argument passed by the observable.
     */
    @Override
    public void update(Observable o, Object arg) {
        List<?> list;

        // Check if the argument is a list
        if (arg instanceof List<?>)
            list = (List<?>) arg;
        else
            throw new RuntimeException("Errore nel metodo Update, non è stata fornita una lista");

        // Handle different signals
        // Signal 0: Initialize players
        if ((int) list.get(0) == 0) {
            List<?> listaPlayer = (List<?>) list.get(1);
            if (listaPlayer.get(0) instanceof Player) {
                int playerIndex = (int) list.get(2);
                Deck discardedCards = ((Deck) list.get(3));
                Deck deckCard = ((Deck) list.get(4));

                creaScacchieraPixel((List<Player>) listaPlayer, playerIndex, discardedCards, deckCard);
            }
            repaintPanelFrame();
        }

        // Signal 8: Rotate the card in the player's board
        if ((int) list.get(0) == 8) {
            int playerIndex = (int) list.get(1);
            int cardInHandIndex = (int) list.get(2);

            if (playerIndex == 0) {
                listaCartaPersonalizzataButton0[cardInHandIndex].avviaRotazione();
                SLEEP(500);
            }
            if (playerIndex == 1) {
                listaCartaPersonalizzataButton1[cardInHandIndex].avviaRotazione();
                SLEEP(500);
            }
            if (playerIndex == 2) {
                listaCartaPersonalizzataButton2[cardInHandIndex].avviaRotazione();
                SLEEP(500);
            }
            if (playerIndex == 3) {
                listaCartaPersonalizzataButton3[cardInHandIndex].avviaRotazione();
                SLEEP(500);
            }
        }

        // Signal 9: Start the initial screen
        if ((int) list.get(0) == 9) {
            new SchermataIniziale();
        }

        // Signal 99: Stop the initial screen loop
        if ((int) list.get(0) == 99) {
            SchermataIniziale.setInterrompiCiclo();
        }

        // Signal 11: Move card (from deck to player's board or discard)
        if ((int) list.get(0) == 11) {
            int playerIndex = (int) list.get(1);
            String sceltaCartaGiocatore = (String) list.get(2);

            Result result = getPosizioneCartaVicinoPlayer(playerIndex);
            System.out.println("Sono qui 3");

            // Move discarded button card to player's board
            if (sceltaCartaGiocatore.equals("terra")) {
                discardedCardsButton.avviaMovimento(result.posX(), result.posY());

                SLEEP(1500);
            }

            // Move deck button card to player's board
            else {
                deckButton.avviaMovimento(result.posX(), result.posY());
                SLEEP(1500);

                deckButton.avviaRotazione();
                SLEEP(800);
            }
        }

        // Signal 12: Move card from player's hand to board
        if ((int) list.get(0) == 12) {
            int playerIndex = (int) list.get(1);
            int cardInHandIndex = (int) list.get(2);
            String sceltaPescata = (String) list.get(5);

            scambiaCartePescataEBoard(playerIndex, cardInHandIndex, sceltaPescata);
        }

        // Signal 13: Discard cards near players
        if ((int) list.get(0) == 13) {
            // Discard cards near players
            String sceltaPescata = (String) list.get(1);
            Deck discardedCards = (Deck) list.get(2);
            Deck deckCard = (Deck) list.get(3);

            // Discard card drawn from the discarded cards
            if (sceltaPescata.equals("terra")) {
                discardedCardsButton.avviaMovimento(3 * lCard, 4 * hCard);
                discardedCardsButton.addMouseListener(pescaTerraListeners);
                SLEEP(1500);
            }

            // Discard card drawn from the deck cards
            else {
                gamePanel.remove(discardedCardsButton);
                gamePanel.revalidate();
                SLEEP(100);

                if (!secondoToLastDiscardedCardPath.isEmpty())
                    gamePanel.remove(secondoToLastDiscardedCardButton);
                gamePanel.revalidate();
                SLEEP(100);

                deckButton.avviaMovimento(3 * lCard, 4 * hCard);
                SLEEP(1800);

                gamePanel.remove(deckButton);
                gamePanel.revalidate();
                SLEEP(100);

                String lastDiscardedCardPath = getStringPathFromCard(discardedCards.getLast());

                if (discardedCards.getDimension() >= 2)
                    secondoToLastDiscardedCardPath = getStringPathFromCard(discardedCards.getSecondToLastCard());

                discardedCardsButton = new CartaPersonalizzataButton(3 * lCard, 4 * hCard, lastDiscardedCardPath, lastDiscardedCardPath);
                discardedCardsButton.addMouseListener(pescaTerraListeners);
                gamePanel.add(discardedCardsButton);
                gamePanel.revalidate();
                SLEEP(100);

                if (discardedCards.getDimension() >= 2) {
                    secondoToLastDiscardedCardButton = new CartaPersonalizzataButton(3 * lCard, 4 * hCard, secondoToLastDiscardedCardPath, secondoToLastDiscardedCardPath);
                    gamePanel.add(secondoToLastDiscardedCardButton);
                }
                gamePanel.revalidate();

                SLEEP(100);

                String deckCardPath = getStringPathFromCard(deckCard.getFirst());
                deckButton = new CartaPersonalizzataButton(5 * lCard, 4 * hCard, cartaCopertaPath, deckCardPath);
                deckButton.addMouseListener(pescaMazzoListeners);
                gamePanel.add(deckButton);

                gamePanel.revalidate();
                gamePanel.repaint();
                SLEEP(500);
            }
        }

        // Signal 14: Add MouseListener to the deck button
        if ((int) list.get(0) == 14) {
            Deck deckCard = (Deck) list.get(1);

            String deckCardPath = getStringPathFromCard(deckCard.getFirst());
            deckButton = new CartaPersonalizzataButton(5 * lCard, 4 * hCard, cartaCopertaPath, deckCardPath);
            deckButton.addMouseListener(pescaMazzoListeners);

            gamePanel.add(deckButton);

            gamePanel.revalidate();
            gamePanel.repaint();

            pescaMazzoListeners.setClickableOnDeckTrue();
            pescaMazzoListeners.setClickableOnBoardCardFalse();

        }

        // Signal 15: Disable MouseListener if it's not the player's turn
        if ((int) list.get(0) == 15) {
            pescaMazzoListeners.setClickableOnDeckFalse();
            pescaMazzoListeners.setClickableOnBoardCardFalse();
        }

        // Signal 16: Enable MouseListener for board card
        if ((int) list.get(0) == 16) {
            pescaMazzoListeners.setClickableOnBoardCardTrue();
        }
    }

    /**
     * Static sleep method to introduce delay.
     *
     * @param millis The number of milliseconds to sleep.
     */
    private static void SLEEP(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Initializes the main game screen.
     */
    public class SchermataIniziale {
        public static boolean interrompiCiclo = false;

        /**
         * Sets the flag to interrupt the cycle.
         */
        public static void setInterrompiCiclo() {
            interrompiCiclo = true;
        }

        /**
         * Constructs the initial screen.
         */
        public SchermataIniziale() {
            // Initialize the animation loop flag
            interrompiCiclo = false;

            // If the main frame is not initialized, create it
            if (mainFrame == null) {
                mainFrame = new JFrame();
                mainFrame.setLayout(new GridBagLayout());
                mainFrame.setSize(new Dimension(1440, 900));
                mainFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            } else {
                // Remove existing UI elements from the main frame
                mainFrame.remove(player1TextArea);
                mainFrame.remove(player2TextArea);
                mainFrame.remove(player3TextArea);
                mainFrame.remove(player4TextArea);
                mainFrame.remove(gamePanel);
                mainFrame.remove(empty1TextArea);
                mainFrame.repaint();

//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
                SLEEP(1000);

            }

            // Create the initial screen panel
            schermataInizialePanel = new MyPanel("/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/CollageCardBackground.png");
            schermataInizialePanel.setLayout(null);

            // Set constraints for the initial screen panel
            GridBagConstraints gbcSchermataInizialePanel = new GridBagConstraints();
            gbcSchermataInizialePanel.weighty = 1;
            gbcSchermataInizialePanel.weightx = 1;
            gbcSchermataInizialePanel.gridx = 0;
            gbcSchermataInizialePanel.gridy = 0;
            gbcSchermataInizialePanel.fill = GridBagConstraints.BOTH;

            // Create and configure the start game button
            startGameButton = new JButton("Avvia Gioco");
            startGameButton.setBackground(Color.BLUE);
            startGameButton.addMouseListener(new PersonalMouseListeners.AvviaGioco());
            startGameButton.setBounds(650, 600, 150, 50);

            // Create and configure the trash label
            trashLabel = new JLabel(new ImageIcon("/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/ProgettoJava/JTrashScritta.png"));
            trashLabel.setBounds(200, 150, 1032, 268);

            // Add UI elements to the initial screen panel
            schermataInizialePanel.add(trashLabel);
            schermataInizialePanel.add(startGameButton);

            // Add the initial screen panel to the main frame
            mainFrame.add(schermataInizialePanel, gbcSchermataInizialePanel);

            // Make the main frame visible
            mainFrame.setVisible(true);

            // Repaint the main frame
            mainFrame.repaint();

            // Animation loop
            while (true) {
                // Exit the loop if animation flag is set
                if (interrompiCiclo)
                    break;

                // Show the trash label
                schermataInizialePanel.add(trashLabel);
                trashLabel.setVisible(true);

//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
                SLEEP(500);

                // Hide the trash label
                trashLabel.setVisible(false);
                schermataInizialePanel.remove(trashLabel);
                schermataInizialePanel.revalidate();
                schermataInizialePanel.repaint();


//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }

                SLEEP(500);
            }
        }
    }
}

