#include <iostream>
#include <unordered_map>
#include <chrono>
#include <random>

using namespace std;

string GenerateLine(int lineLength, const string& alphabet, const string& word) {
    string line;
    random_device rd;
    mt19937 random(rd());
    uniform_int_distribution<> dis(0, alphabet.length() - 1);

    for (int i = 0; i < lineLength; i++) {
        line += alphabet[dis(random)];
    }


    for (int i = 0; i <= lineLength - word.length(); i += (dis(random) % 21 + 980)) {
        for (size_t j = 0; j < word.length(); j++) {
            line[i + j] = word[j];
        }
    }

    return line;
}

int BouerMooreSearch(const string& line, const string& word, const string& alphabet) {
    int count = 0;
    unordered_map<char, int> table;

    for (char ch : alphabet) {
        table[ch] = word.length();
    }

    for (size_t i = 0; i < word.length() - 1; i++) {
        table[word[i]] = word.length() - 1 - i;
    }

    int last = word.length() - 1;
    while (last < line.length()) {
        if (word.back() != line[last]) {
            last += table[line[last]];
        }
        else {
            bool flag = true;
            for (size_t i = 1; i < word.length(); i++) {
                if (word[word.length() - 1 - i] != line[last - i]) {
                    last += max(1, table[line[last - i]] - static_cast<int>(i));
                    flag = false;
                    break;
                }
            }
            if (flag) {
                last += word.length();
                count++;
            }
        }
    }
    return count;
}

int BruteforceSearch(const string& line, const string& word) {
    int count = 0;
    for (size_t i = 0; i <= line.length() - word.length(); i++) {
        bool flag = true;
        for (size_t j = 0; j < word.length(); j++) {
            if (word[j] != line[i + j]) {
                flag = false;
                break;
            }
        }
        if (flag) {
            i += word.length() - 1;
            count++;
        }
    }
    return count;
}

void CheckSearch(int checkCount, int lineLength, const string& word, const string& alphabet, const string& search) {
    double time = 0;
    for (int i = 0; i < checkCount; i++) {
        string line = GenerateLine(lineLength, alphabet, word);
        auto start = chrono::high_resolution_clock::now();

        if (search == "BouerMooreSearch") {
            BouerMooreSearch(line, word, alphabet);
        }
        else if (search == "BruteforceSearch") {
            BruteforceSearch(line, word);
        }

        auto stop = chrono::high_resolution_clock::now();
        chrono::duration<double, micro> elapsed = stop - start;
        time += elapsed.count();
    }
    cout << "Время поиска по методу " << search << " при количестве элементов " << lineLength << " -- " << (time / checkCount) << endl;
}

int main() {
    setlocale(LC_ALL, "");
    int checkCount = 10000;
    string alphabet = "abcdefgh";
    string word = "ababcdcdefefghgh";

    for (int i = 1000; i <= 10000; i += 1000) {
        CheckSearch(checkCount, i, word, alphabet, "BouerMooreSearch");
        CheckSearch(checkCount, i, word, alphabet, "BruteforceSearch");
        cout << endl;
    }
    return 0;
}
