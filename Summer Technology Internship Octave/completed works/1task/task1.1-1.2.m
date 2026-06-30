A = 10^(-1);
B = 1.2;
C = 5.1;
D = 2.05;
format long;
printf('10-ая система:\n')
result = 0.25 * (sin(A) / (5.21e-6 * exp(B)) - 4 * sqrt(C * log(C))) - pi * D^2

printf('16-ая система:\n')
hex_result = dec2hex(result)

printf('2-ая система:\n')
bin_result = dec2bin(result)
