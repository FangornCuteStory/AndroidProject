package com.group5.juggermatch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;


public class JSON {

	public void toPhpFile(Match match){

	 DefaultHttpClient httpclient = new DefaultHttpClient();
     HttpPost httppost = new HttpPost("http://127.0.0.1/test.php");
	
     try {
     	
     	String team_1 = match.getTeam1();
         String team_2 = match.getTeam2();
         int  score_team_1= match.getScoreTeam1();
         int score_team_2 = match.getScoreTeam2(); 
         long start_time = match.getStartTime();
         long end_time = match.getEndTime();
         
        String jString = "{\"Necessary??\": { \"team_1\": \""+team_1 + "\",\"team_2\":\""+team_2+"\",\"score_team_1\":\""+score_team_1+"\",\"score_team_2\":\""+score_team_2+"\",\"start_time\":\""+start_time+"\",\"end_time\":\""+end_time+"\"}}";
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair("value", jString));
        httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));                 
     }catch (UnsupportedEncodingException e) {
     	  throw new AssertionError("UTF-8 not supported");
     	}
        
        HttpResponse response;
        try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
