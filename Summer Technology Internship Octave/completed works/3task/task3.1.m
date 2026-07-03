clc;
clear;
close all;
x = -2:0.1:2;
y1 = 1 + 2 ./ x + 3 ./ (x .^ 2);
y2 = exp(-2 * x);

figure(1);
plot(x, y1, 'b-', x, y2, 'r-');

title('Графики функций y1 = 1 + 2/x + 3/x^2 и y2 = e^{-2x}');
xlabel('x');
ylabel('y');

legend('y1 = 1 + 2/x + 3/x^2', 'y2 = e^{-2x}');

grid on;
