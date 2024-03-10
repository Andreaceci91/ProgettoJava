package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Custom button class representing a card in a graphical user interface.
 */
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

    /**
     * Constructor for CustomCardButton class.
     *
     * @param x                      The x-coordinate of the button.
     * @param y                      The y-coordinate of the button.
     * @param cartaVisualizzataPath The path to the image representing the displayed card.
     * @param retroCartaVisualizzataPath      The path to the image representing the back of the card.
     */
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

        // Movement effect
        if (e.getSource().equals(timerMovimento)) {

            if (x == finalx && y == finaly) {
                timerMovimento.stop();
                this.setBounds(x, y, 76, 88);
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
        }

        // Rotation effect
        if (e.getSource().equals(timerRotazione)) {
            scaleX -= 0.07;

            if (scaleX <= 0 && !revealing) {
                scaleX = 0;
                revealing = true; // Start revealing the underlying card
                timerRotazione.setDelay(20);
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

        Graphics2D g2d = (Graphics2D) g.create();

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        g2d.translate(centerX, centerY);
        g2d.scale(scaleX, 1.0);

        if (!revealing)
            g2d.drawImage(frontCardImage, -frontCardImage.getWidth() / 2, -frontCardImage.getHeight() / 2, null);
        else
            g2d.drawImage(backCardImage, frontCardImage.getWidth(null) / 2, -backCardImage.getHeight(null) / 2, -backCardImage.getWidth(null), backCardImage.getHeight(null), null);

        g2d.dispose();
    }

    /**
     * Initiates movement animation.
     *
     * @param finalX The final x-coordinate for the button.
     * @param finalY The final y-coordinate for the button.
     */
    public void avviaMovimento(int finalX, int finalY) {

        this.finalx = finalX;
        this.finaly = finalY;
        this.deltaX  = 2;
        this.deltaY  = 2;

        timerMovimento.start();

    }

    /**
     * Initiates rotation animation.
     */
    public void avviaRotazione() {
        this.timerRotazione.start();

    }
}

