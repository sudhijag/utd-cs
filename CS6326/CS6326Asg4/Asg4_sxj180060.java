import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Window;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.Optional;
import javafx.util.Duration;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import java.io.FileWriter;
import java.io.IOException;
import javafx.application.Platform;
import java.time.LocalDate;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.util.List;
import java.util.Arrays;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.paint.Color;
import java.util.Collections;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.ProgressBar;
import java.nio.file.Files;
import javax.swing.SwingWorker;
import javafx.scene.control.Alert.AlertType;

/******************************************
End IMPORTS
*******************************************/
public class Asg4_sxj180060 extends Application{

    //UI ELEMENTS
    public static ObservableList<Record> data =FXCollections.observableArrayList();

    public static Label importLabel, searchLabel;
    public static TextField importfield, searchfield, timerfield, numhitsfield, tempfield;
    public static Button importButton, searchButton;
    public static TableView table;
    public static ProgressBar pb;
    public static long starttime;
    public static int numhits;

    //static int searchstring;

    static final int GLOBALFONTSIZE=18;
    private listUpdate listTask;

    public static int bytessofar=0;
    public static long fileSize=0;


    @Override
    public void start(Stage myStage) throws Exception {
        GridPane gridPane = new GridPane();


        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(20);
        gridPane.setVgap(10);

        /*****************************************************
            COLUMN CONSTRAINTS- there are 3 columns, the middle takes 80% of the width
        *****************************************************/
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(10);
        col0.setHalignment(HPos.RIGHT);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(80);
        col1.setHalignment(HPos.RIGHT);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(10);
        col2.setHalignment(HPos.RIGHT);

        /*****************************************************
           ROW CONSTRAINTS- there are 4 rows, the third takes 85% of the width (holds the table)
        *****************************************************/
        RowConstraints row0 = new RowConstraints();
        row0.setPercentHeight(5);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(5); 

        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(85);

        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(5);

        gridPane.getColumnConstraints().addAll(col0, col1, col2);
        gridPane.getRowConstraints().addAll(row0,row1,row2,row3);

        /******************************************
            GRIDPANE VALUES- this Is a 4x3
        ******************************************/
         int importLabelX= 0; int importLabelY=0;
        int importfieldX= 1; int importfieldY=0; int importfieldXSpan=1; int importfieldYspan=1;
        int importbuttonX=2; int importbuttonY=0;

        int searchLabelX= 0; int searchLabelY=1;
        int searchfieldX= 1; int searchfieldY=1; int searchfieldXSpan=1; int searchfieldYspan=1;
        int searchbuttonX=2; int searchbuttonY=1;

        int tableX=0; int tableY=2; int tableSpanX= 3; int tableSpanY=1;
        int pbX=1; int pbY=3; int pbSpanX=1; int pbSpanY=1;

        int timerfieldX= 2; int timerfieldY=3;
        int numhitsfieldX=0; int numhitsfieldY=3;  

        /*************************************************************
        ADD UI ELEMENTS
        ****************************************************************/

        //import
        importLabel = new Label("File: ");
        importLabel.setFont(new Font("Arial", GLOBALFONTSIZE));
        gridPane.add(importLabel, importLabelX, importLabelY);
        importfield = new TextField();
        importfield.setPromptText("Hint: Either paste a path Or click On the browse button");
        importfield.setFont(new Font("Arial", GLOBALFONTSIZE));
        importfield.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gridPane.add(importfield, importfieldX, importfieldY, importfieldXSpan, importfieldYspan);
        importButton = new Button("Browse");
        importButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        importButton.setFont(new Font("Arial", GLOBALFONTSIZE));
        gridPane.add(importButton, importbuttonX, importbuttonY);


        // search
        searchLabel = new Label("Search for: ");
        searchLabel.setFont(new Font("Arial", GLOBALFONTSIZE));
        gridPane.add(searchLabel, searchLabelX, searchLabelY);
        searchfield = new TextField();
        searchfield.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        searchfield.setFont(new Font("Arial", GLOBALFONTSIZE));
        searchfield.setPromptText("The Search button only looks grayed out, it still works.");
        gridPane.add(searchfield, searchfieldX, searchfieldY, searchfieldXSpan, searchfieldYspan);
        searchButton = new Button("");
        searchButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        searchButton.setFont(new Font("Arial", GLOBALFONTSIZE));
        gridPane.add(searchButton, searchbuttonX, searchbuttonY);

        //tempfield is a workaround that helps us change btwn search and cancel
        tempfield= new TextField();
        tempfield.setAlignment(Pos.CENTER);
        tempfield.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        tempfield.setText("Search");
        tempfield.setFont(new Font("Arial", GLOBALFONTSIZE));
        tempfield.setDisable(true);
        gridPane.add(tempfield, searchbuttonX, searchbuttonY);

        // table
        table= new TableView();
        table.setEditable(true);
        TableColumn lineCol = new TableColumn("Line #");
        lineCol.setCellValueFactory( new PropertyValueFactory<Record, String> ("linenum"));
        TableColumn textCol = new TableColumn("Text");
        textCol.setCellValueFactory( new PropertyValueFactory<Record, String> ("line"));
        //Changing the width of the table columns from default pixels to percentage widths of the 
        //overall table width
        ReadOnlyDoubleProperty tablewidth= table.widthProperty();
        lineCol.prefWidthProperty().bind(tablewidth.multiply(0.1)); 
        textCol.prefWidthProperty().bind(tablewidth.multiply(0.9));
        //add table
        table.getColumns().addAll(lineCol, textCol);
        table.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gridPane.add(table, tableX, tableY, tableSpanX, tableSpanY);

        //progress bar
        pb= new ProgressBar();
        pb.setProgress(0.0);
        pb.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gridPane.add(pb, pbX, pbY, pbSpanX, pbSpanY);

        //timer field
        timerfield= new TextField();
        timerfield.setText("_% in _m_s");
        timerfield.setDisable(true);
        gridPane.add(timerfield, timerfieldX, timerfieldY);

        numhitsfield= new TextField();
        numhitsfield.setText("_ hits");
        numhitsfield.setDisable(true);
        gridPane.add(numhitsfield, numhitsfieldX, numhitsfieldY);


        /*******************************************************************************
        BUTTON LISTENERS:
        Input- opens a filechooser and sets the textfield
        Search- checks if file valid, searches the file, displays results.
        *****************************************************************************/
        importButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                File infile = fileChooser.showOpenDialog(myStage);

                if(infile == null){
                    return;
                }


                String readpath= infile.getAbsolutePath();
                importfield.setText(readpath);
            }
        });
     

        searchButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

            Asg4_sxj180060.starttime = System.currentTimeMillis();
            System.out.println("At press: "+ Asg4_sxj180060.starttime);
               
            if(! tempfield.getText().equals("Cancel")){    
                data.clear();
            }

            table.setItems(data);
            pb.setProgress(0.0);
            bytessofar=0;
            fileSize=0;
            numhits=0;


            if(searchfield.getText().equals("") || searchfield.getText().equals(" ")){
                return;
            }

            if(! tempfield.getText().equals("Cancel")){

                //run the initial file-exist check before passing to the executer
                try{
                    File myObj = new File(importfield.getText());
                    Scanner myReader = new Scanner(myObj);
                }catch(Exception e){
                    System.out.println("FILE NOT FOUND");
                    Alert a = new Alert(AlertType.ERROR);
                    a.setTitle("Error.");
                    a.setContentText(e+"");
                    a.showAndWait();
                    return;
                }

                tempfield.setText("Cancel");
                (listTask= new listUpdate()).execute();
                
                table.refresh();
            }
            else{
                System.out.println("Cancelling the search.");
                listTask.cancel(true);
                listTask = null;
                tempfield.setText("Search");
            }
            }
        });


        /*******
         DISPLAY
         ********/

         myStage.setTitle("Multithreaded Search");
         Scene scene = new Scene(gridPane);
         myStage.setScene(scene);
        myStage.setMaximized(true); //expand to fill screen

        myStage.show();
    } // end of start function


   /**************************************************
    RECORD CLASS: Each entry in the table is a record
    Follows standard format for PropertyFactory
   ***************************************************/
   public class Record {
        private String linenum = "";
        private String line = "";
        private float progresspct=0; //not used

        public Record() {
        
        }

        public Record(String linenum, String line, float progresspct) {
            this.linenum = linenum;
            this.line = line;
            this.progresspct= progresspct;
        }

        public String getLinenum() {
            return linenum;
        }

        public void setLinenum(String linenum) {
            this.linenum = linenum;
        }

        public String getLine() {
            return line;
        }

        public float getpp(){
            return progresspct;
        }

        public void setLine(String line) {
            this.line = line;
        }
    }

    /****************************************************
    SwingWorker- consists of three functions
    1. doInBackground- reads file and converts the lines into Record objects. publishes the objects.
    2. process- Picks up the record objects and updates the UI table and trackers
    3. done- fills out the progress bar and trackers
    ****************************************************/
    private class listUpdate extends SwingWorker<Void, Record> {
        protected Void doInBackground() {
            
            try{
                File myObj = new File(importfield.getText());
                Asg4_sxj180060.fileSize = myObj.length(); //get file length in bytes

                Scanner myReader = new Scanner(myObj);

                int linenum=1;
                while (myReader.hasNextLine()) {

                    String line =  ( myReader.nextLine() );
                    String capsline= line.toUpperCase(); //case-insensitive search

                    final byte[] bytearr = line.getBytes("UTF-8"); 
                    Asg4_sxj180060.bytessofar += bytearr.length; //Add number of bytes in the currentline to running total
                    Thread.sleep(0001); //one millisecond

                    String key=  ( searchfield.getText() ).toUpperCase();

                    if (capsline.contains(key)){
                        //System.out.println("Match found");

                        Record recordtoadd= new Record(linenum+"", line, 0);
                        Asg4_sxj180060.numhits += 1;
                        publish(recordtoadd);

                        //System.out.println("DATA ADDED: "+  linenum+ ""+ line);
                    }
                    //Not a hit. Used to make the progress bar smoother. Will slow down the program though.
                    else{
                        Record recordtoadd= new Record(-2+"", line, 0);
                        publish(recordtoadd);
                    }
                    linenum += 1;
                }

                //publish a final record, which will change the button
                Record finalrecord= new Record(-1+"", "", 0);
                publish(finalrecord);
                myReader.close();
            }
            catch (Exception e) {
                System.out.println(e);
            }
        //}
            return null;
        }

        protected void process(List<Record> progress) {
            //System.out.println("process function"+ progress.size());
            long endtime= System.currentTimeMillis();
            //System.out.println("ENDTIME: "+ endtime + " STARTTIME:  " +  Asg4_sxj180060.starttime);

            //Format the elapsed time
            long elapsed= (endtime- Asg4_sxj180060.starttime) / 1000;
            long elapsedmin= elapsed/60; //should be integer
            long elapsedsec= elapsed%60;
            float percentdone= (float)(Asg4_sxj180060.bytessofar) / Asg4_sxj180060.fileSize;

            Asg4_sxj180060.timerfield.setText( (int) (percentdone * 100) + " % in "+ elapsedmin+"m"+elapsedsec+"s");
            Asg4_sxj180060.numhitsfield.setText(Asg4_sxj180060.numhits + " hits");

            //Record recentrecord= progress.get(progress.size()-1);
            ObservableList<Record> newdata= FXCollections.observableList(progress); //progress is a list, not an OL
            //ata.add(newdata);

            for(int i=0; i < newdata.size(); i++){

                //-1 is the code for when the search completes
                if(newdata.get(i).getLinenum().equals("-1")){
                    System.out.println("FINAL RECORD RECEIVED");
                    tempfield.setText("Search");
                    continue;
                }
                //-2 is the code for a non-match.
                else if(newdata.get(i).getLinenum().equals("-2")){
                    continue;
                }

                //update table.
                data.add(newdata.get(i));
                Asg4_sxj180060.table.setItems(data);
            }

            //System.out.println("PROGRESS: "+ (float)(Asg4_sxj180060.bytessofar) / Asg4_sxj180060.fileSize);
            pb.setProgress( percentdone);

        }

        protected void done() {
            //necessary because metadata bytes will cause the bar to not hit 100%

            long endtime= System.currentTimeMillis();
            long elapsed= (endtime- Asg4_sxj180060.starttime) / 1000;
            long elapsedmin= elapsed/60; 
            long elapsedsec= elapsed%60;

            Asg4_sxj180060.timerfield.setText("100% in "+ elapsedmin+"m"+elapsedsec+"s");
            pb.setProgress(1.0);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}




