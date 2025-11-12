#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>

QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

class MainWindow : public QMainWindow {
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

private slots:
    void onAddClicked();
    void onSubClicked();
    void onMulClicked();
    void onDivClicked();
    void onClearOperand1();
    void onClearOperand2();
    void onClearClicked();
    void Swap();
    void SwapOp(QString op1, QString op2);
    void Copy(QString op, short name);
    void Copy1();
    void Copy2();
    void onExitClicked();
    void onAboutClicked();

private:
    Ui::MainWindow *ui;
    void calculate(char op);
};

#endif // MAINWINDOW_H
