package com.NinjaMines;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class OptionsActivity extends Activity{
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
    }
    public void saveSettings(View view){
        RadioGroup group=(RadioGroup)findViewById(R.id.difficultysettings);
        RadioButton radio;
        Intent intent=new Intent(this, StartActivity.class);
        for(int i=0;i<group.getChildCount();i++){
            radio=(RadioButton)group.getChildAt(i);
            if(radio.isChecked())intent.putExtra("com.NinjaMines.OptionsActivity.Difficulty",i);
        }
        startActivity(intent);
    }
}
