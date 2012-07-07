package com.NinjaMines;

import android.app.Activity;

import android.os.Bundle;
import android.util.DisplayMetrics;

import android.widget.*;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;

public class GameActivity extends Activity {
    private int numButtons = 0;
    private ArrayList<Button> buttons = new ArrayList<Button>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        drawBoard(StartActivity.getDifficulty());

    }

    private void drawBoard(int difficulty) {
        Button button;
        LinearLayout layout = (LinearLayout) findViewById(R.id.gamescreen);
        switch (difficulty) {
            case 0:
                numButtons = 3;
                break;
            case 1:
                numButtons = 5;
                break;
            case 2:
                numButtons = 8;
                break;
        }
        TableLayout table=new TableLayout(this);
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        table.setShrinkAllColumns(true);
        table.setStretchAllColumns(true);
        for (int i = 0; i < numButtons; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < numButtons; j++) {
                button=new Button(this);
                buttons.add(button);
                row.addView(button);
            }
            table.addView(row);
        }
        layout.addView(table);
    }


}
