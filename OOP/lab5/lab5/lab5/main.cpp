#include "lab5_1.h"
#include "DynamicArray.h"
#include <iostream>

int main() {
    setlocale(LC_ALL, "");
    try {
        // Создаем массив рациональных чисел
        DynamicArray<Rational<int>> arr(3);

        // Заполняем массив
        arr[0] = Rational<int>(2, 4);
        arr[1] = Rational<int>(3, 9);
        arr[2] = arr[0] + arr[1];

        std::cout << "Array elements:\n";
        arr.print();

        // Попытка обращения к элементу вне диапазона
        try {
            arr[5].print();
        }
        catch (const std::exception& e) {
            std::cerr << "Caught exception: " << e.what() << "\n";
        }

        // Демонстрация деления на ноль
        try {
            Rational<int> r1(1, 2);
            Rational<int> r2(0, 1);
            auto r3 = r1 / r2;
            r3.print();
        }
        catch (const std::exception& e) {
            std::cerr << "Caught exception: " << e.what() << "\n";
        }

    }
    catch (const std::exception& e) {
        std::cerr << "Unexpected error: " << e.what() << "\n";
    }

    return 0;
}
