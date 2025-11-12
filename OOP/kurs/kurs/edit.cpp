#include "edit.h"
#include "database.h"
#include "mainwindow.h"
#include "ui_mainwindow.h"

#include <QLabel>
#include <QPushButton>

EditDialogWindow::EditDialogWindow(QWidget* parent)
    : QDialog( parent )
    , mainWindow((MainWindow*)parent)
{
    setWindowTitle("Редактирование элемента");
        setWindowFlags(Qt::Dialog);
        setWindowModality(Qt::WindowModal);

        resize(300, 200);

        QLabel* userNameLabel = new QLabel(this);
        userNameLabel->move(10, 30);
        userNameLabel->setText("Введите фирму-изготовитель: ");

        userNameEdit = new QLineEdit(this);
        userNameEdit->move(170, 30);

        QLabel* yearInstalledLabel = new QLabel(this);
        yearInstalledLabel->move(10, 60);
        yearInstalledLabel->setText("Введите размер: ");

        yearInstalledEdit = new QLineEdit(this);
        yearInstalledEdit->move(170, 60);

        QLabel* NumberLabel = new QLabel(this);
        NumberLabel->move(10, 90);
        NumberLabel->setText("Введите цену: ");

        numberEdit = new QLineEdit(this);
        numberEdit->move(170, 90);

        QLabel* ModelLabel = new QLabel(this);
        ModelLabel->move(10, 120);
        ModelLabel->setText("Введите диагональ: ");

        modelEdit = new QLineEdit(this);
        modelEdit->move(170, 120);

        QPushButton* EditButton = new QPushButton(this);
        EditButton->setText("Редактировать");
        EditButton->move(200, 150);
        connect(EditButton, SIGNAL(clicked()), this, SLOT(EditButtonExec()));

        QPushButton* BackButton = new QPushButton(this);
        BackButton->setText("Вернуться назад");
        BackButton->move(20, 150);
        connect(BackButton, SIGNAL(clicked()), this, SLOT(CloseEditWindow()));
}

void EditDialogWindow::CloseEditWindow()
{
    userNameEdit->clear();
    yearInstalledEdit->clear();
    numberEdit->clear();
    modelEdit->clear();

    close();
}

void EditDialogWindow::SetElementText()
{
    int selectedRow = mainWindow->ui->tableWidget->selectionModel()->selectedRows().at(0).row();

    userNameEdit->setText(mainWindow->ui->tableWidget->item(selectedRow, 1)->text());
    yearInstalledEdit->setText(mainWindow->ui->tableWidget->item(selectedRow, 2)->text());
    numberEdit->setText(mainWindow->ui->tableWidget->item(selectedRow, 3)->text());
    modelEdit->setText(mainWindow->ui->tableWidget->item(selectedRow, 4)->text());
}

void EditDialogWindow::EditButtonExec()
{
    int selectedRow = mainWindow->ui->tableWidget->selectionModel()->selectedRows().at(0).row();
    QString number = mainWindow->ui->tableWidget->item(selectedRow, 3)->text();

    QString fileName = "C:/Programs/Programs/Study/OOP/kurs/kurs/db/";
    fileName += *mainWindow->currentDatabaseName + ".txt";

    QFile dbFile(fileName);
    if(dbFile.open(QIODevice::ReadWrite)){
        QStringList list;
        QTextStream line(&dbFile);
        while(!line.atEnd())
        {
            list.push_back(line.readLine());
        }

        dbFile.resize(0);

        for(int i = 0; i < list.size(); ++i)
        {
            if(i == selectedRow)
                line << 0 << ":" << userNameEdit->text() << "|"
                     << 1 << ":" << yearInstalledEdit->text() << "|"
                     << 1 << ":" << numberEdit->text() << "|"
                     << 1 << ":" << modelEdit->text() << "|" << '\n';
            else
                line << list[i] << '\n';
        }

        dbFile.close();
    }

    mainWindow->db->db.DeleteAll();
    mainWindow->db->openDataBase(*mainWindow->currentDatabaseName + ".txt");

    mainWindow->ui->tableWidget->clear();
    mainWindow->ui->tableWidget->setRowCount(0);
    mainWindow->ui->tableWidget->setColumnCount(0);

    mainWindow->createUI(QStringList() << ("ID")
                         << ("Фирма-изготовитель")
                         << ("Размер")
                         << ("Цена")
                         << ("Диагональ")
       );

    userNameEdit->clear();
    yearInstalledEdit->clear();
    numberEdit->clear();
    modelEdit->clear();
    close();
}







