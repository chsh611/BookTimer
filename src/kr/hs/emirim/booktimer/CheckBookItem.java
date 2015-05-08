package kr.hs.emirim.booktimer;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckBookItem extends Activity implements OnClickListener, Runnable{

	private TextView txtTitle, txtWriter, txtPublish, txtTimer;
	private Button btnStart, btnRestart, btnEnd;
	private LinearLayout linearTimer;
	
	private BookInfoList theBookInfoList;
	
	private Handler mHandler;
	
	private int position;
	private long time;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.start_enter_right, R.anim.start_exit);
		
		setContentView(R.layout.check_book_item);
		
		Intent getIntent = getIntent();
		position = getIntent.getExtras().getInt("Position");
		
		theBookInfoList = new BookInfoList(this);	
		theBookInfoList.LoadData("BookData");
		
		txtTitle = (TextView) findViewById(R.id.txtTitleView);
		txtWriter = (TextView) findViewById(R.id.txtWriterView);
		txtPublish = (TextView) findViewById(R.id.txtPublishView);		
		txtTimer = (TextView) findViewById(R.id.txtTimer);
		
		linearTimer = (LinearLayout) findViewById(R.id.linearTimer);

		btnStart = (Button) findViewById(R.id.btnStartTimer);
		btnRestart = (Button) findViewById(R.id.btnRestartTimer);
		btnEnd = (Button) findViewById(R.id.btnEndTimer);
		
		mHandler = new Handler();
		
		btnStart.setOnClickListener(this);
		btnRestart.setOnClickListener(this);
		btnEnd.setOnClickListener(this);
				
		txtTitle.setText(theBookInfoList.theList.get(position).bTitle);
		
		String writer = theBookInfoList.theList.get(position).bWriter;
		String publish = theBookInfoList.theList.get(position).bPublish;
		
		if(writer.equals("")) txtWriter.setText("미기입");
		else txtWriter.setText(writer);
		if(publish.equals("")) txtPublish.setText("미기입");
		else txtPublish.setText(publish);
				
		if(theBookInfoList.theList.get(position).startTimer == null &&
				theBookInfoList.theList.get(position).endTimer == null){
			linearTimer.setVisibility(linearTimer.INVISIBLE);
			SettingButton(true,false,false);
		}
		if(theBookInfoList.theList.get(position).startTimer != null &&
				theBookInfoList.theList.get(position).endTimer == null){
			SettingButton(false, true, true);
			mHandler.postDelayed(this, 100);
		}
		
		if(theBookInfoList.theList.get(position).startTimer != null &&
				theBookInfoList.theList.get(position).endTimer != null){
			SettingButton(false, true, false);
			
			StringBuffer diffTime = new StringBuffer();
			diffTime.append(theBookInfoList.theList.get(position).Timing).append("초");
			
			txtTimer.setText(diffTime.toString());
		}
		
	}

	@Override
	public void onClick(View v) {

		if(v instanceof Button){
			if(v.getId() == R.id.btnStartTimer){
				linearTimer.setVisibility(linearTimer.VISIBLE);
				SettingButton(false, true, true);
				theBookInfoList.theList.get(position).startTimer = getNowDateTime();
				mHandler.postDelayed(this, 100);
			}
			else if(v.getId() == R.id.btnRestartTimer){
				SettingButton(false, true, true);
				theBookInfoList.theList.get(position).endTimer = null;
				theBookInfoList.theList.get(position).startTimer = getNowDateTime();	
				theBookInfoList.theList.get(position).bReadChk = false;
				mHandler.postDelayed(this, 100);
			}
			else if(v.getId() == R.id.btnEndTimer){
				SettingButton(false, true, false);
				theBookInfoList.theList.get(position).endTimer = getNowDateTime();
				theBookInfoList.theList.get(position).bReadChk = true;
				theBookInfoList.theList.get(position).Timing = time;
			}
			
			theBookInfoList.SaveData("BookData");
		}
		
	}

	@Override
	public void run() {

		String now = getNowDateTime();
		
		time = SecondCompare(theBookInfoList.theList.get(position).startTimer, now);
		
		StringBuffer diffTime = new StringBuffer();
		diffTime.append(time).append("초");
		
		txtTimer.setText(diffTime.toString());
		
		if(theBookInfoList.theList.get(position).endTimer == null) mHandler.postDelayed(this, 100);
		
	}
	
	public String getNowDateTime(){
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
	}
	
	public long SecondCompare(String toDate, String fromDate){
		Calendar toCal = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date startDay = sf.parse(toDate, new ParsePosition(0));
		
		long startTime = startDay.getTime();
		
		Calendar fromCal = Calendar.getInstance();
		Date endDay = sf.parse(fromDate, new ParsePosition(0));
		
		long endTime = endDay.getTime();
		
		long mills = endTime - startTime;
		
		long sec = mills/1000;
		
		return sec;
	}
	
	public void SettingButton(boolean b1, boolean b2, boolean b3){
		btnStart.setEnabled(b1);
		btnRestart.setEnabled(b2);
		btnEnd.setEnabled(b3);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			Intent intent = new Intent(CheckBookItem.this, CheckBookList.class);
			intent.putExtra("view", "check");
			startActivity(intent);
			
			finish();
		}
		return false;
	}
	
	@Override
	public void finish() {
		super.finish();
		this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit_right);
	}
	
}
