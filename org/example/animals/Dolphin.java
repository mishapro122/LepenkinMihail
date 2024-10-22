package org.example.animals;

import org.example.area.Waterfowl;
import org.example.wayOfEating.Predator;

public class Dolphin extends Predator implements Waterfowl {
  @Override
  public void swim() {
    System.out.println("Dolphin swim");
  }

  @Override
  public void typeAnimal() {
    System.out.println("Dolphin - predator");
  }

  @Override
  public void printEat() {
    System.out.println("Dolphin eat fish");
  }

  @Override
  public void eat(String food) {
    if (food.equals("Fish") || food.equals("fish")) {
      System.out.println("Dolphin eat fish");
    } else {
      System.out.println("Dolphin doesn't eat it");
    }
  }
}
