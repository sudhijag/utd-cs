package com.example.myapplication;

import static com.example.myapplication.IOManager.startread;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

class Circle{
    int cX;
    int cY;
    int r;

    long lifetime; //this actually represents when the object will die
    int color;

    Circle(int one, int two, int three, long lifetime, int color){
        this.cX= one;
        this.cY= two;
        this.r= three;

        this.lifetime= lifetime;
        this.color= color;
    }

    public int getcX(){return cX;}
    public int getcY(){return cY;}
    public int getr(){return r;}
    public int getColor(){return color;}
    public long getLifetime(){return lifetime;}

    public void setcX(int input){cX=input;}
    public void setcY(int input){cY=input;}

    public boolean contains(int X, int Y){
        float distance= ((cX-X) * (cX-X)) + ((cY-Y) * (cY-Y));
        distance= (float) Math.sqrt(distance);

        if(distance <  r){
            return true;
        }
        else{
            return false;
        }
    }
}

class Square{
    int tlX;
    int tlY;
    int brX;
    int brY;

    long lifetime;
    int color;


    Square(int tlX, int tlY, int brX, int brY, long lifetime, int color){
        this.tlX= tlX;
        this.tlY= tlY;
        this.brX= brX;
        this.brY= brY;

        this.lifetime= lifetime;
        this.color= color;
    }

    public int gettlX(){return tlX;}
    public int gettlY(){return tlY;}
    public int getbrX(){return brX;}
    public int getbrY(){return brY;}
    public long getLifetime(){return lifetime;}
    public int getColor(){return color;}

    public void settlX(int input){tlX=input;}
    public void settlY(int input){tlY=input;}
    public void setbrX(int input){brX=input;}
    public void setbrY(int input){brY=input;}

    public boolean contains(int X, int Y){
        //Log.d("Debug", "In contains "+ tlX+  "," + tlY+ "), (" + brX+ "," + brY + ")");
       // Log.d("Debug", "In contains finding "+ X+  ","+ Y);
        if(X >= tlX && X <= brX && Y >= tlY && Y <= brY){
            return true;
        }
        return false;
    }
}

public class CustomView extends View{

    Paint paint = new Paint();

    static ArrayList<Square> squareList;
    static ArrayList<Circle> circleList;

    static long starttime;
    static int numcorrect;
    static int nummissed;
    static int MAXSHAPES=6;
    static int NUMTOCLICK=3;
    long lastshift;
    long shiftspan;

    TextView scoretv, timertv;
    Context context;

    static int scoretoadd;
    static boolean gameOver;

    public CustomView(Context context) {
        super(context);
        squareList = new ArrayList<Square>();
        circleList = new ArrayList<Circle>();
        lastshift= System.currentTimeMillis() / 1000;
        shiftspan=3; //i.e. 3 seconds
        numcorrect=0;
        nummissed=0;
    }


    public void init(Context context){
        squareList= new ArrayList<>();
        circleList= new ArrayList<>();
        this.context=context;
        scoretoadd=0;
        gameOver=false;

        return;
    }

    public CustomView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    /*
     * Removes shapes from the AL whose lifetimes have expired
     * parameters: none
     * input: circleList, squareList
     * output: updated circleList, squareList
     * */
    public void killOld(){
        for(int i=0 ; i< squareList.size(); i++){
            Square s= squareList.get(i);
            if(s.getLifetime() < System.currentTimeMillis() / 1000L){
                Log.d("Debug","Killing old square");
                if(s.getColor() == Instructions.randomcolor && Instructions.randomshape.equals("squares")){
                    nummissed += 1;
                }
                squareList.remove(i);
            }
        }

        for(int i=0 ; i< circleList.size(); i++){
            Circle c= circleList.get(i);
            if(c.getLifetime() < System.currentTimeMillis() / 1000L){
                Log.d("Debug","Killing old circle");
                if(c.getColor() == Instructions.randomcolor && Instructions.randomshape.equals("circles")){
                    nummissed += 1;
                }
                circleList.remove(i);
            }
        }
    }

