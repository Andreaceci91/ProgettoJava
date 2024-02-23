package controller;

import model.MatchManager;
import view.Scacchiera;
import java.util.Scanner;

public class ControlManager {
    public static void main(String[] args) throws InterruptedException {

        MatchManager matchManager = new MatchManager();
        ApplicazionManager.modelInstance = matchManager;

        Scacchiera scacchiera = new Scacchiera();

        ApplicazionManager.modelInstance.addObserver(scacchiera);

        ApplicazionManager.modelInstance.avviaGioco();

        do {
            Thread.sleep(1500);
            ApplicazionManager.modelInstance.turnoDiGioco();
        }while(true);

    }
}
