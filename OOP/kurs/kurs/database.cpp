#include "database.h"
#include <QDir>

DataBase::DataBase(QObject *parent) : QObject(parent)
{

}

DataBase::~DataBase()
{

}

void DataBase::connectToDataBase(QString DataBaseName)
{
    /* Перед подключением к базе данных производим проверку на её существование.
     * В зависимости от результата производим открытие базы данных или её восстановление
     * */

    QString fileName = "C:/Programs/Programs/Study/OOP/kurs/kurs/db/";
    fileName += DataBaseName;
    if(!QFile(fileName).exists()){
        this->restoreDataBase(DataBaseName);
    } else {
        this->openDataBase(DataBaseName);
    }
}

/* Методы восстановления базы данных
 * */
bool DataBase::restoreDataBase(QString DataBaseName)
{
    QString fileName = "C:/Programs/Programs/Study/OOP/kurs/kurs/db/";
    fileName += DataBaseName;
    dbName = DataBaseName;

    QDir dir("C:/Programs/Programs/Study/OOP/kurs/kurs/db/");
    if (!dir.exists()) {
        if (!dir.mkpath(".")) {
            qDebug() << "Ошибка: не удалось создать каталог базы данных!";
            return false;
        }
    }

    QFile dbFile(fileName);
    if (!dbFile.open(QIODevice::WriteOnly | QIODevice::Truncate)) {
        qDebug() << "Ошибка: не удалось открыть файл для записи:" << fileName
                 << " (" << dbFile.errorString() << ")";
        return false;
    }

    QTextStream edit(&dbFile);
    edit << '~';
    dbFile.close();

    qDebug() << "База данных создана:" << fileName;
    return true; // ← ОБЯЗАТЕЛЬНО!
}


bool DataBase::openDataBase(QString DataBaseName)
{
    QString fileName = "C:/Programs/Programs/Study/OOP/kurs/kurs/db/";
    fileName += DataBaseName;
    dbName = DataBaseName;

    QFile dbFile(fileName);

    if(dbFile.open(QIODevice::ReadWrite)){
        QStringList list;
        QTextStream line(&dbFile);
        while(!line.atEnd())
        {
            list.push_back(line.readLine());
        }

        ReadFile(list);
        dbFile.close();

        return true;
    } else {
        return false;
    }
}

void DataBase::ReadFile(QStringList data)
{
   for(int j = 0; j < data.size() - 1; ++j)
   {
       int ptr = 0;
        std::string* str = new std::string[4];
        for(int i = 0; i < 4; ++i)
        {
            ReadItem(data[j].toStdString(), ptr, str + i);
        }

        db.AddElem(str);
    ptr += 2; // \n
   }
}

void DataBase::ReadItem(std::string data, int& ptr, std::string* str)
{
    ptr += 2; // number:
    while(data[ptr] != '|')
    {
        std::string s; s.push_back(data[ptr]);
        str->append(s);
        ++ptr;
    }

    ++ptr; // |
}

void DataBase::closeDataBase()
{

}

/* Метод для создания таблицы устройств в базе данных
 * */
bool DataBase::createDeviceTable()
{
    return false;
}

/* Метод для вставки записи в таблицу устройств
 * */
bool DataBase::inserIntoDeviceTable(const QVariantList &data)
{
    QString fileName = "C:/Programs/Programs/Study/OOP/kurs/kurs/db/";
    fileName += dbName;

    QFile dbFile(fileName);
    dbFile.open(QIODevice::ReadWrite);

    QByteArray dataa;
    dataa = dbFile.readAll();
    dbFile.resize(dataa.size() - 1);

    QTextStream edit(&dbFile);
    std::string* str = new std::string[4];
    for(int i = 0; i < data.size(); ++i)
    {
        *(str + i) = data.at(i).toString().toStdString();
        edit << i << ':' << data.at(i).toString() << '|';
    }
    edit << '\n';
    edit << '~';

    db.AddElem(str);

    return true;
}
