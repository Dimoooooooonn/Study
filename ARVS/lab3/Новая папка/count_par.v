module count_par
    # (parameter N=4)
    (input wire clk,reset,res,ena,rev,load,
    input wire [N-1:0]d,
    output wire [N-1:0]q);
    reg [N-1:0]cnt;
    reg [N-1:0]cnt_next;
    assign q=cnt;
    always@(posedge clk, negedge reset)
    if(!reset)
    cnt<=0;
    else
    cnt<=cnt_next;
    always@*
    if(res)
    cnt_next=0;
    else if(load)
    cnt_next<=d;
  else if(ena&!rev)
  cnt_next=cnt + 1;
  else if(ena&rev)
  cnt_next=cnt-1;
  else
  cnt_next=cnt;
  endmodule

