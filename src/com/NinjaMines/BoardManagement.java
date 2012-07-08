package com.NinjaMines;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class BoardManagement implements View.OnClickListener,GestureDetector.OnGestureListener  {
    private MineSweeperButton[][] buttons;
    private boolean win = false;
    private int numToggled = 0;
    boolean firstClick = true;
    private int numButtons;
    private int numMines;
    private GameActivity parent;

    public BoardManagement(int numButtons, int numMines, GameActivity gameActivity) {
        this.numButtons=numButtons;
        this.numMines=numMines;
        this.parent=gameActivity;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
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
    @Override
    public boolean onDown(MotionEvent motionEvent) {return false;}

    @Override
    public void onShowPress(MotionEvent motionEvent) {}

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {return false;}

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {return false;}

    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }
    public boolean onTouch(MotionEvent event){
        return false;
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {return false;}

    private void resetBoard() {
        for (int i = 0; i < numButtons; i++) {
            for (int j = 0; j < numButtons; j++) {
                buttons[i][j].resetStatus();
            }
        }
        firstClick = true;
    }
    public void drawBoard() {
        MineSweeperButton button;
        LinearLayout layout = (LinearLayout) parent.findViewById(R.id.gamescreen);

        TableLayout table = new TableLayout(parent);
        table.setShrinkAllColumns(true);
        table.setStretchAllColumns(true);
        buttons = new MineSweeperButton[numButtons][numButtons];
        for (int i = 0; i < numButtons; i++) {
            TableRow row = new TableRow(parent);
            for (int j = 0; j < numButtons; j++) {
                button = new MineSweeperButton(parent);
                button.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                button.setOnClickListener(this);
                buttons[i][j] = button;
                row.addView(button);
            }
            table.addView(row);
        }
        layout.addView(table);
    }
}
