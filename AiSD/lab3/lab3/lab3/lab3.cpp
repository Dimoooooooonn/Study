#include <iostream>
#include <cmath>
#include <chrono>
#include <vector>
#include <functional>

using namespace std;
using namespace std::chrono;

double Dichotomy(function<double(double)> f, double a, double b, double epsilon) {
    while (fabs(b - a) > epsilon) {
        double xm = (a + b) / 2;
        double x1 = xm - epsilon;
        double x2 = xm + epsilon;
        if (f(x1) < f(x2)) {
            b = xm;
        }
        else {
            a = xm;
        }
    }
    return (a + b) / 2;
}

double GoldenSection(function<double(double)> f, double a, double b, double epsilon) {
    const double phi = (1 + sqrt(5)) / 2;
    double x1 = b - (b - a) / phi;
    double x2 = a + (b - a) / phi;
    while (fabs(b - a) > epsilon) {
        if (f(x1) < f(x2)) {
            b = x2;
            x2 = x1;
            x1 = b - (b - a) / phi;
        }
        else {
            a = x1;
            x1 = x2;
            x2 = a + (b - a) / phi;
        }
    }
    return (a + b) / 2;
}

double GradientDescent(function<double(double)> f, function<double(double)> df, double x0, double alpha, double epsilon, int maxIter = 1000) {
    double x = x0;
    for (int i = 0; i < maxIter; i++) {
        double grad = df(x);
        if (fabs(grad) < epsilon) {
            break;
        }
        x -= alpha * grad;
    }
    return x;
}

double CheckDichotomy(function<double(double)> f, double a, double b, double epsilon, int iterations) {
    double totalTime = 0;
    for (int i = 0; i < iterations; i++) {
        auto start = high_resolution_clock::now();
        Dichotomy(f, a, b, epsilon);
        auto stop = high_resolution_clock::now();
        totalTime += duration<double, micro>(stop - start).count();
    }
    return totalTime / iterations;
}

double CheckGoldenSection(function<double(double)> f, double a, double b, double epsilon, int iterations) {
    double totalTime = 0;
    for (int i = 0; i < iterations; i++) {
        auto start = high_resolution_clock::now();
        GoldenSection(f, a, b, epsilon);
        auto stop = high_resolution_clock::now();
        totalTime += duration<double, micro>(stop - start).count();
    }
    return totalTime / iterations;
}

double CheckGradientDescent(function<double(double)> f, function<double(double)> df, double x0, double alpha, double epsilon, int iterations) {
    double totalTime = 0;
    for (int i = 0; i < iterations; i++) {
        auto start = high_resolution_clock::now();
        GradientDescent(f, df, x0, alpha, epsilon);
        auto stop = high_resolution_clock::now();
        totalTime += duration<double, micro>(stop - start).count();
    }
    return totalTime / iterations;
}

int main() {
    setlocale(LC_ALL, "");
    auto f = [](double x) { return (x - 2) * (x - 2) + 1; };
    auto df = [](double x) { return 2 * (x - 2); };

    double a = 0, b = 5, alpha = 0.1, x0 = 0;
    int checkCount = 10000;
    vector<double> accuracy = { 1e-2, 1e-3, 1e-4, 1e-5, 1e-6, 1e-7, 1e-8, 1e-9, 1e-10, 1e-11, 1e-12, 1e-13, 1e-14, 1e-15 };

    for (double acc : accuracy) {
        cout << "Точность: " << acc << endl;
        cout << "Метод дихотомии: x_min = " << Dichotomy(f, a, b, acc) << ", время = " << CheckDichotomy(f, a, b, acc, checkCount) << " мкс" << endl;
        cout << "Метод золотого сечения: x_min = " << GoldenSection(f, a, b, acc) << ", время = " << CheckGoldenSection(f, a, b, acc, checkCount) << " мкс" << endl;
        cout << "Метод градиентного спуска: x_min = " << GradientDescent(f, df, x0, alpha, acc) << ", время = " << CheckGradientDescent(f, df, x0, alpha, acc, checkCount) << " мкс" << endl;
        cout << endl;
    }
    return 0;
}
