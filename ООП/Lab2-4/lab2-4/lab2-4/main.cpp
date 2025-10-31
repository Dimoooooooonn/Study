#include "COne.h"
#include "CTwo.h"
#include "CThree.h"
#include "CFour.h"
#include "printAll.h"

int main() {
    // Создаем объект COne
    COne obj1(42, "Base Object");

    // Создаем объект CTwo
    CTwo obj2("Derived CTwo", obj1);

    // Создаем объект CThree
    CThree obj3("Derived CThree", obj1, 100);

    // Создаем объект CFour
    CFour obj4("Derived CFour", obj1, 200, 3.14);

    // Печатаем их индивидуально
    std::cout << "Printing individual objects:" << std::endl;
    obj1.print();
    std::cout << "--------" << std::endl;
    obj2.print();
    std::cout << "--------" << std::endl;
    obj3.print();
    std::cout << "--------" << std::endl;
    obj4.print();
    std::cout << "========" << std::endl;

    // Массив указателей
    CTwo* array[] = { &obj2, &obj3, &obj4 };
    int n = sizeof(array) / sizeof(array[0]);

    // Печатаем с использованием printAll
    std::cout << "Printing objects using printAll:" << std::endl;
    printAll(array, n);

    return 0;
}
