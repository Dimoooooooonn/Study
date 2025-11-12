#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <QMessageBox>

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    // Установка свойства readOnly для поля результата
    ui->lineEditResult->setReadOnly(true);

    // Подключение кнопок операций
    connect(ui->buttonAdd, &QPushButton::clicked, this, &MainWindow::onAddClicked);
    connect(ui->buttonSub, &QPushButton::clicked, this, &MainWindow::onSubClicked);
    connect(ui->buttonMul, &QPushButton::clicked, this, &MainWindow::onMulClicked);
    connect(ui->buttonDiv, &QPushButton::clicked, this, &MainWindow::onDivClicked);

    // Подключение кнопки очистки
    connect(ui->buttonClear, &QPushButton::clicked, this, &MainWindow::onClearClicked);
    connect(ui->pushButton_ClearOperand1, &QPushButton::clicked, this, &MainWindow::onClearOperand1);
    connect(ui->pushButton_ClearOperand2, &QPushButton::clicked, this, &MainWindow::onClearOperand2);

    connect(ui->ButtonSwap, &QPushButton::clicked, this, &MainWindow::Swap);
    connect(ui->ButtonCopy1, &QPushButton::clicked, this, &MainWindow::Copy1);
    connect(ui->ButtonCopy2, &QPushButton::clicked, this, &MainWindow::Copy2);

    // Подключение действий меню
    connect(ui->actionExit, &QAction::triggered, this, &MainWindow::onExitClicked);
    connect(ui->actionAbout, &QAction::triggered, this, &MainWindow::onAboutClicked);
}

MainWindow::~MainWindow() {
    delete ui;
}

void MainWindow::calculate(char op) {
    bool ok1, ok2;
    double operand1 = ui->lineEditOperand1->text().toDouble(&ok1);
    double operand2 = ui->lineEditOperand2->text().toDouble(&ok2);

    if (!ok1 || !ok2) {
        QMessageBox::warning(this, "Error", "Please enter valid numbers!");
        return;
    }

    double result = 0;
    switch (op) {
    case '+': result = operand1 + operand2; break;
    case '-': result = operand1 - operand2; break;
    case '*': result = operand1 * operand2; break;
    case '/':
        if (operand2 == 0) {
            QMessageBox::warning(this, "Error", "Division by zero!");
            return;
        }
        result = operand1 / operand2;
        break;
    }

    ui->lineEditResult->setText(QString::number(result));
}

void MainWindow::SwapOp(QString op1, QString op2){
    QString temp = op1;
    ui->lineEditOperand1->setText(op2);
    ui->lineEditOperand2->setText(temp);
}

void MainWindow::Copy(QString op, short name){
    switch (name) {
    case 1:
        ui->lineEditOperand2->setText(op);
        break;
    case 2:
        ui->lineEditOperand1->setText(op);
        break;
    default:
        break;
    }
}

void MainWindow::onAddClicked() {
    calculate('+');
}

void MainWindow::onSubClicked() {
    calculate('-');
}

void MainWindow::onMulClicked() {
    calculate('*');
}

void MainWindow::onDivClicked() {
    calculate('/');
}

void MainWindow::onClearOperand1(){
    ui->lineEditOperand1->clear();
    ui->lineEditResult->clear();
}

void MainWindow::onClearOperand2(){
    ui->lineEditOperand2->clear();
    ui->lineEditResult->clear();
}

void MainWindow::onClearClicked() {
    ui->lineEditOperand1->clear();
    ui->lineEditOperand2->clear();
    ui->lineEditResult->clear();
}

void MainWindow::Swap(){
    QString op1 = ui->lineEditOperand1->text();
    QString op2 = ui->lineEditOperand2->text();
    SwapOp(op1, op2);
}

void MainWindow::Copy1(){
    QString op = ui->lineEditOperand1->text();
    short name = 1;
    Copy(op, name);
}

void MainWindow::Copy2(){
    QString op = ui->lineEditOperand2->text();
    short name = 2;
    Copy(op, name);
}

void MainWindow::onExitClicked() {
    QApplication::quit();
}

void MainWindow::onAboutClicked() {
    QMessageBox::information(this, "About", "Simple Calculator\nCreated with Qt.");
}
