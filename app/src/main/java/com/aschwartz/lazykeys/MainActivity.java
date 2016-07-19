package com.aschwartz.lazykeys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by Ruby on 02/06/2016.
 */
public class MainActivity extends ActionBarActivity {
    RadioGroup radioGroup;
    TextView textCheckedID, textCheckedIndex;

    final String KEY_SAVED_RADIO_BUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
//                    Intent i = new Intent(MainActivity.this, DefaultIntro.class);
//                    startActivity(i);

                    TextView tutorial_text = (TextView) findViewById(R.id.hint_text);
                    tutorial_text.setText("Enable LazyKeys in Settings > Language and Keyboard");

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();

        //debug only
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);

//        textCheckedID = (TextView)findViewById(R.id.checkedid);
//        textCheckedIndex = (TextView)findViewById(R.id.checkedindex);

        LoadPreferences();

    }

    RadioGroup.OnCheckedChangeListener radioGroupOnCheckedChangeListener =
            new RadioGroup.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedId);
                    int checkedIndex = radioGroup.indexOfChild(checkedRadioButton);

//                    textCheckedID.setText("checkedID = " + checkedId);
//                    textCheckedIndex.setText("checkedIndex = " + checkedIndex);
                    SavePreferences(KEY_SAVED_RADIO_BUTTON_INDEX, checkedIndex);
                }};

    private void SavePreferences(String key, int value){
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void previewToggle(View v){
        CheckBox preview = (CheckBox) findViewById(R.id.check_preview);
        if (preview.isChecked()){
            SavePreferences("PREVIEW",1);
        } else SavePreferences("PREVIEW",0);




    }

    private void LoadPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        int savedRadioIndex = sharedPreferences.getInt(KEY_SAVED_RADIO_BUTTON_INDEX, 0);
        RadioButton savedCheckedRadioButton = (RadioButton)radioGroup.getChildAt(savedRadioIndex);
        savedCheckedRadioButton.setChecked(true);

        int setPreview = sharedPreferences.getInt("PREVIEW",1);
        CheckBox preview = (CheckBox)findViewById(R.id.check_preview);

        if (setPreview==1)
        preview.setChecked(true);
        else
            preview.setChecked(false);
    }




}

