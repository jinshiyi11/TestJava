package com.shuai.test;
/**
 * Derived类的Derived(int num)构造函数是否会被触发？为什么？
 */
public class TestCallConstructor {
	private static class Base {
		private int x;

		public Base() {
			this(0);
		}

		public Base(int num) {
			x=num;
		}
		
		public int getX(){
			return x;
		}
	}
	
	private static class Derived extends Base{
		public  Derived(){
			super();
		}
		
		public Derived(int num) {
			super(num);
			
			System.out.println("x:"+getX());
		}
	}
	
	public static void main(String[] args) {
		Derived x=new Derived();
		
		System.out.println("end");
	}

}
