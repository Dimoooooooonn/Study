import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Main{
    public static Scanner in = new Scanner(System.in);
    public static int n1 = 4;
    public static int n2 = 5;
    public static int minus = 10;

    public static void main(String[] args){
        pervoeZadanie();
        vtoroeZadanie();
        tretieZadanie();
    }

    public static void pervoeZadanie(){
        int[] arr1 = new int[n1];
        int[] arr2 = new int[n2];
        int[] arr3 = new int[n1];
        readArr(arr1, n1);
        readArr(arr2, n2);
        readArr(arr3, n1);
        printArr(arr1, n1);
        printArr(arr2, n2);
        printArr(arr3, n1);
        minusArr(arr1, n1);
        minusArr(arr2, n2);
        minusArr(arr3, n1);
    }

    public static void vtoroeZadanie(){
        ArrayList<Integer> arr1 = new ArrayList<>();
        ArrayList<Integer> arr2 = new ArrayList<>();
        ArrayList<Integer> arr3 = new ArrayList<>();
        readArrList(arr1, n1);
        readArrList(arr2, n2);
        readArrList(arr3, n1);
        printArrList(arr1, n1);
        printArrList(arr2, n2);
        printArrList(arr3, n1);
        minusArrList(arr1, n1);
        minusArrList(arr2, n2);
        minusArrList(arr3, n1);
    }

    public static void tretieZadanie(){
        LinkedList<Integer> arr1 = new LinkedList<>();
        LinkedList<Integer> arr2 = new LinkedList<>();
        LinkedList<Integer> arr3 = new LinkedList<>();
        readLinkedList(arr1, n1);
        readLinkedList(arr2, n2);
        readLinkedList(arr3, n1);
        printLinkedList(arr1, n1);
        printLinkedList(arr2, n2);
        printLinkedList(arr3, n1);
        minusLinkedList(arr1, n1);
        minusLinkedList(arr2, n2);
        minusLinkedList(arr3, n1);
    }

    public static void readArr(int[] arr, int n){
        for (int i = 0; i < n; i++){
            System.out.printf("Введите элемент %d массива: ", i+1);
            arr[i] = in.nextInt();
        }
    }

    public static void printArr(int[] arr, int n){
        System.out.println("Вывод массива:");
        for (int i = 0; i < n; i++){
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void minusArr(int[] arr, int n){
        for (int i = 0; i < n; i++){
            arr[i] = arr[i] - minus;
        }
        printArr(arr, n);
        countArr(arr, n);
    }

    public static void countArr(int[] arr, int n){
        int count = 0;
        for (int i = 0; i < n; i++){
            if (arr[i] < 0){
                count++;
            }
        }
        System.out.printf("Количество отрицательных чисел в массиве: %d\n", count);
    }

    public static void readArrList(ArrayList<Integer> arr, int n){
        for (int i = 0; i < n; i++){
            System.out.printf("Введите элемент %d массива: ", i+1);
            arr.add(in.nextInt());
        }
    }

    public static void printArrList(ArrayList<Integer> arr, int n){
        System.out.println("Вывод массива:");
        for (int i = 0; i < n; i++){
            System.out.print(arr.get(i) + " ");
        }
        System.out.println();
    }

    public static void minusArrList(ArrayList<Integer> arr, int n){
        for (int i = 0; i < n; i++){
            arr.set(i, arr.get(i) - minus);
        }
        printArrList(arr, n);
        countArrList(arr, n);
    }

    public static void countArrList(ArrayList<Integer> arr, int n){
        int count = 0;
        for (int i = 0; i < n; i++){
            if (arr.get(i) < 0){
                count++;
            }
        }
        System.out.printf("Количество отрицательных чисел в массиве: %d\n", count);
    }

    public static void readLinkedList(LinkedList<Integer> arr, int n){
        for (int i = 0; i < n; i++){
            System.out.printf("Введите элемент %d массива: ", i+1);
            arr.add(in.nextInt());
        }
    }

    public static void printLinkedList(LinkedList<Integer> arr, int n){
        System.out.println("Вывод массива:");
        for (int i = 0; i < n; i++){
            System.out.print(arr.get(i) + " ");
        }
        System.out.println();
    }

    public static void minusLinkedList(LinkedList<Integer> arr, int n){
        for (int i = 0; i < n; i++){
            arr.set(i, arr.get(i) - minus);
        }
        printLinkedList(arr, n);
        countLinkedList(arr, n);
    }

    public static void countLinkedList(LinkedList<Integer> arr, int n){
        int count = 0;
        for (int i = 0; i < n; i++){
            if (arr.get(i) < 0){
                count++;
            }
        }
        System.out.printf("Количество отрицательных чисел в массиве: %d\n", count);
    }
}