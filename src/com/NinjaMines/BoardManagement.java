package com.NinjaMines;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class BoardManagement implements View.OnClickListener, View.OnLongClickListener {
    private MineSweeperButton[][] buttons;
    private boolean win = false;
    private int numToggled = 0;
    boolean firstClick = true;
    private int numButtons;
    private int numMines;
    private GameActivity parent;
    private int minesLeftToSet;
    private int flaggedMines;
    boolean flaggedWin;

    public BoardManagement(int numButtons, int numMines, GameActivity gameActivity) {
        this.numButtons = numButtons;
        this.numMines = numMines;
        this.parent = gameActivity;
        minesLeftToSet = numMines;
        flaggedMines = 0;
    }

    private void layMines() {
        while (minesLeftToSet > 0) {
            int x = (int) (Math.random() * numButtons);
            int y = (int) (Math.random() * numButtons);
            if (!buttons[x][y].isToggled() && !buttons[x][y].isMined() && !buttons[x][y].isFlagged()) {//makes sure to not lay mine on toggled buttons
                buttons[x][y].mine();
                minesLeftToSet--;
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

    private void clearMines() {
        for (int i = 0; i < numButtons; i++) {
            for (int j = 0; j < numButtons; j++) {
                if (!buttons[i][j].isFlagged() && buttons[i][j].isMined()) {
                    buttons[i][j].clearMine();
                    buttons[i][j].getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    minesLeftToSet++;
                }
            }
        }
    }

    private void gameOver() {
        String message = "You Lose :D play again??";
        if (win) {
            win = false;
            message = "You Win :( play again?";
        }
        if (flaggedWin) message = "Wow you did flag everything right, wanna try again?";
        flaggedWin=false;
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

    private void resetBoard() {
        for (int i = 0; i < numButtons; i++) {
            for (int j = 0; j < numButtons; j++) {
                buttons[i][j].resetStatus();
            }
        }
        firstClick = true;
        flaggedMines = 0;
        minesLeftToSet=numMines;
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
                button.setOnLongClickListener(this);
                buttons[i][j] = button;
                row.addView(button);
            }
            table.addView(row);
        }
        layout.addView(table);
    }

    @Override
    public boolean onLongClick(View view) {
        if (!firstClick) {
            MineSweeperButton button = (MineSweeperButton) view;
            if (button.isFlagged()) flaggedMines--;
            if (!button.isFlagged()) flaggedMines++;
            button.flagMine();
            if (flaggedMines == numMines) {
                flaggedWin = true;
                checkFlags();
            }
            clearMines();
            layMines();
        }
        return true;

    }

    private void checkFlags() {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setMessage("So you think you've flagged all the mines ehhh? Do you want to end the game now and check to see if you've flagged the right squares?")
                .setCancelable(false)
                .setPositiveButton("Yes, this game is too easy I'm 1000% sure I flagged all the mines", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int numCorrect = 0;
                        for (int i = 0; i < numButtons; i++) {
                            for (int j = 0; j < numButtons; j++) {
                                if (buttons[i][j].isFlagged() && buttons[i][j].isMined()) numCorrect++;
                            }
                        }
                        if (numCorrect != numMines) flaggedWin = false;
                        gameOver();
                    }
                })
                .setNegativeButton("No, I was just playing around lemme keep on playing", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

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
                        numToggled = 0;
                        gameOver();
                    }
                    clearMines();//clears the mined status of all buttons before replanting mines
                    layMines();
                }
            }
        }
    }

}
