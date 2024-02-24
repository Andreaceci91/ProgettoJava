package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SchermataIniziale {
    JFrame schermataInizialeFrame;

    public SchermataIniziale() throws InterruptedException {
        schermataInizialeFrame = new JFrame();
        schermataInizialeFrame.setSize(new Dimension(1440, 900));
        schermataInizialeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MyPanel schermataInizialePanel = new MyPanel("/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/ProgettoJava/Nuovo progetto.png");
        schermataInizialePanel.setLayout(new GridBagLayout());

        GridBagConstraints gbcJtrash = new GridBagConstraints();
        gbcJtrash.gridx = 0;
        gbcJtrash.gridy = 0;

        GridBagConstraints startGameButton = new GridBagConstraints();
        startGameButton.gridx = 0;
        startGameButton.gridy = 1;

        schermataInizialeFrame.add(schermataInizialePanel);
        schermataInizialeFrame.setVisible(true);

        JLabel label = new JLabel(new ImageIcon("/Users/andrea/Il mio Drive/Università/- Metodologie di programmazione/ProgettoJava/JTrashScritta.png"));

        JButton button = new JButton("Avvia Gioco");

        schermataInizialePanel.add(label, gbcJtrash);
        schermataInizialePanel.add(button, startGameButton);


        while (1 == 1) {

            schermataInizialePanel.add(label, gbcJtrash);
            schermataInizialeFrame.repaint();
            Thread.sleep(800);

            schermataInizialePanel.remove(label);
            schermataInizialePanel.add(new JLabel(), gbcJtrash);
            schermataInizialeFrame.repaint();
            Thread.sleep(800);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SchermataIniziale schermataIniziale = new SchermataIniziale();
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
}

