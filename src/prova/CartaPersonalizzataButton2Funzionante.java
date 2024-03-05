package prova;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CartaPersonalizzataButton2Funzionante extends JButton implements ActionListener {
    private Image frontCardImage;
    private Image backCardImage;
    private double scaleX = 1.0;
    private boolean revealing = false;
    private Timer timer;

    String cartaVisualizzataPath;
    String retroCartaVisualizzataPath;

    //Posizionamento del componente rispetto al Panel
    int x = getWidth();
    int y = getHeight();
    //Incremento della funzione avviaMovimento
    int deltaX;
    int deltaY;

    public CartaPersonalizzataButton2Funzionante(String cartaVisualizzataPath, String retroCartaVisualizzataPath) {
        this.cartaVisualizzataPath = cartaVisualizzataPath;
        this.retroCartaVisualizzataPath = retroCartaVisualizzataPath;
        // Carica le immagini delle due carte
        backCardImage = Toolkit.getDefaultToolkit().getImage(retroCartaVisualizzataPath);
        frontCardImage = Toolkit.getDefaultToolkit().getImage(cartaVisualizzataPath);

        timer = new Timer(20, this);
    }

    public void avviaAnimazioneRotazione() {
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Ottieni le dimensioni del pannello
        int width = getWidth();
        int height = getHeight();

        // Calcola il centro del pannello
        int centerX = width / 2;
        int centerY = height / 2;

        // Disegna la carta frontale (superiore)
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.clearRect(0, 0, width, height);
        g2d.translate(centerX, centerY);
        g2d.scale(scaleX, 1.0); // Scala sull'asse X

        if (!revealing) {
            g2d.drawImage(frontCardImage, -frontCardImage.getWidth(null) / 2, -frontCardImage.getHeight(null) / 2, null);
        } else {
//            g2d.drawImage(backCardImage, -backCardImage.getWidth(null) / 2, -backCardImage.getHeight(null) / 2, null);
            g2d.drawImage(backCardImage, frontCardImage.getWidth(null) / 2, -backCardImage.getHeight(null) / 2, -backCardImage.getWidth(null), backCardImage.getHeight(null), null);
        }
        g2d.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Aggiorna l'effetto di girata della carta
        scaleX -= 0.07; // Modifica questo valore per regolare la velocità di girata

        if (scaleX <= 0 && !revealing) {
            scaleX = 0;
            revealing = true; // Inizia a rivelare la carta sottostante
            timer.setDelay(20); // Riduci la velocità di rivelazione
        } else if (scaleX <= -1.0 && revealing) {
            scaleX = -1.0;
            timer.stop(); // Ferma il timer quando la carta è completamente rivelata
        }

        // Ridisegna il pannello
        repaint();
    }

    public void avviaMovimento(int finalx, int finaly){

        System.out.println("Sono entrato dentro avviaMovimento");

        if ((finalx - x) < 0)
            deltaX = -1;
        else
            deltaX = 1;

        if ((finaly - y) < 0)
            deltaY = -1;
        else
            deltaY = 1;

        if(finalx == x)
            deltaX = 0;
        if(finaly == y)
            deltaY = 0;

        Timer timerMovimento = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (x == finalx && y == finaly)
                    ((Timer) e.getSource()).stop();

                // Controlla se l'oggetto ha raggiunto il bordo destro o sinistro del JPanel
                if (x == finalx) {
                    System.out.println("Raggiunto x");
                } else
                    x += deltaX;

                if (y == finaly) {
                    System.out.println("Raggiunto y");
                } else
                    y += deltaY;

                repaint();
            }
        });

        timerMovimento.start();
    }
}
