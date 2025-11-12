#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow),
    db(nullptr),
    currentDatabaseName(nullptr)
{
    ui->setupUi(this);

    createDialog = new CreateDialogWindow(this);

    addDialog = new AddDialogWindow(this);

    openDialog = new OpenDialogWindow(this);

    editDialog = new EditDialogWindow(this);

    uniteDialog = new UniteDialogWindow(this);

    this->setWindowTitle("База данных по телевизорам");

    this->createUI(QStringList() << ("ID")
                   << ("Фирма-изготовитель")
                   << ("Размер")
                   << ("Цена")
                   << ("Диагональ")
 );
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::createUI(const QStringList &headers)
{
    ui->tableWidget->setColumnCount(5);
    ui->tableWidget->setShowGrid(true);

    ui->tableWidget->setSelectionMode(QAbstractItemView::SingleSelection);

    ui->tableWidget->setSelectionBehavior(QAbstractItemView::SelectRows);

    ui->tableWidget->setHorizontalHeaderLabels(headers);

    ui->tableWidget->horizontalHeader()->setStretchLastSection(true);

    ui->tableWidget->hideColumn(0);

    if(db) FillTableWidget();
}

void MainWindow::FillTableWidget()
{
    for(int i = 0; i < db->db.GetSize(); ++i){


        ui->tableWidget->insertRow(i);

        ui->tableWidget->setItem(i,0, new QTableWidgetItem(i));
        ui->tableWidget->setItem(i,1, new QTableWidgetItem(QString(db->db[i].GetValues()->c_str())));
        ui->tableWidget->setItem(i,2, new QTableWidgetItem(QString((db->db[i].GetValues() + 1)->c_str())));
        ui->tableWidget->setItem(i,3, new QTableWidgetItem(QString((db->db[i].GetValues() + 2)->c_str())));
        ui->tableWidget->setItem(i,4, new QTableWidgetItem(QString((db->db[i].GetValues() + 3)->c_str())));
    }

    ui->tableWidget->resizeColumnsToContents();

    UpdateSost();
}

void MainWindow::UpdateSost()
{
    std::string res = "Кол-во телевизоров: ";
    res += std::to_string(db ? db->db.GetSize() : 0);
    ui->sost->setText(QString(res.c_str()));
}

void MainWindow::on_actionAbout_triggered()
{
    QMessageBox::information(this, "О программе",
                             "Выполнил:\n"
                             "Студент третьего курса\n"
                             "Группы ИКПИ-32\n"
                             "Филимонов Дмитрий Евгеньевич",
                             QMessageBox::Ok);
}


void MainWindow::on_toolButton_About_pressed()
{
    on_actionAbout_triggered();
}


void MainWindow::on_actionRemove_triggered()
{
    if(!db || !ui->tableWidget->selectionModel()->hasSelection()) return;

    int selectedRow = ui->tableWidget->selectionModel()->selectedRows().at(0).row();
    QString number = ui->tableWidget->item(selectedRow, 3)->text();

    QString fileName = "G:/study/works/OOP/kurs/kurs/db/";
    fileName += *currentDatabaseName + ".txt";

    QFile dbFile(fileName);
    if(dbFile.open(QIODevice::ReadWrite)){
        QStringList list;
        QTextStream line(&dbFile);
        while(!line.atEnd())
        {
            list.push_back(line.readLine());
        }

        list.removeAt(selectedRow);
        dbFile.resize(0);

        for(int i = 0; i < list.size(); ++i)
            line << list[i] << '\n';

        dbFile.close();
    }

    db->db.DeleteAll();
    db->openDataBase(*currentDatabaseName + ".txt");

    ui->tableWidget->clear();
    ui->tableWidget->setRowCount(0);
    ui->tableWidget->setColumnCount(0);

    createUI(QStringList() << ("ID")
                                 << ("Фирма-изготовитель")
                                 << ("Размер")
                                 << ("Цена")
                                 << ("Диагональ")
               );
}


void MainWindow::on_actionEdit_triggered()
{
    if(!db || !ui->tableWidget->selectionModel()->hasSelection()) return;

    editDialog->SetElementText();
    editDialog->exec();
}

void MainWindow::on_lineEdit_textChanged(const QString &arg1)
{
    if(!db) return;

    int res = -1;
    for(int i = 0; i < ui->tableWidget->rowCount(); ++i)
    {
        if(ui->tableWidget->item(i, 1)->text().toStdString().find(ui->lineEdit->text().toStdString()) != std::string::npos)
        {
            res = i;
            break;
        }
    }

    if(!ui->lineEdit->text().isEmpty() && res != -1)
    {
        ui->tableWidget->selectRow(res);
    }
    else
    {
        ui->tableWidget->clearSelection();
    }
}


void MainWindow::on_actionAdd_triggered()
{
    if(!db) return;

    addDialog->exec();
}


void MainWindow::on_actionUnite_triggered()
{
    if(!db) return;

    uniteDialog->exec();
}


void MainWindow::on_actionCreate_triggered()
{
    createDialog->exec();
}


void MainWindow::on_actionOpen_triggered()
{
    openDialog->exec();
}


void MainWindow::on_actionExit_triggered()
{
    int result =QMessageBox::warning(this, tr("Exit"), "Do you want to exit?", QMessageBox::No
                                     , QMessageBox::Yes | QMessageBox::Default);

    if (result == QMessageBox::Yes)
    {
        close();
    }
}


void MainWindow::on_toolButton_New_clicked()
{
    on_actionCreate_triggered();
}


void MainWindow::on_toolButton_Edit_clicked()
{
    on_actionEdit_triggered();
}


void MainWindow::on_toolButton_Unite_clicked()
{
    on_actionUnite_triggered();
}


void MainWindow::on_toolButton_Open_clicked()
{
    on_actionOpen_triggered();
}


void MainWindow::on_toolButton_Add_clicked()
{
    on_actionAdd_triggered();
}


void MainWindow::on_toolButton_Cut_clicked()
{
    on_actionRemove_triggered();
}

void MainWindow::on_toolButton_Exit_clicked()
{
    on_actionExit_triggered();
}

