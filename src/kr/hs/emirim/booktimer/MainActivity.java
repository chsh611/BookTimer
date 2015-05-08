package kr.hs.emirim.booktimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.KeyEvent;

public class MainActivity extends Activity implements OnClickListener{
	
	public static final String COMMON = "Common";
	public static final String NAME = "Name";
	public static final String AGE = "Age";
	
	private Button btnInput, btnList, btnUser, btnCompare;

	Intent intent;
	
	int mCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnInput = (Button) findViewById(R.id.btnInput);
		btnList = (Button) findViewById(R.id.btnList);
		btnUser = (Button) findViewById(R.id.btnUser);
		btnCompare = (Button) findViewById(R.id.btnCompare);

		btnInput.setOnClickListener(this);
		btnList.setOnClickListener(this);
		btnUser.setOnClickListener(this);
		btnCompare.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		intent = new Intent(MainActivity.this, ProgrammerInfo.class);
		startActivity(intent);
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {

		SharedPreferences activity = getSharedPreferences(COMMON, 0);
		
		String name = activity.getString(NAME, "").toString();
		String age = activity.getString(AGE, "").toString();
		
		if (v instanceof Button) {
			if(name.equals("") || age.equals("")){
				showDialog(0);
			}
			else {
				if (v.getId() == R.id.btnInput) {
					intent = new Intent(this,InputBook.class);
					startActivity(intent);
				} else if (v.getId() == R.id.btnList) {
					intent = new Intent(this,CheckBookList.class);
					intent.putExtra("view", "main");
					startActivity(intent);
				} else if (v.getId() == R.id.btnUser) {
					intent = new Intent(this, UserInfo.class);
					startActivity(intent);
				} else if (v.getId() == R.id.btnCompare) {
					intent = new Intent(this,CompareTime.class);
					
					ConnectivityManager cManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
					NetworkInfo wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
					
					if(mobile.isConnected() || wifi.isConnected()){		
						startActivity(intent);
					}
					else {
						AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);

						alertDlg.setMessage("인터넷(3G/Wifi) 연결이 필요합니다.");

						alertDlg.setPositiveButton("닫기", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								arg0.cancel();
							}
						});

						AlertDialog alert = alertDlg.create();
						alert.setTitle("인터넷연결");

						alert.show();
					}
				}
			}
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id){
		
		AlertDialog dialogDetails = null;
		
		if(id == 0){
			LayoutInflater inflater = LayoutInflater.from(this);
			View dialogview = inflater.inflate(R.layout.custom_dialog, null);
			
			AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
			dialogbuilder.setTitle("내 정보 입력");
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
						
			Button btnOk = (Button) alertDialog.findViewById(R.id.btnOk);
			Button btnCancel = (Button) alertDialog.findViewById(R.id.btnCancel);
			
			btnOk.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(userName.getText().equals("") || userAge.getText().equals("")){
						Toast.makeText(MainActivity.this, "이름과 나이 모두 입력해주세요!", Toast.LENGTH_SHORT).show();
					}
					else {
						SharedPreferences common = getSharedPreferences(COMMON, MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
						Editor editor = common.edit();
						editor.putString(NAME, userName.getText().toString());
						editor.putString(AGE, userAge.getText().toString());
						editor.commit();
						
						alertDialog.dismiss();
						
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (mCount > 0) {
				finish();
			}

			else {
				Toast.makeText(this, "한번 더 누르면 앱이 종료 됩니다.", Toast.LENGTH_SHORT).show();
				mCount++;
			}

		}

		return false;
	}
	
}
