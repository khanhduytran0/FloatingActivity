package com.kdt.floatactivity.floatingact;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.app.*;
import java.lang.reflect.*;
import java.util.*;
import android.widget.*;
import android.os.*;
import android.util.*;

public class FloatingIntent extends Intent {
    private Context ctx;

    public FloatingIntent(Context context, Class<?> cls) {
        super(context, cls);
        this.ctx = context;
    }

    public FloatingIntent(Context context, Intent intent) {
        super(intent);
        this.ctx = context;
    }

    public FloatingIntent(Context context, String str) {
        super(str);
        this.ctx = context;
    }

    public void startFloatingActivity() {
        try {
        /*
			IApplicationThread whoThread = ActivityThread.currentActivityThread().getApplicationThread();
			IBinder token = null;
			int requestCode = -1;
			Activity target = null;
			Bundle options = null;
			
			try {
				// Fields
				Context who = ctx;
				
				// Code
				migrateExtraStreamToClipData();
				prepareToLeaveProcess();
				int result = ActivityManagerNative.getDefault()
					.startActivity(whoThread, who.getBasePackageName(), this,
								   resolveTypeIfNeeded(who.getContentResolver()),
								   token, /* target != null ? target.mEmbeddedID : * / null,
								   requestCode, 0, null, options);
				
				Log.d("FloatingIntent", "Result start: " + result);
		*/
				Intent intent = new Intent(this.ctx, FloatingActivityService.class);
				intent.putExtra("launchIntent", this);
				ctx.startService(intent);
		/*
			} catch (Throwable e) {
				throw new RuntimeException("Failure from system", e);
			}
		*/
		
		/*
			Instrumentation in = ActivityThread.currentActivityThread().getInstrumentation();
			in.addMonitor(new Instrumentation.ActivityMonitor(
				intent.getComponent().getClassName(),
				new Instrumentation.ActivityResult(0, intent),
				false)
			);
		
			Field fi = in.getClass().getDeclaredField("mActivityMonitors");
			fi.setAccessible(true);
			
			List<Instrumentation.ActivityMonitor> list = (List<Instrumentation.ActivityMonitor>) fi.get(in);
			
			for (Instrumentation.ActivityMonitor m : list) {
				Toast.makeText(ctx, "monitor=" + m.getFilter(), Toast.LENGTH_SHORT).show();
			}
		*/
			
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}

