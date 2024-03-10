package controller;

import model.MatchManager;
import view.MatchView;

/**
 * The main class for the JTrash game controller.
 * It initializes the game components and starts the game loop.
 */
public class JTrash {

    /**
     * The main method to start the JTrash game.
     * @param args the command-line arguments (not used)
     */
    public static void main(String[] args){
        //get an instance of Model Matchmanager
        MatchManager matchManager = MatchManager.getInstance();

        //Create an instance of game view
        MatchView matchView = new MatchView();

        //Add the MatchView ad an observer of MatchManager
        matchManager.addObserver(matchView);

        //Call the method of MatchManager to start the game
        matchManager.avviaGioco();

        //Game loop
        do {
            //Execute a game run
            matchManager.turnoDiGioco();
        }
        while(true); //The game loop continues indefinitely
    }
}
