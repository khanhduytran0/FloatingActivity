package com.kdt.crackactivity;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.kdt.crackactivity.floatingact.*;
import android.app.Activity.*;
import android.content.pm.*;
import com.android.internal.app.*;
import android.content.res.*;
import java.lang.reflect.*;
import java.util.*;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		//LoadedApk apk = new LoadedApk();
		//traceMethod();
		
		//ArrayMap<IBinder, ActivityClientRecord>
		//android.R.drawable.
	}
	
	public void start(View view) {
		AlertDialog.Builder build = new AlertDialog.Builder(this);
		build.setTitle("Result");

		try {
			//ActivityManagerNative.getDefault().bootAnimationComplete();
			
			FrameLayout rootView = (FrameLayout) getWindow().getDecorView().getRootView(); //.getRootView();
			View bar = rootView.getChildAt(1);
			rootView.removeView(bar);

			//FrameLayout clonedRoot = cloneFrame(rootView);
		
			rootView.setLayoutParams(new WindowManager.LayoutParams(500, 500));
			ArrayMap<IBinder, ActivityThread.ApplicationThread> mActivities = Utils.getActivites();
			
			IBinder topBinder = mActivities.keyAt(0);
			FloatingActivityService.act_token = topBinder;
		/*
			build.setMessage(
				"Getting activities: " + mActivities.size() + "\n" +
				"IBinder type: " + topBinder.getClass().getName() + "\n");
		*/
		
			FloatingIntent i = new FloatingIntent(MainActivity.this, TestActivity.class);
			i.startFloatingActivity();
			
			finish();
		} catch (Throwable th) {
			build.setTitle("Error!");
			build.setMessage(Log.getStackTraceString(th));
		// } finally {
			build.setPositiveButton(android.R.string.ok, null);
			build.show();
		}
	}
	
	private void traceMethod(Throwable th) {
		
		AlertDialog.Builder build = new AlertDialog.Builder(this);
		build.setTitle(th == null ? "Tracing method" : "Error");
		
		if (th == null) th = new Throwable();
		
		build.setMessage(Log.getStackTraceString(th));
		build.setPositiveButton("OK", null);
		build.show();
	}
}
