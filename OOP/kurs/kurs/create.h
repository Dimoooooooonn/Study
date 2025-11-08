#ifndef CREATEDIALOGWINDOW_H
#define CREATEDIALOGWINDOW_H

#include <QDialog>
#include <QLineEdit>
#include <QBoxLayout>
#include <QPushButton>

#include "database.h"

class MainWindow;

class CreateDialogWindow : public QDialog
{
    Q_OBJECT

private:
    QLineEdit* DataBaseName;
    QPushButton* CreateButton;
    MainWindow* mainWindow;

private slots:
    void CreateButtonExec();
    void CloseCreateWindow();

public:
    CreateDialogWindow(QWidget* parent = 0);
};

#endif // CREATEDIALOGWINDOW_H
