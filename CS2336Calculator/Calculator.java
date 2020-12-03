import java.awt.*;
import javax.swing.*;

import java.util.*;
import java.awt.event.*;
import java.math.BigDecimal;

public class Calculator implements ActionListener{

	JFrame frame;

	public static void main(String[] args) {	
		Calculator window = new Calculator();
		window.frame.setVisible(true);
				
	}
	
	
	JButton One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Zero;
	JButton Equals, Plus, Minus, Multiply, Divide;
	JButton open_par, close_par, decimalPt, plusminus, exp, mod, delete;
	JButton Clear, Delete, ClearRecent;
	JButton A, B, C, D, E , F;
	JButton xor, or, and, rsh, lsh, not;
	
	JTextField Expression, Answer;
	JTextField Answer_Bin, Answer_Oct, Answer_Hex, Answer_Dec;
	
	JRadioButton HexMode, Lights, Byte, Word, QWord, DWord;
	
	boolean hexMode= false;
	
	public Calculator(){
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.gray);
		frame.setBounds(100, 100, 620, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gridBagLayout);
	
		JLabel programmer_label = new JLabel("Programmer");
		programmer_label.setFont(new Font("Times New Roman", Font.BOLD, 16));
		GridBagConstraints gridBagprogrammer_label = new GridBagConstraints();
		gridBagprogrammer_label.gridx = 2;
		gridBagprogrammer_label.gridy = 0;
		frame.getContentPane().add(programmer_label, gridBagprogrammer_label);
		
		
		//**************** 1-9 **************************
		One = new JButton("1");
		GridBagConstraints gridBagOne= new GridBagConstraints();
		gridBagOne.gridx = 2;
		gridBagOne.gridy = 8;
		frame.getContentPane().add(One, gridBagOne);
		
		Two = new JButton("2");
		GridBagConstraints gridBagTwo = new GridBagConstraints();
		gridBagTwo.gridx= 3;
		gridBagTwo.gridy = 8;
		frame.getContentPane().add(Two, gridBagTwo);
		
		Three = new JButton("3");
		GridBagConstraints gridBagThree = new GridBagConstraints();
		gridBagThree.gridx = 4;
		gridBagThree.gridy= 8;
		frame.getContentPane().add(Three, gridBagThree);
		
		Four = new JButton("4");
		GridBagConstraints gridBagFour = new GridBagConstraints();
		gridBagFour.gridx = 2;
		gridBagFour.gridy = 9;
		frame.getContentPane().add(Four, gridBagFour);
		
		Five= new JButton("5");
		GridBagConstraints gridBagFive= new GridBagConstraints();
		gridBagFive.gridx = 3;
		gridBagFive.gridy= 9;
		frame.getContentPane().add(Five, gridBagFive);
		
		Six= new JButton("6");
		GridBagConstraints gridBagSix= new GridBagConstraints();
		gridBagSix.gridx= 4;
		gridBagSix.gridy = 9;
		frame.getContentPane().add(Six, gridBagSix);
		
		Seven= new JButton("7");
		GridBagConstraints gridBagSeven= new GridBagConstraints();
		gridBagSeven.gridx= 2;
		gridBagSeven.gridy = 10 ;
		frame.getContentPane().add(Seven, gridBagSeven);
		
		Eight = new JButton("8");
		GridBagConstraints gridBagEight = new GridBagConstraints();
		gridBagEight.gridx = 3;
		gridBagEight.gridy= 10 ;
		frame.getContentPane().add(Eight, gridBagEight);
		
		Nine= new JButton("9");
		GridBagConstraints gridBagNine= new GridBagConstraints();
		gridBagNine.gridx = 4;
		gridBagNine.gridy = 10 ;
		frame.getContentPane().add(Nine, gridBagNine);
		
		Zero = new JButton("0");
		GridBagConstraints gridBagZero = new GridBagConstraints();
		gridBagZero.gridx= 3;
		gridBagZero.gridy= 11;
		frame.getContentPane().add(Zero, gridBagZero);
		
		//***************  Conversions and Labels ***************************
		JLabel hex_label = new JLabel("HEX");
		GridBagConstraints gridBaghex_label = new GridBagConstraints();
		gridBaghex_label.gridx = 0;
		gridBaghex_label.gridy = 0;
		frame.getContentPane().add(hex_label, gridBaghex_label);
		
		Answer_Hex = new JTextField();
		GridBagConstraints gridBagAnswer_Hex= new GridBagConstraints();
		gridBagAnswer_Hex.fill = GridBagConstraints.HORIZONTAL;
		gridBagAnswer_Hex.gridx = 1;
		gridBagAnswer_Hex.gridy = 0;
		frame.getContentPane().add(Answer_Hex, gridBagAnswer_Hex);
		Answer_Hex.setColumns(10);
		
		JLabel dec_label = new JLabel("DEC");
		GridBagConstraints gridBagdec_label = new GridBagConstraints();
		gridBagdec_label.gridx = 0;
		gridBagdec_label.gridy = 1;
		frame.getContentPane().add(dec_label, gridBagdec_label);
		
		Answer_Dec = new JTextField();
		GridBagConstraints gridBagAnswer_Dec = new GridBagConstraints();
		gridBagAnswer_Dec.fill = GridBagConstraints.HORIZONTAL;
		gridBagAnswer_Dec.gridx = 1;
		gridBagAnswer_Dec.gridy= 1;
		frame.getContentPane().add(Answer_Dec, gridBagAnswer_Dec);
		Answer_Dec.setColumns(10);
		
