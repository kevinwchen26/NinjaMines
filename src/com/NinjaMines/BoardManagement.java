package com.NinjaMines;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
//didn't use getter and setters to increase performance

public class BoardManagement implements View.OnClickListener, View.OnLongClickListener {
    private MineSweeperButton[][] buttons;
    private boolean win = false;
    private int numToggled = 0;
    boolean firstClick = true;
    private int numButtons;
    private int numMines;
    private GameActivity parent;
    private int minesLeftToSet;

    public BoardManagement(int numButtons, int numMines, GameActivity gameActivity) {
        this.numButtons = numButtons;
        this.numMines = numMines;
        this.parent = gameActivity;
        minesLeftToSet = numMines;
    }

    private void layMines() {
        while (minesLeftToSet > 0) {
            int x = (int) (Math.random() * numButtons);
            int y = (int) (Math.random() * numButtons);
            if (!buttons[x][y].toggled && !buttons[x][y].isMine && !buttons[x][y].flagged) {//makes sure to not lay mine on toggled buttons
                buttons[x][y].isMine=true;
                minesLeftToSet--;
            }
        }
        updateNumbers();
    }

    private void updateNumbers() {
        for (int i = 0; i < numMines; i++) {
            for (int j = 0; j < numMines; j++) {
                int surroundingMines = 0;
                if (buttons[i][j].toggled) {
                    if (i - 1 >= 0) {
                        if (buttons[i - 1][j].isMine)
                            surroundingMines++;
                        if (j - 1 >= 0) {
                            if (buttons[i - 1][j - 1].isMine)
                                surroundingMines++;
                            if (buttons[i][j - 1].isMine)
                                surroundingMines++;
                        }
                        if (j + 1 < numMines) {
                            if (buttons[i - 1][j + 1].isMine)
                                surroundingMines++;
                        }
                    }

                    if (i + 1 < numMines) {
                        if (buttons[i + 1][j].isMine)
                            surroundingMines++;
                        if (j - 1 >= 0) {
                            if (buttons[i + 1][j - 1].isMine)
                                surroundingMines++;

                        }
                        if (j + 1 < numMines) {
                            if (buttons[i + 1][j + 1].isMine)
                                surroundingMines++;
                            if (buttons[i][j + 1].isMine)
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
                if (!buttons[i][j].flagged && buttons[i][j].flagged) {
                    buttons[i][j].isMine=false;
                    buttons[i][j].setText("");
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
                buttons[i][j].isMine = false;
                buttons[i][j].toggled = false;
                buttons[i][j].flagged = false;
                buttons[i][j].getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                buttons[i][j].setText("");
            }
        }
        firstClick = true;
        minesLeftToSet = numMines;
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
            if (!button.toggled) {
                button.flagged = !button.flagged;
                if (button.flagged) button.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                else if (!button.flagged) button.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            }
            clearMines();
            layMines();
        }
        return true;

    }

    public void onClick(View view) {
        MineSweeperButton button = (MineSweeperButton) view;
        if (firstClick) {//wont lay mines until AFTER first click, so the user does not auto-lose
            numToggled++;
            if (!button.toggled) {
                button.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
                button.toggled = true;
            }
            layMines();
            firstClick = false;
        } else {
            if (button.isMine) gameOver();//if button is mined GG
            else {
                if (!button.toggled) {
                    numToggled++;
                    if (!button.toggled) {
                        button.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
                        button.toggled = true;
                    }
                    clearMines();//clears the mined status of all buttons before replanting mines
                    layMines();
                }
            }
        }
    }

}
