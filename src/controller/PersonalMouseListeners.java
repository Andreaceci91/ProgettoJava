package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.MatchManager;
import view.CartaPersonalizzataButton;

public class PersonalMouseListeners implements MouseListener, Observer {

    private static Boolean clickableOnDeck = true;
    private static Boolean clickableOnBoardCard = false;

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

    public void setClickableOnBoardCardFalse()
    {
        clickableOnBoardCard = false;
    }

    public void setClickableOnBoardCardTrue()
    {
        clickableOnBoardCard = true;
    }

    public void setClickableOnDeckFalse(){
        clickableOnDeck = false;
    }

    public void setClickableOnDeckTrue()
    {
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
//            System.out.println("Click **************************** PescaMazzo");
            if(clickableOnDeck) {
//                System.out.println("Click **************************** PescaMazzo dentro");
//                ApplicazionManager.modelInstance.movimentoUmanoPescaMazzo();
                MatchManager.getInstance().movimentoUmanoPescaMazzo();
                clickableOnDeck = false;
            }
        }
    }

    public static class PescaTerraListeners extends PersonalMouseListeners {

        @Override
        public void mouseClicked(MouseEvent e) {
//            System.out.println("Click ********************************** PescaTerra");
            if(clickableOnDeck) {
//                System.out.println("Click ********************************** PescaTerra dentro");
//                ApplicazionManager.modelInstance.movimentoUmanoPescaTerra();
                MatchManager.getInstance().movimentoUmanoPescaTerra();
                clickableOnDeck = false;
            }
        }
    }

    public static class PescaBoardIndex extends PersonalMouseListeners{
        int cardIndex;
        CartaPersonalizzataButton cartaPersonalizzataButton;

        public PescaBoardIndex(int cardIndex, CartaPersonalizzataButton cartaPersonalizzataButton){
            this.cardIndex = cardIndex;
            this.cartaPersonalizzataButton = cartaPersonalizzataButton;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
//            System.out.println("Click ********************************** PescaBoardIndex");
            if (clickableOnBoardCard) {
//                System.out.println("Click ********************************** PescaBoardIndex dentro");
                cartaPersonalizzataButton.avviaRotazione();
//                ApplicazionManager.modelInstance.movimentoUmanoPescaBoardIndex(cardIndex);
                MatchManager.getInstance().movimentoUmanoPescaBoardIndex(cardIndex);
                clickableOnBoardCard = false;
            }
        }
    }

    public static class AvviaGioco extends PersonalMouseListeners{

        @Override
        public void mouseClicked(MouseEvent e) {
//            System.out.println("Click ********************************** Gioco Avviato");
//            ApplicazionManager.modelInstance.comandoAvviaGioco();
            MatchManager.getInstance().comandoAvviaGioco();
        }
    }

}
