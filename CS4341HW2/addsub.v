module Add_half (input a, b,  output c_out, sum);
   xor G1(sum, a, b);	 
   and G2(c_out, a, b);
endmodule

module Add_full (input a, b, c_in, output c_out, sum);
   wire w1, w2, w3;	 
   Add_half M1 (a, b, w1, w2);
   Add_half M0 (w2, c_in, w3, sum);
   or (c_out, w1, w3);
endmodule

module testbench;
	reg[3:0] A;
	reg[3:0] B;
	reg M; //1 for subtraction, 0 for addition

	wire c_0, c_1, c_2, c_3, c_4;
    wire [3:0] xor_B; 
    wire [3:0] S;

    xor XOR_0(xor_B[0], M, B[0]);
    xor XOR_1(xor_B[1], M, B[1]);
    xor XOR_2(xor_B[2], M, B[2]);
    xor XOR_3(xor_B[3], M, B[3]);

    Add_full FA_0(A[0], xor_B[0], M, c_1, S[0]);
    Add_full FA_1(A[1], xor_B[1], c_1, c_2, S[1]);
    Add_full FA_2(A[2], xor_B[2], c_2, c_3, S[2]);
    Add_full FA_3(A[3], xor_B[3], c_3, c_4, S[3]);
   
    initial begin
		#5;
		$display("000A  000B  M  000S  000C");
		$display("============================");
		$display("Begin");
		$display("%b %b %b %b %b", A, B, M, S, c_4);
		$display("============================");

		$display("Set addition");
		M=0;
		$display("%b %b %b %b %b", A, B, M, S, c_4);
		$display("Set A=10");
		A=10;
		$display("%b %b %b %b %b", A, B, M, S, c_4);
		$display("Set B=5");
		B=5;
		$display("%b %b %b %b %b", A, B, M, S, c_4);
		#7
		$display("Set A+B=15");
		$display("%b %b %b %b %b", A, B, M, S, c_4);

		$display("============================");
		$display("Set A to 0");
		A=0;
		$display("Set B to 0");
		B=0;
		$display("============================");

		$display("Set subtraction");
		M=1;
		$display("%b %b %b %b %b", A, B, M, S, c_4);
		$display("Set A=10");
		A=10;
		$display("%b %b %b %b %b", A, B, M, S, c_4);
		$display("Set B=3");
		B=3;
		$display("%b %b %b %b %b", A, B, M, S, c_4);
		#7
		$display("Set A-B=7");
		$display("%b %b %b %b %b", A, B, M, S, c_4);
		$display("============================");

		$finish;
	end
endmodule
   
