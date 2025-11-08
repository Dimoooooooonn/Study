#include "open.h"

#include <QLabel>

#include "mainwindow.h"
#include "ui_mainwindow.h"

OpenDialogWindow::OpenDialogWindow(QWidget* parent)
    : QDialog( parent )
    , mainWindow((MainWindow*)parent)
{
    setWindowTitle("Открыть БД");
        setWindowFlags(Qt::Dialog);
        setWindowModality(Qt::WindowModal);

        resize(300, 200);

        QLabel* dataBaseLabel = new QLabel(this);
        dataBaseLabel->move(10, 60);
        dataBaseLabel->setText("Введите имя БД: ");

        QPushButton* OpenButton = new QPushButton(this);
        OpenButton->setText("Открыть");
        OpenButton->move(200, 150);
        connect(OpenButton, SIGNAL(clicked()), this, SLOT(OpenButtonExec()));

        QPushButton* BackButton = new QPushButton(this);
        BackButton->setText("Отменить действие");
        BackButton->move(20, 150);
        connect(BackButton, SIGNAL(clicked()), this, SLOT(CloseOpenWindow()));

        DataBaseName = new QLineEdit(this);
        DataBaseName->move(170, 60);
}

void OpenDialogWindow::CloseOpenWindow()
{
    DataBaseName->clear();

    close();
}

void OpenDialogWindow::OpenButtonExec()
{
    if(!DataBaseName->text().isEmpty())
    {
        mainWindow->ui->tableWidget->clear();
        mainWindow->ui->tableWidget->setRowCount(0);
        mainWindow->ui->tableWidget->setColumnCount(0);

        mainWindow->db = new DataBase();
        mainWindow->db->openDataBase(DataBaseName->text() + ".txt");

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









