public class LavnMover {
    private String model;
    private float capasity;
    private String color;
    private float prise;


    public LavnMover(){
            model = "unknown";
        capasity = 0.0f;
        color = "unknown";
        prise = 0.0f;
    }

    public LavnMover(String model, float capasity, String color, float prise){
        setValue(model, capasity, color, prise);
    }

    public void setValue(String model, float capasity, String color, float prise){
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

    public float getPrise(){
        return prise;
    }

    public void printValue(){
        System.out.println("Модель: " + model);
        System.out.println("Объём бака: " + capasity);
        System.out.println("Цвет: " + color);
        System.out.println("Стоимость: " + prise + "\n");
    }

    public void changeColor(String color){
        this.color = color;
    }


    public void discount(){
        this.prise *= 0.80f;
    }
}
