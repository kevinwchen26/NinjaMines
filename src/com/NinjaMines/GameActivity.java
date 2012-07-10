package com.NinjaMines;
//DONE get progress bar working, or make the object creation process faster //solution: used alert dialog instead of progress bar
//TODO make game harder :(

import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity{
    private int numButtons = 0;
    private int numMines;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        int difficulty = StartActivity.getDifficulty();
        switch (difficulty) {
            case 0:
                numButtons = 3;
                numMines = 5;
                break;
            case 1:
                numButtons = 6;
                numMines = 10;
                break;
            case 2:
                numButtons = 8;
                numMines = 20;
                break;
        }
        BoardManagement manager=new BoardManagement(numButtons,numMines,this);

        manager.drawBoard();
    }
}
