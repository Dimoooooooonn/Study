clc;
clear;
close all;

disp('Задание 5. Вариант 7');
disp('Решение системы нелинейных уравнений графическим методом');

x1 = [-10:0.01:-0.01 NaN 0.01:0.01:10];
x2 = -10:0.01:10;

y1 = 20 ./ x1 + 1;
y2 = x2 .^ 2 - 3 .* x2 - 5;

figure(1);

plot(x1, y1, 'b-', 'linewidth', 2);
hold on;

plot(x2, y2, 'r-', 'linewidth', 2);

plot(5, 5, 'ko', 'markersize', 8, 'markerfacecolor', 'k');

title('Графическое решение системы');
xlabel('x');
ylabel('y');

legend('y_1 = 20/x + 1', ...
       'y_2 = x^2 - 3x - 5', ...
       'Решение (5; 5)');

grid on;

xlim([-2 7]);
ylim([-20 30]);

hold off;

p = [1 -3 -6 -20];

disp('Корни уравнения x^3 - 3*x^2 - 6*x - 20 = 0:');
X = roots(p);
disp(X);

x = 5;
y = 20 / x + 1;

disp('Найденное решение системы:');
disp('x = ');
disp(x);
disp('y = ');
disp(y);

F1 = y - 20 / x - 1;
F2 = y - (x^2 - 3*x - 5);

disp('Проверка первого уравнения y - 20/x - 1:');
disp(F1);

disp('Проверка второго уравнения y - (x^2 - 3*x - 5):');
disp(F2);

xg = -2:0.01:7;
fg = xg .^ 3 - 3 .* xg .^ 2 - 6 .* xg - 20;

figure(2);

plot(xg, fg, 'k-', 'linewidth', 2);
hold on;

plot(xg, 0 .* xg, 'r--', 'linewidth', 1);

plot(5, 0, 'bo', 'markersize', 8, 'markerfacecolor', 'b');

title('График уравнения x^3 - 3x^2 - 6x - 20 = 0');
xlabel('x');
ylabel('f(x)');

legend('f(x) = x^3 - 3x^2 - 6x - 20', ...
       'Ось Ox', ...
       'Корень x = 5');

grid on;

xlim([-2 7]);
ylim([-50 80]);

hold off;