		Expression= new JTextField();
		Expression.setText("");
		GridBagConstraints gridBagExpression= new GridBagConstraints();
		gridBagExpression.gridwidth = 3;
		gridBagExpression.fill = GridBagConstraints.HORIZONTAL;
		gridBagExpression.gridx = 3;
		gridBagExpression.gridy = 1;
		frame.getContentPane().add(Expression, gridBagExpression);
		Expression.setColumns(10);
		
		JLabel oct_label = new JLabel("OCT");
		GridBagConstraints gridBagoct_label = new GridBagConstraints();
		gridBagoct_label.gridx = 0;
		gridBagoct_label.gridy = 2;
		frame.getContentPane().add(oct_label, gridBagoct_label);
		
		Answer_Oct= new JTextField();
		GridBagConstraints gridBagAnswer_Oct= new GridBagConstraints();
		gridBagAnswer_Oct.fill= GridBagConstraints.HORIZONTAL;
		gridBagAnswer_Oct.gridx= 1;
		gridBagAnswer_Oct.gridy = 2;
		frame.getContentPane().add(Answer_Oct, gridBagAnswer_Oct);
		Answer_Oct.setColumns(10);
		
		JLabel bin_label = new JLabel("BIN");
		GridBagConstraints gridBagbin_label = new GridBagConstraints();
		gridBagbin_label.gridx = 0;
		gridBagbin_label.gridy = 3;
		frame.getContentPane().add(bin_label, gridBagbin_label);
		
		Answer_Bin = new JTextField();
		GridBagConstraints gridBagAnswer_Bin = new GridBagConstraints();
		gridBagAnswer_Bin.fill = GridBagConstraints.HORIZONTAL;
		gridBagAnswer_Bin.gridx = 1;
		gridBagAnswer_Bin.gridy = 3;
		frame.getContentPane().add(Answer_Bin, gridBagAnswer_Bin);
		Answer_Bin.setColumns(10);
		
		Answer = new JTextField();
		GridBagConstraints gridBagtxtAns = new GridBagConstraints();
		gridBagtxtAns.gridheight= 2;
		gridBagtxtAns.gridwidth= 4;
		gridBagtxtAns.fill= GridBagConstraints.HORIZONTAL;
		gridBagtxtAns.gridx = 2;
		gridBagtxtAns.gridy = 2;
		frame.getContentPane().add(Answer, gridBagtxtAns);
		Answer.setColumns(1);
		
		//***************  Byte/Word, etc. ***************************
		Byte = new JRadioButton("Byte");
		GridBagConstraints gridBagByte = new GridBagConstraints();
		gridBagByte.gridx = 2;
		gridBagByte.gridy= 5;
		frame.getContentPane().add(Byte, gridBagByte);
		
		Word= new JRadioButton("Word");
		GridBagConstraints gridBagWord = new GridBagConstraints();
		gridBagWord.gridx = 3;
		gridBagWord.gridy = 5;
		frame.getContentPane().add(Word, gridBagWord);
		
		QWord = new JRadioButton("QWord");
		GridBagConstraints gridBagQword= new GridBagConstraints();
		gridBagQword.gridx= 4;
		gridBagQword.gridy = 5;
		frame.getContentPane().add(QWord, gridBagQword);
		
		DWord = new JRadioButton("DWord");
		GridBagConstraints gridBagDword = new GridBagConstraints();
		gridBagDword.gridx =5;
		gridBagDword.gridy = 5;
		frame.getContentPane().add(DWord, gridBagDword);
		
		//***************  System Operations ***************************
		Lights=new JRadioButton("Lights");
		GridBagConstraints gridBagLight= new GridBagConstraints();
		gridBagLight.gridx=5;
		gridBagLight.gridy = 0;
		frame.getContentPane().add(Lights, gridBagLight);
		
		ClearRecent = new JButton("CE");
		GridBagConstraints gridBagClearRecent = new GridBagConstraints();
		gridBagClearRecent.gridx = 2;
		gridBagClearRecent.gridy =7;
		frame.getContentPane().add(ClearRecent, gridBagClearRecent);
		
		Clear= new JButton("C");
		GridBagConstraints gridBagClear= new GridBagConstraints();
		gridBagClear.gridx= 3;
		gridBagClear.gridy = 7;
		frame.getContentPane().add(Clear, gridBagClear);
		
		Delete = new JButton("DEL");
		GridBagConstraints gridBagDelete = new GridBagConstraints();
		gridBagDelete.gridx = 4;
		gridBagDelete.gridy = 7;
		frame.getContentPane().add(Delete, gridBagDelete);
		
		HexMode= new JRadioButton("Hex Mode");
		GridBagConstraints gridBagHexMode= new GridBagConstraints();
		gridBagHexMode.gridx = 0;
		gridBagHexMode.gridy = 5;
		frame.getContentPane().add(HexMode, gridBagHexMode);

		//***************  4 Operations ***************************
		Plus = new JButton("+");
		GridBagConstraints gridBagPlus = new GridBagConstraints();
		gridBagPlus.gridx= 5;
		gridBagPlus.gridy= 10 ;
		frame.getContentPane().add(Plus, gridBagPlus);
		
		Minus = new JButton("-");
		GridBagConstraints gridBagMinus = new GridBagConstraints();
		gridBagMinus.gridx = 5;
		gridBagMinus.gridy = 9;
		frame.getContentPane().add(Minus, gridBagMinus);
		
		Multiply= new JButton("x");
		GridBagConstraints gridBagMultiply= new GridBagConstraints();
		gridBagMultiply.gridx = 5;
		gridBagMultiply.gridy = 8;
		frame.getContentPane().add(Multiply, gridBagMultiply);
		
		Divide = new JButton("/");
		GridBagConstraints gridBagDivide = new GridBagConstraints();
		gridBagDivide.gridx = 5;
		gridBagDivide.gridy= 7;
		frame.getContentPane().add(Divide, gridBagDivide);
		
