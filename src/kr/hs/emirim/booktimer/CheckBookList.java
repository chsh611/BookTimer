package kr.hs.emirim.booktimer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CheckBookList extends Activity implements OnItemClickListener{
	
	private ListView theListView;
		
	private BookInfoList theBookInfoList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		String view = intent.getExtras().getString("view").toString();
		
		if(view.equals("main"))	this.overridePendingTransition(R.anim.start_enter_right, R.anim.start_exit);
		
		setContentView(R.layout.check_book);
		
		theBookInfoList = new BookInfoList(this);
		theBookInfoList.LoadData("BookData");
				
		theListView = (ListView) findViewById(R.id.listContact);
		
		theListView.setOnItemClickListener(this);
		
		theListView.setAdapter(new ItemViewProcess(this,theBookInfoList.theList));
		
	}
	
	public class ItemViewProcess extends ArrayAdapter<BookInfo> {
		
		private final Context context;
		private final ArrayList<BookInfo> itemList;
		
		public ItemViewProcess(Context context, ArrayList<BookInfo> itemList) {
			super(context,R.layout.row,itemList);
			
			this.context = context;
			this.itemList = itemList;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View row = inf.inflate(R.layout.row, parent, false);
			
			TextView txt = (TextView) row.findViewById(R.id.txtTitle);
			CheckBox chk = (CheckBox) row.findViewById(R.id.chkBox);
			
			txt.setText(theBookInfoList.theList.get(position).bTitle);
			chk.setChecked(theBookInfoList.theList.get(position).bReadChk);
			
			return row;
			
		}
		
	}
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

		Intent intent = new Intent();

		intent.putExtra("Position", position);
		intent.setClass(CheckBookList.this, CheckBookItem.class);
		
		startActivity(intent);	
		
		finish();
		
	}
	
	@Override
	public void finish() {
		super.finish();
		this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit_right);
	}
	
}
