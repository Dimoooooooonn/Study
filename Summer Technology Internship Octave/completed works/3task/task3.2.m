clc;
clear;
close all;

t = 0:pi/50:2*pi;
x_t = 16 * (sin(t) .^ 3);

y_t = 13 * cos(t) ...
      - 5 * cos(2 * t) ...
      - 2 * cos(3 * t) ...
      - cos(4 * t);

figure(1);
plot(x_t, y_t, 'm-', 'linewidth', 2);

title('Параметрический график: Сердце');
xlabel('x(t)');
ylabel('y(t)');

grid on;
axis equal;
