package org.example.animals;

import org.example.area.Flying;
import org.example.wayOfEating.Predator;

public class Eagle extends Predator implements Flying {
  @Override
  public void typeAnimal() {
    System.out.println("Eagle - predator");
  }

  @Override
  public void printEat() {
    System.out.println("Eagle eat meat");
  }

  @Override
  public void fly() {

    System.out.println("Eagle fly");
  }

  @Override
  public void eat(String food) {
    if (food.equals("Meat") || food.equals("meat")) {
      System.out.println("Eagle eat meat");
    } else {
      System.out.println("Eagle doesn't eat it");
    }
  }
}
