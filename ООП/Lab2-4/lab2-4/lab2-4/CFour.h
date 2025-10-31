#ifndef CFOUR_H
#define CFOUR_H

#include "CThree.h"
#include <iostream>

class CFour : public CThree {
private:
    double extraValue;

public:
    CFour() : CThree(), extraValue(0.0) {}

    CFour(const std::string& s_val, const COne& one_obj, int number, double extra)
        : CThree(s_val, one_obj, number), extraValue(extra) {
    }

    CFour(const CFour& other) : CThree(other), extraValue(other.extraValue) {}


    ~CFour() {}


    double getExtraValue() const { return extraValue; }
    void setExtraValue(double extra) { extraValue = extra; }


    virtual void print() const {
        CThree::print();
        std::cout << "CFour: extraValue = " << extraValue << std::endl;
    }
};

#endif
