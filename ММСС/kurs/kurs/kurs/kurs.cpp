#include <iostream>
#include <fstream>
#include <iomanip>
#include <vector>
#include <algorithm>
#include <ctime>
#include <numeric>
#include <string>
#include <sstream>
#include <stddef.h>
#include <limits>
int DC, ACCURACY; // Объявление глобальных переменных для параметров оптимизации
using namespace std;
using float_type = long double;
// Функция для реализации алгоритма Флойда
void floyd(vector<vector<float_type>>& matrixR1, vector<vector<int>>& matrixR2)
{
	int n = matrixR1.size(); // Получение размера матрицы (количество вершин)
	float_type x, y;
	// Инициализация матрицы маршрутов (matrixR2) числами от 1 до n для каждого узла
	for (int i = 0; i < n; ++i)
	{
		iota(matrixR2[i].begin(), matrixR2[i].end(), 1); // Заполнение row R2 значениями 1, 2, ..., n
	}
	// Основной цикл
	for (int k = 0; k < n; ++k)
	{
		for (int i = 0; i < n; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				if (i != j && i != k && j != k) // Проверка, что i, j и k - разные узлы
				{
					x = matrixR1[i][k] + matrixR1[k][j]; // Расчет расстояния через промежуточный узел k
					y = matrixR1[i][j]; // Исходное расстояние между i и j
					if (x < y) // Если найдено более короткое расстояние
					{
						matrixR1[i][j] = x; // Обновление расстояния
						matrixR2[i][j] = matrixR2[i][k]; // Обновление маршрута
					}
				}
			}
		}
	}
}
// Функция для загрузки интенсивности трафика
vector<vector<float_type>> load_intensity(const vector<vector<float_type>>& matrixY, vector<vector<int>>& matrixR2)
{
	int n = matrixY.size(); // Получение размера матрицы
	vector<vector<float_type>> matrixY_hatch(n, vector<float_type>(n, 0)); // Инициализация новой матрицы нулями
	// Итерация по матрице Y
	for (int i = 0; i < n; ++i)
	{
		for (int j = 0; j < n; ++j)
		{
			matrixY_hatch[i][matrixR2[i][j] - 1] += matrixY[i][j]; // Обновление матрицы интенсивности
			for (int k = matrixR2[i][j] - 1; k != j; k = matrixR2[k][j] - 1)
			{
				matrixY_hatch[k][matrixR2[k][j] - 1] += matrixY[i][j]; // Обновление для промежуточных маршрутов
			}
		}
	}
	return matrixY_hatch; // Возврат матрицы интенсивности
}
// Рекуррентный расчёт формулы Эрланга B
float_type erlangB(float_type y, int v)
{
	float_type B = 1.0; // B(0, y)
	for (int i = 1; i <= v; ++i)
	{
		B = (y * B) / (i + y * B);
	}
	return B;
}

// Функция генерации матрицы потоков
vector<vector<int>> matrix_of_streams(vector<vector<float_type>>& matrixY_hatch, float_type quality)
{
	int n = matrixY_hatch.size();          // Размер матрицы
	float_type p_max = 1. - quality;      // Пороговая вероятность отказа p0
	vector<vector<int>> matrixV(n, vector<int>(n, 0)); // Матрица потоков

	// Итерация по матрице интенсивностей
	for (int i = 0; i < n; ++i)
	{
		for (int j = 0; j < n; ++j)
		{
			float_type y = matrixY_hatch[i][j];
			int v = 0;

			if (y != 0)
			{
				float_type p = 1.0;  // Начальное значение B(0, y)

				// Ищем минимальное v, при котором p <= p_max
				while (p > p_max)
				{
					++v;
					p = erlangB(y, v);
				}
			}

			matrixV[i][j] = v; // Заполнение матрицы потоков
		}
	}

	return matrixV; // Возврат результата
}

