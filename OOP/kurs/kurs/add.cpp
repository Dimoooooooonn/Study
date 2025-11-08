#include "add.h"

#include <QLabel>
#include <QPushButton>

#include "mainwindow.h"
#include "ui_mainwindow.h"

AddDialogWindow::AddDialogWindow(QWidget* parent)
    : QDialog( parent )
    , mainWindow((MainWindow*)parent)
{
    setWindowTitle("Добавление нового элемента");
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

        QPushButton* AddButton = new QPushButton(this);
        AddButton->setText("Добавить");
        AddButton->move(200, 150);
        connect(AddButton, SIGNAL(clicked()), this, SLOT(AddButtonExec()));

        QPushButton* BackButton = new QPushButton(this);
        BackButton->setText("Вернуться назад");
        BackButton->move(20, 150);
        connect(BackButton, SIGNAL(clicked()), this, SLOT(CloseBackWindow()));
}

void AddDialogWindow::CloseBackWindow()
{
    userNameEdit->clear();
    yearInstalledEdit->clear();
    numberEdit->clear();
    modelEdit->clear();

    close();
}

void AddDialogWindow::AddButtonExec()
{
    if(!userNameEdit->text().isEmpty() && !yearInstalledEdit->text().isEmpty()
            && !numberEdit->text().isEmpty() && !modelEdit->text().isEmpty())
    {
    mainWindow->db->inserIntoDeviceTable(QVariantList() << userNameEdit->text()
                                                << yearInstalledEdit->text()
                                                << numberEdit->text()
                                                << modelEdit->text());

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
    }
    close();
}





