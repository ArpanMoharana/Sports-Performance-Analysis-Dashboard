class Math {
    void add(int a, int b) {
        System.out.println("Sum of two numbers: " + (a + b));
    }

    void add(int a, int b, int c) {
        System.out.println("Sum of three numbers: " + (a + b + c));
    }
}


class SumDemo {
    public void calc() {
        int a = 6;
        int b = 5;
        System.out.println("Inside class SumDemo: " + (a + b));
    }
}

class SubDemo extends SumDemo {
    @Override
    public void calc() {
        int a = 6;
        int b = 5;
        System.out.println("Inside class SubDemo: " + (a - b));
    }
}

public class Test {
    public static void main(String[] args) {
        MathDemo math = new MathDemo();
        math.add(3, 4);
        math.add(2, 5, 7);

        SumDemo s1 = new SumDemo();
        SubDemo s2 = new SubDemo();

        s1.calc();
        s2.calc();
    }
}