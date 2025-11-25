public class LavnMover {
    private String model;
    private float capasity;
    private String color;
    private int prise;


    public LavnMover(){
        model = "unknown";
        capasity = 0.0f;
        color = "unknown";
        prise = 0;
    }

    public LavnMover(String model, float capasity, String color, int prise){
        setValue(model, capasity, color, prise);
    }

    public void setValue(String model, float capasity, String color, int prise){
        this.model = model;
        this.capasity = capasity;
        this.color = color;
        this.prise = prise;
    }

    public String getModel(){
        return model;
    }

    public float getCapasity(){
        return capasity;
    }

    public String getColor(){
        return color;
    }

    public int getPrise(){
        return prise;
    }

    public void printValue(){
        System.out.println("Модель: " + model);
        System.out.println("Объём бака: " + capasity);
        System.out.println("Цвет: " + color);
        System.out.println("Стоимость: " + prise + "\n");
    }
}
