package kr.hs.emirim.booktimer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class CompareTime extends Activity{

	public static final String COMMON = "Common";
	public static final String NAME = "Name";
	public static final String AGE = "Age";
	public static final String MAX = "Max";
	
	final static String STR_URL = "http://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=YTNlODViNmQxYWM4ZTMwMmE3YWM3NDY0NmY3ZWEwZDQ=&format=json&userStatsId=eodn113data/113/DT_113_STBL_1017156/2/1/20140812121228&prdSe=Y&newEstPrdCnt=1";
	
	TextView txtName, txtAge, txtAgeSet, txtRank;
	
	JSONHandler handler;
	
	ArrayList<String> listAge;
	ArrayList<String> listTime;
	ArrayList<String> listTimeName;
	
	String name, age, ageSet, rank;
	int time;
	
	StringBuffer b = new StringBuffer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.start_enter_right, R.anim.start_exit);
		
		setContentView(R.layout.check_time);
		
		txtName = (TextView) findViewById(R.id.txtNameC);
		txtAge = (TextView) findViewById(R.id.txtAgeC);
		txtAgeSet = (TextView) findViewById(R.id.txtAge2C);;
		txtRank = (TextView) findViewById(R.id.txtCompare);
		
		handler = new JSONHandler();
		
		listAge = new ArrayList<String>();
		listTime = new ArrayList<String>();
		listTimeName = new ArrayList<String>();
		
		SharedPreferences activity = getSharedPreferences(COMMON, 0);
		
		name = activity.getString(NAME, "");
		age = activity.getString(AGE, "");
		time = activity.getInt(MAX, 0);
		
		txtName.setText(name.toString());
		txtAge.setText(age.toString());
		
		ageSet = CheckAge(Integer.parseInt(age));
		txtAgeSet.setText(ageSet.toString());
		
		JSONThread thread = new JSONThread();
		thread.start();
		
		
	}
	
	class JSONThread extends Thread{
		public void run(){
			try{
				URL url = new URL(STR_URL);
				
				URLConnection conn = url.openConnection();
				
				InputStream is = conn.getInputStream();
				
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader reader = new BufferedReader(isr);
				
				StringBuffer buf = new StringBuffer();
				String temp;
				
				while((temp = reader.readLine())!=null){
					buf.append(temp);
				}
				reader.close();
				
				String data = buf.toString();
				
				Log.d("json",data);
				
				JSONArray root = new JSONArray(data);
				for(int i=0; i<root.length(); i++){
					JSONObject sub = root.getJSONObject(i);
					
					String dataAge = sub.getString("C1_NM");
					String dataTimeName = sub.getString("ITM_NM");
					String dataTime = sub.getString("DT");
					
					listAge.add(dataAge);
					listTime.add(dataTime);
					listTimeName.add(dataTimeName);
				}
				handler.sendEmptyMessage(1);
			} catch(Exception e){ e.printStackTrace(); }
		}
		
	}
	
	class JSONHandler extends Handler {
		@Override 
		public void handleMessage(Message msg){
			super.handleMessage(msg);
						
			boolean ageSetChk = false;
			
			if(msg.what == 1){
				for(int i=0; i<listAge.size(); i++){
					ageSetChk = listAge.get(i).equals(ageSet);
				
					if(ageSetChk && listTimeName.get(i).equals(CheckTime(time))){
						rank = Double.toString(rankSum(i));
						break;
					}
				}
				txtRank.setText(rank.toString()+"%");
			}
		}
	}
	
	private double rankSum(int chk){
		
		int indexS = 0;
		int indexE = chk;	
		
		double sum = 0;
		
		while(chk > 8) {
			indexS++;	
			chk -= 8;	
		}
		chk = indexS*8;		
		indexS = chk + 4;	
		
		do{
			sum += Double.parseDouble(listTime.get(indexS));
			if(indexS == indexE) break;
			indexS--;
			if(indexS < chk) indexS = chk+6;					
			else if(indexS == chk+5) indexS = chk+7;			
			else if(indexS == chk+6) indexS = chk+5;			
		}while(true);							
		
		sum *= 10;
		sum = Math.round(sum);
		sum/=10;
		
		return sum;
	}
	
	private String CheckTime(int time){

		if(time <= 0) return "안본다/안한다";
		
		time = time / 60;
		
		if(time < 30) return "30분 미만";
		
		time = time / 60;
		
		switch(time){
		case 0: return "30분~1시간 미만";
		case 1: return "1~2시간미만";
		case 2: return "2~3시간미만";
		case 3: return "3~4시간미만";
		case 4: return "4~5시간미만";
		default: return "5시간 이상";
		}
		
	}
	
	private String CheckAge(int age){
		switch(age/10){
		case 5: return "50대";
		case 4: return "40대";
		case 3: return "30대";
		case 2: case 1: case 0: return "20대 이하";
		default: return "60대";
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit_right);
	}
	
}
