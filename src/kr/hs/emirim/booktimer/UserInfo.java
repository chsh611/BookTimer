package kr.hs.emirim.booktimer;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfo extends Activity implements OnClickListener {

	public static final String COMMON = "Common";
	public static final String NAME = "Name";
	public static final String AGE = "Age";
	public static final String MAX = "Max";
	
	private Button btnDelete, btnChange;
	private TextView txtName, txtAge, txtScore, txtBookCnt;
	
	private BookInfoList theBookInfoList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.start_enter_left, R.anim.start_exit);
		
		setContentView(R.layout.check_user);
		
		txtName = (TextView) findViewById(R.id.txtName);
		txtAge = (TextView) findViewById(R.id.txtAge);
		txtScore = (TextView) findViewById(R.id.txtScore);
		txtBookCnt = (TextView) findViewById(R.id.txtBookCnt);
		
		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnChange = (Button) findViewById(R.id.btnChange);
		
		btnDelete.setOnClickListener(this);
		btnChange.setOnClickListener(this);
		
		
		SharedPreferences activity = getSharedPreferences(COMMON, 0);
		
		txtName.setText(activity.getString(NAME, "").toString());
		txtAge.setText(activity.getString(AGE, "").toString()+"세");
		
		
		theBookInfoList = new BookInfoList(this);
		theBookInfoList.LoadData("BookData");
		
		long max = 0;
		long day = 0;
		int index = 0;
		int cnt = 0;
		
		for(int i=0; i<theBookInfoList.theList.size(); i++){
			if(theBookInfoList.theList.get(i).bReadChk == true ){
				if(cnt == 0 || max < theBookInfoList.theList.get(i).Timing){
					max = theBookInfoList.theList.get(i).Timing;
					index = i;
				}
				cnt++;
			}
		}
		
		try {
			day = diffOfDate(theBookInfoList.theList.get(index).startTimer,theBookInfoList.theList.get(index).endTimer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(day > 0) max /= day;
		
		StringBuffer diffTime = new StringBuffer();
		diffTime.append(max);
		
		txtScore.setText(diffTime.toString()+"초");
		txtBookCnt.setText("총 "+Integer.toString(cnt)+"권");
		
		Editor editor = activity.edit();
		
		if(Integer.parseInt(diffTime.toString()) <= 0) editor.putInt(MAX, -1);
		else editor.putInt(MAX, Integer.parseInt(diffTime.toString()));
		
		editor.commit();
		
	}

	@Override
	public void onClick(View v) {

		if(v.getId() == R.id.btnChange){
			showDialog(0);
		}
		else if(v.getId() == R.id.btnDelete){
			
			AlertDialog.Builder alertDlg = new AlertDialog.Builder(UserInfo.this);

			alertDlg.setMessage("정말 삭제하시겠습니까?");

			alertDlg.setPositiveButton("예", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					
					SharedPreferences common = getSharedPreferences(COMMON, MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
					Editor editor = common.edit();
					editor.clear();
					editor.commit();
					
					theBookInfoList.DeleteDate("BookData");
					
					finish();
				}
			});

			alertDlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whitchButton) {
					dialog.cancel();
				}
			});

			AlertDialog alert = alertDlg.create();
			alert.setTitle("데이터 삭제");

			alert.show();
			
		}
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id){
		
		AlertDialog dialogDetails = null;
		
		if(id == 0){
			LayoutInflater inflater = LayoutInflater.from(this);
			View dialogview = inflater.inflate(R.layout.custom_dialog, null);
			
			AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
			dialogbuilder.setTitle("내 정보 변경");
			dialogbuilder.setView(dialogview);
			dialogDetails = dialogbuilder.create();
		}
		return dialogDetails;
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog){
		if(id == 0){
			
			final AlertDialog alertDialog = (AlertDialog) dialog;
			
			final EditText userName = (EditText) alertDialog.findViewById(R.id.edtName);
			final EditText userAge = (EditText) alertDialog.findViewById(R.id.edtAge);
			
			userAge.setRawInputType(Configuration.KEYBOARD_QWERTY);
			
			Button btnOk = (Button) alertDialog.findViewById(R.id.btnOk);
			Button btnCancel = (Button) alertDialog.findViewById(R.id.btnCancel);
			
			btnOk.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					SharedPreferences common = getSharedPreferences(COMMON, MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
					Editor editor = common.edit();
					
					if(userName.getText().toString().equals("") || userAge.getText().toString().equals("")){
						Toast.makeText(UserInfo.this,"이름과 나이를 입력해주세요!", Toast.LENGTH_SHORT).show();
					}
					else{
						editor.putString(NAME, userName.getText().toString());
						editor.putString(AGE, userAge.getText().toString());
						
						editor.commit();
						alertDialog.dismiss();
						
						finish();
						
						Intent intent = new Intent(UserInfo.this, UserInfo.class);
						startActivity(intent);
					}
				}
			});
			
			btnCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					alertDialog.dismiss();
				}
			});
			
		}
	}
	
	public long diffOfDate(String begin, String end) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		
		Date beginDate = formatter.parse(begin);
		Date endDate = formatter.parse(end);
		
		long diff = endDate.getTime() - beginDate.getTime();
		long diffDays = diff/(24*60*60*1000);
		
		return diffDays;
	}
	
	@Override
	public void finish() {
		super.finish();
		this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit_left);
	}
	
}
