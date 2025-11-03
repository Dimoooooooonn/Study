#include <iostream>
#include <chrono>

using namespace std;
using namespace chrono;

const int n = 2000;
const int nn = 10000;
const int v = 8;
int arr1[] = { 40, 11, 83, 57, 32, 21, 75, 64 };
int arr2[] = { 5, 11, 6, 4, 9, 2, 15, 7 };

void ShellSort(int* arr, int n) {
	int j;
	for (int d = n / 2; d > 0; d /= 2) {
		for (int i = d; i < n; i++) {
			int temp = arr[i];
			for (j = i; j >= d; j-=d) {
				if (temp < arr[j - d])
					arr[j] = arr[j - d];
				else
					break;
			}
			arr[j] = temp;
		}
	}
}

void SelectionSort(int* arr, int n) {
	for (int i = 0; i < n - 1; i++) {
		int min = i;
		for (int j = i + 1; j < n; j++) {
			if (arr[j] < arr[min])
				min = j;
		}
		int temp = arr[i];
		arr[i] = arr[min];
		arr[min] = temp;
	}
}

void Copy(int* arr1, int* arr2, int n) {
	for (int i = 0; i < n; i++) {
		arr2[i] = arr1[i];
	}
}

void ArrayPrint(int* arr, int n) {
	for (int i = 0; i < n; i++) {
		cout << arr[i] << " ";
	}
	cout << endl;
}

void Demonstration() {
	cout << "Демонстрация рaботоспособности сортировок:" << endl;
	cout << endl << "Сортировка Шелла" << endl << "Неотсортированный массив:" << endl;
	ArrayPrint(arr1, v);
	ShellSort(arr1, v);
	cout << "Отсортированный массив:" << endl;
	ArrayPrint(arr1, v);
	cout << endl << "Сортировка Выбором" << endl << "Неотсортированный массив:" << endl;
	ArrayPrint(arr2, v);
	SelectionSort(arr2, v);
	cout << "Отсортированный массив:" << endl;
	ArrayPrint(arr2, v);
}

void CalculationTime() {
	int arr_original[n];
	int arr[n];
	srand(time(0));

	for (int i = 0; i < n; i++) {
		arr_original[i] = rand();
	}
	auto start = high_resolution_clock::now();
	for (int i = 0; i < nn; i++) {
		Copy(arr_original, arr, n);
	}
	auto end = high_resolution_clock::now();
	duration<float> t_copy = end - start;
	float T_copy = t_copy.count();

	auto start1 = high_resolution_clock::now();
	for (int i = 0; i < nn; i++) {
		Copy(arr_original, arr, n);
		ShellSort(arr, n);
	}
	auto end1 = high_resolution_clock::now();
	duration<float> t_shell_sort = end1 - start1;
	float T_shell_sort = t_shell_sort.count();
	ArrayPrint(arr, n);
	cout << endl;
	cout << "Среднее время сортировки Шелла в " << nn << " измерений при размере массива " << n << " -- " << T_shell_sort - T_copy << " секунд" << endl;

	auto start2 = high_resolution_clock::now();
	for (int i = 0; i < nn; i++) {
		Copy(arr_original, arr, n);
		SelectionSort(arr, n);
	}
	auto end2 = high_resolution_clock::now();
	duration<float> t_selection_sort = end2 - start2;
	float T_selection_sort = t_selection_sort.count();
	ArrayPrint(arr, n);
	cout << "Среднее время сортировки выбором в " << nn << " измерений при размере массива " << n << " -- " << T_selection_sort - T_copy << "	секунд" << endl;

}

void main() {
	setlocale(LC_ALL, "");
	Demonstration();
	CalculationTime();
}