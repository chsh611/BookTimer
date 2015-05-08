package kr.hs.emirim.booktimer;

import android.app.Activity;
import android.os.Bundle;

public class ProgrammerInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.overridePendingTransition(R.anim.start_enter_down, R.anim.start_exit);
		
		setContentView(R.layout.programmer_info);

	}

	@Override
	public void finish() {
		super.finish();
		this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit_down);
	}

}
