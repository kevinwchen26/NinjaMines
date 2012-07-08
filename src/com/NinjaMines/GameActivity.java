package com.NinjaMines;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.*;


public class GameActivity extends Activity implements View.OnClickListener {
    private int numButtons = 0;
    private int difficulty;
    private int numMines;
    boolean firstClick = true;
    private int numToggled = 0;
    private boolean win = false;

    private MineSweeperButton[][] buttons;

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
        drawBoard();
    }

    private void layMines() {
        int counter = 0;
        while (counter < numMines) {
            int x = (int) (Math.random() * numButtons);
            int y = (int) (Math.random() * numButtons);
            if (!buttons[x][y].isToggled() && !buttons[x][y].isMined()) {//makes sure to not lay mine on toggled buttons
                buttons[x][y].mine();
                buttons[x][y].getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                counter++;
            }
        }
        updateNumbers();
    }

    private void updateNumbers() {
        for (int i = 0; i < numMines; i++) {
            for (int j = 0; j < numMines; j++) {
                int surroundingMines = 0;
                if (buttons[i][j].isToggled()) {
                    if (i - 1 >= 0) {
                        if (buttons[i - 1][j].isMined())
                            surroundingMines++;

                        if (j - 1 >= 0) {
                            if (buttons[i - 1][j - 1].isMined())
                                surroundingMines++;
                            if (buttons[i][j - 1].isMined())
                                surroundingMines++;
                        }
                        if (j + 1 < numMines) {
                            if (buttons[i - 1][j + 1].isMined())
                                surroundingMines++;
                        }
                    }

                    if (i + 1 < numMines) {
                        if (buttons[i + 1][j].isMined())
                            surroundingMines++;
                        if (j - 1 >= 0) {
                            if (buttons[i + 1][j - 1].isMined())
                                surroundingMines++;

                        }
                        if (j + 1 < numMines) {
                            if (buttons[i + 1][j + 1].isMined())
                                surroundingMines++;
                            if (buttons[i][j + 1].isMined())
                                surroundingMines++;
                        }

                    }

                    if (surroundingMines > 0) buttons[i][j].setText(surroundingMines + "");
                }
            }
        }

    }

    private void drawBoard() {
        MineSweeperButton button;
        LinearLayout layout = (LinearLayout) findViewById(R.id.gamescreen);

        TableLayout table = new TableLayout(this);
        table.setShrinkAllColumns(true);
        table.setStretchAllColumns(true);
        buttons = new MineSweeperButton[numButtons][numButtons];
        for (int i = 0; i < numButtons; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < numButtons; j++) {
                button = new MineSweeperButton(this);
                button.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                button.setOnClickListener(this);
                buttons[i][j] = button;
                row.addView(button);
            }
            table.addView(row);
        }
        layout.addView(table);
    }

    @Override
    public void onClick(View view) {
        MineSweeperButton button = (MineSweeperButton) view;
        if (firstClick) {//wont lay mines until AFTER first click, so the user does not auto-lose
            numToggled++;
            button.toggle();
            layMines();
            firstClick = false;
        } else {
            if (button.isMined()) gameOver();//if button is mined GG
            else {
                if (!button.isToggled()) {
                    numToggled++;
                    button.toggle();
                    if (numButtons * numButtons - numToggled == numMines) {
                        win = true;
                        numToggled=0;
                        gameOver();
                    }
                    clearMines();//clears the mined status of all buttons before replanting mines
                    layMines();
                }
            }
        }
    }

    private void clearMines() {
        for (int i = 0; i < numButtons; i++) {
            for (int j = 0; j < numButtons; j++) {
                buttons[i][j].clearMine();
            }
        }
    }

    private void gameOver() {
        String message = "You Lose :D play again??";
        if (win) {
            win=false;
            message = "You Win :( play again?";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetBoard();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void resetBoard() {
        for (int i = 0; i < numButtons; i++) {
            for (int j = 0; j < numButtons; j++) {
                buttons[i][j].resetStatus();
            }
        }
        firstClick = true;
    }


}
