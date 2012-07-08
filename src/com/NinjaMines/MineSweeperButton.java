package com.NinjaMines;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.Button;

public class MineSweeperButton extends Button {
    public boolean isMine;
    public boolean toggled;
    public boolean flagged;

    public MineSweeperButton(Context context) {
        super(context);
        isMine = false;
        toggled = false;
        flagged = false;
    }
}
