package view;

import model.Card;
import model.CardRank;
import model.CardSeed;
import model.Player;
import model.MatchManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.List;

class MyFrame extends JFrame{
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

    int widthPanel =  77 * 9;
    int heightPanel = 88 * 9;

    int nRighe = 9;
    int nColonne = 9;

    private String path = "";

    private int[][] matriceScacchiera2 = new int[nRighe][nColonne];

    public Scacchiera(){}

    public void creaScacchiera(List<Player> listaPlayer){
        MyFrame mainFrame = new MyFrame();
        mainFrame.setSize(new Dimension(1440, 900));
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MyPanel gamePanel = new MyPanel("/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/BackGround_Resized.png");
        gamePanel.setLayout(new GridLayout(nRighe,nColonne));
        gamePanel.setPreferredSize(new Dimension(693, 774));

        JLabel emptyLabel;
        JButton cardButton;
        ImageIcon cardImageIcon;

        impostaMatriceIniziale(listaPlayer);

        int playerIndex;
        int[] k = new int[listaPlayer.size()];

        fromMatrixToImage(listaPlayer, gamePanel, k);

        addingMainElementToFrame(mainFrame, gamePanel);

        stampaMatrice();
        mainFrame.setVisible(true);
    }

    private void fromMatrixToImage(List<Player> listaPlayer, MyPanel gamePanel, int[] k) {
        ImageIcon cardImageIcon;
        int playerIndex;
        JButton cardButton;
        JLabel emptyLabel;
        for(int i = 0; i < matriceScacchiera2.length; i++){
            for(int j = 0; j < matriceScacchiera2[0].length; j++){
                if(matriceScacchiera2[i][j] == 0){
                    emptyLabel = new JLabel();
                    emptyLabel.setPreferredSize(new Dimension(77,86));
                    emptyLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    gamePanel.add(emptyLabel);
                }
                else{
                    if(i <= 1){
                        playerIndex = 0;
                        path = getStringPathFromCard(listaPlayer, k, playerIndex,matriceScacchiera2[i][j]);
                        k[playerIndex] ++;

                        cardImageIcon = new ImageIcon(path);
                        cardButton = new JButton(cardImageIcon);
                        cardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        gamePanel.add(cardButton);
                    }
                    if(i >= matriceScacchiera2.length-2){
                        playerIndex = 1;
                        path = getStringPathFromCard(listaPlayer, k, playerIndex,matriceScacchiera2[i][j]);
                        k[playerIndex] ++;
                        cardImageIcon = new ImageIcon(path);
                        cardButton = new JButton(cardImageIcon);
                        cardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        gamePanel.add(cardButton);
                    }

                    if(i >= 2 && j <= 1){
                        playerIndex = 2;
                        path = getStringPathFromCard(listaPlayer, k, playerIndex,matriceScacchiera2[i][j]);
                        k[playerIndex] ++;
                        cardImageIcon = new ImageIcon(path);
                        cardButton = new JButton(cardImageIcon);
                        cardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        gamePanel.add(cardButton);
                    }

                    if(i >= 2 && j >= matriceScacchiera2[0].length-2){
                        playerIndex = 3;
                        path = getStringPathFromCard(listaPlayer, k, playerIndex, matriceScacchiera2[i][j]);
                        k[playerIndex] ++;
                        cardImageIcon = new ImageIcon(path);
                        cardButton = new JButton(cardImageIcon);
                        cardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        gamePanel.add(cardButton);
                    }
                }
            }
        }
    }

