#ifndef UNITEDIALOGWINDOW_H
#define UNITEDIALOGWINDOW_H

#include <QDialog>
#include <QLineEdit>
#include <QBoxLayout>
#include <QPushButton>

#include "database.h"

class MainWindow;

class UniteDialogWindow : public QDialog
{
    Q_OBJECT

private:
    QLineEdit* DataBaseName;
    QPushButton* CreateButton;
    MainWindow* mainWindow;

private slots:
    void UniteButtonExec();
    void CloseUniteWindow();

public:
    UniteDialogWindow(QWidget* parent = 0);
};

#endif // UNITEDIALOGWINDOW_H