		//***************  A-F ***************************
		A= new JButton("A");
		GridBagConstraints gridBagA= new GridBagConstraints();
		gridBagA.gridx = 0;
		gridBagA.gridy = 8;
		frame.getContentPane().add(A, gridBagA);
		
		B = new JButton("B");
		GridBagConstraints gridBagB = new GridBagConstraints();
		gridBagB.gridx = 1;
		gridBagB.gridy = 8;
		frame.getContentPane().add(B, gridBagB);
		
		C = new JButton("C");
		GridBagConstraints gridBagC = new GridBagConstraints();
		gridBagC.gridx = 0;
		gridBagC.gridy = 9;
		frame.getContentPane().add(C, gridBagC);
		
		D = new JButton("D");
		GridBagConstraints gridBagD= new GridBagConstraints();
		gridBagD.gridx= 1;
		gridBagD.gridy = 9;
		frame.getContentPane().add(D, gridBagD);
		
		E = new JButton("E");
		GridBagConstraints gridBagE = new GridBagConstraints();
		gridBagE.gridx = 0;
		gridBagE.gridy = 10 ;
		frame.getContentPane().add(E, gridBagE);
		
		F = new JButton("F");
		GridBagConstraints gridBagF= new GridBagConstraints();
		gridBagF.gridx = 1;
		gridBagF.gridy = 10 ;
		frame.getContentPane().add(F, gridBagF);
		
		//***************  Advanced Functions ***************************
		open_par = new JButton("(");
		GridBagConstraints gridBagopen_par= new GridBagConstraints();
		gridBagopen_par.gridx = 0;
		gridBagopen_par.gridy = 11;
		frame.getContentPane().add(open_par, gridBagopen_par);
		
		close_par = new JButton(")");
		GridBagConstraints gridBagclose_par = new GridBagConstraints();
		gridBagclose_par.gridx = 1;
		gridBagclose_par.gridy= 11;
		frame.getContentPane().add(close_par, gridBagclose_par);
		
		plusminus = new JButton("+/-");
		GridBagConstraints gridBagplusminus = new GridBagConstraints();
		gridBagplusminus.gridx = 2;
		gridBagplusminus.gridy = 11;
		frame.getContentPane().add(plusminus, gridBagplusminus);
		
		
		decimalPt= new JButton(".");
		GridBagConstraints gridBagdecimalPt = new GridBagConstraints();
		gridBagdecimalPt.gridx = 4;
		gridBagdecimalPt.gridy = 11;
		frame.getContentPane().add(decimalPt, gridBagdecimalPt);
		
		exp = new JButton("^");
		GridBagConstraints gridBagexp = new GridBagConstraints();
		gridBagexp.gridx= 0;
		gridBagexp.gridy = 7;
		frame.getContentPane().add(exp, gridBagexp);
		
		mod = new JButton("%");
		GridBagConstraints gridBagmod = new GridBagConstraints();
		gridBagmod.gridx = 1;
		gridBagmod.gridy = 7;
		frame.getContentPane().add(mod, gridBagmod);
		
		Equals = new JButton("=");
		GridBagConstraints gridBagEquals = new GridBagConstraints();
		gridBagEquals.gridx = 5;
		gridBagEquals.gridy = 11;
		frame.getContentPane().add(Equals, gridBagEquals);
		
		
		//***************  Defunct Buttons ***************************
		lsh = new JButton("Lsh");
		GridBagConstraints gridBaglsh = new GridBagConstraints();
		gridBaglsh.gridx = 0;
		gridBaglsh.gridy = 6;
		frame.getContentPane().add(lsh, gridBaglsh);
		
		rsh = new JButton("Rsh");
		GridBagConstraints gridBagrsh = new GridBagConstraints();
		gridBagrsh.gridx = 1;
		gridBagrsh.gridy= 6;
		frame.getContentPane().add(rsh, gridBagrsh);
		
		or = new JButton("Or");
		GridBagConstraints gridBagor = new GridBagConstraints();
		gridBagor.gridx= 2;
		gridBagor.gridy = 6;
		frame.getContentPane().add(or, gridBagor);
		
		xor = new JButton("Xor");
		GridBagConstraints gridBagxor= new GridBagConstraints();
		gridBagxor.gridx = 3;
		gridBagxor.gridy = 6;
		frame.getContentPane().add(xor, gridBagxor);
		
		not = new JButton("Not");
		GridBagConstraints gridBagnot = new GridBagConstraints();
		gridBagnot.gridx = 4;
		gridBagnot.gridy = 6;
		frame.getContentPane().add(not, gridBagnot);
		
		and = new JButton("And");
		GridBagConstraints gridBagand = new GridBagConstraints();
		gridBagand.gridx= 5;
		gridBagand.gridy = 6;
		frame.getContentPane().add(and, gridBagand);
		
		
		//Begin adding action listeners
		One.addActionListener(this);
        Two.addActionListener(this);
        Three.addActionListener(this);
        Four.addActionListener(this);
        Five.addActionListener(this);
        Six.addActionListener(this);
        Seven.addActionListener(this);
        Eight.addActionListener(this);
        Nine.addActionListener(this);
        Zero.addActionListener(this);
        
        A.addActionListener(this);
        B.addActionListener(this);
        C.addActionListener(this);
        D.addActionListener(this);
        E.addActionListener(this);
        F.addActionListener(this);
        
        Plus.addActionListener(this);
        Minus.addActionListener(this);
        Multiply.addActionListener(this);
        Divide.addActionListener(this);
        
        Clear.addActionListener(this);
        ClearRecent.addActionListener(this);
        Equals.addActionListener(this);
        Delete.addActionListener(this);
		
