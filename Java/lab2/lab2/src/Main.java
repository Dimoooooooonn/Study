import java.util.LinkedList;

public class Main{
    public static void main(String[] args){
        demonstration();
        zadanie();
    }

    public static void demonstration(){
        LavnMover obj1 = new LavnMover();
        obj1.setValue("Grizzly SKY-10", 1.0f, "green", 23990);
        obj1.printValue();

        LavnMover obj2 = new LavnMover("Huter GLM-6.0", 1.2f, "black", 36243);
        obj2.printValue();

        LavnMover obj3 = new LavnMover("Gigant PLM-07", 0.8f, "orange", 40645);
        LavnMover obj4 = new LavnMover("ATLET", 0.7f, "red", 21990);


    }

    public static void zadanie(){
        LavnMover obj1 = new LavnMover("Grizzly SKY-10", 1.0f, "green", 23990);
        LavnMover obj2 = new LavnMover("Huter GLM-6.0", 1.2f, "black", 36243);
        LavnMover obj3 = new LavnMover("Gigant PLM-07", 0.8f, "orange", 40645);
        LavnMover obj4 = new LavnMover("ATLET", 0.7f, "red", 21990);
        LinkedList<LavnMover> arr = new LinkedList<>();
    }

    public static LavnMover createObj(String model, float capasity, String color, int prise){
        LavnMover obj = new LavnMover(model, capasity, color, prise);
        return obj;
    }

    public static void creatorArr(LavnMover obj1, LavnMover obj2, LavnMover obj3, LavnMover obj4){
        LavnMover[] arr = new LavnMover[4];
        arr[0] = obj1;
        arr[1] = obj2;
        arr[2] = obj3;
        arr[3] = obj4;
    }
}