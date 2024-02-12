package controller;

import model.MatchManager;
import model.Player;
import view.Scacchiera;

public class ControlManager {
    public static void main(String[] args){

        System.out.println("ciao");

        MatchManager matchManager = new MatchManager(4);
        Scacchiera scacchiera = new Scacchiera(matchManager.getPlayerList());

        for(Player p : matchManager.getPlayerList())
            p.showCard();

//        do {
//            matchManager.turnoDiGioco();
//        }while(true);
    }
}
