#ifndef CTWO_H
#define CTWO_H

#include "COne.h"
#include <string>
#include <iostream>

class CTwo {
private:
    std::string s;
    COne* p;

public:
    CTwo() : s(""), p(nullptr) {}

    CTwo(const std::string& s_val, const COne& one_obj)
        : s(s_val), p(new COne(one_obj)) {
    }

    CTwo(const CTwo& other) : s(other.s), p(other.p ? new COne(*other.p) : nullptr) {}

    ~CTwo() {
        delete p;
    }

    CTwo& operator=(const CTwo& other) {
        if (this == &other) return *this;

        s = other.s;

        delete p;
        p = other.p ? new COne(*other.p) : nullptr;

        return *this;
    }


    std::string getS() const { return s; }
    void setS(const std::string& s_val) { s = s_val; }

    COne* getP() const { return p; }
    void setP(const COne& one_obj) {
        if (p) delete p;
        p = new COne(one_obj);
    }

    virtual void print() const {
        std::cout << "CTwo: s = " << s << ", ";
        if (p) {
            p->print();
        }
        else {
            std::cout << "p = nullptr" << std::endl;
        }
    }
};

#endif