        open_par.addActionListener(this);
        close_par.addActionListener(this);
        decimalPt.addActionListener(this);
        plusminus.addActionListener(this);
        exp.addActionListener(this);
        mod.addActionListener(this);
        
        Lights.addActionListener(this);
        HexMode.addActionListener(this);
        
        //we start in Dec Mode, lets turn the A-F buttons off.
    	A.setEnabled(false);
		B.setEnabled(false);
		C.setEnabled(false);
		D.setEnabled(false);
		E.setEnabled(false);
		F.setEnabled(false);
       
        //Answer.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        //Expression.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        Font bigsizefont= new Font("Times New Roman", Font.BOLD, 24);
		Answer.setFont(bigsizefont);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == Lights) {
			if(Lights.isSelected()) {
				frame.getContentPane().setBackground(Color.white);
			}
			else {
				frame.getContentPane().setBackground(Color.gray);
			}
		}
		else if(e.getSource() == HexMode) {
			if(HexMode.isSelected()) {
				JOptionPane.showMessageDialog(null, "In hex mode, you can now use the A-F keys.\nThe input and results are all hex numbers.");
				
				A.setEnabled(true);
				B.setEnabled(true);
				C.setEnabled(true);
				D.setEnabled(true);
				E.setEnabled(true);
				F.setEnabled(true);
			}
			else {
				A.setEnabled(false);
				B.setEnabled(false);
				C.setEnabled(false);
				D.setEnabled(false);
				E.setEnabled(false);
				F.setEnabled(false);
			}
		}
		else if(e.getSource()==One) {
			if(Expression.getText().equals("")) {
				Answer.setText("1");
				Expression.setText(Expression.getText() + "1");
			}
			else if(isLastCharOperation(Expression.getText())) {
				Answer.setText("1");
				Expression.setText(Expression.getText() + "1");
			}
			else {
				Answer.setText(Answer.getText()+ "1");
				Expression.setText(Expression.getText() + "1");
			}
		}
		else if(e.getSource()==Two) {
			if(Expression.getText().equals("")) {
				Answer.setText("2");
				Expression.setText(Expression.getText() + "2");
			}
			else if(isLastCharOperation(Expression.getText())) {
				Answer.setText("2");
				Expression.setText(Expression.getText() + "2");
			}
			else {
				Answer.setText(Answer.getText()+ "2");
				Expression.setText(Expression.getText() + "2");
			}
		}
		else if(e.getSource()==Three) {
			if(Expression.getText().equals("")) {
				Answer.setText("3");
				Expression.setText(Expression.getText() + "3");
			}
			else if(isLastCharOperation(Expression.getText())) {
				Answer.setText("3");
				Expression.setText(Expression.getText() + "3");
			}
			else {
				Answer.setText(Answer.getText()+ "3");
				Expression.setText(Expression.getText() + "3");
			}
		}
		else if(e.getSource()==Four) {
			if(Expression.getText().equals("")) {
				Answer.setText("4");
				Expression.setText(Expression.getText() + "4");
			}
			else if(isLastCharOperation(Expression.getText())) {
				Answer.setText("4");
				Expression.setText(Expression.getText() + "4");
			}
			else {
				Answer.setText(Answer.getText()+ "4");
				Expression.setText(Expression.getText() + "4");
			}
		}
		else if(e.getSource()==Five) {
			if(Expression.getText().equals("")) {
				Answer.setText("5");
				Expression.setText(Expression.getText() + "5");
			}
			else if(isLastCharOperation(Expression.getText())) {
				Answer.setText("5");
				Expression.setText(Expression.getText() + "5");
			}
			else{
				Answer.setText(Answer.getText()+ "5");
				Expression.setText(Expression.getText() + "5");
			}
		}
		else if(e.getSource()==Six) {
			if(Expression.getText().equals("")) {
				Answer.setText("6");
				Expression.setText(Expression.getText() + "6");
			}
			else if(isLastCharOperation(Expression.getText())) {
				Answer.setText("6");
				Expression.setText(Expression.getText() + "6");
			}
			else {
				Answer.setText(Answer.getText()+ "6");
				Expression.setText(Expression.getText() + "6");
			}
		}
		else if(e.getSource()==Seven) {
			if(Expression.getText().equals("")) {
				Answer.setText("7");
				Expression.setText(Expression.getText() + "7");
			}
			else if(isLastCharOperation(Expression.getText())) {
				Answer.setText("7");
				Expression.setText(Expression.getText() + "7");
			}
			else {
				Answer.setText(Answer.getText()+ "7");
				Expression.setText(Expression.getText() + "7");
			}
		}
		else if(e.getSource()==Eight) {
			if(Expression.getText().equals("")) {
				Answer.setText("8");
				Expression.setText(Expression.getText() + "8");
			}
			else if(isLastCharOperation(Expression.getText())) {
				Answer.setText("8");
				Expression.setText(Expression.getText() + "8");
			}
			else {
				Answer.setText(Answer.getText()+ "8");
				Expression.setText(Expression.getText() + "8");
			}
		}
		else if(e.getSource()==Nine) {
			if(Expression.getText().equals("")) {
				Answer.setText("9");
				Expression.setText(Expression.getText() + "9");
			}
			else if(isLastCharOperation(Expression.getText())) {
				Answer.setText("9");
				Expression.setText(Expression.getText() + "9");
			}
			else {
				Answer.setText(Answer.getText()+ "9");
				Expression.setText(Expression.getText() + "9");
			}
		}
		else if(e.getSource()==Zero) {
			//if there is no expression or the last character of the expression string is a character
			if(Expression.getText().equals("")) {
				Answer.setText("0");
				Expression.setText(Expression.getText() + "0");
			}
			else if(isLastCharOperation(Expression.getText())) {
				Answer.setText("0");
				Expression.setText(Expression.getText() + "0");
			}
			else {
				Answer.setText(Answer.getText()+ "0");
				Expression.setText(Expression.getText() + "0");
			}
		}
		/*else if(e.getSource()==A) {
			//if there is no expression or the last character of the expression string is a character
			if(Expression.getText().equals("")) {
				Answer.setText("A");
				Expression.setText(Expression.getText() + "A");
			}
			else if(isLastCharOperation(Expression.getText())) {
				Answer.setText("A");
				Expression.setText(Expression.getText() + "A");
			}
			else {
				Answer.setText(Answer.getText()+ "A");
				Expression.setText(Expression.getText() + "A");
			}
		}
		else if(e.getSource()==B) {
			//if there is no expression or the last character of the expression string is a character
			if(Expression.getText().equals("")) {
				Answer.setText("B");
				Expression.setText(Expression.getText() + "B");
			}
			else if(isLastCharOperation(Expression.getText())) {
				Answer.setText("B");
				Expression.setText(Expression.getText() + "B");
			}
			else {
				Answer.setText(Answer.getText()+ "B");
				Expression.setText(Expression.getText() + "B");
			}
		}
		else if(e.getSource()==C) {
			//if there is no expression or the last character of the expression string is a character
			if(Expression.getText().equals("")) {
				Answer.setText("C");
				Expression.setText(Expression.getText() + "C");
			}
			else if(isLastCharOperation(Expression.getText())) {
				Answer.setText("C");
				Expression.setText(Expression.getText() + "C");
			}
			else {
				Answer.setText(Answer.getText()+ "C");
				Expression.setText(Expression.getText() + "C");
			}
		}
		else if(e.getSource()==D) {
			//if there is no expression or the last character of the expression string is a character
			if(Expression.getText().equals("")) {
				Answer.setText("D");
				Expression.setText(Expression.getText() + "D");
			}
			else if(isLastCharOperation(Expression.getText())) {
				Answer.setText("D");
				Expression.setText(Expression.getText() + "D");
			}
			else {
				Answer.setText(Answer.getText()+ "D");
				Expression.setText(Expression.getText() + "D");
			}
		}
		else if(e.getSource()==E) {
			//if there is no expression or the last character of the expression string is a character
			if(Expression.getText().equals("")) {
				Answer.setText("E");
				Expression.setText(Expression.getText() + "E");
			}
			else if(isLastCharOperation(Expression.getText())) {
				Answer.setText("E");
				Expression.setText(Expression.getText() + "E");
			}
			else {
				Answer.setText(Answer.getText()+ "E");
				Expression.setText(Expression.getText() + "E");
			}
		}
		else if(e.getSource()==F) {
			//if there is no expression or the last character of the expression string is a character
			if(Expression.getText().equals("")) {
				Answer.setText("F");
				Expression.setText(Expression.getText() + "F");
			}
			else if(isLastCharOperation(Expression.getText())) {
				Answer.setText("F");
				Expression.setText(Expression.getText() + "F");
			}
			else {
				Answer.setText(Answer.getText()+ "F");
				Expression.setText(Expression.getText() + "F");
			}
		}*/
		else if(e.getSource()== Plus){
			//System.out.println("Answer: " + Answer.getText());
			//System.out.println("Expression: " + Expression.getText());
			if(Answer.getText().equals("") && Expression.getText().equals("")) {
				
			}
			else if(isLastCharOperation(Expression.getText())){
				 Expression.setText(Expression.getText().substring(0, Expression.getText().length() - 1) + "+");
			}
			else if(Expression.getText().equals("") && !Answer.getText().equals("")) {
				Expression.setText(Answer.getText() + "+");
			}
			else {
				Expression.setText(Expression.getText() + "+");
			}
			//Expression.setText("+");
		}
		else if(e.getSource()== Minus){
			if(Answer.getText().equals("") && Expression.getText().equals("")) {
				
			}
			else if(isLastCharOperation(Expression.getText())){
				 Expression.setText(Expression.getText().substring(0, Expression.getText().length() - 1) + "-");
			}
			else if(Expression.getText().equals("") && !Answer.getText().equals("")) {
				Expression.setText(Answer.getText() + "-");
			}
			else {
				Expression.setText(Expression.getText() + "-");
			}
			//Expression.setText("-");
		}
		else if(e.getSource()== Multiply){
			//operation cannot be first in expression
			if(Answer.getText().equals("") && Expression.getText().equals("")) {
				
			}
			else if(isLastCharOperation(Expression.getText())){
				 Expression.setText(Expression.getText().substring(0, Expression.getText().length() - 1) + "*");
			}
			else if(Expression.getText().equals("") && !Answer.getText().equals("")) {
				Expression.setText(Answer.getText() + "*");
			}
			else {
				Expression.setText(Expression.getText() + "*");
			}
			//Expression.setText("x");
		}
		else if(e.getSource()== Divide){
			if(Answer.getText().equals("") && Expression.getText().equals("")) {
				
			}
			else if(isLastCharOperation(Expression.getText())){
				 Expression.setText(Expression.getText().substring(0, Expression.getText().length() - 1) + "/");
			}
			else if(Expression.getText().equals("") && !Answer.getText().equals("")) {
				Expression.setText(Answer.getText() + "/");
			}
			else {
				Expression.setText(Expression.getText() + "/");
			}
			//Expression.setText("/");
		}
		else if(e.getSource()== Delete){
			if( ! Expression.getText().equals("")) {
				//substring expression and answer
				Expression.setText(Expression.getText().substring(0,Expression.getText().length() -1));
				if( ! Answer.getText().equals("")) {
					Answer.setText(Answer.getText().substring(0,Answer.getText().length() -1));
				}
			}
		}
		else if(e.getSource()== ClearRecent){
			
			if(!Expression.getText().equals("")) {
				String s= Answer.getText();
				//System.out.print("s: " + s);
				Answer.setText("");
				
				//subtract s from Expression
				Expression.setText(Expression.getText().substring(0,Expression.getText().length()-s.length()));
			}
			else {
				Answer.setText("");
			}
		}
		else if(e.getSource()== Equals){
			if(Expression.getText().equals("") && !Answer.getText().equals("")){
				
			}
			else {
				String s= Expression.getText();
				if(!isLastCharOperation(s)) {
					//System.out.println("Test");
					String result = "";
					
					for (int i = 0; i < s.length(); i++) {
				      if (s.charAt(i) == '(' || s.charAt(i) == ')' || 
				          s.charAt(i) == '+' || s.charAt(i) == '-' ||
				          s.charAt(i) == '*' || s.charAt(i) == '/' ||
				          s.charAt(i) == '^' || s.charAt(i) == '%'){
				          result += " " + s.charAt(i) + " ";
				       }
				      else{
				        result += s.charAt(i);
				       }
				    }
					int countOfOpenPars =0; 
					int countOfClosePars=0;
				    System.out.println("result" + result);
				    for(int i=0; i< Expression.getText().length(); i++) {
				    	   if(result.charAt(i) == '(') {
				    		   countOfOpenPars++;
				    	   }
				       }
			       for(int i=0; i< Expression.getText().length(); i++) {
			    	   if(Expression.getText().charAt(i) == ')') {
			    		   countOfClosePars++;
			    	   }
			       }
				    
			       if(countOfClosePars != countOfOpenPars) {
			    	   Expression.setText("Press C to Try a New Calculation...");
			    	   Answer.setText("ERROR, mismatched parentheses");
			       }
				   try {
				    	double d=compute(result);
				    	
				    	System.out.println(d);
						Answer.setText(d+ "");
				   }
				   catch(Exception e1) {
				    	Expression.setText("Press C to Try a New Calculation...");
						Answer.setText("ERROR");
				   }
				    
				    //We now want to convert the answer into the various formats. 
					double theanswer= Double.parseDouble(Answer.getText());
					
					if(theanswer < 0) {
						Answer_Hex.setText("-");
						Answer_Bin.setText("-");
						Answer_Dec.setText("-");
						Answer_Oct.setText("-");
						theanswer *= -1;
					}
					
					//1. Binary
					Answer_Bin.setText(whole2Bin((int) theanswer, ""));
					Answer_Bin.setText(Answer_Bin.getText()+ "." + parts2Bin(theanswer- (int) theanswer, ""));
				
				
					//2. Hex
					Answer_Hex.setText(whole2Hex((int) theanswer, ""));
					Answer_Hex.setText(Answer_Hex.getText()+ "." + parts2Hex(theanswer- (int) theanswer, ""));
					
					//3. Octal
					Answer_Oct.setText(whole2Oct((int) theanswer, ""));
					Answer_Oct.setText(Answer_Oct.getText()+ "." + parts2Oct(theanswer- (int) theanswer, ""));
					
					//4. Dec
					Answer_Dec.setText(theanswer+"");
					Expression.setText("");
				}
			}
		}
		else if(e.getSource()== open_par){
			if(Expression.getText().equals("") && Answer.getText().equals("")) {
				Expression.setText(Expression.getText() + "(");
				Answer.setText("");
			}
			else if(Expression.getText().equals("") && !Answer.getText().equals("")) {
				if(isLastCharOperation(Expression.getText())) {
					Expression.setText(Answer.getText() + "(");
					Answer.setText("");
				}
				
			}
			else {
				if(isLastCharOperation(Expression.getText())) {
					Expression.setText(Expression.getText() + "(");
					Answer.setText("");
				}
				//i.e. this would change the 2(5+6) expression to be 2*(5+6) 
				else {
					Expression.setText(Expression.getText() + "*(");
					Answer.setText("");
				}
				
			}
		}
		else if(e.getSource()== close_par){
			//get number of open parantheses
			int countOfOpenPars = 0;
	        
	       for(int i=0; i< Expression.getText().length(); i++) {
	    	   if(Expression.getText().charAt(i) == '(') {
	    		   countOfOpenPars++;
	    	   }
	       }
	       int countOfClosePars = 0;
	        
	       for(int i=0; i< Expression.getText().length(); i++) {
	    	   if(Expression.getText().charAt(i) == ')') {
	    		   countOfClosePars++;
	    	   }
	       }
	       
	       
			
			if(Answer.getText().equals("") && Expression.getText().equals("")) {
				
			}
			else if(countOfClosePars + 1 > countOfOpenPars) {
		    	   //System.out.print("gottem");
		    	   
		    }
			else if(isLastCharOperation(Expression.getText())){
				 Expression.setText(Expression.getText().substring(0, Expression.getText().length() - 1) + ")");
			}
			else if(Expression.getText().equals("") && !Answer.getText().equals("")) {
				Expression.setText(Expression.getText() + ")");
				
			}
			else {
				Expression.setText(Expression.getText() + ")");
			}
		}
		else if(e.getSource()== decimalPt){
			if(Answer.getText().equals("") && Expression.getText().equals("")) {
				Expression.setText("0.");
				 Answer.setText("0.");
			}
			else if(Expression.getText().endsWith("(") || Expression.getText().endsWith(")") || Expression.getText().endsWith(".")) {
				
			}
			else if(isLastCharOperation(Expression.getText())){
				 Expression.setText(Expression.getText() + "0.");
				 Answer.setText("0.");
			}
			else if(Expression.getText().equals("") && !Answer.getText().equals("")) {
				Expression.setText(Answer.getText() + ".");
			}
			else {
				Expression.setText(Expression.getText() + ".");
				Answer.setText(Answer.getText() + ".");
			}
		}
		else if(e.getSource() == plusminus) {
			//int temp = Answer.getText().length();
			
			//String construct= Expression.getText().substring( Expression.getText().length - temp, Expression.getText().length() -1);
			//Expression.setText( construct);
			//must change 
			if(! isLastCharOperation(Expression.getText())) {
				Answer.setText("-" + Answer.getText());
				Expression.setText(Expression.getText() + "*(0-1)");
			}
		}
		else if(e.getSource() == exp) {
			if(Answer.getText().equals("") && Expression.getText().equals("")) {
				
			}
			else if(isLastCharOperation(Expression.getText())){
				 Expression.setText(Expression.getText().substring(0, Expression.getText().length() - 1) + "^");
			}
			else if(Expression.getText().equals("") && !Answer.getText().equals("")) {
				Expression.setText(Answer.getText() + "^");
			}
			else {
				Expression.setText(Expression.getText() + "^");
			}
		}
		else if(e.getSource() == mod) {
			if(Answer.getText().equals("") && Expression.getText().equals("")) {
				
			}
			else if(isLastCharOperation(Expression.getText())) {
					Expression.setText(Expression.getText().substring(0, Expression.getText().length() - 1) + "%");
			}
			else if(Expression.getText().equals("") && !Answer.getText().equals("")) {
				Expression.setText(Answer.getText() + "%");
			}
			else {
				Expression.setText(Expression.getText() + "%");
			}
		}
		else if(e.getSource()== Clear){
			Expression.setText("");
			Answer.setText("");
			Answer_Bin.setText("");
			Answer_Hex.setText("");
			Answer_Dec.setText("");
			Answer_Oct.setText("");
			Answer.setText("");
		}
	}
	
	public static double compute(String expression) {
		//double answer=0.0;
		Stack<Double> numberStack = new Stack<Double>();
		Stack<Character> operatorStack = new Stack<Character>();
		
		//System.out.print("expression:" +expression);
		String[] exparray= expression.split(" ");
		
		System.out.print("You entered:");
		for(String token: exparray){
			System.out.println(token);
			if(token.length() == 0){
				//System.out.print("...");
				continue;
			}
			else if(token.charAt(0) == '+' || token.charAt(0) == '-'){
				while( (!operatorStack.isEmpty()) && 
					(operatorStack.peek() == '+' || operatorStack.peek() == '-' || operatorStack.peek() == '*' ||  operatorStack.peek() == '/' || operatorStack.peek() == '^') ){
					pop2(numberStack, operatorStack);
				}
				operatorStack.push(token.charAt(0));
			}
			else if(token.charAt(0) == '*' || token.charAt(0) == '/' || token.charAt(0) == '%'){
				while( (!operatorStack.isEmpty()) && 
					(operatorStack.peek() == '*' ||  operatorStack.peek() == '/' ||operatorStack.peek() == '%' )){
					pop2(numberStack, operatorStack);
				}
				operatorStack.push(token.charAt(0));
			}
			else if(token.charAt(0) == '^'){
				while( (!operatorStack.isEmpty()) && operatorStack.peek() == '^') {
					pop2(numberStack, operatorStack);
				}
				operatorStack.push(token.charAt(0));
			}
			else if(token.charAt(0) == '('){
				operatorStack.push('(');
			}
			else if(token.charAt(0) == ')'){
				while(operatorStack.peek() != '('){
					pop2(numberStack, operatorStack);
				}
				operatorStack.pop();
			}
			else{
				double token_as_double=Double.parseDouble(token);
				numberStack.push(token_as_double);
			}
		}

		//System.out.println("Size of numberStack: " + numberStack.size());
		//System.out.println("Size of operatorStack: " + operatorStack.size());
		while(!operatorStack.isEmpty()){
			pop2(numberStack, operatorStack);
		}

		return numberStack.pop();
	}
	
	public static double pop2(Stack <Double> numberStack, Stack <Character> operatorStack){
		char tempoperation= operatorStack.pop();
		double tempnumber1= numberStack.pop();
		double tempnumber2= numberStack.pop();
		
		/*I sometimes get answers like 5+10-4 = 11.000000002 or someshit
		BigDecimal bd_1 = new BigDecimal(tempnumber1);
		BigDecimal bd_2 = new BigDecimal(tempnumber2);

		BigDecimal bd_result = new BigDecimal(0.0);*/
		double result=0.0;
		
		if(tempoperation == '*'){
			result += tempnumber2 * tempnumber1;
		}
		else if(tempoperation == '/'){
			
			if(tempnumber1 != 0) {
				result += tempnumber2 / tempnumber1;
			}
		
		} 
		else if(tempoperation == '+'){
			result += tempnumber1 + tempnumber2;
		}
		else if(tempoperation == '-'){
			result += tempnumber2 - tempnumber1;
		}
		else if(tempoperation == '^'){
			result += (Math.pow(tempnumber2, tempnumber1));
		}
		else if (tempoperation == '%') {
			result +=  (tempnumber2 % tempnumber1);
		}
		numberStack.push(result);

		return result;
	}
	
	public static boolean isLastCharOperation(String s) {
		
		if(s.endsWith("+") || s.endsWith("-") || s.endsWith("*") || s.endsWith("/") || s.endsWith("^") || s.endsWith("%")){
			//System.out.print(last_char + "is an operation");
			return true;
		}
		else {
			return false;
		}
	}

	public static String whole2Oct(int value, String tempoctresult) {
		//System.out.print("to convert: " + value);
		if (value / 8 == 0) { 
			return (value % 8) + tempoctresult;
		}
		else {
			//+= will cause the number to print in reverse order
			tempoctresult = ((value % 8) + tempoctresult);
			//System.out.println("result: " + result);
			//System.out.println("value: " + value);
			//System.out.println();
			return whole2Oct(value / 8, tempoctresult);
		}
	}
	
	public static String parts2Oct(double value, String tempoctresult) {
		BigDecimal bd_value= new BigDecimal(value+"");
		//System.out.println("initial value: " + bd_value);
		
		//some decimal values' conversions go on for a LONG time, so we stop at 5 places out
		for(int i=0; i< 5; i++) {
			BigDecimal bd_8 = new BigDecimal(8.0);
			bd_value= bd_value.multiply(bd_8);
			
			int a= (int) bd_value.doubleValue();
			//System.out.println("a: " + a);
			
			BigDecimal bd_subtract= new BigDecimal(a + "");
			
			bd_value=bd_value.subtract(bd_subtract);
			tempoctresult += (a+ "");
			
			if(bd_value.signum() == 0) {
				return tempoctresult;
			}
			//System.out.println("bd_value: " + bd_value);
			//System.out.println("");
		}
		//System.out.print(": " +tempresult);
		return tempoctresult;
	}
	
	public static String whole2Bin(int value, String tempresult) {
		//System.out.println("initial value: " + value);
		if (value / 2 == 0) { 
			return (value % 2) + tempresult;
		}
		else {
			//+= will cause the number to print in reverse order
			tempresult = ((value % 2) + tempresult);
			//System.out.println("result: " + result);
			//System.out.println("value: " + value);
			//System.out.println();
			return whole2Bin(value / 2, tempresult);
		}
	}
	
	public static String parts2Bin(double value, String tempresult) {
		BigDecimal bd_value= new BigDecimal(value+"");
		//System.out.println("initial value: " + bd_value);
		
		//some decimal values' conversions go on for a LONG time, so we stop at 5 places out
		for(int i=0; i< 5; i++) {
			BigDecimal bd_2 = new BigDecimal(2.0);
			bd_value= bd_value.multiply(bd_2);
			
			int a= (int) bd_value.doubleValue();
			//System.out.println("a: " + a);
			
			BigDecimal bd_subtract= new BigDecimal(a + "");
			
			bd_value=bd_value.subtract(bd_subtract);
			tempresult += (a+ "");
			
			if(bd_value.signum() == 0) {
				return tempresult;
			}
			//System.out.println("bd_value: " + bd_value);
			//System.out.println("");
		}
		//System.out.print(": " +tempresult);
		return tempresult;
	}
	
	public static String whole2Hex(int value, String tempresult) {
		if (value / 16 == 0) { 
			if(value % 16 == 10) {
				return ("A" + tempresult);
			}
			else if(value % 16 == 11) {
				return("B" + tempresult);
			}
			else if(value % 16 == 12) {
				return ("C" + tempresult);
			}
			else if(value % 16 == 13) {
				return ("D" + tempresult);
			}
			else if(value % 16 == 14) {
				return ("E" + tempresult);
			}
			else if (value % 16 == 15) {
				return ("F" + tempresult);
			}	
			//i.e. is a 0-9 digit
			else {
				return ((value % 16) + tempresult);
			}
		}
		else {
			//+= will cause the number to print in reverse order
			if(value % 16 == 10) {
				tempresult = ("A" + tempresult);
			}
			else if(value % 16 == 11) {
				tempresult = ("B" + tempresult);
			}
			else if(value % 16 == 12) {
				tempresult = ("C" + tempresult);
			}
			else if(value % 16 == 13) {
				tempresult = ("D" + tempresult);
			}
			else if(value % 16 == 14) {
				tempresult = ("E" + tempresult);
			}
			else if (value % 16 == 15) {
				tempresult = ("F" + tempresult);
			}	
			else {
				tempresult = ((value % 16) + tempresult);
			}
			//System.out.println("result: " + result);
			//System.out.println("value: " + value);
			//System.out.println();
			//recursion, cut off a digit using integer division
			return whole2Hex(value / 16 , tempresult);
		}
	}
	
	public static String parts2Hex(double value, String tempresult) {
		BigDecimal bd_value= new BigDecimal(value+"");
		//System.out.println("initial value: " + bd_value);
		
		//some decimal values' conversions go on for a LONG time, so we stop at 5 places out
		for(int i=0; i< 5; i++) {
			BigDecimal bd_16 = new BigDecimal(16.0);
			bd_value= bd_value.multiply(bd_16);
			
			int a= (int) bd_value.doubleValue();
			//System.out.println("a: " + a);
			
			BigDecimal bd_subtract= new BigDecimal(a + "");
			
			bd_value=bd_value.subtract(bd_subtract);
			if(a == 10) {
				tempresult += ("A"+ "");
			}
			else if(a == 11) {
				tempresult += ("B"+ "");
			}
			else if(a == 12) {
				tempresult += ("C"+ "");
			}
			else if(a == 13) {
				tempresult += ("D"+ "");
			}
			else if(a == 14) {
				tempresult += ("E"+ "");
			}
			else if (a == 15) {
				tempresult += ("F"+ "");
			}	
			else {
				tempresult += (a+ "");
			}
			
			
			if(bd_value.signum() == 0) {
				return tempresult;
			}
			//System.out.println("bd_value: " + bd_value);
			//System.out.println("");
		}
		//System.out.print(": " +tempresult);
		return tempresult;
	}
}
