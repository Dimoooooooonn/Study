#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QtSql/qsqlquery.h>
#include <QMessageBox>

/* My includes */
#include "database.h"
#include "create.h"
#include "add.h"
#include "open.h"
#include "edit.h"
#include "unite.h"

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();

private slots:
    void on_actionAbout_triggered();
    void on_toolButton_About_pressed();

    void on_actionRemove_triggered();

    void on_actionEdit_triggered();

    void on_lineEdit_textChanged(const QString &arg1);

    void on_actionAdd_triggered();

    void on_actionUnite_triggered();

    void on_actionCreate_triggered();

    void on_actionOpen_triggered();

    void on_actionExit_triggered();

    void on_toolButton_New_clicked();

    void on_toolButton_Edit_clicked();

    void on_toolButton_Unite_clicked();

    void on_toolButton_Open_clicked();

    void on_toolButton_Add_clicked();

    void on_toolButton_Cut_clicked();

    void on_toolButton_Exit_clicked();

public:
    Ui::MainWindow  *ui;
    DataBase* db;
    QString* currentDatabaseName;
    int dbElementsCount;

private:
    CreateDialogWindow* createDialog;
    AddDialogWindow* addDialog;
    OpenDialogWindow* openDialog;
    EditDialogWindow* editDialog;
    UniteDialogWindow* uniteDialog;

public:
    void createUI(const QStringList &headers);
    void FillTableWidget();
    void UpdateSost();
};

#endif // MAINWINDOW_H
