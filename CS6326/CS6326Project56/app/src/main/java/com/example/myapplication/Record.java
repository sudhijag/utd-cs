/*Written by Sudarshana Jagadeeshi for CS6326.001, assignment 4, starting October 29.2021.
        NetID: sxj18060*/

package com.example.myapplication;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Record implements Comparable<Record>{
        public String name;
        public int score;
        public String datetime; //stored as a string, but can be parsed to Date using formatter.parse

        public Record(String name, int score, String datetime) {
            this.name = name;
            this.score = score;
            this.datetime = datetime;
        }

        public String getName() {	return name; }
        public int getScore(){ return score; }
        public String getDatetime(){ return datetime; }

        public void setName(String s){ name= s; }
        public void setScore(int i){ score= i; }
        public void setDatetime(String s){ datetime=s; }

        //Custom Comparator- First compare on score, then on date(most recent takes precedence)
        @Override
        public int compareTo(Record r){
            int myscore= this.getScore();
            int myscore2= r.getScore();
            Date mydate, rdate;
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
                mydate = formatter.parse(this.getDatetime()); //covert to date
                rdate = formatter.parse(r.getDatetime());

                //score matches but date is different
                if (myscore2 == myscore && mydate.before(rdate)){
                    return -1;
                }
                else if(myscore2 == myscore && ! mydate.before(rdate)){
                    return 1;
                }
            }catch(Exception e){
                Log.d("Debug", "Some error here");
            }

            if (myscore > myscore2){
                return 1;
            }
            else if (myscore2 > myscore){
                return -1;
            }

            return 0; //in case all checks don't return, i.e. date also same
        }
}
