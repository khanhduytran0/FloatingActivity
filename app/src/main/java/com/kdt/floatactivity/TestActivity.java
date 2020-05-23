package com.kdt.floatactivity;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.view.inputmethod.*;

public class TestActivity extends Activity
{
	@Override
	protected void onCreate(Bundle p1)
	{
		super.onCreate(p1);
		LinearLayout l = new LinearLayout(this);
		l.setOrientation(l.VERTICAL);
		
		EditText e = new EditText(this);
		e.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		e.setSingleLine(false);
		l.addView(e);
		
		TextView t = new TextView(this);
		t.setText("Some features may not work correctly:\n" +
			" - Show an AlertDialog cause crash.\n" +
			" - SurfaceView still visible while minimize.");
			
		Button b = new Button(this);
		b.setText("Try");
		b.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					AlertDialog.Builder build = new AlertDialog.Builder(TestActivity.this);
					build.setMessage("Works fine???");
					build.setPositiveButton("OK", null);
					build.show();
					
				}
			});
		
		l.addView(t);
		l.addView(b);
		
		setContentView(l);
	}
}
