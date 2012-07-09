package com.NinjaMines;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Random;
//didn't use getter and setters to increase performance
//DONE get the buttons to fill the screen //problem: had to set param for both buttons AND rows
//TODO get the images to display on the buttons
//TODO get a separate view with a timer, and menu
//TODO fix mine counting

public class BoardManagement implements View.OnClickListener, View.OnLongClickListener {
    public MineSweeperButton[][] buttons;
    private boolean win = false;
    private int numToggled = 0;
    boolean firstClick = true;
    private int numButtons;
    private int numMines;
    private GameActivity parent;
    private int minesLeftToSet;
    Random random = new Random();
    int x, y;
    int surroundingMines;

    public BoardManagement(int numButtons, int numMines, GameActivity gameActivity) {
        this.numButtons = numButtons;
        this.numMines = numMines;
        this.parent = gameActivity;
        minesLeftToSet = numMines;
    }

    private void layMines() {
        while (minesLeftToSet > 0) {
            x = random.nextInt(numButtons);
            y = random.nextInt(numButtons);
            if (!buttons[x][y].toggled && !buttons[x][y].isMine && !buttons[x][y].flagged) {//makes sure to not lay mine on toggled buttons
                buttons[x][y].isMine = true;
                minesLeftToSet--;
            }
        }
        updateNumbers();
    }

    private void updateNumbers() {
        for (int i = 0; i < numMines; i++) {
            for (int j = 0; j < numMines; j++) {
                if (buttons[i][j].toggled) {
                    surroundingMines = 0;
                    surroundingMines += isMine(x + 1, y);//r
                    surroundingMines += isMine(x - 1, y);//l
                    surroundingMines += isMine(x, y + 1);//u
                    surroundingMines += isMine(x, y - 1);//d
                    surroundingMines += isMine(x + 1, y + 1);//ru
                    surroundingMines += isMine(x - 1, y - 1);//ld
                    surroundingMines += isMine(x + 1, y - 1);//rd
                    surroundingMines += isMine(x - 1, y + 1);//lu
                    buttons[x][y].setText(surroundingMines + "");
                }
            }
        }
    }

    private int isMine(int x, int y) {
        if (x < 0 || y < 0 || x >= numButtons || y >= numButtons) return 0;
        if (buttons[x][y].isMine) return 1;
        return 0;
    }

    private void clearMines() {
        for (int i = 0; i < numButtons; i++) {
            for (int j = 0; j < numButtons; j++) {
                if (!buttons[i][j].flagged && buttons[i][j].flagged) {
                    buttons[i][j].isMine = false;
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
                        parent.finish();
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
        TableLayout table = (TableLayout) parent.findViewById(R.id.gamescreen);
        //table.setShrinkAllColumns(true);
        table.setStretchAllColumns(true);
        buttons = new MineSweeperButton[numButtons][numButtons];
        TableLayout.LayoutParams rowParam = new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT,
                1.0f);
        TableRow.LayoutParams cellLParam = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT,
                1.0f);

        for (int i = 0; i < numButtons; i++) {

            TableRow row = new TableRow(parent);
            for (int j = 0; j < numButtons; j++) {
                button = new MineSweeperButton(parent);
                button.setOnClickListener(this);
                button.setOnLongClickListener(this);
                button.setLayoutParams(cellLParam);
                buttons[i][j] = button;
                row.addView(button);
            }
            table.addView(row,rowParam);
        }
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
            if (button.isMine) {
                button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                gameOver();//if button is mined GG
            } else {
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
