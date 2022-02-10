/*Written by Sudarshana Jagadeeshi for CS6326.001, assignment 4, starting October 29.2021.
        NetID: sxj18060*/

package com.example.myapplication;
import static com.example.myapplication.IOManager.startread;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.io.File;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;

public class MainActivity extends AppCompatActivity {

    ListView mylistview;
    String data[] = {};

    //public String path= MainActivity.this.getFilesDir()+ "/Asg5";



    /*in onCreate:
    1. populate initial data if necessary
    2. apply the list adapter to the list
    3. button click switches activities*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);

        }catch(Exception e){
            Log.d("Notes", e.toString());
        }


        CustomListViewAdapter aAdapter = new CustomListViewAdapter(this, IOManager.data);

        mylistview = findViewById(R.id.list);
        mylistview.setAdapter(aAdapter);


        final Button button = (Button) findViewById(R.id.my_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Instructions.class));
            }
        });
    }
}
