package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PersonalMouseListeners implements MouseListener{

    private static Boolean clickableOnDeck = true;
    private static Boolean clickableOnBoardCard = true;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void setClickableFalse(){
        clickableOnDeck = false;
    }

    public void setClickableTrue(){
        clickableOnDeck = true;
    }

    public static class PescaMazzoListeners extends PersonalMouseListeners {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(clickableOnDeck) {
                System.out.println("Click **************************** PescaMazzo");
                ApplicazionManager.modelInstance.movimentoUmanoPescaMazzo();
                clickableOnDeck = false;
            }
        }
    }

    public static class PescaTerraListeners extends PersonalMouseListeners {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(clickableOnDeck) {
                System.out.println("Click ********************************** PescaTerra");
                ApplicazionManager.modelInstance.movimentoUmanoPescaTerra();
                clickableOnDeck = false;
            }
        }
    }
}
