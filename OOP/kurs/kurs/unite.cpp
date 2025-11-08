#include "unite.h"
#include "mainwindow.h"
#include "ui_mainwindow.h"

#include <QLabel>

UniteDialogWindow::UniteDialogWindow(QWidget* parent)
    : QDialog( parent )
    , mainWindow((MainWindow*)parent)
{
    setWindowTitle("Объединение БД");
        setWindowFlags(Qt::Dialog);
        setWindowModality(Qt::WindowModal);

        resize(300, 200);

        QLabel* dataBaseLabel = new QLabel(this);
        dataBaseLabel->move(10, 60);
        dataBaseLabel->setText("Введите имя БД: ");

        QPushButton* UniteButton = new QPushButton(this);
        UniteButton->setText("Объединить");
        UniteButton->move(200, 150);
        connect(UniteButton, SIGNAL(clicked()), this, SLOT(UniteButtonExec()));

        QPushButton* BackButton = new QPushButton(this);
        BackButton->setText("Отменить действие");
        BackButton->move(20, 150);
        connect(BackButton, SIGNAL(clicked()), this, SLOT(CloseUniteWindow()));

        DataBaseName = new QLineEdit(this);
        DataBaseName->move(170, 60);
}

void UniteDialogWindow::CloseUniteWindow()
{
    DataBaseName->clear();
    close();
}

void UniteDialogWindow::UniteButtonExec()
{
    if(!DataBaseName->text().isEmpty())
    {
    if(DataBaseName->text() == *mainWindow->currentDatabaseName)
    {
        close();
        return;
    }

    DataBase otherDB;
    otherDB.openDataBase(DataBaseName->text() + ".txt");

    for(int i = 0; i < otherDB.db.GetSize(); ++i)
        mainWindow->db->inserIntoDeviceTable(QVariantList() << QString(otherDB.db[i].GetValues()->c_str())
                                             << QString((otherDB.db[i].GetValues() + 1)->c_str())
                                             << QString((otherDB.db[i].GetValues() + 2)->c_str())
                                             << QString((otherDB.db[i].GetValues() + 3)->c_str()));

    mainWindow->ui->tableWidget->clear();
    mainWindow->ui->tableWidget->setRowCount(0);
    mainWindow->ui->tableWidget->setColumnCount(0);

    mainWindow->createUI(QStringList() << ("ID")
                         << ("Фирма-изготовитель")
                         << ("Размер")
                         << ("Цена")
                         << ("Диагональ")
       );

    DataBaseName->clear();
    }
    close();
}
