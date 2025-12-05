import java.util.LinkedList;
import java.util.Scanner;

public class Main{
    public static Scanner in = new Scanner(System.in);
    public static void main(String[] args){
        menu();
    }

    public static void menu(){
        LavnMover obj1 = new LavnMover("Grizzly SKY-10", 1.0f, "green", 23990f);
        LavnMover obj2 = new LavnMover("Huter GLM-6.0", 1.2f, "black", 36243f);
        LavnMover obj3 = new LavnMover("Gigant PLM-07", 0.8f, "orange", 40645f);
        LavnMover obj4 = new LavnMover("ATLET", 0.7f, "red", 21990f);
        LinkedList<LavnMover> arr = new LinkedList<>();
        arr.add(obj1);
        arr.add(obj2);
        arr.add(obj3);
        arr.add(obj4);
        printMenu();
        int i = in.nextInt();
        while (i != 0) {
            switch (i) {
                case 1:
                    demonstration();
                    break;
                case 2:
                    arr.add(createObj());
                    break;
                case 3:
                    dellObj(arr);
                    break;
                case 4:
                    printAll(arr);
                    break;
                case 5:
                    changeArr(arr);
                    break;
                case 6:
                    findPar(arr);
                    break;
                case 7:
                    discount(arr);
                    break;
                default:
                    break;
            }
            printMenu();
            i = in.nextInt();
        }
    }

    public static void demonstration(){
        LavnMover obj1 = new LavnMover();
        obj1.printValue();
        obj1.setValue("Grizzly SKY-10", 1.0f, "green", 23990f);
        obj1.printValue();

        LavnMover obj2 = new LavnMover("Huter GLM-6.0", 1.2f, "black", 36243f);
        obj2.printValue();
    }

    public static LavnMover createObj(){
        LavnMover obj = new LavnMover();
        return obj;
    }

    public static void dellObj(LinkedList<LavnMover> arr){
        System.out.print("Введите необходимый индекс для удаления: ");
        int i = in.nextInt();
        in.nextLine();
        arr.remove(i);
    }

    public static void printAll(LinkedList<LavnMover> arr){
        for (LavnMover el: arr){
            el.printValue();
        }
    }

    public static void changeArr(LinkedList<LavnMover> arr){
        System.out.print("Введите номер объекта который вы хотите изменить: ");
        int n = in.nextInt();
        in.nextLine();
        int i = 1;
        for (LavnMover el: arr){
            if (i == n){
                System.out.print("Введите цвет: ");
                String color = in.nextLine();
                el.changeColor(color);
            }
            i++;
        }
    }

    public static void findPar(LinkedList<LavnMover> arr){
        System.out.print("Введите номер параметра который вы хотите найти: ");
        int i = in.nextInt();
        in.nextLine();
        if (i>4){
            System.out.println("Нет параметра с таким номером");
        }
        else {
            for (LavnMover el: arr){
                switch (i){
                    case 1:
                        System.out.println(el.getModel());
                        break;
                    case 2:
                        System.out.println(el.getCapasity());
                        break;
                    case 3:
                        System.out.println(el.getColor());
                        break;
                    case 4:
                        System.out.println(el.getPrise());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public static void discount(LinkedList<LavnMover> arr){
        for (LavnMover el: arr){
            el.discount();
        }
    }

    public static void printMenu(){
        System.out.println(
                "1 - демонстрация работы класса\n" +
                        "2 - создание нового объекта и добавление его в массив\n" +
                        "3 - удаление объекта по индексу\n" +
                        "4 - вывести все параметры всех объектов\n" +
                        "5 - изменение цвета объекта по номеру\n" +
                        "6 - поиск параметра по номеру\n" +
                        "7 - применение скидки в 20%\n" +
                        "0 - выход из программы");

        System.out.print("Введите номер действия которое вы хотите сделать: ");
    }
}