package controller;

import model.MatchManager;
import view.MatchView;

public class JTrash {
    public static void main(String[] args){

        MatchManager matchManager = MatchManager.getInstance();

        MatchView matchView = new MatchView();

        matchManager.addObserver(matchView);

        matchManager.avviaGioco();

        do {
            matchManager.turnoDiGioco();
        }
        while(true);
    }
}
