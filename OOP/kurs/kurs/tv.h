#ifndef MOBILEPHONE_H
#define MOBILEPHONE_H

#include <iostream>

class TV
{
private:
    std::string* values;

public:
    TV(std::string* values);

    void SetValues(std::string* otherValues);
    std::string* GetValues();
};

#endif // MOBILEPHONE_H
