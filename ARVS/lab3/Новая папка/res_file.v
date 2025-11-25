module res_file (
	input wire clk,
	input wire [9:0] SW,
	output wire [6:0] HEX0
);
	wire reset = SW[0];               
    	wire res = SW[1];
    	wire ena = SW[2];
    	wire rev = SW[3];
    	wire load = SW[4];
    	wire [3:0] d = {SW[9],SW[8],SW[7],SW[6]};

	wire sync;
	count_div2 u_div (.clk(clk), .sync(sync));
	
	wire [3:0] q;
	count_par #(.N(4)) u_counter (.clk(clk), .reset(reset), .res(res), .ena(ena), .rev(rev), .load(load), .d(d), .q(q));

	coder u_coder (.data(q), .seg(HEX0));
endmodule 