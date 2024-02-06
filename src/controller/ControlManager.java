package controller;

import model.MatchManager;

public class ControlManager {
    public static void main(String[] args){

        MatchManager matchManager = new MatchManager(2);
        do {
            matchManager.turnoDiGioco();
        }while(true);
    }
}
