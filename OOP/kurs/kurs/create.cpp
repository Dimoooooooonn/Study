#include "create.h"
#include <QLabel>

#include "mainwindow.h"
#include "ui_mainwindow.h"

CreateDialogWindow::CreateDialogWindow(QWidget* parent)
    : QDialog( parent )
    , mainWindow((MainWindow*)parent)
{
    setWindowTitle("Создание новой БД");
        setWindowFlags(Qt::Dialog);
        setWindowModality(Qt::WindowModal);

        resize(300, 200);

        QLabel* dataBaseLabel = new QLabel(this);
        dataBaseLabel->move(10, 60);
        dataBaseLabel->setText("Введите имя БД: ");

        QPushButton* CreateButton = new QPushButton(this);
        CreateButton->setText("Создать");
        CreateButton->move(200, 150);
        connect(CreateButton, SIGNAL(clicked()), this, SLOT(CreateButtonExec()));

        QPushButton* BackButton = new QPushButton(this);
        BackButton->setText("Вернуться назад");
        BackButton->move(20, 150);
        connect(BackButton, SIGNAL(clicked()), this, SLOT(CloseCreateWindow()));

        DataBaseName = new QLineEdit(this);
        DataBaseName->move(170, 60);
}

void CreateDialogWindow::CloseCreateWindow()
{
    DataBaseName->clear();
    close();
}

void CreateDialogWindow::CreateButtonExec()
{
    if(!DataBaseName->text().isEmpty())
    {
        mainWindow->ui->tableWidget->clear();
        mainWindow->ui->tableWidget->setRowCount(0);
        mainWindow->ui->tableWidget->setColumnCount(0);

        mainWindow->db = new DataBase();
        mainWindow->db->restoreDataBase(DataBaseName->text() + ".txt");
        mainWindow->currentDatabaseName = new QString(DataBaseName->text());

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