// Шаблонная функция для вывода вектора
template <class T>
void print_vector(std::ostream& os, const vector<T>& matrix)
{
	for (auto& s : matrix) // Итерация по элементам вектора
		os << s << ' '; // Вывод элемента
	os << endl << endl; // Переход на новую строку
}
// Шаблонная функция для вывода матрицы
template <class T>
void Output(std::ostream& os, const vector<vector<T>>& matrix, int width = 20)
{
	//string line(420, '-');
	//os << line << endl;
	for (auto& s : matrix) // Итерация по строкам матрицы
	{
		//os << "|";
		for (auto& t : s)
			os << /*std::setw(width)*/ t << " "; // Вывод каждого элемента
		os << endl; // Переход на новую строку
	}
	//os << line << endl;
	os << endl; // Переход на новую строку после матрицы
}
int main()
{
	// Считывание данных с файла
	auto start = clock(); // Запоминаем время начала работы
	int n, a0, optimizeT0; // Объявление переменных для чтения
	float_type y0, L, quality, T0; // Объявление переменных для параметров
	ifstream file_in("input.txt"); // Открытие файла для чтения
	if (!file_in.is_open())
	{
		cout << "Error opening file 'input.txt'" << endl; // Сообщение об ошибке
		return 1;
	}
	stringstream s;
	while (!file_in.eof())
	{
		string param;
		getline(file_in, param);
		s << param.substr(0, param.find(';')) << ' '; // Извлечение нужной части строки и запись в поток
	}
	file_in.close();
	s >> n >> y0 >> a0 >> L >> quality >> T0 >> optimizeT0 >> DC >> ACCURACY; // Чтение параметров из потока
	vector<float_type> subscribers(n, 0);
	for (auto& sub : subscribers)
		s >> sub;
	vector<vector<float_type>> distance_matrix(n, vector<float_type>(n, 0)); // Инициализация матрицы расстояний
	for (auto& str : distance_matrix)
		for (auto& d : str)
			s >> d; // Чтение значений расстояний
	ofstream file_out("output.txt"); // Открытие файла для записи
	if (!file_out.is_open())
	{
		cout << "Error opening file 'output.txt'" << endl; // Сообщение об ошибке
		return 1;
	}
	file_out << setprecision(std::numeric_limits<float_type>::digits10); // Установка точности вывода
	// Интенсивности производимого в узлах сети трафика
	file_out << " / Интенсивности производимого в узлах сети трафика / " << endl;
	vector<float_type> matrixY(subscribers); // Инициализация матрицы интенсивности
	transform(matrixY.begin(), matrixY.end(), matrixY.begin(), [&y0](auto n) { return n * y0; }); // Преобразование интенсивностей
	print_vector(file_out, matrixY); // Вывод матрицы интенсивности
	// Коэффициенты распределения трафика по направлениям связи
	file_out << " / Коэффициенты распределения трафика по направлениям связи / " << endl;
	auto sum = accumulate(matrixY.begin(), matrixY.end(), 0.L); // Суммирование интенсивностей
	vector<float_type> matrixK(matrixY); // Инициализация коэффициентов
	transform(matrixK.begin(), matrixK.end(), matrixK.begin(), [&sum](auto n) { return n / sum; }); // Нормализация
	print_vector(file_out, matrixK); // Вывод коэффициентов распределения
	// Матрица интенсивностей трафика в направлениях связи
	file_out << " / Матрица интенсивностей трафика в направлениях связи / " << endl;
	vector<vector<float_type>> matrixYij(n, vector<float_type>(n, 0)); // Инициализация матрицы интенсивностей
	for (int i = 0; i < n; ++i)
		for (int j = 0; j < n; ++j)
			matrixYij[i][j] = matrixY[i] * matrixK[j]; // Вычисление интенсивностей по направлениям
	Output(file_out, matrixYij); // Вывод матрицы интенсивностей
	// Матрицы кратчайших расстояний и кратчайших маршрутов между вершинами графа
	vector<vector<float_type>> matrixR1(distance_matrix); // Копирование матрицы расстояний
	vector<vector<int>> matrixR2(n, vector<int>(n, 0)); // Инициализация матрицы маршрутов
	floyd(matrixR1, matrixR2); // Вычисление кратчайших расстояний и маршрутов
	file_out << " / Матрица кратчайших расстояний между вершинами графа / " << endl;
	Output(file_out, matrixR1); // Вывод матрицы кратчайших расстояний
	file_out << " / Матрица кратчайших маршрутов между вершинами графа / " << endl;
	Output(file_out, matrixR2); // Вывод матрицы маршрутов
	// Матрица интенсивностей нагрузок на линиях связи
	file_out << " / Матрица интенсивностей нагрузок на линиях связи / " << endl;
	auto matrixY_hatch = load_intensity(matrixYij, matrixR2); // Загрузка интенсивностей
	Output(file_out, matrixY_hatch); // Вывод матрицы нагрузок
	// Матрица потоков
	file_out << " / Матрица потоков / " << endl;
	auto streams_matrix = matrix_of_streams(matrixY_hatch, quality); // Генерация матрицы потоков
	Output(file_out, streams_matrix); // Вывод матрицы потоков
	// Матрица интенсивности трафика ПД для линий связи
	file_out << " / Матрица интенсивности трафика ПД для линий связи / " << endl;
	vector<vector<int>> Aij(streams_matrix); // Инициализация матрицы PDU
	for (auto& str : Aij)
	{
		transform(str.begin(), str.end(), str.begin(), [&a0](auto v) { return v * a0; }); // Масштабирование
	}
	Output(file_out, Aij); // Вывод матрицы PDU
	// Матрица пропускных способностей
	file_out << " / Матрица пропускных способностей / " << endl;
	vector<vector<float_type>> Bij(n, vector<float_type>(n, 0)); // Инициализация матрицы пропускных способностей
	for (int i = 0; i < n; ++i)
	{
		for (int j = 0; j < n; ++j)
		{
			Bij[i][j] = (Aij[i][j]) ? (Aij[i][j] + (8. * L) / T0) : 0; // Вычисление пропускной способности
		}
	}
	cout << "Please, wait" << endl;
	Output(file_out, Bij); // Вывод матрицы пропускных способностей
}
