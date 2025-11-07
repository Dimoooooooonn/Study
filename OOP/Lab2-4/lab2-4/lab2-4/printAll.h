#ifndef PRINTALL_H
#define PRINTALL_H

#include "CTwo.h"
#include <iostream>


void printAll(CTwo* array[], int n) {
    for (int i = 0; i < n; ++i) {
        if (array[i]) {
            array[i]->print();
            std::cout << "--------" << std::endl;
        }
    }
}

#endif
