import java.util.Scanner;

public class Main{
    public static Scanner in = new Scanner(System.in);
    public static int n1 = 4;
    public static int n2 = 5;
    public static int minus = 10;
    public static void main(String[] args){
        pervoeZadanie();
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
}