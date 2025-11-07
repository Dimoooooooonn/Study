#ifndef CONE_H
#define CONE_H

#include <string>
#include <iostream>

class COne {
private:
    long l;
    std::string s;

public:
    COne() : l(0), s("") {}

    COne(long l_val, const std::string& s_val) : l(l_val), s(s_val) {}

    COne(const COne& other) : l(other.l), s(other.s) {}

    ~COne() {}

    COne& operator=(const COne& other) {
        if (this == &other) return *this;
        l = other.l;
        s = other.s;
        return *this;
    }

    long getL() const { return l; }
    void setL(long l_val) { l = l_val; }

    std::string getS() const { return s; }
    void setS(const std::string& s_val) { s = s_val; }

    void print() const {
        std::cout << "COne: l = " << l << ", s = " << s << std::endl;
    }
};

#endif
    