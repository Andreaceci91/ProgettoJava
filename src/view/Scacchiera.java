package view;

import model.Card;
import model.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.List;

class MyFrame extends JFrame {
}

class MyPanel extends JPanel {
    private BufferedImage backgroundImage;

    public MyPanel(String imagePath) {
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

public class Scacchiera implements Observer {

    final int lCard = 77;
    final int hCard = 88;

    int widthPanel = 77 * 9;
    int heightPanel = 88 * 9;

    int nRighe = 9;
    int nColonne = 9;

    private int[][] matriceScacchiera2;

    JTextArea player1TextArea = new JTextArea();
    JTextArea player2TextArea = new JTextArea();
    JTextArea player3TextArea = new JTextArea();
    JTextArea player4TextArea = new JTextArea();

    MyPanel gamePanel;
    MyFrame mainFrame;

    public Scacchiera() {
    }

    public void creaScacchiera(List<Player> listaPlayer) {
        if(mainFrame != null)
            mainFrame.remove(gamePanel);

        gamePanel = new MyPanel("/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/BackGround_Resized.png");
        gamePanel.setLayout(new GridLayout(9, 9));
        gamePanel.setPreferredSize(new Dimension(widthPanel, heightPanel));

        matriceScacchiera2 = new int[nRighe][nColonne];

        setPlayerBoxDetails(listaPlayer, player1TextArea, player2TextArea, player3TextArea, player4TextArea);
        impostaMatriceIniziale(listaPlayer);

        composeGamePanelFromMatrix(listaPlayer, gamePanel);

        if(mainFrame == null){
            mainFrame = new MyFrame();
            mainFrame.setSize(new Dimension(1440, 900));
            mainFrame.setLayout(new GridBagLayout());
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            addingMainElementToFrame(mainFrame, gamePanel);

            stampaMatrice();
            mainFrame.setVisible(true);
            repaintPanelFrame();
        }
        else{
            gamePanel.setOpaque(true);
            addingMainElementToFrame(mainFrame, gamePanel);
            repaintPanelFrame();
        }
    }

    private void composeGamePanelFromMatrix(List<Player> listaPlayer, MyPanel gamePanel) {
        ImageIcon cardImageIcon;
        int playerIndex;
        JButton cardButton;
        JButton emptyButton;
        Player player;
        Card playerCardVisionata;
        String pathPlayerCardVisionata;

        for (int i = 0; i < matriceScacchiera2.length; i++) {
            for (int j = 0; j < matriceScacchiera2[0].length; j++) {

                if (matriceScacchiera2[i][j] == 0) {
                    emptyButton = new JButton();
                    emptyButton.setPreferredSize(new Dimension(lCard, hCard));
                    emptyButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    gamePanel.add(emptyButton);
                } else {
                    if (i == 0 || i == 1) {
                        playerIndex = 0;

                        player = listaPlayer.get(playerIndex);
                        playerCardVisionata = player.getCardFromIndex(matriceScacchiera2[i][j]);

                        if(playerCardVisionata.getFaceUp())
                            pathPlayerCardVisionata = getStringPathFromCard(playerCardVisionata);
                        else
                            pathPlayerCardVisionata = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/iloveimg-resized/CartaCoperta.png";

                        cardImageIcon = new ImageIcon(pathPlayerCardVisionata);
                        cardButton = new JButton(cardImageIcon);

//                        if (playerCardVisionata.getFaceUp())
//                            cardButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
//                        else
//                            cardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                        gamePanel.add(cardButton);
                    }
                    if (i == matriceScacchiera2.length - 1 || i == matriceScacchiera2.length - 2) {
                        playerIndex = 1;
                        player = listaPlayer.get(playerIndex);
                        playerCardVisionata = player.getCardFromIndex(matriceScacchiera2[i][j]);
                        pathPlayerCardVisionata = getStringPathFromCard(playerCardVisionata);

                        cardImageIcon = new ImageIcon(pathPlayerCardVisionata);
                        cardButton = new JButton(cardImageIcon);

                        if (playerCardVisionata.getFaceUp())
                            cardButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                        else
                            cardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                        gamePanel.add(cardButton);
                    }

                    if (i > 1 && j <= 1) {
//                        System.out.println("player left");
                        playerIndex = 2;
                        player = listaPlayer.get(playerIndex);
                        playerCardVisionata = player.getCardFromIndex(matriceScacchiera2[i][j]);
                        pathPlayerCardVisionata = getStringPathFromCard(playerCardVisionata);

                        cardImageIcon = new ImageIcon(pathPlayerCardVisionata);
                        cardButton = new JButton(cardImageIcon);

                        if (playerCardVisionata.getFaceUp())
                            cardButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                        else
                            cardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                        gamePanel.add(cardButton);
                    }

                    if (i >= 1 && j >= matriceScacchiera2[0].length - 2) {
                        playerIndex = 3;
                        player = listaPlayer.get(playerIndex);
                        playerCardVisionata = player.getCardFromIndex(matriceScacchiera2[i][j]);
                        pathPlayerCardVisionata = getStringPathFromCard(playerCardVisionata);

                        cardImageIcon = new ImageIcon(pathPlayerCardVisionata);
                        cardButton = new JButton(cardImageIcon);

                        if (playerCardVisionata.getFaceUp())
                            cardButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                        else
                            cardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                        gamePanel.add(cardButton);
                    }
                }
            }
        }

        ImageIcon deckImageIcon = new ImageIcon("/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/" +
                "iloveimg-resized/CartaCoperta.png");
        JButton deckButton = new JButton(deckImageIcon);
        sostituisciElemento(5, 4, deckButton);
    }

    private void addingMainElementToFrame(MyFrame mainFrame, MyPanel gamePanel) {
        GridBagConstraints gbcEmpty1TextArea = new GridBagConstraints();
        gbcEmpty1TextArea.weighty = 0.1;
        gbcEmpty1TextArea.weightx = 0.1;
        gbcEmpty1TextArea.gridx = 0;
        gbcEmpty1TextArea.gridy = 0;
        mainFrame.add(new JTextArea("Text 1"), gbcEmpty1TextArea);

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
        mainFrame.add(new JTextArea("Text 2"), gbcEmpty2TextArea);

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
        mainFrame.add(new JTextArea("Text 3"), gbcEmpty3TextArea);


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
        mainFrame.add(new JTextArea("Text 4"), gbcEmpty4TextArea);
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

        path = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/iloveimg-resized" +
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
                matriceScacchiera2[i][nColonne - 2] = indice--;
            }
        }

        if(nFileCarta == 2){
            for (int i = 2 + (10 - numeroCartePlayer); i < 7; i++)
                matriceScacchiera2[i][nColonne - 1] = indice--;

            for (int i = 2 ; i < 7; i++)
                matriceScacchiera2[i][nColonne - 2] = indice--;
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
                matriceScacchiera2[i][1] = indice++;
        }

        if (nCol == 2) {
            for (int i = 2; i < 7; i++)
                matriceScacchiera2[i][1] = indice++;

            for (int i = 2; i < 7 - (10 - numeroCartePlayer); i++)
                matriceScacchiera2[i][0] = indice++;
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
                matriceScacchiera2[1][j] = indice--;
            }
        }