    /*
     * Shifts coordinates of shapes in a random direction
     * parameters: none
     * input: circleList, squareList
     * output: updated circleList, squareList
     * */
    public void shift(){
        Random rand;
        Log.d("debug","In shift..." + squareList.size()+ " " +circleList.size());

        for(int i=0 ; i< squareList.size(); i++){
            Log.d("debug","Shifting a square...");
            int X1, Y1, X2, Y2;
            Square currsquare= squareList.get(i);

            
            ArrayList<Integer> coordarray= new ArrayList<>();
             
            while(true) {

                int tlX= currsquare.gettlX();
                int tlY= currsquare.gettlY();
                int brX= currsquare.getbrX();
                int brY= currsquare.getbrY();


               rand= new Random();
               int direction = rand.nextInt(5);

                if(direction  == 0){ //shift up
                    tlY -= 20;
                    brY -= 20;
                }
                else if(direction == 1){ //shift down
                    tlY += 20;
                    brY += 20;
                }
                else if(direction  == 2){ //shift left
                    tlX -= 20;
                    brX -= 20;
                }
                else if(direction == 3){ //shift right
                    tlX += 20;
                    brX += 20;
                }
                else if (direction == 4){ //do nothing (this case b/c shapes can get trapped)
                }

                Log.d("Debug", "shift shape coordinates");


                if(isIB_square(tlX,tlY,brX,brY) && co_squarexsquare(tlX,tlY,brX,brY, i)   ){
                    currsquare.settlX(tlX);
                    currsquare.settlY(tlY);
                    currsquare.setbrX(brX);
                    currsquare.setbrY(brY);

                    //Log.d("Debug","New coordinates after shift: "+X1 + " "+ Y1+ " " + X2 + " "+ Y2);
                    break; //stop trying to find a home for this square
                }

            }
        }


        for(int i=0 ; i< circleList.size(); i++){
            Circle currcircle= circleList.get(i);
            ArrayList<Integer> coordarray= new ArrayList<>();
            while(true) {
                //coordarray= gencoordinatescircle();
                rand= new Random();
                int direction = rand.nextInt(5);

                int one= currcircle.getcX();
                int two= currcircle.getcY();
                int r= currcircle.getr();

                if(direction== 0){ //shift up
                    two -= 20;
                }
                else if(direction == 1){ //shift down
                    two += 20;
                }
                else if(direction == 2){ //shift left
                    one -= 20;
                }
                else if(direction == 3){ //shift right
                    two += 20;
                }
                else{ //if shape gets trapped
                }

                if(isIB_circle(one, two, r) && co_circlexsquare(one, two, r) && co_circlexcircle(one, two, r, i)){
                    Log.d("Debug", "Shifted to:" + one + " " + two + " " + r);
                    currcircle.setcX(one);
                    currcircle.setcY(two);
                    break;
                }

            }


        }
    }

    /*
     * Adds the text and score views, initializes the start time
     * parameters: TextView score, TextView timer
     * input: none
     * output: updated starttime
     * */
    public void initViews(TextView score, TextView timer){
        scoretv = score;
        timertv = timer;
        starttime=System.currentTimeMillis()/1000L;
    }

    /*
     * Calls the shift, killOld and add shapes
     * parameters: Canvas canvas
     * input: none
     * output: updated canvas
     * */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(int i=0 ; i< squareList.size(); i++){
            Square currsquare= squareList.get(i);
            int one= currsquare.gettlX();
            int two= currsquare.gettlY();
            int three= currsquare.getbrX();
            int four= currsquare.getbrY();

            paint.setColor(currsquare.getColor());
            canvas.drawRect(one, two, three, four, paint);
        }

