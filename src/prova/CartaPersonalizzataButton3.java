package prova;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public class CartaPersonalizzataButton3 extends JPanel implements ActionListener{
    private String cartaVisualizzataPath;
    private String retroCartaVisualizzataPath;
    private int x;
    private int y;
    private int deltaX;
    private int deltaY;
    private Timer timerMovimento;
    private Timer timerRotazione;
    private int finalx;
    private int finaly;
    private BufferedImage frontCardImage;
    private BufferedImage backCardImage;
    private boolean revealing = false;
    private double scaleX = 1.0;

    public CartaPersonalizzataButton3(int x, int y, String cartaVisualizzataPath, String retroCartaVisualizzataPath ) {
        this.x = x;
        this.y = y;
        this.cartaVisualizzataPath = cartaVisualizzataPath;
        this.retroCartaVisualizzataPath = retroCartaVisualizzataPath;
        timerMovimento = new Timer(10,this);
        timerRotazione = new Timer(10, this);

        // Caricamento dell'immagine dal percorso specificato
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
        if(e.getSource().equals(timerMovimento)) {
            System.out.println("Sono qui");
            System.out.println("x: " + x);
            System.out.println("y: " + x);

            if (x == finalx && y == finaly) {
                timerMovimento.stop();
                System.out.println("Terminato movimento");
            }

            if (x == finalx)
                deltaX = 0;

            if (y == finaly)
                deltaY = 0;

            x += deltaX;
            y += deltaY;

            repaint();
        }
        //Effetto rotazione
        if(e.getSource().equals(timerRotazione)){
            // Aggiorna l'effetto di girata della carta
            scaleX -= 0.01; // Modifica questo valore per regolare la velocità di girata

            if (scaleX <= 0 && !revealing) {
                scaleX = 0;
                revealing = true; // Inizia a rivelare la carta sottostante
                timerRotazione.setDelay(20); // Riduci la velocità di rivelazione
            } else if (scaleX <= -1.0 && revealing) {
                scaleX = -1.0;
                timerRotazione.stop(); // Ferma il timer quando la carta è completamente rivelata
            }

            // Ridisegna il pannello
            repaint();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

            int centerX = x + frontCardImage.getWidth()/2;
            int centerY = y + frontCardImage.getHeight()/2;

            // Disegna la carta frontale (superiore)
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.translate(centerX, centerY);
            g2d.scale(scaleX, 1.0); // Scala sull'asse X

            if (!revealing)
                g2d.drawImage(frontCardImage, -frontCardImage.getWidth(null) / 2, -frontCardImage.getHeight(null) / 2, null);
            else
                g2d.drawImage(backCardImage, frontCardImage.getWidth(null) / 2, -backCardImage.getHeight(null) / 2, -backCardImage.getWidth(null), backCardImage.getHeight(null), null);

            g2d.dispose();
    }

    public void avviaMovimento(int finalx, int finaly, int deltaX, int deltaY) {

        this.finalx = finalx;
        this.finaly = finaly;
        this.deltaX = deltaX;
        this.deltaY = deltaY;

        timerMovimento.start();

    }

    public void avviaRotazione(){
        this.timerRotazione.start();

    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            String frontCardPath = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/iloveimg-resized/CartaCoperta.png";
//            String backCardPath = "/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/iloveimg-resized/CuoriAsso.png";
//            JFrame frame = new JFrame();
//            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//            frame.setSize(new Dimension(800, 800)); // Aggiungi questa riga per impostare le dimensioni della finestra
//
//            CartaPersonalizzataButton3 panel = new CartaPersonalizzataButton3(0, 0, frontCardPath, backCardPath); // Crea un'istanza del pannello
//
//            frame.add(panel); // Aggiungi il pannello alla finestra
//            frame.setVisible(true); // Rendi la finestra visibile
//
//            panel.avviaMovimento(400, 0, 1, 1); // Aggiungi questa riga per avviare il movimento
//            panel.avviaRotazione();
////            // Delay prima di avviare il secondo movimento
////            Timer delayTimer = new Timer(9000, new ActionListener() {
////                @Override
////                public void actionPerformed(ActionEvent e) {
////                    panel.avviaMovimento(800, 400, 1, 1);
////                }
////            });
////            delayTimer.setRepeats(false);
////            delayTimer.start();
//        });
//    }
}
