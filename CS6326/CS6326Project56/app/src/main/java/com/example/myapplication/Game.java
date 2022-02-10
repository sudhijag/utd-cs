package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.IOManager;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Record;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Pattern;
import android.graphics.Color;

public class Game extends AppCompatActivity{

    static int BOUNDX;
    static int BOUNDY;
    static int UPPERBOUNDY; //150 for now
    CustomView gamearea;
    TextView gametimer, correctclicked;
    static Context cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Display display = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        BOUNDX = 1000;
        BOUNDY = 1300;

        gamearea = findViewById(R.id.gamearea);
        cont=gamearea.getContext();
        gametimer = findViewById(R.id.gametimer);
        correctclicked = findViewById(R.id.correctclicked);

        gamearea.initViews(gametimer, correctclicked);


    }

}