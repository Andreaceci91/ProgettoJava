package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class PersonalMouseListeners implements MouseListener, Observer {

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

    public void setClickableOnDeckFalse(){
        clickableOnDeck = false;
    }

    public void setClickableOnDeckTrue(){
        clickableOnDeck = true;
    }

    @Override
    public void update(Observable o, Object arg) {
        List<?> list;

        if (arg instanceof List<?>)
            list = (List<?>) arg;
        else
            throw new RuntimeException("Non è stato passata una lista");


    }

    public static class PescaMazzoListeners extends PersonalMouseListeners {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(clickableOnDeck) {
                System.out.println("Click **************************** PescaMazzo");
                ApplicazionManager.modelInstance.movimentoUmanoPescaMazzo();
//                clickableOnDeck = false;
            }
        }
    }

    public static class PescaTerraListeners extends PersonalMouseListeners {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(clickableOnDeck) {
                System.out.println("Click ********************************** PescaTerra");
                ApplicazionManager.modelInstance.movimentoUmanoPescaTerra();
//                clickableOnDeck = false;
            }
        }
    }

    public static class PescaBoardIndex extends PersonalMouseListeners{
        int cardIndex;

        public PescaBoardIndex(int cardIndex){
            this.cardIndex = cardIndex;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("Click ********************************** PescaBoardIndex");
            ApplicazionManager.modelInstance.movimentoUmanoPescaBoardIndex(cardIndex);
        }
    }
}
