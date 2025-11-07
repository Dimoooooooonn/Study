#include <iostream>
#include <cstdlib> // для std::abs

class Rational {
private:
    int numerator;   // числитель
    int denominator; // знаменатель

    // Вспомогательная функция для нахождения НОД
    int nod(int a, int b) const {
        a = std::abs(a);
        b = std::abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return (a == 0) ? 1 : a;
    }

    // Сокращение дроби
    void reduce() {
        int div = nod(numerator, denominator);
        numerator /= div;
        denominator /= div;

        // Перенос знака в числитель (знаменатель всегда положительный)
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
    }

public:
    // 🔹 Конструктор по умолчанию
    Rational() : numerator(0), denominator(1) {
        // создаём рациональное число 0/1
    }

    // 🔹 Конструктор с параметрами
    Rational(int p, int q = 1) {
        if (q == 0) {
            std::cerr << "Ошибка: знаменатель не может быть 0! Заменён на 1.\n";
            q = 1;
        }
        numerator = p;
        denominator = q;
        reduce(); // приведение к редуцированной форме
    }

    // 🔹 Методы доступа
    int getNumerator() const { return numerator; }
    int getDenominator() const { return denominator; }

    void setNumerator(int n) { numerator = n; reduce(); }
    void setDenominator(int d) {
        if (d == 0) {
            std::cerr << "Ошибка: знаменатель не может быть 0! Игнорируем.\n";
            return;
        }
        denominator = d;
        reduce();
    }

    // 🔹 Арифметические операции
    Rational operator+(const Rational& other) const {
        int num = numerator * other.denominator + other.numerator * denominator;
        int den = denominator * other.denominator;
        return Rational(num, den);
    }

    Rational operator-(const Rational& other) const {
        int num = numerator * other.denominator - other.numerator * denominator;
        int den = denominator * other.denominator;
        return Rational(num, den);
    }

    Rational operator*(const Rational& other) const {
        int num = numerator * other.numerator;
        int den = denominator * other.denominator;
        return Rational(num, den);
    }

    Rational operator/(const Rational& other) const {
        if (other.numerator == 0) {
            std::cerr << "Ошибка: деление на ноль!\n";
            return Rational(0, 1);
        }
        int num = numerator * other.denominator;
        int den = denominator * other.numerator;
        return Rational(num, den);
    }

    // 🔹 Операции сравнения
    bool operator==(const Rational& other) const {
        return numerator == other.numerator && denominator == other.denominator;
    }

    bool operator!=(const Rational& other) const {
        return !(*this == other);
    }

    bool operator<(const Rational& other) const {
        return numerator * other.denominator < other.numerator * denominator;
    }

    bool operator>(const Rational& other) const {
        return other < *this;
    }

    bool operator<=(const Rational& other) const {
        return !(*this > other);
    }

    bool operator>=(const Rational& other) const {
        return !(*this < other);
    }

    // 🔹 Вывод рационального числа
    void print() const {
        std::cout << numerator << "/" << denominator << std::endl;
    }
};

// 🔹 Пример использования
int main() {
    Rational a(2, 4);    // 1/2
    Rational b(3, 9);    // 1/3
    Rational c;          // 0/1

    std::cout << "a = "; a.print();
    std::cout << "b = "; b.print();

    c = a + b;
    std::cout << "a + b = "; c.print();

    c = a - b;
    std::cout << "a - b = "; c.print();

    c = a * b;
    std::cout << "a * b = "; c.print();

    c = a / b;
    std::cout << "a / b = "; c.print();

    std::cout << std::boolalpha; // выводим true/false
    std::cout << "a == b ? " << (a == b) << std::endl;
    std::cout << "a > b ? " << (a > b) << std::endl;
    std::cout << "a < b ? " << (a < b) << std::endl;

    return 0;
}
