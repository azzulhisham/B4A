package b4a.MyFirstHelloAndroid;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.MyFirstHelloAndroid", "b4a.MyFirstHelloAndroid.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "b4a.MyFirstHelloAndroid", "b4a.MyFirstHelloAndroid.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.MyFirstHelloAndroid.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (main) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public static int _num1 = 0;
public static int _num2 = 0;
public static String[] _selectedlangs = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblnumber1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblnumber2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_httpresult = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn_httpcall = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinner1 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext2 = null;
public b4a.MyFirstHelloAndroid.starter _starter = null;
public b4a.MyFirstHelloAndroid.httputils2service _httputils2service = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 41;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 42;BA.debugLine="Activity.LoadLayout(\"LayoutMain\")";
mostCurrent._activity.LoadLayout("LayoutMain",mostCurrent.activityBA);
 //BA.debugLineNum = 43;BA.debugLine="Spinner1.AddAll(Array As String(\"English\", \"中文 (简";
mostCurrent._spinner1.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"English","中文 (简体)","한국어","日本語"}));
 //BA.debugLineNum = 45;BA.debugLine="NewEquation";
_newequation();
 //BA.debugLineNum = 46;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 52;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 48;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 50;BA.debugLine="End Sub";
return "";
}
public static void  _btn_httpcall_click() throws Exception{
ResumableSub_btn_HttpCall_Click rsub = new ResumableSub_btn_HttpCall_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btn_HttpCall_Click extends BA.ResumableSub {
public ResumableSub_btn_HttpCall_Click(b4a.MyFirstHelloAndroid.main parent) {
this.parent = parent;
}
b4a.MyFirstHelloAndroid.main parent;
b4a.MyFirstHelloAndroid.httpjob _job = null;
String _str = "";
String _word = "";
String _result = "";
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.List _root = null;
anywheresoftware.b4a.objects.collections.Map _map1 = null;
anywheresoftware.b4a.objects.collections.List _msg = null;
String _res = "";
anywheresoftware.b4a.objects.collections.Map _data = null;
anywheresoftware.b4a.BA.IterableList group20;
int index20;
int groupLen20;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 96;BA.debugLine="Dim Job As HttpJob";
_job = new b4a.MyFirstHelloAndroid.httpjob();
 //BA.debugLineNum = 98;BA.debugLine="Job.Initialize(\"\",Me)";
_job._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 103;BA.debugLine="Private str As String = $\"https://api.cognitive.m";
_str = ("https://api.cognitive.microsofttranslator.com/translate?api-version=3.0&from=en&to="+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(parent.mostCurrent._selectedlangs[parent.mostCurrent._spinner1.getSelectedIndex()]))+"");
 //BA.debugLineNum = 104;BA.debugLine="Dim word As String = $\"[{'text':'${EditText2.Text";
_word = ("[{'text':'"+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(parent.mostCurrent._edittext2.getText().trim()))+"'}]");
 //BA.debugLineNum = 106;BA.debugLine="Job.PostString(str, word)";
_job._poststring /*String*/ (_str,_word);
 //BA.debugLineNum = 108;BA.debugLine="Job.GetRequest.SetHeader(\"Ocp-Apim-Subscription-K";
_job._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("Ocp-Apim-Subscription-Key","5f6d8efdd5b54de69a35b76a8e320332");
 //BA.debugLineNum = 109;BA.debugLine="Job.GetRequest.SetHeader(\"Ocp-Apim-Subscription-R";
_job._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("Ocp-Apim-Subscription-Region","southeastasia");
 //BA.debugLineNum = 110;BA.debugLine="Job.GetRequest.SetHeader(\"accept\",\"application/js";
_job._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("accept","application/json");
 //BA.debugLineNum = 111;BA.debugLine="Job.GetRequest.SetContentType(\"application/json\")";
_job._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetContentType("application/json");
 //BA.debugLineNum = 113;BA.debugLine="Wait For (Job) JobDone(Job As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_job));
this.state = 11;
return;
case 11:
//C
this.state = 1;
_job = (b4a.MyFirstHelloAndroid.httpjob) result[0];
;
 //BA.debugLineNum = 115;BA.debugLine="If Job.Success Then";
