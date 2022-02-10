/*Written by Sudarshana Jagadeeshi for CS6326.001, assignment 4, starting October 29.2021.
        NetID: sxj18060*/

package com.example.myapplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.*;

import android.os.Environment;
import android.util.Log;

public class IOManager {

    public static ArrayList<Record> data = new ArrayList();

    /*
    * Writes the data AL to the file
    * parameters: none
    * input: IOManager.data
    * output: updated highscores.txt file
    * */
    public static void write() {
        try {
            FileWriter writer = new FileWriter(Instructions.FILE_PATH);
            for (int i = 0; i < IOManager.data.size(); i++) {
                Log.d("Debug", "Writing an object to file");
                writer.append(IOManager.data.get(i).getName() + "\t" + IOManager.data.get(i).getScore() + "\t" + IOManager.data.get(i).getDatetime() +"\n");
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            Log.d("Debug",e+"");
        }
    }

    /*
     * Adds a certain record
     * parameters: rectoadd, the Record object to be added
     * output: updated AL
     * */
    public static void add(Record rectoadd) {
        Log.d("debug","entering add. SIZE is: "+data.size() + "TO ADD: " + rectoadd.getName());
        Collections.sort(data);
        if(data.size() > 20){
            Log.d("Debug", "some error made, more than 20 scores");
            while(data.size() > 20) {
                data.remove(data.size() - 1);
            }
        }

        try {

            Collections.sort(data);

            if(data.size() > 0) {
                Record lowestrecord = data.get(data.size()-1);
                Log.d("Debug", "LOWEST SCORE: " + lowestrecord.getScore() + " by " + lowestrecord.getName());


                if(lowestrecord.compareTo(rectoadd) != 1 && data.size() == 20){
                    Log.d("Debug", "No room for this high score.");
                    return;
                }
                else if(data.size() == 20){ //20 before the new record is added
                    Log.d("Debug", "SIZE is 20, removing: ");
                    Log.d("Debug", data.get(data.size()-1).getName());
                    data.remove(data.size()-1);
                }
            }

            //else bump the lowest record
            data.add(rectoadd);
            Collections.sort(data);
            Log.d("Debug", "Wrote object to file.");

        } catch (Exception e) {
            Log.d("Debug", "Some error here..." + e.toString());
            System.out.println(e);
        }
        Log.d("debug","Exiting add...");
    }

    /*
     * Read the file into the AL when the program intially starts. Calls add on each line
     * parameters: none
     * input: highscores.txt
     * output: updated AL
     * */
    public static void startread(){
        File root = new File(Instructions.DIR_PATH);
        File hsfile = new File(Instructions.FILE_PATH);

        Log.d("debug","Entering startread...");

        Scanner myReader = null;
        try {
            myReader = new Scanner(hsfile);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        int linenum = 1;
        while (myReader.hasNextLine()) {

            String line = myReader.nextLine();
            String[] attributes = line.split("\\\t"); //splits on tab into an array

            String name = attributes[0];
            String score = attributes[1];
            String date = attributes[2];

            Record recordtoAdd= new Record(name, Integer.parseInt(score), date);
            add(recordtoAdd);


        }
        myReader.close();
        Log.d("debug","Leaving startread...");
    }
}