    private static void addingMainElementToFrame(MyFrame mainFrame, MyPanel gamePanel) {
        GridBagConstraints gbcTextArea = new GridBagConstraints();
        gbcTextArea.weighty = 0.1;
        gbcTextArea.weightx = 0.1;
        gbcTextArea.gridx = 0;
        gbcTextArea.gridy = 0;
        mainFrame.add(new JTextArea("Text 1"),gbcTextArea);

        gbcTextArea = new GridBagConstraints();
        gbcTextArea.weighty = 0.1;
        gbcTextArea.weightx = 0.1;
        gbcTextArea.gridx = 1;
        gbcTextArea.gridy = 0;
        mainFrame.add(new JTextArea("Text 2"), gbcTextArea);

        gbcTextArea = new GridBagConstraints();
        gbcTextArea.weighty = 0.1;
        gbcTextArea.weightx = 0.1;
        gbcTextArea.gridx = 2;
        gbcTextArea.gridy = 0;
        mainFrame.add(new JTextArea("Text 3"),gbcTextArea);

        gbcTextArea = new GridBagConstraints();
        gbcTextArea.weighty = 0.1;
        gbcTextArea.weightx = 0.1;
        gbcTextArea.gridx = 0;
        gbcTextArea.gridy = 1;
        mainFrame.add(new JTextArea("Text 4"),gbcTextArea);

        GridBagConstraints gbcMyPanel = new GridBagConstraints();
        gbcMyPanel.weightx = 0.1;
        gbcMyPanel.weighty = 0.1;
        gbcMyPanel.gridx = 1;
        gbcMyPanel.gridy = 1;
        mainFrame.add(gamePanel, gbcMyPanel);

        gbcTextArea = new GridBagConstraints();
        gbcTextArea.weighty = 0.1;
        gbcTextArea.weightx = 0.1;
        gbcTextArea.gridx = 2;
        gbcTextArea.gridy = 1;
        mainFrame.add(new JTextArea("Text 6"),gbcTextArea);

        gbcTextArea = new GridBagConstraints();
        gbcTextArea.weighty = 0.1;
        gbcTextArea.weightx = 0.1;
        gbcTextArea.gridx = 0;
        gbcTextArea.gridy = 2;
        mainFrame.add(new JTextArea("Text 7"),gbcTextArea);

        gbcTextArea = new GridBagConstraints();
        gbcTextArea.weighty = 0.1;
        gbcTextArea.weightx = 0.1;
        gbcTextArea.gridx = 1;
        gbcTextArea.gridy = 2;
        mainFrame.add(new JTextArea("Text 8"),gbcTextArea);

        gbcTextArea = new GridBagConstraints();
        gbcTextArea.weighty = 0.1;
        gbcTextArea.weightx = 0.1;
        gbcTextArea.gridx = 2;
        gbcTextArea.gridy = 2;
        mainFrame.add(new JTextArea("Text 9"), gbcTextArea);
    }

    private static JLabel getjLabel(String path) {
        JLabel label;
        label = new JLabel(new ImageIcon(path));
        label.setPreferredSize(new Dimension(77,86));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setOpaque(true);
        return label;
    }

    private static String getStringPathFromCard(List<Player> listaPlayer, int[] k, int playerIndex, int numeroMatrice) {
        String path;
        Player player = listaPlayer.get(playerIndex);
        Card card = null;

        card = player.getCardFromIndex(numeroMatrice);

        String cardRank = card.getRank().toString().toLowerCase();
        String normalizedCardRank = Character.toUpperCase(cardRank.charAt(0)) + cardRank.substring(1);

        String normalizedCardSeed = "";

        if(card.getSeed() != null) {
            String cardSeed = card.getSeed().toString().toLowerCase();
            normalizedCardSeed = Character.toUpperCase(cardSeed.charAt(0)) + cardSeed.substring(1);
        }
        else{
            normalizedCardSeed = "";
        }
        path = "";
        path = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/iloveimg-resized" +
                "/" + normalizedCardSeed +normalizedCardRank + ".png";

        return path;
    }

    private void stampaMatrice() {
        for(int[] riga :matriceScacchiera2){
            for(int el : riga)
                System.out.print(el);
            System.out.print("\n");
        }
    }

