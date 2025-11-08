#ifndef OPENDIALOGWINDOW_H
#define OPENDIALOGWINDOW_H

#include <QDialog>
#include <QLineEdit>
#include <QBoxLayout>
#include <QPushButton>

class MainWindow;

class OpenDialogWindow : public QDialog
{
    Q_OBJECT

private:
    QLineEdit* DataBaseName;
    MainWindow* mainWindow;

private slots:
    void OpenButtonExec();
    void CloseOpenWindow();

public:

public:
    OpenDialogWindow(QWidget* parent = 0);
};

#endif // OPENDIALOGWINDOW_H
