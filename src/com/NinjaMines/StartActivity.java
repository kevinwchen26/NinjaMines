package com.NinjaMines;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Building...");
        AlertDialog alert = builder.create();
        alert.show();
        startActivity(intent);

    }

    public void optionsMenu(View view){
        Intent intent=new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    public static int getDifficulty(){
        return difficulty;
    }
    public void quit(View view){
        this.finish();
    }


}