        if (nFileCarte == 2) {
            for (int j = 2 + (10 - numeroCartePlayer); j < 7; j++) {
                matriceScacchiera2[0][j] = indice--;
            }

            for (int j = 2; j < 7; j++) {
                matriceScacchiera2[1][j] = indice--;
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
                matriceScacchiera2[nRighe - 2][j] = indice++;
            }
        }

        if (nFileCarte == 2) {
            for (int j = 2; j < 7; j++)
                matriceScacchiera2[nRighe - 2][j] = indice++;

            for (int j = 2; j < 7 - (10 - numeroCartePlayer); j++)
                matriceScacchiera2[nRighe - 1][j] = indice++;
        }
    }

    private void segnaleModificaCartaSulTavolo(Card card) {
        String path = getStringPathFromCard(card);
        ImageIcon imageIcon = new ImageIcon(path);
        JButton buttonCard = new JButton(imageIcon);
        buttonCard.setPreferredSize(new Dimension(lCard, hCard));
        buttonCard.setOpaque(true);
        sostituisciElemento(3, 4, buttonCard);
    }

    private void segnaleModificaCartaVicinoGiocatore(Card card, int p_col, int p_row) {
        String path = getStringPathFromCard(card);
        ImageIcon imageIcon = new ImageIcon(path);
        JButton buttonCard = new JButton(imageIcon);
        buttonCard.setPreferredSize(new Dimension(lCard, hCard));
        buttonCard.setOpaque(true);
        sostituisciElemento(p_col, p_row, buttonCard);
        repaintPanelFrame();

    }

    private void segnaleRimuoviCartaSpecifica(int p_col, int p_row) {
        JButton buttonCard = new JButton();
        buttonCard.setPreferredSize(new Dimension(lCard, hCard));
        buttonCard.setOpaque(false);
        sostituisciElemento(p_col, p_row, buttonCard);
        repaintPanelFrame();
    }

    public void sostituisciElemento(int p_col, int p_row, JButton component) {
        int indexToRemove = (p_row * nColonne + p_col);
        this.gamePanel.remove(indexToRemove);

        component.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));

        this.gamePanel.add(component, indexToRemove);
    }

    private void setPlayerBoxDetails(List<Player> playerList, JTextArea player1TextArea, JTextArea player2TextArea,
                                     JTextArea player3TextArea, JTextArea player4TextArea) {

        player1TextArea.setText("");
        player1TextArea.append("Nickname: " + playerList.get(0).getNickname() + "\n");
        player1TextArea.append("BoardCardDimension: " + playerList.get(0).getboardCardDimension());

        player2TextArea.setText("");
        player2TextArea.append("Nickname: " + playerList.get(1).getNickname() + "\n");
        player2TextArea.append("BoardCardDimension: " + playerList.get(1).getboardCardDimension());

        if (playerList.size() >= 3) {
            player3TextArea.setText("");
            player3TextArea.append("Nickname: " + playerList.get(2).getNickname() + "\n");
            player3TextArea.append("BoardCardDimension: " + playerList.get(2).getboardCardDimension());
        }

        if (playerList.size() >= 4) {
            player4TextArea.setText("");
            player4TextArea.append("Nickname: " + playerList.get(3).getNickname() + "\n");
            player4TextArea.append("BoardCardDimension: " + playerList.get(3).getboardCardDimension());
        }
    }

    private void stampaMatrice() {
        for (int[] riga : matriceScacchiera2) {
            for (int el : riga)
                System.out.print(el);
            System.out.print("\n");
        }
    }

    private void repaintPanelFrame() {
        mainFrame.invalidate();
        gamePanel.invalidate();
        mainFrame.repaint();
        gamePanel.repaint();
        mainFrame.revalidate();
        gamePanel.revalidate();
    }

    @Override
    public void update(Observable o, Object arg) {
        List<?> list = null;

        if (arg instanceof List<?>)
            list = (List<?>) arg;
        else
            throw new RuntimeException("Errore nel metodo Update, non è stata fornita una lista");

        //Passato segnale 0 che corrisponde ad inizializzazione giocatori
        if ((int) list.get(0) == 0) {
            List<?> listaPlayer = (List<?>) list.get(1);
            if (listaPlayer.get(0) instanceof Player) {

//                setPlayerBoxDetails((List<Player>) listaPlayer);
                creaScacchiera((List<Player>) listaPlayer);
            }

            repaintPanelFrame();
        }

        //Segnale 1: aggiornamento carta sul tavolo
        // [0] int numero del segnale
        // [1] Card
        if ((int) list.get(0) == 1) {
            if (list.get(1) instanceof Card) {
                segnaleModificaCartaSulTavolo((Card) list.get(1));
            }
            repaintPanelFrame();
        }

        //Segnale 2: Aggiorno carta pescata vicino a giocatore
        if ((int) list.get(0) == 2) {
            switch ((int) list.get(2)) {
                case 0:
                    segnaleModificaCartaVicinoGiocatore((Card) list.get(1), 1, 0);
                    break;
                case 1:
                    segnaleModificaCartaVicinoGiocatore((Card) list.get(1), nColonne - 2, nRighe - 2);
                    break;
                case 2:
                    segnaleModificaCartaVicinoGiocatore((Card) list.get(1), 0, nRighe - 2);
                    break;
                case 3:
                    segnaleModificaCartaVicinoGiocatore((Card) list.get(1), nColonne - 1, 1);
                    break;
            }
            repaintPanelFrame();
        }

        //Segnale 3: Rimuovo carta pescata dal giocatore
        //[0] Segnale
        //[1] Giocatore
        if ((int) list.get(0) == 3) {
            switch ((int) list.get(1)) {
                case 0:
                    segnaleRimuoviCartaSpecifica(1, 0);
                    break;
                case 1:
                    segnaleRimuoviCartaSpecifica(nColonne - 2, nRighe - 2);
                    break;
                case 2:
                    segnaleRimuoviCartaSpecifica(0, nRighe - 2);
                    break;
                case 3:
                    segnaleRimuoviCartaSpecifica(nColonne-1, 1);
                    break;
            }
            repaintPanelFrame();
        }

        //Segnale 4 metodo Repain Game panel
        if ((int) list.get(0) == 4) {
            //Reimposto scacchiera giocatori
            impostaMatriceIniziale((List<Player>) list.get(1));
            //Creo una panel di appoggio
            MyPanel appGamePanel = new MyPanel("/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/BackGround_Resized.png");
            appGamePanel.setLayout(new GridLayout(9, 9));
            appGamePanel.setPreferredSize(new Dimension(widthPanel, heightPanel));

            composeGamePanelFromMatrix((List<Player>) list.get(1), appGamePanel);

            //Rimuovo ed aggiungo il nuovo pannello
            mainFrame.remove(this.gamePanel);


            GridBagConstraints gbcAppPanel = new GridBagConstraints();
            gbcAppPanel.weightx = 0.1;
            gbcAppPanel.weighty = 0.1;
            gbcAppPanel.gridx = 1;
            gbcAppPanel.gridy = 1;

            mainFrame.add(appGamePanel, gbcAppPanel);
            this.gamePanel = appGamePanel;

            ImageIcon deckImageIcon = new ImageIcon("/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/" +
                    "iloveimg-resized/CartaCoperta.png");
            JButton deckButton = new JButton(deckImageIcon);
            sostituisciElemento(5, 4, deckButton);

            //Ristampo il pannello
            repaintPanelFrame();
        }

        //Segnale 5 rimuovi carta sul tavolo
        if ((int) list.get(0) == 5) {
            segnaleRimuoviCartaSpecifica(3, 4);
            repaintPanelFrame();
        }
    }

    public static void main(String[] args) {
//        Player player1 = new Player("Andrea");
//        player1.takeCardToBoard(new Card(CardSeed.FIORI, CardRank.ASSO));
//        player1.takeCardToBoard(new Card(CardSeed.FIORI, CardRank.DUE));
//        player1.takeCardToBoard(new Card(CardSeed.FIORI, CardRank.TRE));
//        player1.takeCardToBoard(new Card(CardSeed.FIORI, CardRank.QUATTRO));
//        player1.takeCardToBoard(new Card(CardSeed.FIORI, CardRank.CINQUE));
//        player1.takeCardToBoard(new Card(CardSeed.FIORI, CardRank.SEI));
//        player1.takeCardToBoard(new Card(CardSeed.FIORI, CardRank.SETTE));
//        player1.takeCardToBoard(new Card(CardSeed.FIORI, CardRank.OTTO));
//        player1.takeCardToBoard(new Card(CardSeed.FIORI, CardRank.NOVE));
//        player1.takeCardToBoard(new Card(CardSeed.FIORI, CardRank.DIECI));
//
//        Player player2 = new Player("Francesco");
//        player2.takeCardToBoard(new Card(CardSeed.QUADRI, CardRank.ASSO));
//        player2.takeCardToBoard(new Card(CardSeed.QUADRI, CardRank.DUE));
//        player2.takeCardToBoard(new Card(CardSeed.QUADRI, CardRank.TRE));
//        player2.takeCardToBoard(new Card(CardSeed.QUADRI, CardRank.QUATTRO));
//        player2.takeCardToBoard(new Card(CardSeed.QUADRI, CardRank.CINQUE));
//        player2.takeCardToBoard(new Card(CardSeed.QUADRI, CardRank.SEI));
//        player2.takeCardToBoard(new Card(CardSeed.QUADRI, CardRank.SETTE));
//        player2.takeCardToBoard(new Card(CardSeed.QUADRI, CardRank.OTTO));
//        player2.takeCardToBoard(new Card(CardSeed.QUADRI, CardRank.NOVE));
//        player2.takeCardToBoard(new Card(CardSeed.QUADRI, CardRank.DIECI));
//
//        Player player3 = new Player("Flavio");
//        player3.takeCardToBoard(new Card(CardSeed.CUORI, CardRank.ASSO));
//        player3.takeCardToBoard(new Card(CardSeed.CUORI, CardRank.DUE));
//        player3.takeCardToBoard(new Card(CardSeed.CUORI, CardRank.TRE));
//        player3.takeCardToBoard(new Card(CardSeed.CUORI, CardRank.QUATTRO));
//        player3.takeCardToBoard(new Card(CardSeed.CUORI, CardRank.CINQUE));
//        player3.takeCardToBoard(new Card(CardSeed.CUORI, CardRank.SEI));
//        player3.takeCardToBoard(new Card(CardSeed.CUORI, CardRank.SETTE));
//        player3.takeCardToBoard(new Card(CardSeed.CUORI, CardRank.OTTO));
//        player3.takeCardToBoard(new Card(CardSeed.CUORI, CardRank.NOVE));
//        player3.takeCardToBoard(new Card(CardSeed.CUORI, CardRank.DIECI));
//
//        Player player4 = new Player("Gaetano");
//
//        player4.takeCardToBoard(new Card(CardSeed.PICCHE, CardRank.ASSO));
//        player4.takeCardToBoard(new Card(CardSeed.PICCHE, CardRank.DUE));
//        player4.takeCardToBoard(new Card(CardSeed.PICCHE, CardRank.TRE));
//        player4.takeCardToBoard(new Card(CardSeed.PICCHE, CardRank.QUATTRO));
//        player4.takeCardToBoard(new Card(CardSeed.PICCHE, CardRank.CINQUE));
//        player4.takeCardToBoard(new Card(CardSeed.PICCHE, CardRank.SEI));
//        player4.takeCardToBoard(new Card(CardSeed.PICCHE, CardRank.SETTE));
//        player4.takeCardToBoard(new Card(CardSeed.PICCHE, CardRank.OTTO));
//        player4.takeCardToBoard(new Card(CardSeed.PICCHE, CardRank.NOVE));
//        player4.takeCardToBoard(new Card(CardSeed.PICCHE, CardRank.DIECI));
//
//        Scacchiera scacchiera = new Scacchiera(Arrays.asList(player1,player2, player3, player4));
    }
}
