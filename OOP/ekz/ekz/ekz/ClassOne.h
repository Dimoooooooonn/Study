#ifndef CLASSONE_H
#define CLASSONE_H

#include "ClassThree.h"

class ClassOne {
protected:
	double d;
	ClassThree* p;

public:
	ClassOne() {
		this->d = 0.0;
		p = nullptr;
	}
	ClassOne(double d, ClassThree* obj) {
		this->d = d;
		p = obj;
	}
	ClassOne(double d) {
		this->d = d;
		p = nullptr;
	}
	ClassOne(ClassThree* obj) {
		this->d = 0.0;
		p = obj;
	}
	ClassOne(ClassOne& other) {
		this->d = other.d;
		p = other.p;
	}
	~ClassOne() {}

	double getD() {
		return d;
	}
	void setD(double d) {
		this->d = d;
	}
	ClassThree* getP() {
		return p;
	}
	void setP(ClassThree* obj) {
		p = obj;
	}

};

#endif