        for(int i=0 ; i< circleList.size(); i++){
            Circle currcircle= circleList.get(i);
            int one= currcircle.getcX();
            int two= currcircle.getcY();
            int radius= currcircle.getr();

            paint.setColor(currcircle.getColor());
            canvas.drawCircle(one, two,radius, paint);
        }

        //set timer text
        long elapsedseconds = System.currentTimeMillis() / 1000L - starttime;
        timertv.setText(elapsedseconds + "");
        scoretv.setText(numcorrect + "");

        paint.setColor(Color.WHITE);
        //paint.setStyle(Paint.Style.STROKE);

        while ((squareList.size() + circleList.size()) <= MAXSHAPES) {
            add_shape(canvas);
        }

        if(numcorrect == NUMTOCLICK){
            gameOver=true;
            scoretoadd = Integer.parseInt((String) ( timertv.getText()) );

           
            boolean highscore = true;
            //compare to lowest record
            Record lowestrecord = IOManager.data.get(IOManager.data.size() - 1);
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
            Date tempdate = new Date();
            Record rectoadd = new Record("", scoretoadd, formatter.format(tempdate));
            if (lowestrecord.compareTo(rectoadd) != 1 && IOManager.data.size() == 20) {
                Log.d("Debug", "This is not a high score. You missed: " + nummissed + " shapes you should have pressed");
                highscore = false;
            }

            AlertDialog.Builder builder= new AlertDialog.Builder(getContext());

            if(! highscore){
                builder.setMessage("You do not have a high score! You missed: " + nummissed +" shapes you should have pressed");
                builder.setPositiveButton(
                    "Play Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(getContext(), Instructions.class);
                            getContext().startActivity(i);
                    }   
                });

