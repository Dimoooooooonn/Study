import static java.lang.Integer.parseInt;
import static java.lang.Math.clamp;

public class LavnMover extends GardenDevice{
    private int grass_container_level;
    private int blade_sharpness;
    private int cut_height;
    private int velocity;

    public LavnMover(){
        super();
        grass_container_level = 0;
        blade_sharpness = 0;
        cut_height = 0;
        velocity = 0;
    }

    public LavnMover(String device_manufacturer, String device_model, String power_supply, boolean powered_on, short work_intensity, int wear_level,
                     int grass_container_level, int blade_sharpness, int cut_height){
        super(device_manufacturer, device_model, power_supply, powered_on, work_intensity, wear_level);
        this.grass_container_level = grass_container_level;
        this.blade_sharpness = blade_sharpness;
        this.cut_height = cut_height;
        velocity = 0;
    }

    public void setGrassContainerLevel(int grass_container_level) { this.grass_container_level = grass_container_level; }
    public void setBladeSharpness(int blade_sharpness) { this.blade_sharpness = blade_sharpness; }
    public void setCutHeight(int cut_height) {this.cut_height = cut_height; }

    @Override
    public void changeWorkIntensivity(short intensivity){
        if (!powered_on) { System.out.println("Прежде чем поменять скорость включите газонакосилку");}
        else if (intensivity == 1 || intensivity == 2 || intensivity == 3 || intensivity == 4){
            switch (intensivity){
                case 1:
                    velocity = 2;
                    System.out.println("Скорость газонокосилки составляет: " + velocity + " км/ч");
                    break;
                case 2:
                    velocity = 3;
                    System.out.println("Скорость газонокосилки составляет: " + velocity + " км/ч");
                    break;
                case 3:
                    velocity = 4;
                    System.out.println("Скорость газонокосилки составляет: " + velocity + " км/ч");
                    break;
                case 4:
                    velocity = 7;
                    System.out.println("Скорость газонокосилки составляет: " + velocity + " км/ч");
                    break;
                default:
                    velocity = 0;
                    System.out.println("Скорость газонокосилки составляет: " + velocity + " км/ч");
                    break;
            }
        }
    }

    @Override
    public void deviceOperation(){
        if (!powered_on) {
            System.out.println("Газонокосилка выключена. Сначала включите устройство");
            return;
        }
        if (wear_level >= 90) {
            System.out.println("Газонокосилка слишком изношена. Нужен ремонт");
            return;
        }
        if (blade_sharpness < 25) {
            System.out.println("Ножи слишком тупые. Требуется заточка");
            return;
        }
        if (grass_container_level >= 95) {
            System.out.println("Травосборник переполнен. Сначала очистите его");
            return;
        }

        int grass = randomValue(12, 25);
        int sharpness_decrease = randomValue(5, 12);
        int wear_increase = randomValue(4, 9);

        grass_container_level = clamp(grass_container_level + grass, 0, 100);
        blade_sharpness = clamp(blade_sharpness - sharpness_decrease, 0, 100);
        wear_level = clamp(wear_level + wear_increase, 0, 100);
        System.out.println("Газонокосилка косит траву с высотой среза " + cut_height + " см");
    }

    public void sharpenBlades() {
        blade_sharpness = 100;
        System.out.println("Ножи заточены. Острота восстановлена до 100");
    }

    public void emptyGrassContainer() {
        grass_container_level = 0;
        System.out.println("Травосборник очищен");
    }

    @Override
    public void randomizeSpecificProperties(){
        grass_container_level = clamp(grass_container_level + randomValue(-15, 15), 0, 100);
        blade_sharpness = clamp(blade_sharpness + randomValue(-10, 5), 0, 100);
        cut_height = clamp(cut_height + randomValue(-2, 2), 2, 15);
    }

    @Override
    protected String specificInfo(){
        return "Заполненность травосборника: " + grass_container_level +
                "\nОстрота ножей: " + blade_sharpness +
                "\nВысота скашивания: " + cut_height + " см\n";
    }
}
