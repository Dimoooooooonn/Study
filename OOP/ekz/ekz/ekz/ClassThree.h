#ifndef CLASSTHREE_H
#define CLASSTHREE_H

#include <string>
#include <iostream>

class ClassThree{
private:
	std::string s;
public:
    ClassThree() : s("") {}

    ClassThree(const std::string& str) : s(str) {}

    ClassThree(ClassThree& other) : s(other.s) {}

    ~ClassThree() {}

    void setS(const std::string& str) {
        s = str;
    }

    std::string getS() {
        return s;
    }
};

#endif