                builder.setNegativeButton(
                    "View High Scores",
                    new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getContext(), MainActivity.class);
                        getContext().startActivity(i);
                    }
                });
                
                builder.show();
            }
            else{
                builder.setMessage("You have a high score. You missed: " + nummissed + " shapes you should have pressed");
                builder.setPositiveButton(
                    "Add High Score",
                    new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getContext(), Adder.class);
                        getContext().startActivity(i);
                    }
                });
                builder.show();
            }
        }

        if (lastshift + shiftspan < System.currentTimeMillis() / 1000L) {
            Log.d("Debug", "Time to shift");
            shift();
            lastshift = System.currentTimeMillis() / 1000L;
        }

        killOld();

        //shift
        if (!gameOver){
                Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            }, 10);
        }
    }

    /*
     * Returns an integer corresponding to a random color
     * parameters: none
     * input: RNG
     * output: integer corresponding to a random color
     * */
    public int gen_color(){
        Random rand=new Random();
        int temp2= rand.nextInt(7);

        if(temp2 == 0){
            return Color.parseColor("#FF0000");
        }
        else if(temp2 == 1){
            return Color.parseColor("#ffa500");
        }
        else if(temp2 == 2){
            return Color.parseColor("#FFFF00");
        }
        else if(temp2 == 3){
            return Color.parseColor("#00FF00");
        }
        else if(temp2 == 4){
            return Color.parseColor("#00008b");
        }
        else if(temp2 == 5){
            return Color.parseColor("#00FFFF");
        }
        else if(temp2 == 6){
            return Color.parseColor("#FFFFFF");
        }
        else{
           return -1;
        }
    }

    /*
     * Checks if square is inbounds
     * parameters: topleftX and Y (X1, Y1) and botrightX and Y(X2, Y2)
     * input: none
     * output: boolean where false means shape is not IB
     * */
    public boolean isIB_square(int X1, int Y1, int X2, int Y2){
        //get bounds
        Log.d("Debug", "IBSQUARE: " + X1 + " " + Y1 +" " + X2 + " " +Y2);
        if(X1 < 0 || Y1 < 150 || X2 < 0 || Y2 < 150){ //150 is the width of the timer
            Log.d("Debug", "detected OOB left edge, top edge");
            return false;
        }

        //bottom edge check
        if(Y1 > Game.BOUNDY || Y2 > Game.BOUNDY){
            Log.d("Debug", "detected OOB bottom edge");
            return false;
        }

        //right edge check
        if(X1 > Game.BOUNDX || X2 > Game.BOUNDX){
            Log.d("Debug", "detected OOB Right edge");
            return false;
        }

        return true;
    }

    /*
     * Checks if circle is inbounds
     * parameters: centerX and Y (cX, cY) and radius(r)
     * input: none
     * output: boolean where false means shape is not IB
     * */
    public boolean isIB_circle(int cX,int cY, int r){
        if(cX - r < 0 || cX + r < 0 || cY-r < 0 || cY + r < 0){
            Log.d("debug", "ISIB circle left edge, top edge");
            return false;
        }
        if(cX + r > Game.BOUNDX){
            Log.d("debug", "ISIB circle Right edge");
            return false;
        }

        if(cY + r > Game.BOUNDY){
            Log.d("debug", "ISIB circle Bottom edge");
            return false;
        }

        return true;
    }


    /*
     * Checks if square intersects any other square
     * parameters: topleftX and Y (X1, Y1) and botrightX and Y(X2, Y2)
     * input: none
     * output: true means the shape is safe to be added
     * */
    public boolean co_squarexsquare(int X1, int Y1, int X2, int Y2, int idxtoskip){
        if(squareList.size() == 0){
            return true;
        }

        for(int i = 0; i< squareList.size(); i++ ){
            if(i == idxtoskip){
                continue;
            }

            Square s= squareList.get(i);

            int l2x= s.gettlX();
            int l2y= s.gettlY();
            int r2x= s.getbrX();
            int r2y= s.getbrY();
            //1 is ours, 2 is to totest

            if (r2x < X1 || X2 <l2x || Y2 < l2y || r2y < Y1 ) {
                Log.d("Debug", "No intersection with square: " + i);
            }
            else{
                Log.d("Debug", "Intersection with square: " + i);
                Log.d("Debug", "Square 1: " + l2x + " " + l2y + " " + r2x +  " " + r2y);
                Log.d("Debug", "Square 2: " + X1 + " " + Y1 + " " + X2 +  " " + Y2);

                return false;
            }
        }
        
        return true;
    }

    /*
     * Checks if square intersects any other circle
     * parameters: topleftX and Y (X1, Y1) and botrightX and Y(X2, Y2)
     * input: none
     * output: true means the shape is safe to be added
     * */
    public boolean co_squarexcircle(int X1, int Y1, int X2, int Y2){

        for(int i=0; i< circleList.size(); i++ ){
            Circle totest= circleList.get(i);

            int one= totest.getcX();
            int two= totest.getcY();
            int r= totest.getr();

            //distance 1- X1, Y1... distance 2- X2, Y1
            double distance1= Math.sqrt( ( X1- one ) * ( X1- one ) + ( Y1- two ) * ( Y1- two ));
            double distance2=Math.sqrt( ( X2- one ) * ( X2- one ) + ( Y1- two ) * ( Y1- two ));
            double distance3=Math.sqrt( ( X2- one ) * ( X2- one ) + ( Y2- two ) * ( Y2- two ));
            double distance4=Math.sqrt( ( X1- one ) * ( Y2- one ) + ( X1- two ) * ( Y2- two ));
            if(distance1 < r || distance2 < r || distance3 < r || distance4 < r){
                return false;
            }
            else{

            }

        }

        return true;
    }

    /*
     * Checks if circle intersects any other square
     * parameters: centerX and Y (cX, cY) and radius(r)
     * input: none
     * output: true means the shape is safe to be added
     * */
    public boolean co_circlexsquare(int one, int two, int r){
        for(int i=0; i< squareList.size(); i++ ){
            Square totest= squareList.get(i);

            int X1= totest.gettlX();
            int Y1= totest.gettlY();
            int X2= totest.getbrX();
            int Y2= totest.getbrY();

            //distance 1- X1, Y1... distance 2- X2, Y1
            //Check all four points to see if any of them is within r of the center
            double distance1= Math.sqrt( ( X1- one ) * ( X1- one ) + ( Y1- two ) * ( Y1- two ));
            double distance2=Math.sqrt( ( X2- one ) * ( X2- one ) + ( Y1- two ) * ( Y1- two ));
            double distance3=Math.sqrt( ( X2- one ) * ( X2- one ) + ( Y2- two ) * ( Y2- two ));
            double distance4=Math.sqrt( ( X1- one ) * ( Y2- one ) + ( X1- two ) * ( Y2- two ));

            if(distance1 < r || distance2 < r || distance3 < r || distance4 < r){
                return false;
            }
            else{

            }

        }

        return true;
    }

    /*
     * Checks if circle intersects any other circle
     * parameters: centerX and Y (cX, cY) and radius(r)
     * input: none
     * output: true means the shape is safe to be added
     * */
    public boolean co_circlexcircle(int cX, int cY, int r, int idxtoskip){

        for(int i=0; i< circleList.size(); i++ ){

            if( i == idxtoskip) { //This avoids self comparison, -1 if the shape is not yet in AL
                continue;
            }

            Circle totest= circleList.get(i);
            int one= totest.getcX();
            int two= totest.getcY();
            int r2= totest.getr();

            //Euclidean distance
            double distance= Math.sqrt( ( cX- one ) * ( cX- one ) + ( cY- two ) * ( cY- two ));

            if(distance > r + r2){ }
            else if(distance < Math.abs(r-r2)){ }
            else if(distance == 0 && r == r2){ }
            else{
                return false;
            }
        }

        return true;
    }


    public String gen_shape(){
        Random rand=new Random();
        int temp1= rand.nextInt(2);

        String randomshape = "";
        String randomcolor= "";
        if(temp1 == 1){
            return "circles";
        }
        else if(temp1 == 0){
            return "squares";
        }
        else{
            return "ERROR";
        }

    }

    /*
     * Returns an string corresponding to a random shape
     * parameters: none
     * input: RNG
     * output: string corresponding to a random shape
     * */
    public void add_shape(Canvas canvas){

        String randomshape= gen_shape();
        int randomcolor= gen_color();

        //RNG is done, now to make the shape
        if(randomshape.equals("circles")){
            paint.setStrokeWidth(3);
            paint.setColor(randomcolor); //make this randomcolor

            //generate safe coordinates
            int cX, cY, r=0;
            ArrayList<Integer> coordarray= new ArrayList<>();
            while(true) {
                coordarray= gencoordinatescircle();
                cX= coordarray.get(0);
                cY= coordarray.get(1);
                r= coordarray.get(2);

                if(isIB_circle(cX, cY, r) && co_circlexsquare(cX, cY, r) && co_circlexcircle(cX, cY, r, -1) ){
                    break;
                }

            }

            Log.d("Debug", "New Circle: "+ cX+  " " + cY+ " " +r);
            canvas.drawCircle(cX,cY, r, paint);
            //push to AL
            Random rand= new Random();
            int ttl =rand.nextInt(5) + 4;
            circleList.add(new Circle(cX, cY, r, (System.currentTimeMillis() / 1000) + ttl, randomcolor));
        }
        else if(randomshape.equals("squares")){
            paint.setStrokeWidth(3);
            paint.setColor(randomcolor); //make this randomcolor

            //generate safe coordinates
            int X1, X2, Y1, Y2=0;

            ArrayList<Integer> coordarray= new ArrayList<>();
            while(true) {
                coordarray= gencoordinatessquare();
                X1= coordarray.get(0);
                Y1= coordarray.get(1);
                X2= coordarray.get(2);
                Y2= coordarray.get(3);

                    Log.d("Debug", "ISIB" + isIB_square(X1,Y1,X2,Y2));
                    Log.d("Debug", "CSC" + co_squarexsquare(X1, Y1, X2, Y2, -1));
                if( isIB_square(X1,Y1,X2,Y2) && co_squarexsquare(X1, Y1, X2, Y2,-1) && co_squarexcircle(X1, Y1, X2, Y2)){
                   
                    break;
                }

            }

            Log.d("Debug", "New square: ("+ X1+  "," + Y1+ "), (" + X2+ "," + Y2 + ")");
            
            canvas.drawRect(X1, Y1, X2, Y2, paint);

            Random rand= new Random();
            int ttl =rand.nextInt(5) + 4;
            squareList.add(new Square(X1, Y1, X2, Y2, (System.currentTimeMillis() / 1000) + ttl, randomcolor));
            Log.d("Debug", "SLS:"+ squareList.size());
        }
        else{
            Log.d("Debug", "ERROR");
        }

    }

    /*
     * Returns an AL with TopLeft XY and BotRight XY
     * parameters: none
     * input: RNG between size and BOUND-size
     * output: AL with TopLeft XY and BotRight XY
     * */
    public ArrayList<Integer> gencoordinatessquare(){
        Random rand=new Random();
        float density = context.getResources().getDisplayMetrics().density;
        //Log.d("Debug", density+"");
        int sizedp= rand.nextInt(15) + 15;
        int size = (int) (sizedp* density);

        int botrightx= rand.nextInt(Game.BOUNDX-size) + size;
        int botrighty= rand.nextInt(Game.BOUNDY-size) + size;

        int topleftx= botrightx - size;
        int toplefty= botrighty - size;


        ArrayList<Integer> coordinates= new ArrayList<>();

        Log.d("Debug", "Coordinates in gcs" + topleftx + " " + toplefty + " " + botrightx +  " " + botrighty);
        coordinates.add(topleftx);
        coordinates.add(toplefty);
        coordinates.add(botrightx);
        coordinates.add(botrighty);

        Log.d("Debug", "We are returning in square:"+ coordinates.get(0)+  " " + coordinates.get(1)
                + " " +coordinates.get(2)+ " " +coordinates.get(3));

        return coordinates;
    }

    /*
     * Returns an AL with Center XY and Radius r
     * parameters: none
     * input: RNG between size and BOUND-size
     * output: AL with Center XY and Radius r
     * */
    public ArrayList<Integer> gencoordinatescircle(){
        Random rand=new Random();
        int size= rand.nextInt(33) + 32;
        int centerx= rand.nextInt(Game.BOUNDX)- size;
        int centery= rand.nextInt(Game.BOUNDY)- size;

        ArrayList<Integer> coordinates= new ArrayList<>();
        coordinates.add(centerx);
        coordinates.add(centery);
        coordinates.add(size);

        //Log.d("Debug", "We are returning in circle:"+ coordinates.get(0)+  " " + coordinates.get(1)
        //+ " " +coordinates.get(2));
        return coordinates;
    }

    /*
     * Removes shapes when motionevent detected
     * parameters: MotionEvent event
     * input: touchX and touchY from event
     * output: updated AL
     * */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();

        Log.d("Debug", "Touch coordinates" + touchX + " " + touchY);

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:

                for(int i=0 ;i  < squareList.size(); i++){
                    Square s= squareList.get(i);
                    if(s.contains(touchX,touchY)  ){
                        Log.d("Debug", "Hit Detected");

                        squareList.remove(i);
                        if(s.getColor() == Instructions.randomcolor && Instructions.randomshape.equals("squares")) {
                            numcorrect++;
                        }
                        return true;
                    }
                }

                for(int i=0 ;i  < circleList.size(); i++){
                    Circle c= circleList.get(i);
                    if(c.contains(touchX,touchY) == true ){
                        Log.d("Debug", "Removing circle");

                        circleList.remove(i);
                        if(c.getColor() == Instructions.randomcolor && Instructions.randomshape.equals("circles")) {
                            numcorrect++;
                        }
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return true;
    }

}
