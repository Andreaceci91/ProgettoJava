package controller;

import model.MatchManager;
import view.Scacchiera;
import java.util.Scanner;

public class ControlManager {
    public static void main(String[] args) throws InterruptedException {

        MatchManager matchManager = new MatchManager();
        Scacchiera scacchiera = new Scacchiera();

        matchManager.addObserver(scacchiera);

        matchManager.avviaGioco();

        do {
            Thread.sleep(1500);
            matchManager.turnoDiGioco();

        }while(true);

    }
}
