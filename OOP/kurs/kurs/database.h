#ifndef DATABASE_H
#define DATABASE_H

#include <QObject>
#include <QFile>
#include <QDate>
#include <QDebug>

#include "arr.h"
#include "tv.h"

/* Директивы имен таблицы, полей таблицы и базы данных */

class DataBase : public QObject
{
    Q_OBJECT
public:
    explicit DataBase(QObject *parent = 0);
    ~DataBase();

    void connectToDataBase(QString DataBaseName);
    bool inserIntoDeviceTable(const QVariantList &data);

    bool openDataBase(QString DataBaseName);
    bool restoreDataBase(QString DataBaseName);
    void closeDataBase();
    bool createDeviceTable();

private:
    void ReadFile(QStringList data);
    void ReadItem(std::string data, int& ptr, std::string* str);

public:
    Array<TV>    db;
    QString dbName;
};

#endif // DATABASE_H
