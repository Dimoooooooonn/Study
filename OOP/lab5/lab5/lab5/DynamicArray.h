#ifndef DYNAMIC_ARRAY_H
#define DYNAMIC_ARRAY_H

#include <iostream>
#include <stdexcept>

template<typename T>
class DynamicArray {
private:
    T* data;
    size_t size;

public:
    DynamicArray(size_t n) : size(n) {
        if (n == 0) throw std::invalid_argument("Array size must be > 0");
        data = new T[n];
    }

    ~DynamicArray() {
        delete[] data;
    }

    T& operator[](size_t index) {
        if (index >= size) throw std::out_of_range("Index out of range");
        return data[index];
    }

    const T& operator[](size_t index) const {
        if (index >= size) throw std::out_of_range("Index out of range");
        return data[index];
    }

    size_t getSize() const { return size; }

    void print() const {
        for (size_t i = 0; i < size; ++i) {
            data[i].print();
        }
        std::cout << "\n";
    }
};

#endif // DYNAMIC_ARRAY_H
