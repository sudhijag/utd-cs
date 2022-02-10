package com.example.myapplication;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import static com.example.myapplication.IOManager.add;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Pattern;

public class Adder extends AppCompatActivity {

    EditText namefield, scorefield, datefield;
    Button aButton;

    public String path= Environment.getExternalStorageDirectory().getAbsolutePath() + "/Asg5";
    public static boolean namevalid=false;
    public static boolean datevalid=false;
    public static boolean scorevalid=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adder);

        aButton = (Button) findViewById(R.id.Add);
        namefield = (EditText) findViewById(R.id.Name);
        scorefield = (EditText) findViewById(R.id.Score);
        datefield = (EditText) findViewById(R.id.Date);

        aButton.setEnabled(false); //Only change this once all the booleans are valid
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        Date tempdate = new Date();
        datefield.setText(formatter.format(tempdate));
        datevalid=true;
        scorevalid=true; //we know these are both valid because they are pre-populated

        scorefield.setText(CustomView.scoretoadd+"");
        scorefield.setEnabled(false);

        aButton.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
                //grab text from the fields
                String rtaname= namefield.getText().toString();
                int rtascore= Integer.parseInt(scorefield.getText().toString());
                String rtadate= datefield.getText().toString();

                Record rectoadd= new Record(rtaname, rtascore, rtadate);
                //add to the array list and update the file
                try {
                    Collections.sort(IOManager.data);
                    Log.d("Debug","Calling add!");
                    IOManager.add(rectoadd); //the add function, not data.add, which will add unconditionally
                    IOManager.write();
                }catch(Exception e){
                    System.out.println(e);
                }

                startActivity(new Intent(Adder.this,MainActivity.class));
            }
        });

        //cannot be used, never called
        scorefield.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                scorevalid=false;

                if(s.length() != 0){ //the field is not empty
                    try {
                        if (Integer.parseInt(s.toString()) <= 0) {
                            scorefield.setError("Must be positive");
                            return;
                        }
                    }catch(Exception e){
                        scorefield.setError("Cannot be parsed to integer");
                        return;
                    }
                }
                else{ //only shows when the user erases all of his input
                    scorefield.setError("Cannot be empty");
                    return;
                }

                //see if we can enable the button
                scorevalid= true;
                if(datevalid && scorevalid && namevalid){
                    aButton.setEnabled(true);
                }
            }
        });

        namefield.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.d("Debug", "Hello from namefield tcl");

                namevalid= false;

                if(s.length() == 0){
                    namefield.setError("Cannot be empty");
                    return;
                }

                //see if we can enable the button
                namevalid= true;
                if(datevalid && scorevalid && namevalid){
                    aButton.setEnabled(true);
                }
            }

        });

        datefield.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                datevalid=false; //assume it is not correct

                if(s.length() == 0){
                    datefield.setError("Cannot be empty");
                    return;
                }

                if (! Pattern.matches("[0-9][0-9]/[0-9][0-9]/[0-9][0-9] [0-9][0-9]:[0-9][0-9]:[0-9][0-9]",datefield.getText())){
                    datefield.setError("Does not match format xx/xx/xx xx:xx:xx");
                    return;
                }
                try {
                    Date inputdate = formatter.parse(datefield.getText().toString());
                    Date today = new Date();

                    if( today.before(inputdate)){
                        datefield.setError("Date is in the future");
                        return;
                    }
                }catch(Exception e){
                    datefield.setError("Could not parse to date");
                    return;
                }

                //see if we can enable the button
                datevalid=true;

                Log.d("Validation", ""+datevalid + "" + scorevalid + "" + namevalid);
                if(datevalid && scorevalid && namevalid){
                    aButton.setEnabled(true);
                }
            }
        });

    }
}