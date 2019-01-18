package mylib2;

import mylib1.Lib1;

public class Lib2 {
    public void sayHello() {
        new Lib1().sayHello();
        System.out.println("Hello from lib2");
    }
}
