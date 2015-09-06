package com.shuai.test;
import java.util.ArrayList;


public class TestFinal {
	
	static class Item{
		int x;
		String name;
		
		Item(int x,String name){
			this.x=x;
			this.name=name;
		}
		
		public String toString(){
			return String.format("x:%d,name:%s", x,name);
		}
	}

	
	public static void main(String[] args) throws InterruptedException {
		ArrayList<Item> items=new ArrayList<Item>();
		
		items.add(new Item(1, "xx"));
		items.add(new Item(2, "yy"));
		
		final Item item=new Item(9, "bbbb");
		
		Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println(item);
			}
		});
		
		item.x=0;
		item.name="tom";
		thread.start();
		thread.join();
		
		System.out.println(item);
		
		

	}

}