if (true) break;

case 1:
//if
this.state = 10;
if (_job._success /*boolean*/ ) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 116;BA.debugLine="lbl_HttpResult.Text = \"Success! - \"";
parent.mostCurrent._lbl_httpresult.setText(BA.ObjectToCharSequence("Success! - "));
 //BA.debugLineNum = 117;BA.debugLine="Dim result As String = Job.GetString";
_result = _job._getstring /*String*/ ();
 //BA.debugLineNum = 119;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 120;BA.debugLine="parser.Initialize(result)";
_parser.Initialize(_result);
 //BA.debugLineNum = 121;BA.debugLine="Dim root As List = parser.NextArray";
_root = new anywheresoftware.b4a.objects.collections.List();
_root = _parser.NextArray();
 //BA.debugLineNum = 122;BA.debugLine="Dim map1 As Map = root.Get(0)";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
_map1 = (anywheresoftware.b4a.objects.collections.Map) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), (anywheresoftware.b4a.objects.collections.Map.MyMap)(_root.Get((int) (0))));
 //BA.debugLineNum = 124;BA.debugLine="Dim msg As List = map1.Get(\"translations\")";
_msg = new anywheresoftware.b4a.objects.collections.List();
_msg = (anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(_map1.Get((Object)("translations"))));
 //BA.debugLineNum = 125;BA.debugLine="Dim res As String";
_res = "";
 //BA.debugLineNum = 127;BA.debugLine="For Each data As Map In msg";
if (true) break;

case 4:
//for
this.state = 7;
_data = new anywheresoftware.b4a.objects.collections.Map();
group20 = _msg;
index20 = 0;
groupLen20 = group20.getSize();
this.state = 12;
if (true) break;

case 12:
//C
this.state = 7;
if (index20 < groupLen20) {
this.state = 6;
_data = (anywheresoftware.b4a.objects.collections.Map) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.Map(), (anywheresoftware.b4a.objects.collections.Map.MyMap)(group20.Get(index20)));}
if (true) break;

case 13:
//C
this.state = 12;
index20++;
if (true) break;

case 6:
//C
this.state = 13;
 //BA.debugLineNum = 128;BA.debugLine="res = data.Get(\"text\")";
