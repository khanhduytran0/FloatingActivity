package com.kdt.floatactivity.floatingact;

import android.app.*;
import android.content.pm.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.android.server.am.*;
import java.lang.reflect.*;
import android.content.*;
import android.content.res.*;

public class Utils
{
	public static ArrayMap<IBinder, ActivityThread.ApplicationThread> getActivites() throws Throwable {
		ActivityThread thread = ActivityThread.currentActivityThread();
		Field mActivitiesField = thread.getClass().getDeclaredField("mActivities");
		mActivitiesField.setAccessible(true);
		return (ArrayMap<IBinder, ActivityThread.ApplicationThread>) mActivitiesField.get(thread);
	}

	public static Activity getRunningActivity(String name) throws Throwable {
		// This method use Private API to get running Activity.
		ArrayMap arr = getActivites();

		for (int i = 0; i < arr.size(); i++) {
			Activity act = ActivityThread.currentActivityThread().getActivity((IBinder) arr.keyAt(i));
			if (act.getClass().getName().equals(name)) {
				return act;
			}
		}

		return null; //ActivityThread.currentActivityThread().getActivity(binder);
	}

	public static ActivityInfo makeActvityInfo(ApplicationInfo info) {
		ActivityInfo act = new ActivityInfo();

		// Copy all info from ApplicationInfo to ActivityInfo
		act.applicationInfo = info;
		//act.colorMode
		//act.configChanges
		act.banner = info.banner;
		act.descriptionRes = info.descriptionRes;
		act.enabled = info.enabled;
		act.exported = false; // Read from manifest
		act.flags = info.flags;
		act.icon = info.icon;
		act.labelRes = info.labelRes;
		act.logo = info.logo;
		act.metaData = info.metaData;
		act.name = info.name;
		act.nonLocalizedLabel = info.nonLocalizedLabel;
		act.packageName = info.packageName;
		act.permission = info.permission;
		act.processName = info.processName;
		//act.screenOrientation
		act.showUserIcon = info.showUserIcon;
		//act.softInputMode
		act.taskAffinity = info.taskAffinity;
		act.theme = info.theme;
		act.uiOptions = info.uiOptions;
		return act;
	}
	
	public static ActivityRecord createRecord(String appPkg, ActivityInfo act_info, Configuration act_config) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		ActivityRecord record = null;
		Constructor recordConstruct = ActivityRecord.class.getConstructor(
			ActivityManagerService.class,
			ProcessRecord.class, int.class,
			String.class, Intent.class,
			String.class, ActivityInfo.class,
			Configuration.class, ActivityRecord.class,
			String.class, int.class, boolean.class,
			ActivityStackSupervisor.class
		);
		
		/*
		 ActivityManagerService _service, ProcessRecord _caller,
		 int _launchedFromUid, String _launchedFromPackage, Intent _intent, String _resolvedType,
		 ActivityInfo aInfo, Configuration _configuration,
		 ActivityRecord _resultTo, String _resultWho, int _reqCode,
		 boolean _componentSpecified, ActivityStackSupervisor supervisor
		*/
		
		ActivityManagerService act_service = null;
		//ActivityManagerNative s;
		record = (ActivityRecord) recordConstruct.newInstance(
			act_service,
			null,
			android.os.Process.myUid(),
			appPkg,
			null, // Intent
			"", // resolvedType
			act_info, // activity info
			act_config, // Config
			null, // Result to
			"", // Result who
			0, // Request code
			false, // Component specified
			new ActivityStackSupervisor(act_service)
		);
		return record;
	}

	public static LinearLayout convertTopbarTolayout(View statusbar, LinearLayout clone) {
		if (clone == null) clone = new LinearLayout(statusbar.getContext());
		clone.setActivated(statusbar.isActivated());
		clone.setDrawingCacheEnabled(statusbar.isDrawingCacheEnabled());
		clone.setDuplicateParentStateEnabled(statusbar.isDuplicateParentStateEnabled());
		clone.setClickable(statusbar.isClickable());
		clone.setFocusable(statusbar.isFocusable());
		clone.setEnabled(statusbar.isEnabled());

		clone.setAnimation(statusbar.getAnimation());
		clone.setBackground(statusbar.getBackground());
		clone.setBackgroundTintList(statusbar.getBackgroundTintList());
		clone.setBackgroundTintMode(statusbar.getBackgroundTintMode());

		clone.setId(statusbar.getId());
		clone.setLayoutDirection(statusbar.getLayoutDirection());
		clone.setLayoutParams(statusbar.getLayoutParams());
		clone.setOnFocusChangeListener(statusbar.getOnFocusChangeListener());
		clone.setOutlineProvider(statusbar.getOutlineProvider());
		return clone;
	}
}
