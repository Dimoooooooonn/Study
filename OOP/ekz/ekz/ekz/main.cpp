#include <iostream>
#include "ClassOne.h"
#include "ClassTwo.h"
#include "ClassThree.h"

int main() {
	setlocale(LC_ALL, "");
    ClassThree obj3("Третий класс");
    ClassOne obj1(3.14, &obj3);

    std::cout << "ClassOne d: " << obj1.getD() << std::endl;
    std::cout << "ClassOne указывает на ClassThree s: " << obj1.getP()->getS() << std::endl;

    ClassTwo obj2(2.718, &obj3, 42.0);

    std::cout << "ClassTwo d: " << obj2.getD() << std::endl;
    std::cout << "ClassTwo two: " << obj2.getTwo() << std::endl;
    std::cout << "ClassTwo указывает на ClassThree s: " << obj2.getP()->getS() << std::endl;

    std::cout << "ClassThree s: " << obj3.getS() << std::endl;

    obj2.setD(1.618);
    obj2.setTwo(99.9);
    obj2.getP()->setS("Новая строка");


    std::cout << "\nПосле изменений:" << std::endl;
    std::cout << "ClassTwo d: " << obj2.getD() << std::endl;
    std::cout << "ClassTwo two: " << obj2.getTwo() << std::endl;
    std::cout << "ClassTwo указывает на ClassThree s: " << obj2.getP()->getS() << std::endl;
    
    obj3.setS("Другая строка");
    std::cout << "\nClassTwo указывает на ClassThree s: " << obj2.getP()->getS() << std::endl;

    return 0;
}