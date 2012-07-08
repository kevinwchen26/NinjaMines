package com.NinjaMines;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;


public class GameActivity extends Activity{
    private int numButtons = 0;
    private int difficulty;
    private int numMines;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        difficulty = StartActivity.getDifficulty();
        switch (difficulty) {
            case 0:
                numButtons = 3;
                numMines = 3;
                break;
            case 1:
                numButtons = 5;
                numMines = 5;
                break;
            case 2:
                numButtons = 8;
                numMines = 8;
                break;
        }
        BoardManagement manager=new BoardManagement(numButtons,numMines,this);
        manager.drawBoard();
    }













}
