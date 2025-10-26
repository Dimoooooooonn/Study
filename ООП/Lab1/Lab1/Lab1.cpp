#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <cstring> 

class String {
private:
	char* data;
	int length;

public:
	// Конструктор
	String(const char* str = "") {
		length = strlen(str);
		data = new char[length + 1];
		strcpy(data, str);
	}

	// Конструктор копирования
	String(const String& other) {
		length = other.length;
		data = new char[length + 1];
		strcpy(data, other.data);
	}

	// Оператор присваивания
	String& operator=(const String& other) {
		if (this != &other) {          // защита от самоприсваивания
			delete[] data;             // освобождаем старую память
			length = other.length;
			data = new char[length + 1];
			strcpy(data, other.data);
		}
		return *this;
	}

	// Деструктор
	~String() {
		delete[] data;
	}
	// Метод определение длины строки
	int Length() {
		return length;
	}

	// Метод для копирования строки
	void Copy(const String& str) {
		delete[] data;
		length = str.length;
		data = new char[length + 1];
		strcpy(data, str.data);
	}

	// Метод копирования строки
	void Copy(const char* str) {
		delete[] data;
		length = strlen(str);
		data = new char[length + 1];
		strcpy(data, str);
	}

	// Метод выделения подстроки
	String Substr(int index, int count) const {
		if (index < 0 || count < 0 || index >= length) {
			std::cout << "Некорректные параметры для Substr." << std::endl;
			return String("");
		}

		if (index + count > length)
			count = length - index;

		char* temp = new char[count + 1];
		strncpy(temp, data + index, count);
		temp[count] = '\0';

		String result(temp);
		delete[] temp;
		return result;
	}

	// Метод удаления подстроки, начиная с индекса
	void Remove(int index, int count) {
		if (index < 0 || count < 0 || index >= length) 
			return;
		if (index + count > length) 
			count = length - index;

		for (int i = index + count; i <= length; ++i) {
			data[i - count] = data[i];
		}
		length -= count;
	}

	// Метод вставки строки в стиле языка C в строку
	void Insert(const char* s, int index) {
		if (index < 0 || index > length)
			return;

		int s_lenght = strlen(s);
		char* new_data = new char[length + s_lenght + 1];

		strncpy(new_data, data, index);
		new_data[index] = '\0';
		strcat(new_data, s);
		strcat(new_data, data + index);
		delete[] data;
		data = new_data;
		length += s_lenght;
	}

	// Метод удаления из строки ведущие и завершающие пробелы
	void trim() {
		int start = 0;
		while (start < length && data[start] == ' ')
			++start;

		int end = length - 1;
		while (end >= start && data[end] == ' ')
			--end;


		int new_len = end - start + 1;
		if (new_len <= 0) {
			delete[] data;
			data = new char[1];
			data[0] = '\0';
			length = 0;
			return;
		}

		char* trimmed = new char[new_len + 1];
		for (int i = 0; i < new_len; ++i)
			trimmed[i] = data[start + i];
		trimmed[new_len] = '\0';

		delete[] data;
		data = trimmed;
		length = new_len;
	}

	// Метод для ввода строки с клавиатуры
	void read() {
		char buffer[256];
		std::cout << "Enter string: ";
		std::cin.getline(buffer, 256);
		Copy(buffer);
	}

	// Метод для вывода строки на экран дисплея
	void print() const{
		std::cout << data << std::endl;
	}
};

//int main() {
//	String s1("   Hello World!   ");
//	String s2;
//
//	std::cout << "Source string: ";
//	s1.print();
//
//	s1.trim();
//	std::cout << "After trim(): ";
//	s1.print();
//
//	s2.Copy("Inserted text");
//	s1.Insert(" *** ", 5);
//	std::cout << "After inserts: ";
//	s1.print();
//
//	s1.Remove(5, 4);
//	std::cout << "After remove: ";
//	s1.print();
//
//	String sub = s1.Substr(0, 5);
//	std::cout << "Substring (0,5): ";
//	sub.print();
//
//	std::cout << "Enter a new line: ";
//	s2.read();
//	std::cout << "You entered: ";
//	s2.print();
//	
//	return 0;
//}