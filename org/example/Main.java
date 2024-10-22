package org.example;

import org.example.animals.*;
import org.example.area.*;

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
