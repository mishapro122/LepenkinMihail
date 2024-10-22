package org.example.animal;

public class Camel {
  public static class Camel extends Herbivores implements Terrestrial {
    public Camel() {
      animalTitle = "Верблюд";
    }
    public void walk() {
      System.out.println(animalTitle + " ходит");
    }
  }
}
