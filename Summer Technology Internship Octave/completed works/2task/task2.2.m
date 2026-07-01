clc;
clear;
disp('Задание: записать матрицу в файл и считать ее обратно');

N = input('Введите количество сторк N = ');
M = input('Введите количество столбцов M = ');

for i = 1 : N
  for j = 1 : M
    A(i, j) = input(sprintf('A(%d,%d) = ', i, j));
  end;
end;

disp('Исходная матрица A:');
disp(A);

f = fopen('primer.txt', 'wt');
fprintf(f, '%d\t%d\n', N, M);

for i = 1 : N
  for j = 1 : M
    fprintf(f, '%g\t', A(i, j));
  end;
  fprintf(f, '\n');
end;

fclose(f);
disp('Матрица записана в файл primer.txt');
clear;

f = fopen('primer.txt', 'rt');
stroka = fgetl(f);
razmer = sscanf(stroka, '%d\t%d');
N = razmer(1);
M = razmer(2);
for i = 1:N
    for j = 1:M
        B(i,j) = fscanf(f, '%g', 1);
    end
end

fclose(f);
disp('Матрица, считанная из файла primer.txt:');
disp(B);
