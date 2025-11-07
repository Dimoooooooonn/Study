#ifndef CTREE_H
#define CTREE_H

#include "CTwo.h"
#include <iostream>

class CThree : public CTwo
{
private:
    int num;
public:
    CThree() : CTwo(), num(0) {}

    CThree(const std::string& s_val, const COne& one_obj, int number) : CTwo(s_val, one_obj), num(number) {}

    CThree(const CThree& other) : CTwo(other), num(other.num) {}

    ~CThree() {}

    int getNum() const { return num; }
    void setNum(int number) { num = number; }

    virtual void print() const {
        CTwo::print();
        std::cout << "CThree: value = " << num << std::endl;
    }
};

#endif
