import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Window;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.Optional;
import javafx.util.Duration;
import javafx.collections.ListChangeListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Arrays;
import java.text.SimpleDateFormat;  
import java.util.Date;  
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.Collections;
import javafx.beans.property.ReadOnlyDoubleProperty;
/******************************************
                END IMPORTS
*******************************************/
public class Asg2_sxj180060 extends Application{
    public static ObservableList<Record> data =FXCollections.observableArrayList();
    public static Record selectedRecord; //this is the current record in the OL that is selected
    public static final int NULL_MODE=0;
    public static final int ADD_MODE=1;
    public static final int MOD_MODE=2;
    public static final int DEL_MODE=3;
    public static int OP_MODE= NULL_MODE; //0 is nothing, 1 is add, 2 is modify, 3 is delete
    public static int globalnumback=0; //cleared after every record
    public static String globaltimesubmit;
    public static String globaltimestart;
    public static int flagStart=1; //set to 0 on first field type, back to 1 on submit
    public static String todaysstringdate="";

    TextField sysmsg, fnameField, minitField, lnameField, addressField, address2Field, cityField,zipField,
    phoneField, emailField, dateField, genderField, stateField, ppField;
    Label fnameLabel, minitLabel, lnameLabel, addressLabel, address2Label, cityLabel, stateLabel, zipLabel, genderLabel,
    phoneLabel, emailLabel, ppLabel, dateLabel;
    TableView table;


