package com.kdt.crackactivity.floatingact;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.android.internal.app.*;
import com.android.server.am.*;
import com.kdt.crackactivity.*;
import java.lang.reflect.*;
import java.util.*;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class FloatingActivityService extends Service implements View.OnTouchListener, View.OnClickListener//, Window.Callback
{
	public static boolean isStarted = false;

	private final int ID_TOPBAR = 10000;
	private final int ID_BTN_MIN = 10001;
	private final int ID_BTN_CLOSE = 10002;

	private final int bg_topBar_onTop = Color.parseColor("#66000000");
	private final int bg_topBar_onDown = Color.parseColor("#99000000");
	
	private FrameLayout windowDecorView;
	private FrameLayout windowView;
	private RelativeLayout windowTopbar;
	private LinearLayout windowContent;
	private ImageView windowMinimizeBtn, windowCloseBtn;
	private TextView windowTitleView;

	private WindowManager windowManager;
	private WindowManager.LayoutParams params;
	private Activity main;

	private int sttbarHeight = 0;

	// BinderProxy instance from default activity.
	public static IBinder act_token;

	private int WINDOW_WIDTH = 680;
	private int WINDOW_HEIGHT = 408;

	public FloatingActivityService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int p2, int p3)
	{
		int startBy = isStarted ? START_NOT_STICKY : START_STICKY;

		try {
			startWindowedActivity((Intent) intent.getParcelableExtra("launchIntent"));
		} catch (Throwable th) {
			th.printStackTrace();
			//startWindowedActivity(ErrorActivity.class);
		}
		
		return startBy;

		// return super.onStartCommand(p1, p2, p3);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		if (isStarted) {
			stopSelf();
			//throw new RuntimeException();
			return;
		}

		isStarted = true;

		sttbarHeight = (int) getResources().getDimension(com.android.internal.R.dimen.status_bar_height);

		makeWindow(sttbarHeight);

		//main.attach(pa1, pa2, pa3, pa4, pa5, pa6, pa7, pa8, pa9, pa10, pa11, null, pa13, pa14);

		params = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.TYPE_PHONE,
			WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, //FLAG_NOT_FOCUSABLE,
			PixelFormat.TRANSLUCENT);

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		windowManager.addView(windowView, params);
		windowManager.getDefaultDisplay();
		
		//params.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
		windowView.setLayoutParams(params);
		//expandedView.addView(main.getWindow().getDecorView());
	}
	
	// From View code
	/*

	 public void getWindowVisibleDisplayFrame(Rect outRect) {
	 if (mAttachInfo != null) {
	 try {
	 mAttachInfo.mSession.getDisplayFrame(mAttachInfo.mWindow, outRect);
	 } catch (RemoteException e) {
	 return;
	 }
	 // XXX This is really broken, and probably all needs to be done
	 // in the window manager, and we need to know more about whether
	 // we want the area behind or in front of the IME.
	 final Rect insets = mAttachInfo.mVisibleInsets;
	 outRect.left += insets.left;
	 outRect.top += insets.top;
	 outRect.right -= insets.right;
	 outRect.bottom -= insets.bottom;
	 return;
	 }
	 Display d = WindowManagerImpl.getDefault().getDefaultDisplay();
	 d.getRectSize(outRect);
	 }

	 */

	private void setStatusBarHeightInActivity() // throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException
	{
		windowDecorView.setPadding(0, sttbarHeight, 0, 0);
		/*
		 // Try to hack the Window session.

		 // method1: getting internal fields.
		 Field field = windowDecorView.getClass().getField("mAttachInfo");
		 field.setAccessible(true);
		 // The subclass is hidden, so leave it as Object.
		 Object mHiddenAttachInfo = field.get(windowDecorView);

		 // Getting mSession
		 Field field2 = mHiddenAttachInfo.getClass().getField("mSession");
		 field2.setAccessible(true);
		 IWindowSession mHiddenSession = (IWindowSession) field2.get(mHiddenAttachInfo);
		 mHiddenSession.

		 // Some experimentals
		 */
	}

	private void makeWindow(int sttbarHeight) {
		// # Part 1: Instance
		windowView = new FrameLayout(this);
		// { 0
		windowContent = new LinearLayout(this);
		windowTopbar = new RelativeLayout(this);
		// { 1
		windowTitleView = new TextView(this);
		windowMinimizeBtn = new ImageView(this);
		windowCloseBtn = new ImageView(this);
		// } 1
		// } 0

		// # Part 2: Adding
		windowTopbar.addView(windowTitleView);
		windowTopbar.addView(windowMinimizeBtn);
		windowTopbar.addView(windowCloseBtn);

		windowView.addView(windowContent);
		windowView.addView(windowTopbar);

		// # Part 3: ID
		windowTopbar.setId(ID_TOPBAR);
		windowMinimizeBtn.setId(ID_BTN_MIN);
		windowCloseBtn.setId(ID_BTN_CLOSE);

		// # Part 4: Params
		windowTopbar.setLayoutParams(new FrameLayout.LayoutParams(WINDOW_WIDTH, sttbarHeight));
		RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
		titleParams.leftMargin = 10;
		titleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		windowTitleView.setLayoutParams(titleParams);
		
		RelativeLayout.LayoutParams btnMinParams = new RelativeLayout.LayoutParams(sttbarHeight, sttbarHeight);
		btnMinParams.addRule(RelativeLayout.LEFT_OF, ID_BTN_CLOSE);

		RelativeLayout.LayoutParams btnCloseParams = new RelativeLayout.LayoutParams(sttbarHeight, sttbarHeight);
		btnCloseParams.rightMargin = 10;
		btnCloseParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		windowMinimizeBtn.setLayoutParams(btnMinParams);
		windowCloseBtn.setLayoutParams(btnCloseParams);
		
		windowContent.setLayoutParams(new FrameLayout.LayoutParams(WINDOW_WIDTH, WINDOW_HEIGHT));
		
		// Part 5: Resources
		windowView.setBackgroundResource(android.R.drawable.alert_light_frame);
		windowTopbar.setBackgroundColor(bg_topBar_onTop);
		windowMinimizeBtn.setImageResource(R.drawable.ic_minimize);
		windowCloseBtn.setImageResource(R.drawable.ic_close);

		// Part 6: Settings
		windowTitleView.setGravity(Gravity.CENTER_VERTICAL);
		
		// Part 7: Listeners
		windowTopbar.setOnTouchListener(FloatingActivityService.this);
		windowMinimizeBtn.setOnClickListener(FloatingActivityService.this);
		windowCloseBtn.setOnClickListener(FloatingActivityService.this);
	}


	private FrameLayout resetView(ViewGroup target) {
		FrameLayout clone = new FrameLayout(this);
		clone.setActivated(target.isActivated());
		clone.setDrawingCacheEnabled(target.isDrawingCacheEnabled());
		clone.setDuplicateParentStateEnabled(target.isDuplicateParentStateEnabled());
		clone.setClickable(target.isClickable());
		clone.setFocusable(target.isFocusable());
		clone.setEnabled(target.isEnabled());

		clone.setAnimation(target.getAnimation());
		clone.setBackground(target.getBackground());
		clone.setBackgroundTintList(target.getBackgroundTintList());
		clone.setBackgroundTintMode(target.getBackgroundTintMode());

		clone.setId(target.getId());
		clone.setLayoutDirection(target.getLayoutDirection());
		if (target.getLayoutParams() != null)
			clone.setLayoutParams(target.getLayoutParams()); //groupToFrameParams(target.getLayoutParams()));
		clone.setOnFocusChangeListener(target.getOnFocusChangeListener());
		clone.setOutlineProvider(target.getOutlineProvider());
		for (int i = 0; i < target.getChildCount(); i++) {
			View v = target.getChildAt(i);
			target.removeView(v);
			clone.addView(v);
		}

		return clone;
	}
	/*
	 private FrameLayout.LayoutParams groupToFrameParams(ViewGroup.LayoutParams params) {
	 FrameLayout.LayoutParams result = new FrameLayout.LayoutParams(params.width, params.height);
	 result.layoutAnimationParameters = params.layoutAnimationParameters;
	 return result;
	 }
	 */
	private void startWindowedActivity(final Intent act_intent) {
		try {
			// Context act_context = this;
			final ComponentName act_component = act_intent.getComponent();
			final Class<?> act_class = Class.forName(act_component.getClassName());
			
			// Activity initialize fields
			final ActivityThread act_thread = ActivityThread.currentActivityThread();
			// final Application act_application = getApplication();
			final ActivityInfo act_info;
			try {
				act_info = getPackageManager().getActivityInfo(act_component, 0);
			} catch (PackageManager.NameNotFoundException e) {
				throw new ActivityNotFoundException(
					"Unable to find explicit activity class "
					+ act_component.toShortString()
					+ "; have you declared this activity in your AndroidManifest.xml?")
					.initCause(e);
			}
			
			final int act_ident = 0; // ???
			final Configuration act_config = getResources().getConfiguration(); // new Configuration();
			final CompatibilityInfo act_compatInfo = CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO; // new CompatibilityInfo(act_application.getApplicationInfo());
			final IVoiceInteractor act_voiceInteractor = null;
			final int act_procState = 0;
			final Bundle act_state = act_intent.getExtras();
			final PersistableBundle act_persistState = new PersistableBundle();
			final List<ResultInfo> act_pendingResults = new ArrayList<ResultInfo>();
			final List<Intent> act_pendingNewIntents = new ArrayList<Intent>();
			final boolean act_notResumed = false;
			final boolean act_isForward = false;
			final ProfilerInfo act_profilerInfo = null;
			// Invoke start method
			Object applicationThread = act_thread.getApplicationThread();
			//act_token = applicationThread();
			/*
			 * If invoke it directly (by cracking the private API), example:
			 * <code> act_thread.scheduleLaunchActivity(args...) </code>
			 * App will crash as Illegal access to method.
			 */
// METHOD 1: SUCCESS BUT MISSING REGISTERS (forgot here REGISTER is what)
			Method m = applicationThread.getClass().getDeclaredMethod("scheduleLaunchActivity",
																	  Intent.class, IBinder.class, int.class,
																	  ActivityInfo.class, Configuration.class, CompatibilityInfo.class,
																	  IVoiceInteractor.class, int.class, Bundle.class,
																	  PersistableBundle.class, List.class, List.class,
																	  boolean.class, boolean.class, ProfilerInfo.class
																	  );
			m.setAccessible(true);
			m.invoke(applicationThread,
					 act_intent, act_token, act_ident,
					 act_info, act_config, act_compatInfo,
					 act_voiceInteractor, act_procState, act_state,
					 act_persistState, act_pendingResults,
					 act_pendingNewIntents, act_notResumed,
					 act_isForward, act_profilerInfo);

// METHOD 2: intent code
/*
			new Thread(new Runnable(){

					@Override
					public void run()
					{
						/*
						main = act_thread.getInstrumentation().startActivitySync(act_intent);
						* /
						
						while (main == null) {
							try {
								Thread.sleep(100);
							} catch (Throwable th) {}
						}
						
						// These code will execute after the loop end (main is not null).
						
					}
				}).start();
*/
			new Handler().post(new Runnable(){

					@Override
					public void run()
					{
						// Method1
						try {
							main = Utils.getRunningActivity(act_class.getName());
							
							while (main == null) {
								Thread.sleep(100);
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}

						// No additional code in Method2

						final ViewGroup decorView = (ViewGroup) main.getWindow().getDecorView();
						decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
						decorView.dispatchConfigurationChanged(act_config);

						windowContent.addView(windowDecorView = resetView(decorView));
						windowTitleView.setText(main.getTitle());

						setStatusBarHeightInActivity();

						// Make the Activity not fullscreen
						//decorView.getWindowVisibleDisplayFrame();
					}
				});
			
			
		} catch (Throwable th) {
			th.printStackTrace();
			Toolbar toolb = new Toolbar(FloatingActivityService.this, null, android.R.style.Widget_Toolbar);
			ScrollView scr = new ScrollView(FloatingActivityService.this);
			TextView text = new TextView(FloatingActivityService.this);

			toolb.setTitle("Error");
			toolb.setLayoutParams(new Toolbar.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

			text.setText(Log.getStackTraceString(th));
			text.setLayoutParams(new ScrollView.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
			text.setTextColor(Color.BLACK);

			scr.addView(text);
			windowContent.setOrientation(LinearLayout.VERTICAL);
			windowContent.addView(toolb);
			windowContent.addView(scr);
		}
	}
	
	/*
	 private Class<?> findClassByName(Class<?>[] classes, String name) {
	 for (Class<?> clazz : classes) {
	 //Log.d("Check", "Checking: " + clazz.getName());
	 if (name.equals(clazz.getName())) {
	 return clazz;
	 }
	 }

	 return null;
	 }
	 
	private Method findMethodByName(Method[] me, String name) {
		for (Method m : me) {
			//Log.d("Check", "Checking: " + clazz.getName());
			if (name.equals(m.getName())) {
				return m;
			}
		}

		return null;
	}
*/
	private boolean isViewCollapsed() {
		return windowContent == null || windowContent.getVisibility() == View.GONE;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (windowView != null) windowManager.removeView(windowView);
	}

	private int initialX;
	private int initialY;
	private float initialTouchX;
	private float initialTouchY;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == ID_TOPBAR) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//remember the initial position.
					initialX = params.x;
					initialY = params.y;

					//get the touch location
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					return true;
				case MotionEvent.ACTION_MOVE:
					//Calculate the X and Y coordinates of the view.
					params.x = initialX + (int) (event.getRawX() - initialTouchX);
					params.y = initialY + (int) (event.getRawY() - initialTouchY);

					//Update the layout with new X & Y coordinate
					windowManager.updateViewLayout(windowView, params);
					return true;

					/*
					 case MotionEvent.ACTION_UP:
					 int Xdiff = (int) (event.getRawX() - initialTouchX);
					 int Ydiff = (int) (event.getRawY() - initialTouchY);

					 //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
					 //So that is click event.
					 if (Xdiff < 10 && Ydiff < 10) {
					 windowContent.setVisibility(isViewCollapsed() ? View.VISIBLE : View.GONE);
					 }
					 return true;
					 */
			}
		}
		return false;
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId()) { 
			case ID_BTN_MIN: {
					if (isViewCollapsed()) {
						// windowTitleView.setVisibility(View.GONE);
						windowContent.setVisibility(View.VISIBLE);
					} else {
						// windowTitleView.setVisibility(View.VISIBLE);
						windowContent.setVisibility(View.GONE);
					}
				} break;
			case ID_BTN_CLOSE: {
					main.finish();
					stopSelf();
				}
				break;
		}
	}
}
