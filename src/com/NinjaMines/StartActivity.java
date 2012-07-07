package com.NinjaMines;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class StartActivity extends Activity {
    private static int difficulty;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent=getIntent();
        difficulty=intent.getIntExtra("com.NinjaMines.OptionsActivity.Difficulty", 1);
    }
    public void startGame(View view){
        Intent intent=new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void optionsMenu(View view){
        Intent intent=new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    public static int getDifficulty(){
        return difficulty;
    }


}
