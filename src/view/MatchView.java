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

public class MatchView implements Observer {

    final int lCard = 76;
    final int hCard = 88;

    int widthPanel = 77 * 9;
    int heightPanel = 88 * 9;

    int nRighe = 9;
    int nColonne = 9;

    private int[][] matriceScacchiera;

    JTextArea player1TextArea = new JTextArea();
    JTextArea player2TextArea = new JTextArea();
    JTextArea player3TextArea = new JTextArea();
    JTextArea player4TextArea = new JTextArea();

    JTextArea empty1TextArea = new JTextArea();

    MyPanel schermataInizialePanel;
    JLabel trashLabel;
    JButton startGameButton;

    MyPanel gamePanel;
    JFrame mainFrame;

    CartaPersonalizzataButton[] listaCartaPersonalizzataButton0;
    CartaPersonalizzataButton[] listaCartaPersonalizzataButton1;
    CartaPersonalizzataButton[] listaCartaPersonalizzataButton2;
    CartaPersonalizzataButton[] listaCartaPersonalizzataButton3;

    CartaPersonalizzataButton discardedCardsButton;
    CartaPersonalizzataButton deckButton;
    CartaPersonalizzataButton secondoToLastDiscardedCardButton;

    String cartaCopertaPath = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/" + "SpriteProgetto/CartaCoperta.png";
    String secondoToLastDiscardedCardPath = "";

    PersonalMouseListeners.PescaTerraListeners pescaTerraListeners = new PersonalMouseListeners.PescaTerraListeners();
    PersonalMouseListeners.PescaMazzoListeners pescaMazzoListeners = new PersonalMouseListeners.PescaMazzoListeners();

    public MatchView() {
    }

