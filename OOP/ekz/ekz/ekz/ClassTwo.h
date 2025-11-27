#ifndef CLASSTWO_H
#define CLASSTWO_H

#include "ClassOne.h"

class ClassTwo : public ClassOne {
private:
	double two;
public:
    ClassTwo() : ClassOne(), two(0.0) {}

    ClassTwo(double dVal, ClassThree* ptr, double twoVal)
        : ClassOne(dVal, ptr), two(twoVal) {}

    ClassTwo(ClassTwo& other) : ClassOne(other), two(other.two) {}

    ~ClassTwo() {}

    void setTwo(double v) {
        two = v;
    }

    double getTwo(){
        return two;
    }

};

#endif