    private void impostaMatriceIniziale(List<Player> listaPlayer) {

        impostaMatricePlayerUp(listaPlayer);
        impostaMatricePlayerDown(listaPlayer);

        switch (listaPlayer.size()){
            case 3:
                impostaMatricePlayerLeft(listaPlayer);
                break;
            case 4:
                impostaMatricePlayerLeft(listaPlayer);
                impostaMatricePlayerRight(listaPlayer);
                break;
        }

        impostaDiscardedCardsToMatrix();

    }

    private void impostaDiscardedCardsToMatrix() {

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
                matriceScacchiera2[i][nColonne-2] = indice--;
            }
        }

        if (nFileCarta == 2 && numeroCartePlayer % 2 == 0) {
            for (int j = nColonne-1; j >= nColonne-2 ; j--) {
                 for (int i = 2; i < 2 + numeroCartePlayer/2; i++){
                    matriceScacchiera2[i][j] = indice--;
                }
            }
        }

        if (nFileCarta == 2 && numeroCartePlayer % 2 != 0) {

            for (int i = 3; i < numeroCartePlayer / 2; i++)
                matriceScacchiera2[i][18] = indice--;

            for (int i = 3; i < numeroCartePlayer / 2+1; i++)
                matriceScacchiera2[i][17] = indice--;
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
            for (int i = 3; i < 3 + numeroCartePlayer; i++) {
                matriceScacchiera2[i][0] = indice++;
            }
        }

        if (nCol == 2 && numeroCartePlayer % 2 == 0) {
            for (int j = 1; j >= 0 ; j--) {
                for (int i = 2; i < 2 + numeroCartePlayer/2; i++) {
                    matriceScacchiera2[i][j] = indice++;
                }
            }
        }

        if (nCol == 2 && numeroCartePlayer % 2 != 0) {

            for (int i = 2; i < 2 + numeroCartePlayer / 2+1; i++)
                matriceScacchiera2[i][1] = indice++;

            for (int i = 2; i < 2+ numeroCartePlayer / 2; i++)
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
            for (int j = 2; j < 2+ numeroCartePlayer; j++) {
                matriceScacchiera2[1][j] = indice--;
            }
        }

        if (nFileCarte == 2 && numeroCartePlayer % 2 == 0) {
            for (int i = 0; i < nFileCarte; i++) {
                for (int j = 2; j < 2 +(numeroCartePlayer / 2); j++) {
                    matriceScacchiera2[i][j] = indice--;
                }
            }
        }

        if (nFileCarte == 2 && numeroCartePlayer % 2 != 0) {

            for (int i = 0; i < nFileCarte; i++) {

                if (i == 0) {
                    for (int j = 2; j < 2 + numeroCartePlayer / 2; j++)
                        matriceScacchiera2[i][j] = indice--;
                }
                else{
                    for (int j = 2; j < 2 + numeroCartePlayer / 2 + 1; j++)
                        matriceScacchiera2[i][j] = indice--;
                }
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
                matriceScacchiera2[nRighe-2][j] = indice++;
            }

        }

        if (nFileCarte == 2 && numeroCartePlayer % 2 == 0) {
            for (int i = nRighe-2; i < nRighe-2 + nFileCarte; i++) {
                for (int j = 2; j < 2 +(numeroCartePlayer / 2); j++) {
                    matriceScacchiera2[i][j] = indice++;
                }
            }
        }

        if (nFileCarte == 2 && numeroCartePlayer % 2 != 0) {

            for (int i = nRighe-2; i < nRighe-2 + nFileCarte; i++) {

                if (i == 0) {
                    for (int j = 2; j < 2+ numeroCartePlayer / 2 +1; j++)
                        matriceScacchiera2[i][j] = indice++;
                }
                else{
                    for (int j = 2; j < 2+ numeroCartePlayer / 2 ; j++)
                        matriceScacchiera2[i][j] = indice++;
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof List<?>){
            List<?> list = (List<?>) arg;

            if(list.get(0) instanceof Player)
                creaScacchiera((List<Player>)arg);

        }

    }


    public static void main(String[] args){
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
