package mylib1;

import com.google.common.base.Joiner;

public class Lib1 {
    public void sayHello() {
        System.out.println(Joiner.on(" ").join("Hello", "from", "lib1!"));
    }
}