    @Override
    public void start(Stage myStage) throws Exception {
        GridPane gridPane = new GridPane();

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10, 20, 10, 10));
        gridPane.setHgap(20);
        gridPane.setVgap(10);

         ColumnConstraints col0 = new ColumnConstraints();
         col0.setPercentWidth(10);
         col0.setHalignment(HPos.RIGHT);

         ColumnConstraints col1 = new ColumnConstraints();
         col1.setPercentWidth(40);

         ColumnConstraints col2 = new ColumnConstraints();
         col2.setPercentWidth(5);
         col2.setHalignment(HPos.RIGHT);

         ColumnConstraints col3 = new ColumnConstraints();
         col3.setPercentWidth(5);

         //table columns
         ColumnConstraints col4 = new ColumnConstraints();
         col4.setPercentWidth(20);

         ColumnConstraints col5 = new ColumnConstraints();
         col5.setPercentWidth(20);

         gridPane.getColumnConstraints().addAll(col0,col1,col2, col3, col4);


        addFields(gridPane);

        myStage.setTitle("Data Entry Form");
        Scene scene = new Scene(gridPane, 1400, 800);
        myStage.setScene(scene);
        myStage.setMaximized(true);

        Collections.sort(data); 
        table.refresh();
        myStage.show();
    }

    @Override
    public void stop() {
        System.out.println("System exiting...");

        myIO writer= new myIO();
        writer.write();
    }


    private void addFields(GridPane gridPane) {
        myIO myIOunit= new myIO();

        /******************************************
                GRIDPANE VALUES
        *******************************************/

        int fnameLabelX=0;  int fnameLabelY=2;
        int fnameFieldX=1;  int fnameFieldY=2;

        int minitLabelX=2;  int minitLabelY=2;
        int minitFieldX=3;  int minitFieldY=2;

        int lnameLabelX=0;  int lnameLabelY=3;
        int lnameFieldX=1;  int lnameFieldY=3; 

        int addressLabelX=0;  int addressLabelY=4;
        int addressFieldX=1;  int addressFieldY=4; int addressFieldSpanX= 3; int addressFieldSpanY= 1; 

        int address2LabelX=0;  int address2LabelY=5;
        int address2FieldX=1;  int address2FieldY=5; int address2FieldSpanX= 3; int address2FieldSpanY= 1; 

        int cityLabelX=0;  int cityLabelY=6;
        int cityFieldX=1;  int cityFieldY=6;

        int stateLabelX=0;  int stateLabelY=7;
        int stateFieldX=1;  int stateFieldY=7;

        int zipLabelX=0;  int zipLabelY=8;
        int zipFieldX=1;  int zipFieldY=8;

        int genderLabelX=0;  int genderLabelY=9;
        int genderFieldX=1;  int genderFieldY=9;

        int phoneLabelX=0;  int phoneLabelY=10;
        int phoneFieldX=1;  int phoneFieldY=10;

        int emailLabelX=0;  int emailLabelY=11; 
        int emailFieldX=1;  int emailFieldY=11; int emailFieldSpanX= 3; int emailFieldSpanY= 1; 

        int ppLabelX=0;  int ppLabelY=12;
        int ppFieldX=1;  int ppFieldY=12;

        int dateLabelX=0;  int dateLabelY=13;
        int dateFieldX=1;  int dateFieldY=13;

        int submitButtonX=1; int submitButtonY=14; int submitButtonSpanX=2; int submitButtonSpanY=2;
        int listX=4; int listY=3; int listSpanX=2; int listSpanY=12;

        int clearButtonX=4; int clearButtonY=2; int clearButtonSpanX=2; int clearButtonSpanY=1;

        int sysmsgX=1; int sysmsgY=0; int sysmsgXSpan=5; int sysMsgYspan=2;


        /******************************************
                ADD UI ELEMENTS
        *******************************************/
        //sysmsg
        sysmsg = new TextField();
        //minitField.setPrefHeight(40)
        sysmsg.setDisable(true);
        sysmsg.setText(sysmsg.getText() + "Hint: when no row is selected, you can only add. Otherwise, submitting w/o modifying any fields will delete.");
        sysmsg.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gridPane.add(sysmsg, sysmsgX, sysmsgY, sysmsgXSpan, sysMsgYspan);

        // First Name
        fnameLabel = new Label("First Name* : ");
        gridPane.add(fnameLabel, fnameLabelX, fnameLabelY);
        fnameField = new TextField();
        //fnameField.setPrefHeight(40);
        gridPane.add(fnameField, fnameFieldX, fnameLabelY);

        // Middle Initial
        minitLabel = new Label("MI: ");
        gridPane.add(minitLabel, minitLabelX, minitLabelY);
        minitField = new TextField();
        //minitField.setPrefHeight(40);
        gridPane.add(minitField, minitFieldX, minitFieldY);

        // Last Name
        lnameLabel = new Label("Last Name* : ");
        gridPane.add(lnameLabel, lnameLabelX, lnameLabelY);
        lnameField = new TextField();
        //lnameField.setPrefHeight(40);
        gridPane.add(lnameField, lnameFieldX, lnameFieldY);

        // Address
        addressLabel = new Label("Address* : ");
        gridPane.add(addressLabel, addressLabelX, addressLabelY);
        addressField = new TextField();
        //addressField.setPrefHeight(40);
        gridPane.add(addressField, addressFieldX, addressLabelY, addressFieldSpanX, addressFieldSpanY);


        // Address 2
        address2Label = new Label("Address2 : ");
        gridPane.add(address2Label, address2LabelX, address2LabelY);
        address2Field = new TextField();
        gridPane.add(address2Field, address2FieldX, address2LabelY, address2FieldSpanX, address2FieldSpanY);

        // City
        cityLabel = new Label("City* : ");
        gridPane.add(cityLabel, cityLabelX, cityLabelY);
        cityField = new TextField();
        gridPane.add(cityField, cityFieldX, cityFieldY);

        //State
        stateLabel = new Label("State* : ");
        gridPane.add(stateLabel, stateLabelX, stateLabelY);
        TextField stateField = new TextField();
        stateField.setMaxWidth(60);
        gridPane.add(stateField, stateFieldX, stateFieldY);

        //Zip
        zipLabel = new Label("Zip* : ");
        gridPane.add(zipLabel, zipLabelX, zipLabelY);
        TextField zipField = new TextField();
        zipField.setPromptText("9-digit: xxxxx-xxxx");
        gridPane.add(zipField, zipFieldX, zipFieldY);

        //Gender
        genderLabel = new Label("Gender* : ");
        gridPane.add(genderLabel, genderLabelX, genderLabelY);
        TextField genderField = new TextField();
        genderField.setPromptText("M/F");
        genderField.setMaxWidth(60);
        gridPane.add(genderField, genderFieldX, genderFieldY);

        //Phone
        phoneLabel = new Label("Phone* : ");
        gridPane.add(phoneLabel, phoneLabelX, phoneLabelY);
        phoneField = new TextField();
        phoneField.setPromptText("Just numbers: i.e. 5559999");
        gridPane.add(phoneField, phoneFieldX, phoneFieldY);

        //eMail
        emailLabel = new Label("Email* : ");
        gridPane.add(emailLabel, emailLabelX, emailLabelY);
        emailField = new TextField();

        gridPane.add(emailField, emailFieldX, emailFieldY,emailFieldSpanX, emailFieldSpanY);

        //Purchase Proof
        ppLabel = new Label("Purchase Proof* : ");
        gridPane.add(ppLabel, ppLabelX, ppLabelY);
        ppField = new TextField();
        ppField.setPromptText("Y/N");
        ppField.setMaxWidth(60);
        gridPane.add(ppField, ppFieldX, ppFieldY);

        //Date
        dateLabel = new Label("Date* : ");
        gridPane.add(dateLabel, dateLabelX, dateLabelY);
        dateField = new TextField();
        dateField.setPromptText("MM/DD/YY i.e. 09/15/21");
        dateField.setPrefHeight(40);
        gridPane.add(dateField, dateFieldX, dateFieldY);

        // TODO: default this to todays date
        Date today= new Date();
        int currMonth= today.getMonth();
        int currDay= today.getDate();
        int currYear= today.getYear() -100; //since 1900 docs

        String monthString=Integer.toString(currMonth);
        String dayString=Integer.toString(currDay);
        String yearString=Integer.toString(currYear);

        if(currMonth < 10){
            monthString= "0"+currMonth;
        }
        if(currDay < 10){
            dayString= "0"+ currDay;
        }
        todaysstringdate= monthString + "/"+ dayString + "/" + yearString;
        dateField.setText(todaysstringdate);

        //Creating the table
        //this is where read used to be
        table = new TableView();
        table.setEditable(true);
   
 
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory( new PropertyValueFactory<Record, String> ("fullname"));

        TableColumn phoneCol = new TableColumn("Phone");
        phoneCol.setCellValueFactory( new PropertyValueFactory<Record, String>("phone"));

        ReadOnlyDoubleProperty tablewidth= table.widthProperty();
        nameCol.prefWidthProperty().bind(tablewidth.multiply(0.5)); 
        phoneCol.prefWidthProperty().bind(tablewidth.multiply(0.5));
        
        table.getColumns().addAll(nameCol, phoneCol);

        //fill the table 
        Collections.sort(data); 
        table.setItems(data);
 
 		//HBox mybox = new HBox(20, addButton, modButton, delButton);
        VBox vbox = new VBox(50, table);
       // vbox.setSpacing(25);
        //vbox.setPadding(new Insets(10, 0, 0, 10));
        gridPane.add(vbox, listX, listY, listSpanX, listSpanY);

        // Add Submit Button
        Button submitButton = new Button("Add");
        gridPane.add(submitButton, submitButtonX, submitButtonY, submitButtonSpanX, submitButtonSpanY);
        submitButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Button clearButton = new Button("Clear");
        gridPane.add(clearButton, clearButtonX, clearButtonY, clearButtonSpanX, clearButtonSpanY);
        clearButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);


        /*********************************************************************************
         * CAPS LOCK FORMATTERS*
         * *****************************************************************************/
        stateField.setTextFormatter(new TextFormatter<>((c) -> {
            String ucase= c.getText().toUpperCase();
            c.setText(ucase);
            return c;
        }));

        genderField.setTextFormatter(new TextFormatter<>((c) -> {
            String ucase= c.getText().toUpperCase();
            c.setText(ucase);
            return c;
        }));

        ppField.setTextFormatter(new TextFormatter<>((c) -> {
            String ucase= c.getText().toUpperCase();
            c.setText(ucase);
            return c;
        }));


        /*******************************************************************************
                    Hidden Field Listeners
        *****************************************************************************/
        fnameField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent k) {
                if(submitButton.getText().equals("Add") && flagStart == 1){
                    System.out.println("Key has been pressed on first field with add mode.");

                    Date timenow= new Date();
                    int hournow= timenow.getHours();
                    int minutenow= timenow.getMinutes();
                    int secondnow= timenow.getSeconds();

                    String hourString=Integer.toString(hournow);
                    String minuteString=Integer.toString(minutenow);
                    String secondString=Integer.toString(secondnow);
                    //zero-pad
                    if(minutenow < 10){
                        minuteString= "0"+minutenow;
                    }
                    if(secondnow < 10){
                        minuteString= "0"+secondnow;
                    }

                    globaltimestart= hourString+ ":" +minuteString + ":" + secondString;
                    System.out.println("globaltimesubmit" + globaltimestart);


                    flagStart=0; //so it never gets triggered until next record
                }
                if( k.getCode() == KeyCode.BACK_SPACE){
                    System.out.println("Backspace pressed");
                    globalnumback += 1;
                }
            }
        });

        minitField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent k) {
                if( k.getCode() == KeyCode.BACK_SPACE){
                    System.out.println("Backspace pressed");
                    globalnumback += 1;
                }
            }
        });

        lnameField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent k) {
                if( k.getCode() == KeyCode.BACK_SPACE){
                    System.out.println("Backspace pressed");
                    globalnumback += 1;
                }
            }
        });

        addressField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent k) {
                if( k.getCode() == KeyCode.BACK_SPACE){
                    System.out.println("Backspace pressed");
                    globalnumback += 1;
                }
            }
        });

        address2Field.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent k) {
                if( k.getCode() == KeyCode.BACK_SPACE){
                    System.out.println("Backspace pressed");
                    globalnumback += 1;
                }
            }
        });

        cityField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent k) {
                if( k.getCode() == KeyCode.BACK_SPACE){
                    System.out.println("Backspace pressed");
                    globalnumback += 1;
                }
            }
        });

        stateField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent k) {
                if( k.getCode() == KeyCode.BACK_SPACE){
                    System.out.println("Backspace pressed");
                    globalnumback += 1;
                }
            }
        });

        zipField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent k) {
                if( k.getCode() == KeyCode.BACK_SPACE){
                    System.out.println("Backspace pressed");
                    globalnumback += 1;
                }
            }
        });

        genderField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent k) {
                if( k.getCode() == KeyCode.BACK_SPACE){
                    System.out.println("Backspace pressed");
                    globalnumback += 1;
                }
            }
        });

        phoneField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent k) {
                if( k.getCode() == KeyCode.BACK_SPACE){
                    System.out.println("Backspace pressed");
                    globalnumback += 1;
                }
            }
        });

        emailField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent k) {
                if( k.getCode() == KeyCode.BACK_SPACE){
                    System.out.println("Backspace pressed");
                    globalnumback += 1;
                }
            }
        });

        ppField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent k) {
                if( k.getCode() == KeyCode.BACK_SPACE){
                    System.out.println("Backspace pressed");
                    globalnumback += 1;
                }
            }
        });

        dateField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent k) {
                if( k.getCode() == KeyCode.BACK_SPACE){
                    System.out.println("Backspace pressed");
                    globalnumback += 1;
                }
            }
        });


        /*******************************************************************************
        Listener for the Table
        *****************************************************************************/

        ObservableList<Record> selectedItems = table.getSelectionModel().getSelectedItems();
        selectedItems.addListener(new ListChangeListener<Record>() {
          @Override
          public void onChanged(Change<? extends Record> change) {
             while (change.next()) {
                int start = change.getFrom() ;
                int end = change.getTo() ;
                
                //System.out.println("Start:"+ start);
                //System.out.println("End:" + end);

                for (int i = start ; i < end ; i++) {
                    selectedRecord= change.getList().get(i);

                    fnameField.setText(selectedRecord.getFname());
                    minitField.setText(selectedRecord.getMinit());
                    lnameField.setText(selectedRecord.getLname());
                    addressField.setText(selectedRecord.getAddress1());
                    address2Field.setText(selectedRecord.getAddress2());
                    cityField.setText(selectedRecord.getCity());
                    stateField.setText(selectedRecord.getState());
                    zipField.setText(selectedRecord.getZip());
                    genderField.setText(selectedRecord.getGender());
                    emailField.setText(selectedRecord.getEmail());
                    phoneField.setText(selectedRecord.getPhone());
                    ppField.setText(selectedRecord.getPp());
                    dateField.setText(selectedRecord.getDaterecv());

                    submitButton.setText("Modify/Delete");
                    OP_MODE= MOD_MODE; // doesnt matter
                }
            }
          }
        });

        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fnameField.setText("");
                minitField.setText("");
                lnameField.setText("");
                addressField.setText("");
                address2Field.setText("");
                genderField.setText("");
                cityField.setText("");
                stateField.setText("");
                zipField.setText("");
                emailField.setText("");
                phoneField.setText("");
                ppField.setText("");
                dateField.setText(todaysstringdate);

                table.getSelectionModel().clearSelection(); //clear the selection
                submitButton.setText("Add");
                OP_MODE= ADD_MODE;

                flagStart= 1;
            }
        });

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
            	System.out.println("Submit pressed.");

                //Fixes Bug: you only change one field and you dont tab out of that field, doesn't count as a modify
                fnameField.requestFocus();
                minitField.requestFocus();

                //get hidden field
                Date timenow= new Date();
                int hournow= timenow.getHours();
                int minutenow= timenow.getMinutes();
                int secondnow= timenow.getSeconds();

                String hourString=Integer.toString(hournow);
                String minuteString=Integer.toString(minutenow);
                String secondString=Integer.toString(secondnow);
                //zero-pad
                if(minutenow < 10){
                    minuteString= "0"+minutenow;
                }
                if(secondnow < 10){
                    minuteString= "0"+secondnow;
                }

                globaltimesubmit= hourString+ ":" +minuteString + ":" + secondString;
                System.out.println("globaltimesubmit" + globaltimesubmit);

                //run checks
                if(fnameField.getText().isEmpty() || lnameField.getText().isEmpty() || lnameField.getText().isEmpty() || 
                        addressField.getText().isEmpty() || cityField.getText().isEmpty() || phoneField.getText().isEmpty()
                        || zipField.getText().isEmpty() || phoneField.getText().isEmpty() || stateField.getText().isEmpty()
                        || emailField.getText().isEmpty() || phoneField.getText().isEmpty()  || dateField.getText().isEmpty() ) {

                        sysmsg.setStyle("-fx-control-inner-background: red;");
                        sysmsg.setText("You have missing required fields.");
                        return;
                }

                if(fnameField.getText().length() > 25 || minitField.getText().length() > 1 || lnameField.getText().length() > 25 || 
                    addressField.getText().length() > 35 || address2Field.getText().length() > 35 || cityField.getText().length() > 35 
                    || zipField.getText().length() > 10 || phoneField.getText().length() > 10 || emailField.getText().length() > 60){

                    sysmsg.setStyle("-fx-control-inner-background: red;");
                    sysmsg.setText("You have exceeded the character limit on one or more fields.");
                    return;
                }


                if( (!genderField.getText().equals("M") ) && ( ! genderField.getText().equals("F") ) ){
                    sysmsg.setStyle("-fx-control-inner-background: red;");
                    sysmsg.setText("Invalid gender field: only 'M' or 'F' allowed.");
                    return;
                }

                if( (!ppField.getText().equals("Y") ) && ( ! ppField.getText().equals("N") ) ){
                    sysmsg.setStyle("-fx-control-inner-background: red;");
                    sysmsg.setText("Invalid purchase proof field: only 'Y' or 'N' allowed.");
                    return;
                }
                String tempstates[] = {"AK", "AL", "AR" , "AS" , "AZ", "CA", "CA" , "CT", "DC", "DE", "FL", "GA", "GU", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MP", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "PR", "RI", "SC", "SD", "TN", "TX", "UM", "UT", "VA", "VI", "VT", "WA", "WI", "WV", "WY"};
                List fiftystates = Arrays.asList(tempstates);
                if( ! fiftystates.contains(stateField.getText())  ){
                    sysmsg.setStyle("-fx-control-inner-background: red;");
                    sysmsg.setText("Invalid state field. Use recognized two-letter abbreviation.");
                    return;
                }

                if (! Pattern.matches("[0-9][0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]",zipField.getText())){
                    sysmsg.setStyle("-fx-control-inner-background: red;");
                    sysmsg.setText("Invalid zip field: must match pattern xxxxx-xxxx.");
                    return;
                }

                if (! Pattern.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]",phoneField.getText())){
                    sysmsg.setStyle("-fx-control-inner-background: red;");
                    sysmsg.setText("Invalid phone field: must match pattern xxxxxxxxxx.");
                    return;
                }

                if (! Pattern.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]",phoneField.getText())){
                    sysmsg.setStyle("-fx-control-inner-background: red;");
                    sysmsg.setText("Invalid phone field: must match pattern xxxxxxxxxx.");
                    return;
                }

                if (! emailField.getText().contains("@")){
                    sysmsg.setStyle("-fx-control-inner-background: red;");
                    sysmsg.setText("Invalid email field: must contain an ampersand.");
                    return;
                }

                if (! Pattern.matches("[0-9][0-9]/[0-9][0-9]/[0-9][0-9]",dateField.getText())){
                    sysmsg.setStyle("-fx-control-inner-background: red;");
                    sysmsg.setText("Invalid date field: must match pattern xx/xx/xx.");
                    return;
                }

                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
                String dateString = format.format(new Date());
                
                try{
                    Date dateobj, todaysdate;
                    dateobj= format.parse(dateField.getText());
                    todaysdate = new Date();  //defaults to today?

                    if(dateobj.compareTo(todaysdate) > 0){
                    sysmsg.setStyle("-fx-control-inner-background: red;");
                    sysmsg.setText("Invalid date field: is a date in the future.");
                    return;
                    }
                }catch(Exception e){
                    System.out.println(e);
                }

                if(submitButton.getText().equals("Add")){
                    OP_MODE = ADD_MODE;
                }

                //infer the difference between
                else if ( (fnameField.getText().equals(selectedRecord.getFname()) ) && (minitField.getText().equals(selectedRecord.getMinit()))
                    && (lnameField.getText().equals(selectedRecord.getLname())) && (addressField.getText().equals(selectedRecord.getAddress1())) && 
                    (cityField.getText().equals(selectedRecord.getCity())) && (stateField.getText().equals(selectedRecord.getState()))
                        && (zipField.getText().equals(selectedRecord.getZip())) && (genderField.getText().equals(selectedRecord.getGender()))
                         && (phoneField.getText().equals(selectedRecord.getPhone())) && (emailField.getText().equals(selectedRecord.getEmail()))
                          && (ppField.getText().equals(selectedRecord.getPp()))  && (dateField.getText().equals(selectedRecord.getDaterecv()) )  ){
                    System.out.println("All fields are the same, deleting...");
                    OP_MODE = DEL_MODE; 
                }
                else{
                    System.out.println("Going to modify...");
                    OP_MODE= MOD_MODE;
                }

                System.out.println("Checks over, op_mode is:" + OP_MODE);
				if(OP_MODE == DEL_MODE){
			      	//Delete the entry by looping through for a match
                    for(int i=0; i<data.size(); i++) {
                      Record r= data.get(i);
                      System.out.println(r.getFname());
                        
                        if( (r.getFname().equals( fnameField.getText())) && (r.getLname().equals(lnameField.getText())) &&
                            (r.getPhone().equals(phoneField.getText())) ){
                            //data= data.sublist(i,i).clear();
                            data.remove(i);

                            sysmsg.setStyle("-fx-control-inner-background: green;");
                            sysmsg.setText("The item has been successfully deleted.");

                            OP_MODE= ADD_MODE;
                            submitButton.setText("Add");
                        }
                    }       

			      	//Clear text boxes
		            fnameField.setText("");
			        minitField.setText("");
			        lnameField.setText("");
			        addressField.setText("");
			        address2Field.setText("");
			        genderField.setText("");
                    cityField.setText("");
                    stateField.setText("");
			        zipField.setText("");
			        emailField.setText("");
			        phoneField.setText("");
                    ppField.setText("");
                    dateField.setText(todaysstringdate);

                    return;
				}

          		else if(OP_MODE == MOD_MODE){
                    System.out.println("Modifying record!");
                    
                    for(int i=0; i<data.size(); i++) {
                        Record tempr= data.get(i);
                        if(fnameField.getText().equals(tempr.getFname()) && lnameField.getText().equals(tempr.getLname()) && phoneField.getText().equals(tempr.getPhone()) ){
                                sysmsg.setStyle("-fx-control-inner-background: red;");
                                sysmsg.setText("Error: attempt to modify into a duplicate record.");
                                return;
                        }
                    }

                   selectedRecord.setFname(fnameField.getText());
                   selectedRecord.setMinit(minitField.getText());
                   selectedRecord.setLname(lnameField.getText());
                   selectedRecord.setAddress1(addressField.getText());
                   selectedRecord.setAddress2(address2Field.getText());
                   selectedRecord.setGender((String) genderField.getText());
                   selectedRecord.setCity(cityField.getText());
                   selectedRecord.setState((String) stateField.getText());
                   selectedRecord.setZip(zipField.getText());
                   selectedRecord.setEmail(emailField.getText());
                   selectedRecord.setPhone(phoneField.getText());
                   selectedRecord.setPp((String) ppField.getText());
                   selectedRecord.setDaterecv(dateField.getText());
                   selectedRecord.setFullname(fnameField.getText() + " "+ lnameField.getText());

                   Collections.sort(data); 
                   table.refresh(); //to fix bug: the data has changed, but the visual does not refleect
                   sysmsg.setStyle("-fx-control-inner-background: green;");
                   sysmsg.setText("The item has been successfully modified.");

                   OP_MODE= ADD_MODE;
                   submitButton.setText("Add");

                   //Clear text boxes
                    fnameField.setText("");
                    minitField.setText("");
                    lnameField.setText("");
                    addressField.setText("");
                    address2Field.setText("");
                    genderField.setText("");
                    cityField.setText("");
                    stateField.setText("");
                    zipField.setText("");
                    emailField.setText("");
                    phoneField.setText("");
                    ppField.setText("");
                    dateField.setText(todaysstringdate);

                    table.getSelectionModel().clearSelection(); //clear the selection
                   return;
          		}

                else if(OP_MODE == ADD_MODE){   
            //(String fName, String minit, String lName, String address1, String address2, 
            //String city, String state, String zip, String gender, String phone, String email, String pp, String date, 
            //String timeStarted, String timeSaved, String numBackspace)
                    for(int i=0; i<data.size(); i++) {
                        Record tempr= data.get(i);

                        if(fnameField.getText().equals(tempr.getFname()) && lnameField.getText().equals(tempr.getLname()) && phoneField.getText().equals(tempr.getPhone()) ){
                            sysmsg.setStyle("-fx-control-inner-background: red;");
                            sysmsg.setText("Error: attempt to add duplicate record.");
                            return;
                        }

                    }

                    String fullname= fnameField.getText() + " "+  lnameField.getText();
                    Record newRecord = new Record(fnameField.getText(), minitField.getText(), lnameField.getText(), addressField.getText()
                        ,address2Field.getText(), cityField.getText(), (String) stateField.getText(), zipField.getText(), (String) genderField.getText(),
                        phoneField.getText(), emailField.getText(), (String) ppField.getText(), dateField.getText().toString(), globaltimestart, globaltimesubmit, Integer.toString(globalnumback), fullname);
                    data.add(newRecord);

                    globalnumback=0;
                    globaltimesubmit= "";
                    globaltimestart = "";
                    flagStart= 1;

                    sysmsg.setStyle("-fx-control-inner-background: green;");
                    sysmsg.setText("The item has been successfully added.");

                    fnameField.setText("");
                    minitField.setText("");
                    lnameField.setText("");
                    addressField.setText("");
                    address2Field.setText("");
                    genderField.setText("");
                    cityField.setText("");
                    stateField.setText("");
                    zipField.setText("");
                    emailField.setText("");
                    phoneField.setText("");
                    ppField.setText("");
                    dateField.setText(todaysstringdate);

                    table.getSelectionModel().clearSelection(); //clear the selection

                    return;
                }
            }
        });
    }

    public static void main(String[] args) {
        myIO myIOunit= new myIO();
        data=myIOunit.read();
        launch(args);
    }

    public static class Record implements Comparable<Record>{
    	private final SimpleStringProperty fname;
    	private final SimpleStringProperty minit;
        private final SimpleStringProperty lname;
        private final SimpleStringProperty address1;
        private final SimpleStringProperty address2;
        private final SimpleStringProperty city;
        private final SimpleStringProperty state;
        private final SimpleStringProperty zip;
        private final SimpleStringProperty gender;
        private final SimpleStringProperty phone;
        private final SimpleStringProperty email;
        private final SimpleStringProperty pp;
        private final SimpleStringProperty daterecv;
        private final SimpleStringProperty timeStarted;
        private final SimpleStringProperty timeSaved;
        private final SimpleStringProperty numBackspace;

        //Bad code, but I can't figure out how to put both fname and lname in the same column otherwise.
        private final SimpleStringProperty fullname;
 
 		//(String fName, String minit, String lName, String address1, String address2, 
            //String city, String state, String zip, String gender, String phone, String email, String pp, String date, 
            //String timeStarted, String timeSaved, String numBackspace)
        private Record(String fname, String minit, String lname, String address1, String address2, 
        	String city, String state, String zip, String gender, String phone, String email, String pp, String daterecv, 
            String timeStarted, String timeSaved, String numBackspace, String fullname) {
            this.fname = new SimpleStringProperty(fname);
            this.minit = new SimpleStringProperty(minit);
            this.lname = new SimpleStringProperty(lname);
            this.address1 = new SimpleStringProperty(address1);
            this.address2 = new SimpleStringProperty(address2);
            this.city = new SimpleStringProperty(city);
            this.state = new SimpleStringProperty(state);
            this.zip = new SimpleStringProperty(zip);
            this.gender = new SimpleStringProperty(gender);                 
            this.phone= new SimpleStringProperty(phone);
            this.email = new SimpleStringProperty(email);
            this.pp = new SimpleStringProperty(pp);
            this.daterecv= new SimpleStringProperty(daterecv);
            this.timeStarted = new SimpleStringProperty(timeStarted);
            this.timeSaved= new SimpleStringProperty(timeSaved);
            this.numBackspace= new SimpleStringProperty(numBackspace);

            this.fullname = new SimpleStringProperty(fullname);
        }
 
        public String getFname() {	return fname.get(); }
        public String getMinit() {return minit.get();}
        public String getLname(){ return lname.get();}
        public String getAddress1() {return address1.get();}
        public String getAddress2(){ return address2.get();}
        public String getCity(){ return city.get();}
        public String getState(){ return state.get();}
        public String getZip(){ return zip.get();}
        public String getGender(){ return gender.get();}
        public String getPhone(){ return phone.get();}
        public String getEmail(){ return email.get();}
        public String getPp() { return pp.get();}
        public String getDaterecv() { return daterecv.get();}
        public String getTimeStarted(){ return timeStarted.get();}
        public String getTimeSaved() {return timeSaved.get();}
        public String getNumBackspace(){return numBackspace.get();}
        public String getFullname(){return fullname.get();}


        public void setFname(String s){fname.set(s);}
        public void setMinit(String s){minit.set(s);}
        public void setLname(String s){lname.set(s);}
        public void setAddress1(String s){address1.set(s);}
        public void setAddress2(String s){address2.set(s);}
        public void setCity(String s){city.set(s);}
        public void setState(String s){state.set(s);}
        public void setZip(String s){zip.set(s);}
        public void setGender(String s){gender.set(s);}
        public void setPhone(String s){phone.set(s);}
        public void setEmail(String s){email.set(s);}
        public void setPp(String s){pp.set(s);}
        public void setDaterecv(String s){daterecv.set(s);}
        public void setTimeStarted(String s){timeStarted.set(s);}
        public void setTimeSaved(String s){timeSaved.set(s);}
        public void setNumBackspace(String s){numBackspace.set(s);}
        public void setFullname(String s){fullname.set(s);}


        @Override
         public int compareTo(Record r){    
            String myfname= this.getFname();
            String myfname2= r.getFname();
            int comp = myfname.compareTo(myfname2);
            return comp;
          }
    }

    public static class myIO{ //Requirement: seperate class for IO
        //returns an OL of Record Objects
        public static ObservableList<Record> read(){

            ObservableList<Record> parsedObjects =FXCollections.observableArrayList();
            try{
                File myObj = new File("CS6326Asg2.txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
            
                    String line = myReader.nextLine();
                    System.out.println("The present line:\n" + line);
                    String[] attributes = line.split("\\\t");
                                
                    if(attributes.length != 16){
                        System.out.println("Fatal error: attributes not read properly..."+ attributes.length +"attributes found");
                        java.lang.System.exit(0);
                    }

                    String timeStarted= attributes[0];
                    String timeSaved= attributes[1];
                    String numBackspace= attributes[2];
                    String myfname= attributes[3];
                    String myminit= attributes[4];
                    String mylname= attributes[5];
                    String myaddress= attributes[6];
                    String myaddress2= attributes[7];
                    String mycity= attributes[8];
                    String mystate= attributes[9];
                    String myzip= attributes[10];
                    String mygender= attributes[11];
                    String myphone= attributes[12];
                    String mymail= attributes[13];
                    String mypp= attributes[14];
                    String mydate= attributes[15];
                    String fullname = myfname + " " + mylname;

                          //(String fName, String minit, String lName, String address1, String address2, 
                    //String city, String state, String zip, String gender, String phone, String email, String pp, String date, 
                    //String timeStarted, String timeSaved, String numBackspace)
                    Record myRecord= new Record(myfname, myminit, mylname, myaddress, myaddress2, 
                        mycity, mystate, myzip, mygender, myphone, mymail, mypp, mydate
                        ,timeStarted, timeSaved, numBackspace, fullname);
                    parsedObjects.add(myRecord);

                    System.out.println("\n\nNAME: "+ myfname+ "DATE: " + mydate);
                    
                }
                myReader.close();
            }
            catch (FileNotFoundException e) {
                  System.out.println("That file was not found, exiting....");
                  System.out.println(e);
            }

             System.out.println("Number of objects:"+ parsedObjects.size());

            return parsedObjects;
        }


        public static void write(){
            try {
              FileWriter myWriter = new FileWriter("CS6326Asg2.txt");

                for(int i=0; i<data.size(); i++) {
                  Record r= data.get(i);

                  if(r.getFname() != null){
                      myWriter.write(r.getTimeStarted()+ "\t" + r.getTimeSaved() + "\t" + r.getNumBackspace() + "\t" + 
                        r.getFname() + "\t" + r.getMinit()+ "\t" + r.getLname()+ "\t" + r.getAddress1()+ "\t" +
                        r.getAddress2() + "\t" + r.getCity()+ "\t" +r.getState() + "\t" +r.getZip() + 
                        "\t" + r.getGender() + "\t" + r.getPhone() + "\t" + r.getEmail() + "\t" +r.getPp() + "\t" + r.getDaterecv() + "\n");
                      
                      System.out.println("Wrote object"+i+" to file.");
                    }
                }

                myWriter.close();
            } catch (IOException e) {
              System.out.println("Could not write to file, exiting...");
              System.out.println(e);
            }
        }
    }
}