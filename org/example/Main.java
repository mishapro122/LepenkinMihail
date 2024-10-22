package org.example;

public class Main {
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
