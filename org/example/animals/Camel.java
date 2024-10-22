package org.example.animals;

import org.example.area.*;
import org.example.eating.*;

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
}
