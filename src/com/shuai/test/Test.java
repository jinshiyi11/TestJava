package com.shuai.test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(int i=0;i<100;i++){
			System.out.println(generateRandomPassword());
		}
		
		test();
		String num="7天";
		float parsedFloat = Float.parseFloat(num.replaceAll("[^0-9.]",""));
		
		
		String xxs=String.format("haha:%s rr", null);
		String yxs="haha"+null;
		
		int x2=-8>>1;
		int x3=-30>>1;
		
		String message="【微小保】验证码：1896，有效期：10分钟";
		
		Pattern pattern = Pattern.compile("【微小保】验证码：(\\d{4})");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
        	
			
        	String verfiyCode=matcher.group(1);
        	System.out.println();
        }
		// TODO Auto-generated method stub
		String s=String.format("%f", 2f);
		String t=String.format("%.0f", 2f);
		String a=String.format("%.0f", 2.456f);
		String b=Float.toString(3);
		
		System.out.println();
		
		List<String> list=new CopyOnWriteArrayList<String>();
		list.add("1");
		list.add("a");
		list.add("b");
		
		for(String obj:list){
			list.remove(obj);
		}
		
		
//		Iterator<String> it = list.iterator();
//		list.

		System.out.println();
	}
	
	private static void test(){
		String regex="^http://m.wiixiaobao.com[/]?$";
		String url="http://m.wiixiaobao.com/q";
		Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
        	System.out.println("match");
        }
	}
	
	public static String generateRandomPassword(){
    	StringBuilder builder=new StringBuilder();
    	Random random=new Random();
    	for(int i=0;i<8;i++){
    		builder.append((char)('a'+random.nextInt(26)));
    	}
    	
    	return builder.toString();
    }

}