    public void creaScacchieraPixel(List<Player> listaPlayer, int playerIndex, Deck discardedCards, Deck deckCard) {
        if (schermataInizialePanel != null)
            mainFrame.remove(schermataInizialePanel);

        if (gamePanel != null)
            mainFrame.remove(gamePanel);
        else {
            //Setto Mainframe prima volta
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

    public void composeGamePanelFromMatrix(List<Player> listaPlayer, MyPanel gamePanel, int playerIndex, Deck discardedCards, Deck deckCard) {
        int playerIndexMatrix;
        CartaPersonalizzataButton cartaPersonalizzataButton;

        listaCartaPersonalizzataButton0 = new CartaPersonalizzataButton[10];
        listaCartaPersonalizzataButton1 = new CartaPersonalizzataButton[10];
        listaCartaPersonalizzataButton2 = new CartaPersonalizzataButton[10];
        listaCartaPersonalizzataButton3 = new CartaPersonalizzataButton[10];

//        int indiceCarteScacchiera = 0;

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

        //Carta del mazzo
        String deckCardPath = getStringPathFromCard(deckCard.getFirst());
        deckButton = new CartaPersonalizzataButton(5 * lCard, 4 * hCard, cartaCopertaPath, deckCardPath);

        if (playerIndex == 0)
            deckButton.addMouseListener(pescaMazzoListeners);

        gamePanel.add(deckButton);

        //Carta sempre coperta per simulare carte che rimangono a terra del mazzo
        CartaPersonalizzataButton deckButtonVisualizzataCoperta = new CartaPersonalizzataButton(5 * lCard, 4 * hCard, cartaCopertaPath, cartaCopertaPath);
        gamePanel.add(deckButtonVisualizzataCoperta);


        // Carta a terra
        if (!discardedCards.isEmpty()) {
            Card lastCardDiscarded = discardedCards.getLast();
            String pathDiscardedCars = getStringPathFromCard(lastCardDiscarded);
            discardedCardsButton = new CartaPersonalizzataButton(3 * lCard, 4 * hCard, pathDiscardedCars, pathDiscardedCars);

            if (playerIndex == 0)
                discardedCardsButton.addMouseListener(pescaTerraListeners);

            gamePanel.add(discardedCardsButton);
        }
    }

    private CartaPersonalizzataButton generazioneCarteGiocatori2(List<Player> listaPlayer, int playerIndex, int i, int j) {
        CartaPersonalizzataButton cardButton;
        String pathPlayerCardVisionata;
        String pathPlayerCardRetro;
        Card playerCardVisionata;
        Player player = listaPlayer.get(playerIndex);
        playerCardVisionata = player.getCardFromIndex(matriceScacchiera[i][j]);

        if (playerCardVisionata.getFaceUp()) {
            pathPlayerCardVisionata = getStringPathFromCard(playerCardVisionata);
            pathPlayerCardRetro = getStringPathFromCard(playerCardVisionata);
        } else {
            pathPlayerCardVisionata = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/SpriteProgetto/CartaCoperta.png";
            pathPlayerCardRetro = getStringPathFromCard(playerCardVisionata);
        }

        cardButton = new CartaPersonalizzataButton(j * lCard, i * hCard, pathPlayerCardVisionata, pathPlayerCardRetro);

        cardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return cardButton;
    }

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

    private void impostaMatricePlayerLeft(List<Player> listaPlayer) {
        int indice = 1;
        int nCol;
        int numeroCartePlayer = (listaPlayer.get(2)).getboardCardDimension();

        if (numeroCartePlayer <= 5)
            nCol = 1;
        else
            nCol = 2;

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

    private void impostaMatricePlayerUp(List<Player> listaPlayer) {
        int nFileCarte;
        int numeroCartePlayer = (listaPlayer.getFirst()).getboardCardDimension();
        int indice = numeroCartePlayer;

        if (numeroCartePlayer <= 5)
            nFileCarte = 1;
        else
            nFileCarte = 2;

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

    private void impostaMatricePlayerDown(List<Player> listaPlayer) {
        int indice = 1;

        int nFileCarte;
        int numeroCartePlayer = (listaPlayer.get(1)).getboardCardDimension();

        if (numeroCartePlayer <= 5)
            nFileCarte = 1;
        else
            nFileCarte = 2;

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

    private void setPlayerBoxDetails(List<Player> playerList, JTextArea player1TextArea, JTextArea player2TextArea,
                                     JTextArea player3TextArea, JTextArea player4TextArea) {

        player1TextArea.setText("");
        player1TextArea.append("Nickname: " + playerList.get(0).getNickname() + "\n");
        player1TextArea.append("BoardCardDimension: " + playerList.get(0).getboardCardDimension() + "\n");
        player1TextArea.append("Lv: " + playerList.get(0).getPartiteVinte() + " - ");
        player1TextArea.append("Perse: " + playerList.get(0).getPartitePerse());

        player2TextArea.setText("");
        player2TextArea.append("Nickname: " + playerList.get(1).getNickname() + "\n");
        player2TextArea.append("BoardCardDimension: " + playerList.get(1).getboardCardDimension() + "\n");
        player2TextArea.append("Lv: " + playerList.get(1).getPartiteVinte()+ " - ");
        player2TextArea.append("Perse: " + playerList.get(1).getPartitePerse());

        if (playerList.size() >= 3) {
            player3TextArea.setText("");
            player3TextArea.append("Nickname: " + playerList.get(2).getNickname() + "\n");
            player3TextArea.append("BoardCardDimension: " + playerList.get(2).getboardCardDimension() + "\n");
            player3TextArea.append("Lv: " + playerList.get(2).getPartiteVinte() +" - ");
            player3TextArea.append("Perse: " + playerList.get(2).getPartitePerse());
        }

        if (playerList.size() >= 4) {
            player4TextArea.setText("");
            player4TextArea.append("Nickname: " + playerList.get(3).getNickname() + "\n");
            player4TextArea.append("BoardCardDimension: " + playerList.get(3).getboardCardDimension() + "\n");
            player4TextArea.append("Lv: " + playerList.get(3).getPartiteVinte() + " - ");
            player4TextArea.append("Perse: " + playerList.get(3).getPartitePerse());
        }
    }

    public void repaintPanelFrame() {
        mainFrame.invalidate();
        gamePanel.invalidate();
        mainFrame.repaint();
        gamePanel.repaint();
        mainFrame.revalidate();
        gamePanel.revalidate();
    }

    private Result getPosizioneCartaVicinoPlayer(int playerIndex) {
        int posX = 0;
        int posY = 0;

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

    public void scambiaCartePescataEBoard(int playerIndex, int cardInHandIndex, String sceltaPescata) {

        //Prendo posizione carta Vicino al giocatore in base al PlayerIndex
        Result result = getPosizioneCartaVicinoPlayer(playerIndex);
        Point point = new Point();
        point.x = result.posX;
        point.y = result.posY;
        Point cardBoardPoint;

        CartaPersonalizzataButton appCard;

        //Muovo la carta dal board del giocatore alla posizione vicino al giocatore
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

        if (sceltaPescata.equals("mazzo")) {
            //Muovo la carta pescata dal mazzo nelle carte del Board del giocatore
            deckButton.avviaMovimento(cardBoardPoint.x, cardBoardPoint.y);
            SLEEP(1000);

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
            //Muovo la carta pescata mazzo nelle carte del Board del giocatore
            discardedCardsButton.avviaMovimento(cardBoardPoint.x, cardBoardPoint.y);
            SLEEP(100);

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

    private record Result(int posX, int posY) {
    }

    @Override
    public void update(Observable o, Object arg) {
        List<?> list;

        if (arg instanceof List<?>)
            list = (List<?>) arg;
        else
            throw new RuntimeException("Errore nel metodo Update, non è stata fornita una lista");

        //Segnale 0 che corrisponde ad inizializzazione giocatori
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

        //Avvio la rotazione della carta nel board del giocatoere che viene scambiata
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

        //Passato segnale 9 che corrisponde ad inizializzazione giocatori
        if ((int) list.get(0) == 9) {
            new SchermataIniziale();
        }

        //Passato segnale 99 che corrisponde ad inizializzazione giocatori
        if ((int) list.get(0) == 99) {
            SchermataIniziale.setInterrompiCiclo();
        }

        //Movimento carta(terra o mazzo) -> vicino ai giocatori
        if ((int) list.get(0) == 11) {
            int playerIndex = (int) list.get(1);
            String sceltaCartaGiocatore = (String) list.get(2);

            Result result = getPosizioneCartaVicinoPlayer(playerIndex);
            System.out.println("Sono qui 3");

            //Movimento carta a Terra
            if (sceltaCartaGiocatore.equals("terra")) {
                discardedCardsButton.avviaMovimento(result.posX(), result.posY());

                SLEEP(1500);

                System.out.println("sono qui 4");
            }
            //Movimento carta Mazzo
            else {
                deckButton.avviaMovimento(result.posX(), result.posY());
                SLEEP(1500);

                deckButton.avviaRotazione();
                SLEEP(800);
            }
        }

        //Movimento carta da vicino giocatore a Board del giocatore
        if ((int) list.get(0) == 12) {
            int playerIndex = (int) list.get(1);
            int cardInHandIndex = (int) list.get(2);
            String sceltaPescata = (String) list.get(5);

            scambiaCartePescataEBoard(playerIndex, cardInHandIndex, sceltaPescata);
        }

        //Scarto delle carte vicino ai player
        if ((int) list.get(0) == 13) {
            String sceltaPescata = (String) list.get(1);
            Deck discardedCards = (Deck) list.get(2);
            Deck deckCard = (Deck) list.get(3);

            //Scarto carta pescata da terra
            if (sceltaPescata.equals("terra")) {
                discardedCardsButton.avviaMovimento(3 * lCard, 4 * hCard);
                discardedCardsButton.addMouseListener(pescaTerraListeners);
                SLEEP(1500);
            }
            //Scarto carta pescata dal mazzo
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

        //Aggiunta del MouseListener al DeckButton
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

        //Disattiva MouseListener se non è turno dell'umano
        if ((int) list.get(0) == 15) {
            pescaMazzoListeners.setClickableOnDeckFalse();
            pescaMazzoListeners.setClickableOnBoardCardFalse();
        }

        if ((int) list.get(0) == 16) {
            pescaMazzoListeners.setClickableOnBoardCardTrue();
        }
    }

    private static void SLEEP(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public class SchermataIniziale {
        public static boolean interrompiCiclo = false;

        public static void setInterrompiCiclo() {
            interrompiCiclo = true;
        }

        public SchermataIniziale() {
            interrompiCiclo = false;

            if (mainFrame == null) {
                mainFrame = new JFrame();
                mainFrame.setLayout(new GridBagLayout());
                mainFrame.setSize(new Dimension(1440, 900));
                mainFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            } else {
                mainFrame.remove(player1TextArea);
                mainFrame.remove(player2TextArea);
                mainFrame.remove(player3TextArea);
                mainFrame.remove(player4TextArea);
                mainFrame.remove(gamePanel);
                mainFrame.remove(empty1TextArea);
                mainFrame.repaint();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

            schermataInizialePanel = new MyPanel("/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/CollageCardBackground.png");
            schermataInizialePanel.setLayout(null);

            GridBagConstraints gbcSchermataInizialePanel = new GridBagConstraints();
            gbcSchermataInizialePanel.weighty = 1;
            gbcSchermataInizialePanel.weightx = 1;
            gbcSchermataInizialePanel.gridx = 0;
            gbcSchermataInizialePanel.gridy = 0;
            gbcSchermataInizialePanel.fill = GridBagConstraints.BOTH;


            startGameButton = new JButton("Avvia Gioco");
            startGameButton.setBackground(Color.BLUE);
            startGameButton.addMouseListener(new PersonalMouseListeners.AvviaGioco());
            startGameButton.setBounds(650, 600, 150, 50);

            trashLabel = new JLabel(new ImageIcon("/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/ProgettoJava/JTrashScritta.png"));
            trashLabel.setBounds(200, 150, 1032, 268);

            schermataInizialePanel.add(trashLabel);
            schermataInizialePanel.add(startGameButton);

            mainFrame.add(schermataInizialePanel, gbcSchermataInizialePanel);

            mainFrame.setVisible(true);

            mainFrame.repaint();


            while (true) {

                if (interrompiCiclo)
                    break;

                schermataInizialePanel.add(trashLabel);
                trashLabel.setVisible(true);


                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                trashLabel.setVisible(false);
                schermataInizialePanel.remove(trashLabel);
                schermataInizialePanel.revalidate();
                schermataInizialePanel.repaint();


                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}

