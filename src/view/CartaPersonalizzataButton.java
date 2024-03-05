package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CartaPersonalizzataButton extends JButton implements ActionListener {
    private int x;
    private int y;
    private String cartaVisualizzataPath;
    private String retroCartaVisualizzataPath;
    private int deltaX;
    private int deltaY;
    private int finalx;
    private int finaly;
    private BufferedImage frontCardImage;
    private BufferedImage backCardImage;
    private boolean revealing = false;
    private double scaleX = 1.0;

    private Timer timerMovimento;
    private Timer timerRotazione;

    public CartaPersonalizzataButton(int x, int y, String cartaVisualizzataPath, String retroCartaVisualizzataPath) {
        this.x = x;
        this.y = y;
        this.cartaVisualizzataPath = cartaVisualizzataPath;
        this.retroCartaVisualizzataPath = retroCartaVisualizzataPath;
        timerMovimento = new Timer(5, this);
        timerRotazione = new Timer(5, this);

        setBounds(x, y, 77, 88);

        try {
            frontCardImage = ImageIO.read(new File(this.cartaVisualizzataPath));
            backCardImage = ImageIO.read(new File(this.retroCartaVisualizzataPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //Effetto movimento
        if (e.getSource().equals(timerMovimento)) {

            if (x == finalx && y == finaly) {
                timerMovimento.stop();
                this.setBounds(x,y,76,88);
            }

            if (x == finalx)
                deltaX = 0;

            if (y == finaly)
                deltaY = 0;

            if (x < finalx)
                x += deltaX;
            else
                x -= deltaX;

            if (y < finaly)
                y += deltaY;
            else
                y -= deltaY;

            this.setLocation(x, y);
//            this.setBounds(x, y, 77, 88);
        }

        //Effetto rotazione
        if (e.getSource().equals(timerRotazione)) {
            scaleX -= 0.07;

            if (scaleX <= 0 && !revealing) {
                scaleX = 0;
                revealing = true; // Inizia a rivelare la carta sottostante
                timerRotazione.setDelay(20); // Riduci la velocità di rivelazione
            } else if (scaleX <= -1.0 && revealing) {
                scaleX = -1.0;
                timerRotazione.stop();
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Disegna la carta frontale (superiore)
        Graphics2D g2d = (Graphics2D) g.create();

        int centerX = getWidth() / 2; // Calcola il centro orizzontale
        int centerY = getHeight() / 2; // Calcola il centro verticale

        g2d.translate(centerX, centerY); // Trasla all'origine
        g2d.scale(scaleX, 1.0); // Scala sull'asse X

        if (!revealing)
            g2d.drawImage(frontCardImage, -frontCardImage.getWidth() / 2, -frontCardImage.getHeight() / 2, null);
        else
            g2d.drawImage(backCardImage, frontCardImage.getWidth(null) / 2, -backCardImage.getHeight(null) / 2, -backCardImage.getWidth(null), backCardImage.getHeight(null), null);

        g2d.dispose();
    }

//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
////        centerX = x + frontCardImage.getWidth() / 2;
////        centerY = y + frontCardImage.getHeight() / 2;
//
//        int xPos = (getWidth() - frontCardImage.getWidth()) / 2; // Calcola la posizione x per centrare l'immagine
//        int yPos = (getHeight() - frontCardImage.getHeight()) / 2; // Calcola la posizione y per centrare l'immagine
//
//        // Disegna la carta frontale (superiore)
//        Graphics2D g2d = (Graphics2D) g.create();
////        g2d.translate(centerX, centerY);
//        g2d.scale(scaleX, 1.0); // Scala sull'asse X
//
//        if (!revealing)
//            g2d.drawImage(frontCardImage, -xPos, -yPos, null);
////            g2d.drawImage(frontCardImage, -frontCardImage.getWidth(null) / 2, -frontCardImage.getHeight(null) / 2, null);
//        else
////            g2d.drawImage(backCardImage, frontCardImage.getWidth(null) / 2, -backCardImage.getHeight(null) / 2, -backCardImage.getWidth(null), backCardImage.getHeight(null), null);
//            g2d.drawImage(backCardImage, xPos, -yPos, -xPos, yPos, null);
//
//        g2d.dispose();
//    }

    public void avviaMovimento(int finalx, int finaly) {

        this.finalx = finalx;
        this.finaly = finaly;
        this.deltaX = deltaX = 2;
        this.deltaY = deltaY = 2;

        timerMovimento.start();

    }

    public void avviaRotazione() {
        this.timerRotazione.start();

    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            String frontCardPath = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/iloveimg-resized/CartaCoperta.png";
//            String backCardPath = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/iloveimg-resized/CuoriAsso.png";
//
//            JFrame frame = new JFrame();
//
//            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//            frame.setSize(new Dimension(1440, 900)); // Imposta le dimensioni della finestra
//            frame.setLayout(new BorderLayout());
//
//            MyPanel myPanel = new MyPanel("/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/BackGround_Resized.png");
//            myPanel.setSize(new Dimension(800,800));
//            myPanel.setLayout(null);
//
//            CartaPersonalizzataButton4 button = new CartaPersonalizzataButton4(400, 400, frontCardPath, backCardPath);
//
//            myPanel.add(button);
//
//            frame.add(new JButton("Ciao"), BorderLayout.NORTH);
//            frame.add(myPanel, BorderLayout.CENTER);
//            frame.add(new JButton("Ciao"), BorderLayout.SOUTH);
//            frame.add(new JButton("Ciao"), BorderLayout.LINE_START);
//            frame.add(new JButton("Ciao"), BorderLayout.LINE_END);
//
//            frame.setVisible(true); // Rendi la finestra visibile
//
//            button.avviaMovimento(0, 0);
////            button.avviaRotazione();
//
//        });
//    }
}


//            // Delay prima di avviare il secondo movimento
//            Timer delayTimer = new Timer(9000, new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    panel.avviaMovimento(800, 400, 1, 1);
//                }
//            });
//            delayTimer.setRepeats(false);
//            delayTimer.start();
//        });
//    }
//}
