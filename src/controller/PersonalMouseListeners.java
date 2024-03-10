package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import model.MatchManager;
import view.CartaPersonalizzataButton;

/**
 * A set of custom MouseListeners for handling mouse events in the JTrash game.
 */
public class PersonalMouseListeners implements MouseListener {

    // Boolean flag to control the clickability
    private static Boolean clickableOnDeck = true;
    private static Boolean clickableOnBoardCard = false;

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    /**
     * Setter method to make board cards unclickable.
     */
    public void setClickableOnBoardCardFalse()
    {
        clickableOnBoardCard = false;
    }

    /**
     * Setter method to make board cards clickable.
     */
    public void setClickableOnBoardCardTrue()
    {
        clickableOnBoardCard = true;
    }

    /**
     * Setter method to make deck cards unclickable.
     */
    public void setClickableOnDeckFalse(){
        clickableOnDeck = false;
    }

    /**
     * Setter method to make deck cards clickable.
     */
    public void setClickableOnDeckTrue()
    {
        clickableOnDeck = true;
    }

    /**
     * MouseListener for deck cards.
     */
    public static class PescaMazzoListeners extends PersonalMouseListeners {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(clickableOnDeck) {
                MatchManager.getInstance().movimentoUmanoPescaMazzo();
                clickableOnDeck = false;
            }
        }
    }

    /**
     * MouseListener for discarded cards.
     */
    public static class PescaTerraListeners extends PersonalMouseListeners {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(clickableOnDeck) {
                MatchManager.getInstance().movimentoUmanoPescaTerra();
                clickableOnDeck = false;
            }
        }
    }

    /**
     * MouseListener for player cards on the table
     */
    public static class PescaBoardIndex extends PersonalMouseListeners{
        private int cardIndex;
        private CartaPersonalizzataButton cartaPersonalizzataButton;

        public PescaBoardIndex(int cardIndex, CartaPersonalizzataButton cartaPersonalizzataButton){
            this.cardIndex = cardIndex;
            this.cartaPersonalizzataButton = cartaPersonalizzataButton;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (clickableOnBoardCard) {
                cartaPersonalizzataButton.avviaRotazione();
                MatchManager.getInstance().movimentoUmanoPescaBoardIndex(cardIndex);
                clickableOnBoardCard = false;
            }
        }
    }

    /**
     * MouseListener to start the game.
     */
    public static class AvviaGioco extends PersonalMouseListeners{
        @Override
        public void mouseClicked(MouseEvent e) {
            MatchManager.getInstance().comandoAvviaGioco();
        }
    }
}
