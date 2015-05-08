package kr.hs.emirim.booktimer;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputBook extends Activity {

	EditText edtTitle, edtWriter, edtPublish;
	private Button btnSave;

	private BookInfoList theBookList = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.start_enter_left, R.anim.start_exit);
		
		setContentView(R.layout.input_book);

		theBookList = new BookInfoList(this);
		theBookList.LoadData("BookData");
		
		edtTitle = (EditText) findViewById(R.id.edtBookTitle);
		edtWriter = (EditText) findViewById(R.id.edtBookWriter);
		edtPublish = (EditText) findViewById(R.id.edtBookPublish);
		btnSave = (Button) findViewById(R.id.btnSaveBook);

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String aTitle = edtTitle.getText().toString();
				
				if(edtTitle.getText().toString().equals("") == true) {
					Toast.makeText(InputBook.this, "도서명은 필수로 입력해주세요.!", Toast.LENGTH_SHORT).show();
				}
				
				else{
					
					String aWirter = edtWriter.getText().toString();
					String aPublish = edtPublish.getText().toString();
					
					theBookList.AddContact(aTitle, aWirter, aPublish);
	
					theBookList.SaveData("BookData");
					
					Toast.makeText(InputBook.this, "'"+aTitle+"'책을 저장했어요!", Toast.LENGTH_SHORT).show();
					
					finish();
				}
			}			
		});

	}
	
	@Override
	public void finish() {
		super.finish();
		this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit_left);
	}

}
