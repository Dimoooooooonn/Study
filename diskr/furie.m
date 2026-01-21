% Исходная последовательность (8 букв -> 8 чисел)
x = [4 1 2 1 2 3 2 3];

N = length(x);
n = 0:N-1;
k = 0:N-1;

%% ===== Дискретное преобразование Фурье (DFT) =====
X = zeros(1, N);

for kk = 1:N
    for nn = 1:N
        X(kk) = X(kk) + x(nn) * exp(-1j * 2*pi * (kk-1) * (nn-1) / N);
    end
end

disp('DFT:');
disp(X);

%% ===== Обратное дискретное преобразование Фурье (IDFT) =====
x_rec = zeros(1, N);

for nn = 1:N
    for kk = 1:N
        x_rec(nn) = x_rec(nn) + X(kk) * exp(1j * 2*pi * (kk-1) * (nn-1) / N);
    end
    x_rec(nn) = x_rec(nn) / N;
end

disp('Обратное преобразование:');
x_round = round(real(x_rec));
disp(x_round);





