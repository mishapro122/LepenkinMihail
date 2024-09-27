public class Zoo {
    public interface Flying {
        void fly();
    }
    public interface Terrestrial {
        void walk();
    }
    public interface Waterfowl {
        void swim();
    }
    public static class Meat implements BaseMeal{
        public String getMealName() {
            return "мясо";
        }
    }
    public static interface BaseMeal {
        public String getMealName();
    }
    public static class Beef extends Meat{
        public String getMealName() {
            return "говядину";
        }
    }
    public static class Fish extends Meat {
        public String getMealName() {
            return "рыбу";
        }
    }
    public static class Grass implements BaseMeal{
        public String getMealName() {
            return "траву";
        }
    }
    public static abstract class BaseType {
        public String animalTitle;

        public void printType(String typeName){
            System.out.println(typeName);
        }

        public void eat(BaseMeal meal){
            System.out.println(animalTitle + " ест " + meal.getMealName());
        }
    }
    public static class Herbivores extends BaseType{
        public void printType() {
            super.printType("Травоядные");
        }

        public void eat(Grass meal) {
            super.eat(meal);
        }
    }
    public static class Predators extends BaseType {
        public void printType() {
            super.printType("Хищники");
        }

        public void eat(Meat meal) {
            super.eat(meal);
        }
    }
    public static class Camel extends Herbivores implements Terrestrial {
        public Camel(){
            animalTitle = "Верблюд";
        }
        public void walk() {
            System.out.println(animalTitle + " ходит");
        }
    }
    public static class Dolphin extends Predators implements Waterfowl {
        public Dolphin(){
            animalTitle = "Дельфин";
        }

        public void eat(Fish meal) {
            super.eat(meal);
        }

        public void swim() {
            System.out.println(animalTitle + " плавает");
        }
    }
    public static class Eagle extends Predators implements Flying {
        public Eagle(){
            animalTitle = "Орел";
        }

        public void fly() {
            System.out.println(animalTitle + " летает");
        }
    }
    public static class Hourse extends Herbivores implements Terrestrial {
        public Hourse() {
            animalTitle = "Лощадь";
        }

        public void walk() {
            System.out.println(animalTitle + " ходит");
        }
    }
    public static class Tiger extends Predators implements Terrestrial{
        public Tiger(){
            animalTitle = "Тигр";
        }

        public void walk() {
            System.out.println(animalTitle + " ходит");
        }

        public void eat(Beef meal) {
            super.eat(meal);
        }
    }
    public static void main(String[] args) {
        Camel camal = new Camel();
        Grass grass = new Grass();
        camal.eat(grass);
        Dolphin dolphin = new Dolphin();
        dolphin.swim();
        Eagle eagle = new Eagle();
        eagle.fly();
    }
}
