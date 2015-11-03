package com.shuai.test;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

public class TestHttpClient {
	
	class TokenInfo {
		
		@SerializedName("uid")
		private long mUid;
		
		@SerializedName("token")
		private String mToken;
		
		public long getUid() {
			return mUid;
		}
		public void setUid(long uid) {
			this.mUid = uid;
		}
		public String getToken() {
			return mToken;
		}
		public void setToken(String token) {
			this.mToken = token;
		}

	}

	/**
	 * @param args
	 * @throws CertificateException 
	 * @throws KeyStoreException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyPort", "8888");
		
		System.err.println("start!!!!");
		
		int times=1000;
		boolean result;
		for(int i=0;i<times;i++){
			System.out.println(String.valueOf(i));
			String token=getToken();
			
			result=testFlight(token);
			if(!result){
				System.err.println("get session failed!!!!");
			}
			
			result=testCheckin(token);
			if(!result){
				System.err.println("get session failed!!!!");
			}
			
		}

	}
	
	private static String getToken() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException{
		String result="";
		
		// Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(
                        new TrustStrategy(){

							@Override
							public boolean isTrusted(X509Certificate[] chain,
									String authType)
									throws CertificateException {
								return true;
							}
                        	
                        })
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
//        	HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
//            RequestConfig config = RequestConfig.custom()
//                    .setProxy(proxy)
//                    .build();
            
        	//HttpPost httpPost = new HttpPost("https://www.baidu.com");
            HttpPost httpPost = new HttpPost("https://passport.wiixiaobao.com/login/login_submit");
            //httpPost.setConfig(config);
           
            List <NameValuePair> params = new ArrayList <NameValuePair>();
            params.add(new BasicNameValuePair("zxtype", "phone"));
            params.add(new BasicNameValuePair("username", "13718440838"));
			params.add(new BasicNameValuePair("password", Utils.md5("1234567")));
			params.add(new BasicNameValuePair("device", "android"));
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = httpclient.execute(httpPost);
            String content = fixJson(EntityUtils.toString(response.getEntity()));
            
            JsonParser parser=new JsonParser();
			JsonObject root = parser.parse(content).getAsJsonObject();
			
			String errmsg=root.get("errmsg").toString();
			
			Gson gson = new Gson();
			TokenInfo info = gson.fromJson(root.getAsJsonObject("result"), TokenInfo.class);

    		result=info.getToken();
        } catch(Exception e){
        	System.err.println(e.toString());
        }finally {
            try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        return result;
	}
	
	private static boolean testFlight(String token){
		boolean result=false;
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
        	HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
            RequestConfig config = RequestConfig.custom()
                    .setProxy(proxy)
                    .build();
            
            HttpGet httpGet = new HttpGet("http://m.wiixiaobao.com/activities/flight_delay?_page=info");
            httpGet.setConfig(config);
            //httpGet.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
            httpGet.setHeader("Cookie", String.format("hybird_token=%s; hybird_uid=83667245923; hybird_device=android",token));
            CloseableHttpResponse response = httpclient.execute(httpGet);
            Header[] headers = response.getHeaders("Set-Cookie");
            for(Header head : headers){
            	String value = head.getValue();
            	if(value.contains("ci_sessions")){
            		result=true;
            		System.out.println(value);
            	}
            }
            
            //
            
        } catch(Exception e){
        	System.err.println(e.toString());
        }finally {
            try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        return result;
	}
	
	private static boolean testCheckin(String token){
		boolean result=true;
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
        	HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
            RequestConfig config = RequestConfig.custom()
                    .setProxy(proxy)
                    .build();
            
            HttpGet httpGet = new HttpGet("http://m.wiixiaobao.com/user/checkin");
            httpGet.setConfig(config);
            //httpGet.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
            httpGet.setHeader("Cookie", String.format("hybird_token=%s; hybird_uid=83667245923; hybird_device=android",token));
            CloseableHttpResponse response = httpclient.execute(httpGet);
            String content = EntityUtils.toString(response.getEntity());
            
            //System.out.println(content);
            if(content.contains("hybird app user login token timeout")){
            	System.err.println(content);
            	result=false;
            }
            
        } catch(Exception e){
        	System.err.println(e.toString());
        }finally {
            try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        return result;
	}
	
	public static String fixJson(String json){		
		int start=0;
		for(;start<json.length();start++){
			if(json.charAt(start)=='\n' || json.charAt(start)=='\ufeff')
				continue;
			else 
				break;
		}
		
		if(start>0)
			return json.substring(start);
		else {
			return json;
		}
	}

}
