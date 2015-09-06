package com.shuai.test;

public class TestInit {

    public static int M=8;
    static{
        M=9;
        N=9;
    }
    
    public static int N=8;
    
    private int x = 2;

    {
        printData();
        x = 5;
        y = 5;
        printData();
    }

    private int y = 3;

    public TestInit() {
        printData();
        x = 1;
        y = 1;
        printData();
    }

    public void printData() {
        System.out.println(toString());
    }

    public String toString() {
        return x + "," + y;
    }

    public static void main(String[] args) {
        System.out.println("start");
        TestInit a = new TestInit();
        a.printData();
    }

}
