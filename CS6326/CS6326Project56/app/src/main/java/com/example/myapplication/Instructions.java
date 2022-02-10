package com.example.myapplication;

import static com.example.myapplication.IOManager.startread;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.IOManager;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Record;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

/*
* Instructions- displays the shape and color to be clicked
* Also calls the IOManager
* */

public class Instructions extends AppCompatActivity {

    TextView instructiontext;
    Button gtgButton;

    static int randomcolor=0;
    static String randomcolorString="";
    static String randomshape= "";
    static boolean firsttime=true;
    public static String DIR_PATH = "";
    public static String FILE_PATH = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //doing preliminary work and checks before passing it to the IOManager
        DIR_PATH= Instructions.this.getFilesDir() + "/Asg5";
        FILE_PATH= DIR_PATH+"/highscores.txt";

        File root = new File(DIR_PATH);
        if (!root.exists()) {
            root.mkdir();
        }
        //File dir= new File(path);
        //dir.mkdirs();

        Collections.sort(IOManager.data);
        File hsfile = new File(FILE_PATH);

        if (hsfile.exists()) {
            Log.d("error", "file already exists");
        }
        else {
            Log.d("error", "file doesn't already exist");
        }


        if(firsttime) {
            if (hsfile.length() == 0){ //i.e. file empty
                //populate some initial data- just to observe the functionality easier
                Log.d("Debug", "file empty");
                IOManager.add(new Record("Bart", 55, "09/26/21 15:34:32"));
                IOManager.add(new Record("Bert", 555, "09/23/21 15:34:32"));
                IOManager.add(new Record("Bort", 5555, "09/22/21 15:34:32"));
                IOManager.add(new Record("Birt", 5, "09/21/21 15:34:32"));
                IOManager.add(new Record("Burt", 556, "09/27/21 15:34:32"));
                IOManager.add(new Record("Tart", 44, "09/21/21 15:34:32"));
                IOManager.add(new Record("Tert", 444, "10/23/21 15:34:32"));
                IOManager.add(new Record("Tort", 4444, "11/22/21 15:34:32"));
                IOManager.add(new Record("Tirt", 4, "07/21/21 15:34:32"));
                IOManager.add(new Record("Turt", 445, "08/27/21 15:34:32"));
                IOManager.add(new Record("Mart", 33, "09/21/21 15:34:32"));
                IOManager.add(new Record("Mert", 333, "10/19/21 15:34:32"));
                IOManager.add(new Record("Mort", 3333, "11/12/21 15:34:32"));
                IOManager.add(new Record("Mirt", 3, "07/09/21 15:34:32"));
                IOManager.add(new Record("Murt", 334, "08/12/21 15:34:32"));
                IOManager.add(new Record("Sart", 22, "09/24/21 15:34:32"));
                IOManager.add(new Record("Sert", 222, "11/19/21 15:34:32"));
                IOManager.add(new Record("Sort", 2222, "01/12/21 15:34:32"));
                IOManager.add(new Record("Sirt", 2, "04/01/21 15:34:32"));
                IOManager.add(new Record("Surt", 223, "08/14/21 15:34:32"));
                IOManager.add(new Record("Nart", 11, "09/23/21 15:34:32"));
                IOManager.add(new Record("Nert", 111, "10/19/20 15:34:32"));
                IOManager.add(new Record("Nort", 1111, "11/12/21 15:34:32"));
                IOManager.add(new Record("Nirt", 1, "07/01/21 15:34:32"));
                IOManager.add(new Record("Nurt", 112, "01/12/21 15:34:32"));
            }
            else{
                //there is already data in file
                startread();
            }

            firsttime = false;
        }

        //2. Write those to file
        IOManager.write();


        //reset in case they play again
        randomcolorString="";
        randomshape= "";
        randomcolor=0;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        instructiontext= (TextView) findViewById(R.id.InstructionText);
        gtgButton = (Button) findViewById(R.id.GtgButton);

        //randomize and set instruction text
        Random rand=new Random();
        int temp1= rand.nextInt(2);
        int temp2= rand.nextInt(7);


        if(temp1 == 1){
            randomshape += "circles";
        }
        else if(temp1 == 0){
            randomshape += "squares";
        }
        else{
            Log.d("debug","Some error in rng 1");
        }

        //randomcolorString is not ever used, but it could be useful
        if(temp2 == 0){
            randomcolor = Color.parseColor("#FF0000");
            randomcolorString += "red";
        }
        else if(temp2 == 1){
            randomcolor = Color.parseColor("#ffa500");
            randomcolorString += "orange";
        }
        else if(temp2 == 2){
            randomcolor = Color.parseColor("#FFFF00");
            randomcolorString += "yellow";
        }
        else if(temp2 == 3){
            randomcolor = Color.parseColor("#00FF00");
            randomcolorString += "green";
        }
        else if(temp2 == 4){ //this is a darker blue
            randomcolor = Color.parseColor("#00008b");
            randomcolorString += "blue";
        }
        else if(temp2 == 5){
            randomcolor = Color.parseColor("#00FFFF");
            randomcolorString += "cyan";
        }
        else if(temp2 == 6){
            randomcolor = Color.parseColor("#FFFFFF");
            randomcolorString += "white";
        }
        else{
            Log.d("debug","Some error in rng 2");
        }

        instructiontext.setText("Click the\n" + randomcolorString + "\n" + randomshape);

        //goes to the game
        gtgButton.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Instructions.this,Game.class));
            }
        });
    }


}