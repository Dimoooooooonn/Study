#include "tv.h"

TV::TV(std::string* values) : values(values)
{ }

void TV::SetValues(std::string* otherValues)
{
    values = otherValues;
}

std::string* TV::GetValues()
{
    return values;
}
