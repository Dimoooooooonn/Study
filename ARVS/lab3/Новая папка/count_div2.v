  module count_div2
  # (parameter N=4,M=4'd10)
  (input wire clk,
 output reg sync=0,
 output wire [N-1:0]q);
reg [N-1:0]cnt=0;
wire [N-1:0]cnt_next;
assign q=cnt;
assign cnt_next=cnt+1'b1;
always@(posedge clk)
begin
cnt<=cnt_next;
sync<=sync;
if (cnt==M-1)
begin
cnt<=0;
sync<=~sync;
end
end
endmodule

