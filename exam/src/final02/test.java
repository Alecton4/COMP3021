package final02;

import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        new B();
    }
}

class A {
    {
        System.out.print("A");
    }
    static {
        System.out.print("B");
    }
}

class B extends A {
    static {
        System.out.print("C");
    }

    B() {
        System.out.print("D");
    }
}