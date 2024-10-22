package org.example.animals;

import org.example.area.Overland;
import org.example.wayOfEating.Herbivorous;

public class Camel extends Animals implements Herbivorous, Overland{
  @Override
  public void walk() {
    System.out.println("Camel walk");
  }

  @Override
  public void eat(String food) {
    if (food.equals("grass") || food.equals("Grass")) {
      System.out.println("Camel eat grass");
    } else {
      System.out.println("Camel doesn't eat it");
    }
  }

  @Override
  public void printEat() {
    System.out.println("Camel eat grass");
  }

  @Override
  public void typeAnimal() {
    System.out.println("Camel - herbivorous");
  }
}
