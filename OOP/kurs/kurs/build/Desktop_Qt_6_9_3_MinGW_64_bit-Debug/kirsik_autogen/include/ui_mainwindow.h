/********************************************************************************
** Form generated from reading UI file 'mainwindow.ui'
**
** Created by: Qt User Interface Compiler version 6.9.3
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_MAINWINDOW_H
#define UI_MAINWINDOW_H

#include <QtCore/QVariant>
#include <QtGui/QAction>
#include <QtGui/QIcon>
#include <QtWidgets/QApplication>
#include <QtWidgets/QHeaderView>
#include <QtWidgets/QLabel>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenu>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QTableWidget>
#include <QtWidgets/QToolButton>
#include <QtWidgets/QVBoxLayout>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_MainWindow
{
public:
    QAction *actionCreate;
    QAction *actionUnite;
    QAction *actionOpen;
    QAction *actionExit;
    QAction *actionAdd;
    QAction *actionRemove;
    QAction *actionAbout;
    QAction *actionEdit;
    QWidget *centralwidget;
    QLabel *label;
    QTableWidget *tableWidget;
    QLineEdit *lineEdit;
    QLabel *sost;
    QWidget *verticalLayoutWidget;
    QVBoxLayout *verticalLayout;
    QToolButton *toolButton_Edit;
    QToolButton *toolButton_Open;
    QToolButton *toolButton_About;
    QToolButton *toolButton_Cut;
    QToolButton *toolButton_Add;
    QToolButton *toolButton_New;
    QToolButton *toolButton_Unite;
    QMenuBar *menubar;
    QMenu *menuggggg;
    QMenu *menuwerwewe;
    QMenu *menuwqfqwfq;
    QStatusBar *statusbar;

    void setupUi(QMainWindow *MainWindow)
    {
        if (MainWindow->objectName().isEmpty())
            MainWindow->setObjectName("MainWindow");
        MainWindow->resize(522, 400);
        MainWindow->setMinimumSize(QSize(500, 400));
        actionCreate = new QAction(MainWindow);
        actionCreate->setObjectName("actionCreate");
        actionUnite = new QAction(MainWindow);
        actionUnite->setObjectName("actionUnite");
        actionOpen = new QAction(MainWindow);
        actionOpen->setObjectName("actionOpen");
        actionExit = new QAction(MainWindow);
        actionExit->setObjectName("actionExit");
        actionAdd = new QAction(MainWindow);
        actionAdd->setObjectName("actionAdd");
        actionRemove = new QAction(MainWindow);
        actionRemove->setObjectName("actionRemove");
        actionAbout = new QAction(MainWindow);
        actionAbout->setObjectName("actionAbout");
        actionEdit = new QAction(MainWindow);
        actionEdit->setObjectName("actionEdit");
        centralwidget = new QWidget(MainWindow);
        centralwidget->setObjectName("centralwidget");
        label = new QLabel(centralwidget);
        label->setObjectName("label");
        label->setGeometry(QRect(10, 0, 66, 18));
        tableWidget = new QTableWidget(centralwidget);
        tableWidget->setObjectName("tableWidget");
        tableWidget->setGeometry(QRect(10, 30, 451, 291));
        lineEdit = new QLineEdit(centralwidget);
        lineEdit->setObjectName("lineEdit");
        lineEdit->setGeometry(QRect(50, 0, 241, 26));
        sost = new QLabel(centralwidget);
        sost->setObjectName("sost");
        sost->setGeometry(QRect(10, 330, 221, 18));
        verticalLayoutWidget = new QWidget(centralwidget);
        verticalLayoutWidget->setObjectName("verticalLayoutWidget");
        verticalLayoutWidget->setGeometry(QRect(470, 30, 42, 311));
        verticalLayout = new QVBoxLayout(verticalLayoutWidget);
        verticalLayout->setObjectName("verticalLayout");
        verticalLayout->setContentsMargins(0, 0, 0, 0);
        toolButton_Edit = new QToolButton(verticalLayoutWidget);
        toolButton_Edit->setObjectName("toolButton_Edit");
        QIcon icon(QIcon::fromTheme(QIcon::ThemeIcon::MailMessageNew));
        toolButton_Edit->setIcon(icon);
        toolButton_Edit->setIconSize(QSize(32, 32));

        verticalLayout->addWidget(toolButton_Edit);

        toolButton_Open = new QToolButton(verticalLayoutWidget);
        toolButton_Open->setObjectName("toolButton_Open");
        QIcon icon1(QIcon::fromTheme(QString::fromUtf8("document-open")));
        toolButton_Open->setIcon(icon1);
        toolButton_Open->setIconSize(QSize(32, 32));

        verticalLayout->addWidget(toolButton_Open);

        toolButton_About = new QToolButton(verticalLayoutWidget);
        toolButton_About->setObjectName("toolButton_About");
        QIcon icon2(QIcon::fromTheme(QString::fromUtf8("dialog-information")));
        toolButton_About->setIcon(icon2);
        toolButton_About->setIconSize(QSize(32, 32));

        verticalLayout->addWidget(toolButton_About);

        toolButton_Cut = new QToolButton(verticalLayoutWidget);
        toolButton_Cut->setObjectName("toolButton_Cut");
        QIcon icon3(QIcon::fromTheme(QString::fromUtf8("edit-cut")));
        toolButton_Cut->setIcon(icon3);
        toolButton_Cut->setIconSize(QSize(32, 32));

        verticalLayout->addWidget(toolButton_Cut);

        toolButton_Add = new QToolButton(verticalLayoutWidget);
        toolButton_Add->setObjectName("toolButton_Add");
        QIcon icon4(QIcon::fromTheme(QString::fromUtf8("document-new")));
        toolButton_Add->setIcon(icon4);
        toolButton_Add->setIconSize(QSize(32, 32));

        verticalLayout->addWidget(toolButton_Add);

        toolButton_New = new QToolButton(verticalLayoutWidget);
        toolButton_New->setObjectName("toolButton_New");
        QIcon icon5(QIcon::fromTheme(QString::fromUtf8("appointment-new")));
        toolButton_New->setIcon(icon5);
        toolButton_New->setIconSize(QSize(32, 32));

        verticalLayout->addWidget(toolButton_New);

        toolButton_Unite = new QToolButton(verticalLayoutWidget);
        toolButton_Unite->setObjectName("toolButton_Unite");
        QIcon icon6(QIcon::fromTheme(QString::fromUtf8("edit-select-all")));
        toolButton_Unite->setIcon(icon6);
        toolButton_Unite->setIconSize(QSize(32, 32));

        verticalLayout->addWidget(toolButton_Unite);

        MainWindow->setCentralWidget(centralwidget);
        menubar = new QMenuBar(MainWindow);
        menubar->setObjectName("menubar");
        menubar->setGeometry(QRect(0, 0, 522, 21));
        menuggggg = new QMenu(menubar);
        menuggggg->setObjectName("menuggggg");
        menuwerwewe = new QMenu(menubar);
        menuwerwewe->setObjectName("menuwerwewe");
        menuwqfqwfq = new QMenu(menubar);
        menuwqfqwfq->setObjectName("menuwqfqwfq");
        MainWindow->setMenuBar(menubar);
        statusbar = new QStatusBar(MainWindow);
        statusbar->setObjectName("statusbar");
        MainWindow->setStatusBar(statusbar);

        menubar->addAction(menuggggg->menuAction());
        menubar->addAction(menuwerwewe->menuAction());
        menubar->addAction(menuwqfqwfq->menuAction());
        menuggggg->addAction(actionCreate);
        menuggggg->addAction(actionUnite);
        menuggggg->addAction(actionOpen);
        menuggggg->addAction(actionExit);
        menuwerwewe->addAction(actionAdd);
        menuwerwewe->addAction(actionRemove);
        menuwerwewe->addAction(actionEdit);
        menuwqfqwfq->addAction(actionAbout);

        retranslateUi(MainWindow);

        QMetaObject::connectSlotsByName(MainWindow);
    } // setupUi

    void retranslateUi(QMainWindow *MainWindow)
    {
        MainWindow->setWindowTitle(QCoreApplication::translate("MainWindow", "MainWindow", nullptr));
        actionCreate->setText(QCoreApplication::translate("MainWindow", "\320\241\320\276\320\267\320\264\320\260\321\202\321\214 \320\275\320\276\320\262\321\203\321\216 \320\221\320\224", nullptr));
        actionUnite->setText(QCoreApplication::translate("MainWindow", "\320\236\320\261\321\212\320\265\320\264\320\270\320\275\320\270\321\202\321\214 \320\221\320\224", nullptr));
        actionOpen->setText(QCoreApplication::translate("MainWindow", "\320\236\321\202\320\272\321\200\321\213\321\202\321\214 \320\221\320\224", nullptr));
        actionExit->setText(QCoreApplication::translate("MainWindow", "\320\222\321\213\320\271\321\202\320\270", nullptr));
        actionAdd->setText(QCoreApplication::translate("MainWindow", "\320\224\320\276\320\261\320\260\320\262\320\270\321\202\321\214", nullptr));
        actionRemove->setText(QCoreApplication::translate("MainWindow", "\320\243\320\264\320\260\320\273\320\270\321\202\321\214", nullptr));
        actionAbout->setText(QCoreApplication::translate("MainWindow", "\320\236 \320\277\321\200\320\276\320\263\321\200\320\260\320\274\320\274\320\265", nullptr));
        actionEdit->setText(QCoreApplication::translate("MainWindow", "\320\240\320\265\320\264\320\260\320\272\321\202\320\270\321\200\320\276\320\262\320\260\321\202\321\214", nullptr));
        label->setText(QCoreApplication::translate("MainWindow", "Search", nullptr));
        sost->setText(QString());
        toolButton_Edit->setText(QString());
        toolButton_Open->setText(QCoreApplication::translate("MainWindow", "...", nullptr));
        toolButton_About->setText(QCoreApplication::translate("MainWindow", "...", nullptr));
        toolButton_Cut->setText(QCoreApplication::translate("MainWindow", "...", nullptr));
        toolButton_Add->setText(QCoreApplication::translate("MainWindow", "...", nullptr));
        toolButton_New->setText(QCoreApplication::translate("MainWindow", "...", nullptr));
        toolButton_Unite->setText(QCoreApplication::translate("MainWindow", "...", nullptr));
        menuggggg->setTitle(QCoreApplication::translate("MainWindow", "\320\244\320\260\320\271\320\273", nullptr));
        menuwerwewe->setTitle(QCoreApplication::translate("MainWindow", "\320\227\320\260\320\277\320\270\321\201\321\214", nullptr));
        menuwqfqwfq->setTitle(QCoreApplication::translate("MainWindow", "\320\237\320\276\320\274\320\276\321\211\321\214", nullptr));
    } // retranslateUi

};

namespace Ui {
    class MainWindow: public Ui_MainWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_MAINWINDOW_H
