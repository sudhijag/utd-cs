/*
   Cohort: Lone Wolf
   Assigment: ALU
   Software: iverilog
   Source: http://iverilog.icarus.com/
 */

//====================================================================
`define RESET           4'b0000 //0
`define ADD             4'b0001 //1
`define SUB             4'b0010 //2
`define AND             4'b0011 //3 
`define OR              4'b0100 //4
`define XOR             4'b0101 //5
`define NOT             4'b0110 //6
`define NAND            4'b0111 //7 
`define NOR             4'b1000 //8
`define XNOR            4'b1001 //9
`define NOOP            4'b1010 //10
//====================================================================

module Reg32(input clk, input [31:0] D, output reg [31:0] Q);
    always @(posedge clk)
        Q <= D;
endmodule

                                                                                    // V storedvalue on the circuit
module Selector(input[31:0] A, B, input errorIn, input [3:0] opcode, output reg [31:0] store, output reg errorOut);

reg [32:0] store2; 
wire c_out= store2 > 4294967295; //included to match with circuit
wire b_out= B>A; //included to match with circuit
always @(*) begin
        case(opcode) //SelectorBigMux
            `RESET:
                begin
                    store=0;
                    errorOut=0;
                end
            `ADD:
                begin
                    store2=A+B; //Adder "module"
                    errorOut=(c_out) ? 1:0;
                    if(c_out) //shown on circuit as c_out=1 b/c this code is behavioral
                        $display("ERROR in ADD: Overflow\nReset to Continue.");
                    else
                        store=store2; //SelectorTinyMux
                end
            `SUB:
                begin
                    store=b_out ? store:B-A; //Subtraction "module"
                    errorOut=b_out ? 1:0; //errorbit.Q
                    if (b_out) 
                        $display("ERROR in SUB: Negative Number.\nReset to Continue..");
                end
            `AND:
                begin
                    store=(errorIn) ? store:A&B; //SelectorTinyMux
                end
            `OR:
                begin
                    store=(errorIn) ? store:A|B;
                end
            `XOR:
                begin
                    store=(errorIn) ? store:A^B;
                end
            `NOT:
                begin
                     store=(errorIn) ? store:~A;
                end
            `NAND:
                begin
                     store=(errorIn) ? store:~(A&B);
                end
            `NOR:
                begin
                     store= (errorIn) ? store:~(A|B);
                end
            `XNOR:
                begin
                     store= (errorIn) ? store:~(A^B);
                end
            `NOOP:
                begin
                    store= store;
                end
        endcase
    end
endmodule

                                                                                        
module ALU(input clk, rst, input [31:0] A, B, input [3:0] opcode, output [31:0] C, output Error);
wire [31:0] store; //storedvalue.D, storevalue.Q
wire errorIn, errorOut; //errorbit.D, errorbit.Q

Selector selector(A,B, errorIn, opcode, store, errorOut);
Reg32 storedvalue(clk, store, C);

assign C= store;
assign errorIn=errorOut;
assign Error=errorOut;

endmodule



module testbench();
    reg clk;
    reg rst;
    reg [31:0] A;
    reg [31:0] B;
    wire [31:0] C;
    wire Error;

    reg [3:0] opcode;

ALU my_alu(clk,rst, A, B, opcode, C, Error);

//CLOCK
initial begin
    clk = 0;
    forever begin
        #5;
        clk = ~clk;
    end
end

 //OUTPUT
 initial begin //Start Output Thread
    $display("OPCODE GUIDE: RESET-0, ADD-1, SUB-2, AND-3, OR-4, XOR-5, NOT-6, NAND-7, NOR-8, XNOR-9, NOOP-10");
    $display("\tA\t\t\t\t\t\tB\t\t\t\t\tOpcode\t\t\t\t\tC\t\t\terror");

    forever
         begin
         $display("%b[%d]\t %b[%d]\t %b[%d]\t %b[%d]\t %b[%d]\t", A, A, B, B, opcode, opcode, C, C, Error, Error);
         #10;
         end
    end

 //STIMULUS
    initial begin;
    #4; 
    //---------------------------------
    opcode=`RESET; #10;                   
    //--------------------------------- //demonstrating error
    A=4294967295;
    B=2;
    opcode=`ADD;                        
    #10;
    //---------------------------------
    opcode=`NOOP;                       
    #10;
    //---------------------------------
    A=10;
    B=30;
    opcode=`AND;//result is not stored
    #10;
    //---------------------------------
    opcode=`NOOP; #10; opcode=`RESET; #10;
    //----------------------------------
    A=1234;
    B=4321;
    opcode=`ADD; 
    #10;
    //---------------------------------
    opcode=`NOOP; #10; opcode=`RESET; #10; 
    //---------------------------------
    A=13143;
    B=1212;
    opcode=`SUB;                   
    #10;
    //---------------------------------
    opcode=`NOOP; #10; opcode=`RESET; #10;
    //----------------------------------
    A=13823;
    B=192;
    opcode=`AND;                   
    #10;
    //---------------------------------
    opcode=`NOOP; #10; opcode=`RESET; #10;
    //----------------------------------
    A=13283;
    B=192;
    opcode=`OR;                   
    #10;
    //---------------------------------
    opcode=`NOOP; #10; opcode=`RESET; #10;
    //----------------------------------
    A=13203;
    B=421;
    opcode=`XOR;                   
    #10;
    //---------------------------------
    opcode=`NOOP; #10; opcode=`RESET; #10;
    //----------------------------------
    A=13716;
    opcode=`NOT;                   
    #10;
    //---------------------------------
    opcode=`NOOP; #10; opcode=`RESET; #10;
    //----------------------------------
    A=137216;
    B=42211;
    opcode=`NAND;                   
    #10;
    //---------------------------------
    opcode=`NOOP; #10; opcode=`RESET; #10;
    //----------------------------------
    A=156126;
    B=42921;
    opcode=`NOR;                   
    #10;
    //---------------------------------
    opcode=`NOOP; #10; opcode=`RESET; #10;
    //----------------------------------
    A=87126;
    B=42881;
    opcode=`XNOR;                   
    #10;
    //---------------------------------
    opcode=`NOOP; #10; opcode=`RESET; #10;
    //----------------------------------

    $finish;

    end
endmodule
