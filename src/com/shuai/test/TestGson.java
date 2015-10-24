package com.shuai.test;

import com.google.gson.Gson;

public class TestGson {
	//测试默认值
	static class Info{
		private int x;
		private String url="asfasdf";
		private boolean ok=true;
		
		Info(){
			
		}
	}
	
	public static void main(String[] args) {
		String json="{x=1}";
		Gson gson=new Gson();
		Info info = gson.fromJson(json, Info.class);
		System.out.println(info.toString());
	}

}
