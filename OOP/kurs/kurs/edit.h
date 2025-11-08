#ifndef EDITDIALOGWINDOW_H
#define EDITDIALOGWINDOW_H

#include <QDialog>
#include <QLineEdit>

class MainWindow;

class EditDialogWindow : public QDialog
{
    Q_OBJECT

private:
    MainWindow* mainWindow;
    QLineEdit* userNameEdit;
    QLineEdit* yearInstalledEdit;
    QLineEdit* numberEdit;
    QLineEdit* modelEdit;

private slots:
    void EditButtonExec();
    void CloseEditWindow();

public:
    EditDialogWindow(QWidget* parent = 0);

    void SetElementText();
};

#endif // EDITDIALOGWINDOW_H
