#ifndef ADDDIALOGWINDOW_H
#define ADDDIALOGWINDOW_H

#include <QDialog>
#include <QLineEdit>

#include "database.h"

class MainWindow;

class AddDialogWindow : public QDialog
{
    Q_OBJECT

private:
    MainWindow* mainWindow;
    QLineEdit* userNameEdit;
    QLineEdit* yearInstalledEdit;
    QLineEdit* numberEdit;
    QLineEdit* modelEdit;

private slots:
    void AddButtonExec();
    void CloseBackWindow();

public:
    AddDialogWindow(QWidget* parent = 0);
};

#endif // ADDDIALOGWINDOW_H