_res = BA.ObjectToString(_data.Get((Object)("text")));
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 131;BA.debugLine="lbl_HttpResult.Text = res";
parent.mostCurrent._lbl_httpresult.setText(BA.ObjectToCharSequence(_res));
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 134;BA.debugLine="lbl_HttpResult.Text = \"Fail! - \"  + Job.ErrorMes";
parent.mostCurrent._lbl_httpresult.setText(BA.ObjectToCharSequence((double)(Double.parseDouble("Fail! - "))+(double)(Double.parseDouble(_job._errormessage /*String*/ ))));
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 137;BA.debugLine="Job.Release";
_job._release /*String*/ ();
 //BA.debugLineNum = 139;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _jobdone(b4a.MyFirstHelloAndroid.httpjob _job) throws Exception{
}
public static String  _button1_click() throws Exception{
 //BA.debugLineNum = 80;BA.debugLine="Private Sub Button1_Click";
 //BA.debugLineNum = 82;BA.debugLine="If Button1.Text = \"Check Result\" Then";
if ((mostCurrent._button1.getText()).equals("Check Result")) { 
 //BA.debugLineNum = 83;BA.debugLine="If EditText1.Text = \"\" Then";
if ((mostCurrent._edittext1.getText()).equals("")) { 
 //BA.debugLineNum = 84;BA.debugLine="xui.MsgboxAsync(\"Please enter your result!\", \"S";
_xui.MsgboxAsync(processBA,BA.ObjectToCharSequence("Please enter your result!"),BA.ObjectToCharSequence("Simple Math"));
 }else {
 //BA.debugLineNum = 86;BA.debugLine="CheckResult";
_checkresult();
 };
 }else {
 //BA.debugLineNum = 89;BA.debugLine="NewEquation";
_newequation();
 };
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
return "";
}
public static String  _checkresult() throws Exception{
 //BA.debugLineNum = 69;BA.debugLine="Sub CheckResult";
 //BA.debugLineNum = 71;BA.debugLine="If EditText1.Text = num1 + num2 Then";
if ((mostCurrent._edittext1.getText()).equals(BA.NumberToString(_num1+_num2))) { 
 //BA.debugLineNum = 72;BA.debugLine="xui.MsgboxAsync(\"Well Done!\", \"Simple Math\")";
_xui.MsgboxAsync(processBA,BA.ObjectToCharSequence("Well Done!"),BA.ObjectToCharSequence("Simple Math"));
 //BA.debugLineNum = 73;BA.debugLine="Button1.Text = \"Try Another!\"";
mostCurrent._button1.setText(BA.ObjectToCharSequence("Try Another!"));
 }else {
 //BA.debugLineNum = 75;BA.debugLine="xui.MsgboxAsync(\"Ops! Please try again.\", \"Simpl";
_xui.MsgboxAsync(processBA,BA.ObjectToCharSequence("Ops! Please try again."),BA.ObjectToCharSequence("Simple Math"));
 };
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
return "";
}
public static String  _edittext2_focuschanged(boolean _hasfocus) throws Exception{
 //BA.debugLineNum = 147;BA.debugLine="Private Sub EditText2_FocusChanged (HasFocus As Bo";
 //BA.debugLineNum = 148;BA.debugLine="If HasFocus Then";
if (_hasfocus) { 
 //BA.debugLineNum = 149;BA.debugLine="EditText2.SelectAll";
mostCurrent._edittext2.SelectAll();
 };
 //BA.debugLineNum = 151;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 24;BA.debugLine="Public num1 As Int";
_num1 = 0;
 //BA.debugLineNum = 25;BA.debugLine="Public num2 As Int";
_num2 = 0;
 //BA.debugLineNum = 26;BA.debugLine="Public selectedlangs() As String = Array As Strin";
mostCurrent._selectedlangs = new String[]{"en","zh-hans","ko","ja"};
 //BA.debugLineNum = 28;BA.debugLine="Private EditText1 As EditText";
mostCurrent._edittext1 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private lblNumber1 As Label";
mostCurrent._lblnumber1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private lblNumber2 As Label";
mostCurrent._lblnumber2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private Button1 As Button";
mostCurrent._button1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private lbl_HttpResult As Label";
mostCurrent._lbl_httpresult = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private btn_HttpCall As Button";
mostCurrent._btn_httpcall = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private Spinner1 As Spinner";
mostCurrent._spinner1 = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private EditText2 As EditText";
mostCurrent._edittext2 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 39;BA.debugLine="End Sub";
return "";
}
public static String  _newequation() throws Exception{
 //BA.debugLineNum = 56;BA.debugLine="Sub NewEquation";
 //BA.debugLineNum = 58;BA.debugLine="num1 = Rnd(1, 10)";
_num1 = anywheresoftware.b4a.keywords.Common.Rnd((int) (1),(int) (10));
 //BA.debugLineNum = 59;BA.debugLine="num2 = Rnd(1, 10)";
_num2 = anywheresoftware.b4a.keywords.Common.Rnd((int) (1),(int) (10));
 //BA.debugLineNum = 61;BA.debugLine="lblNumber1.Text = num1";
mostCurrent._lblnumber1.setText(BA.ObjectToCharSequence(_num1));
 //BA.debugLineNum = 62;BA.debugLine="lblNumber2.Text = num2";
mostCurrent._lblnumber2.setText(BA.ObjectToCharSequence(_num2));
 //BA.debugLineNum = 64;BA.debugLine="EditText1.Text = \"\"";
mostCurrent._edittext1.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 65;BA.debugLine="Button1.Text = \"Check Result\"";
mostCurrent._button1.setText(BA.ObjectToCharSequence("Check Result"));
 //BA.debugLineNum = 67;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
starter._process_globals();
httputils2service._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 18;BA.debugLine="Private xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _spinner1_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 143;BA.debugLine="Private Sub Spinner1_ItemClick (Position As Int, V";
 //BA.debugLineNum = 145;BA.debugLine="End Sub";
return "";
}
}
