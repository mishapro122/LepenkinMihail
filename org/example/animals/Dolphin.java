package org.example.animals;

import org.example.area.*;
import org.example.eating.*;

public class Dolphin extends Predator implements Waterfowl {
  @Override
  public void swim() {
    System.out.println("Dolphin swim");
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
