package b4a.mobilenav;


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
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.mobilenav", "b4a.mobilenav.main");
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
		activityBA = new BA(this, layout, processBA, "b4a.mobilenav", "b4a.mobilenav.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.mobilenav.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create " + (isFirst ? "(first time)" : "") + " **");
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
public qrcodereaderviewwrapper.qrCodeReaderViewWrapper _qrcrv = null;
public anywheresoftware.b4a.objects.WebViewWrapper _webview1 = null;
public anywheresoftware.b4a.phone.Phone.PhoneWakeState _pw = null;
public anywheresoftware.b4a.phone.Phone.PhoneId _ph = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_lat = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_lon = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_cog = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_sog = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_api = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_userid = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbl_qr = null;
public static String _userid = "";
public b4a.mobilenav.starter _starter = null;
public b4a.mobilenav.httputils2service _httputils2service = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 40;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 41;BA.debugLine="Activity.LoadLayout(\"Layout\")";
mostCurrent._activity.LoadLayout("Layout",mostCurrent.activityBA);
 //BA.debugLineNum = 42;BA.debugLine="pw.KeepAlive(True)";
mostCurrent._pw.KeepAlive(processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 43;BA.debugLine="qrcrv.Visible = False";
mostCurrent._qrcrv.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 45;BA.debugLine="userId = ph.GetLine1Number";
mostCurrent._userid = mostCurrent._ph.GetLine1Number();
 //BA.debugLineNum = 46;BA.debugLine="lbl_UserId.Text = IIf (userId.Length >= 8, userId";
mostCurrent._lbl_userid.setText(BA.ObjectToCharSequence(((mostCurrent._userid.length()>=8) ? ((Object)(mostCurrent._userid.substring((int) (2)))) : ((Object)("")))));
 //BA.debugLineNum = 49;BA.debugLine="WebView1.LoadUrl(\"http://ad351ef644a2840e48a104de";
mostCurrent._webview1.LoadUrl("http://ad351ef644a2840e48a104def355969a-1173132100.ap-southeast-1.elb.amazonaws.com:3838/realtime?mmsi="+mostCurrent._lbl_userid.getText());
 //BA.debugLineNum = 50;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 63;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 64;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 65;BA.debugLine="pw.ReleaseKeepAlive";
mostCurrent._pw.ReleaseKeepAlive();
 //BA.debugLineNum = 66;BA.debugLine="CallSubDelayed(Starter, \"StopGPS\")";
anywheresoftware.b4a.keywords.Common.CallSubDelayed(processBA,(Object)(mostCurrent._starter.getObject()),"StopGPS");
 };
 //BA.debugLineNum = 68;BA.debugLine="End Sub";
return "";
}
public static void  _activity_resume() throws Exception{
ResumableSub_Activity_Resume rsub = new ResumableSub_Activity_Resume(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Resume extends BA.ResumableSub {
public ResumableSub_Activity_Resume(b4a.mobilenav.main parent) {
this.parent = parent;
}
b4a.mobilenav.main parent;
String _permission = "";
boolean _result = false;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 53;BA.debugLine="If Starter.GPS1.GPSEnabled = False Then";
if (true) break;

case 1:
//if
this.state = 12;
if (parent.mostCurrent._starter._gps1 /*anywheresoftware.b4a.gps.GPS*/ .getGPSEnabled()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 12;
 //BA.debugLineNum = 54;BA.debugLine="ToastMessageShow(\"Please enable the GPS device.\"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please enable the GPS device."),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 55;BA.debugLine="StartActivity(Starter.GPS1.LocationSettingsInten";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._starter._gps1 /*anywheresoftware.b4a.gps.GPS*/ .getLocationSettingsIntent()));
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 57;BA.debugLine="Starter.rp.CheckAndRequest(Starter.rp.PERMISSION";
parent.mostCurrent._starter._rp /*anywheresoftware.b4a.objects.RuntimePermissions*/ .CheckAndRequest(processBA,parent.mostCurrent._starter._rp /*anywheresoftware.b4a.objects.RuntimePermissions*/ .PERMISSION_ACCESS_FINE_LOCATION);
 //BA.debugLineNum = 58;BA.debugLine="Wait For Activity_PermissionResult (Permission A";
anywheresoftware.b4a.keywords.Common.WaitFor("activity_permissionresult", processBA, this, null);
this.state = 13;
return;
case 13:
//C
this.state = 6;
_permission = (String) result[0];
_result = (Boolean) result[1];
;
 //BA.debugLineNum = 59;BA.debugLine="If Result Then CallSubDelayed(Starter, \"StartGPS";
if (true) break;

case 6:
//if
this.state = 11;
if (_result) { 
this.state = 8;
;}if (true) break;

case 8:
//C
this.state = 11;
anywheresoftware.b4a.keywords.Common.CallSubDelayed(processBA,(Object)(parent.mostCurrent._starter.getObject()),"StartGPS");
if (true) break;

case 11:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 61;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _activity_permissionresult(String _permission,boolean _result) throws Exception{
}
public static void  _button1_click() throws Exception{
ResumableSub_Button1_Click rsub = new ResumableSub_Button1_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Button1_Click extends BA.ResumableSub {
public ResumableSub_Button1_Click(b4a.mobilenav.main parent) {
this.parent = parent;
}
b4a.mobilenav.main parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 144;BA.debugLine="qrcrv.Visible = True";
parent.mostCurrent._qrcrv.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 146;BA.debugLine="Sleep(0)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (0));
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 147;BA.debugLine="qrcrv.startScan";
parent.mostCurrent._qrcrv.startScan();
 //BA.debugLineNum = 148;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 23;BA.debugLine="Private qrcrv As QRCodeReaderView";
mostCurrent._qrcrv = new qrcodereaderviewwrapper.qrCodeReaderViewWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private WebView1 As WebView";
mostCurrent._webview1 = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Dim pw As PhoneWakeState";
mostCurrent._pw = new anywheresoftware.b4a.phone.Phone.PhoneWakeState();
 //BA.debugLineNum = 26;BA.debugLine="Dim ph As PhoneId";
mostCurrent._ph = new anywheresoftware.b4a.phone.Phone.PhoneId();
 //BA.debugLineNum = 28;BA.debugLine="Private Button1 As Button";
mostCurrent._button1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Dim lbl_Lat As Label";
mostCurrent._lbl_lat = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Dim lbl_Lon As Label";
mostCurrent._lbl_lon = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Dim lbl_Cog As Label";
mostCurrent._lbl_cog = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Dim lbl_Sog As Label";
mostCurrent._lbl_sog = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Dim lbl_Api As Label";
mostCurrent._lbl_api = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Dim lbl_UserId As Label";
mostCurrent._lbl_userid = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Dim lbl_QR As Label";
mostCurrent._lbl_qr = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Dim userId As String";
mostCurrent._userid = "";
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
public static String  _gpsstatus(anywheresoftware.b4a.objects.collections.List _satellites) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _sb = null;
int _i = 0;
anywheresoftware.b4a.gps.GpsSatelliteWrapper _satellite = null;
 //BA.debugLineNum = 70;BA.debugLine="Public Sub GpsStatus (Satellites As List)";
 //BA.debugLineNum = 71;BA.debugLine="Dim sb As StringBuilder";
_sb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 72;BA.debugLine="sb.Initialize";
_sb.Initialize();
 //BA.debugLineNum = 73;BA.debugLine="sb.Append(\"Satellites:\").Append(CRLF)";
_sb.Append("Satellites:").Append(anywheresoftware.b4a.keywords.Common.CRLF);
 //BA.debugLineNum = 74;BA.debugLine="For i = 0 To Satellites.Size - 1";
{
final int step4 = 1;
final int limit4 = (int) (_satellites.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit4 ;_i = _i + step4 ) {
 //BA.debugLineNum = 75;BA.debugLine="Dim Satellite As GPSSatellite = Satellites.Get(i";
_satellite = new anywheresoftware.b4a.gps.GpsSatelliteWrapper();
_satellite = (anywheresoftware.b4a.gps.GpsSatelliteWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.gps.GpsSatelliteWrapper(), (android.location.GpsSatellite)(_satellites.Get(_i)));
 //BA.debugLineNum = 76;BA.debugLine="sb.Append(CRLF).Append(Satellite.Prn).Append($\"";
_sb.Append(anywheresoftware.b4a.keywords.Common.CRLF).Append(BA.NumberToString(_satellite.getPrn())).Append((" "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("1.2",(Object)(_satellite.getSnr()))+"")).Append(" ").Append(BA.ObjectToString(_satellite.getUsedInFix()));
 //BA.debugLineNum = 77;BA.debugLine="sb.Append(\" \").Append($\" $1.2{Satellite.Azimuth}";
_sb.Append(" ").Append((" "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("1.2",(Object)(_satellite.getAzimuth()))+"")).Append((" "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("1.2",(Object)(_satellite.getElevation()))+""));
 }
};
 //BA.debugLineNum = 81;BA.debugLine="End Sub";
return "";
}
public static void  _locationchanged(anywheresoftware.b4a.gps.LocationWrapper _location1) throws Exception{
ResumableSub_LocationChanged rsub = new ResumableSub_LocationChanged(null,_location1);
rsub.resume(processBA, null);
}
public static class ResumableSub_LocationChanged extends BA.ResumableSub {
public ResumableSub_LocationChanged(b4a.mobilenav.main parent,anywheresoftware.b4a.gps.LocationWrapper _location1) {
this.parent = parent;
this._location1 = _location1;
}
b4a.mobilenav.main parent;
anywheresoftware.b4a.gps.LocationWrapper _location1;
long _now = 0L;
String _datestr = "";
b4a.mobilenav.httpjob _job = null;
anywheresoftware.b4a.objects.collections.Map _data = null;
anywheresoftware.b4a.objects.collections.JSONParser.JSONGenerator _json = null;
String _str = "";
String _result = "";

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
try {

        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 84;BA.debugLine="lbl_Lat.Text = Location1.Latitude";
parent.mostCurrent._lbl_lat.setText(BA.ObjectToCharSequence(_location1.getLatitude()));
 //BA.debugLineNum = 85;BA.debugLine="lbl_Lon.Text = Location1.Longitude";
parent.mostCurrent._lbl_lon.setText(BA.ObjectToCharSequence(_location1.getLongitude()));
 //BA.debugLineNum = 86;BA.debugLine="lbl_Sog.Text = $\"$1.4{Location1.Speed} m/s \"$";
parent.mostCurrent._lbl_sog.setText(BA.ObjectToCharSequence((""+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("1.4",(Object)(_location1.getSpeed()))+" m/s ")));
 //BA.debugLineNum = 87;BA.debugLine="lbl_Cog.Text = Location1.Bearing";
parent.mostCurrent._lbl_cog.setText(BA.ObjectToCharSequence(_location1.getBearing()));
 //BA.debugLineNum = 89;BA.debugLine="DateTime.DateFormat = \"yyyy-MM-dd\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("yyyy-MM-dd");
 //BA.debugLineNum = 90;BA.debugLine="DateTime.TimeFormat = \"HH:mm:ss\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("HH:mm:ss");
 //BA.debugLineNum = 92;BA.debugLine="Dim now As Long = DateTime.Now";
_now = anywheresoftware.b4a.keywords.Common.DateTime.getNow();
 //BA.debugLineNum = 93;BA.debugLine="Dim dateStr As String = DateTime.Date(now) & \" \"";
_datestr = anywheresoftware.b4a.keywords.Common.DateTime.Date(_now)+" "+anywheresoftware.b4a.keywords.Common.DateTime.Time(_now);
 //BA.debugLineNum = 95;BA.debugLine="Dim Job As HttpJob";
_job = new b4a.mobilenav.httpjob();
 //BA.debugLineNum = 96;BA.debugLine="Dim data As Map";
_data = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 97;BA.debugLine="Dim json As JSONGenerator";
_json = new anywheresoftware.b4a.objects.collections.JSONParser.JSONGenerator();
 //BA.debugLineNum = 99;BA.debugLine="Job.Initialize(\"\",Me)";
_job._initialize /*String*/ (processBA,"",main.getObject());
 //BA.debugLineNum = 101;BA.debugLine="data.Initialize";
_data.Initialize();
 //BA.debugLineNum = 102;BA.debugLine="data.Put(\"ts\", dateStr)";
_data.Put((Object)("ts"),(Object)(_datestr));
 //BA.debugLineNum = 103;BA.debugLine="data.Put(\"msgtype\", \"3\")";
_data.Put((Object)("msgtype"),(Object)("3"));
 //BA.debugLineNum = 104;BA.debugLine="data.Put(\"mmsi\", \"'\" & lbl_UserId.Text & \"'\")";
_data.Put((Object)("mmsi"),(Object)("'"+parent.mostCurrent._lbl_userid.getText()+"'"));
 //BA.debugLineNum = 105;BA.debugLine="data.Put(\"sog\", \"'\" & Location1.Speed & \"'\")";
_data.Put((Object)("sog"),(Object)("'"+BA.NumberToString(_location1.getSpeed())+"'"));
 //BA.debugLineNum = 106;BA.debugLine="data.Put(\"cog\", \"'\" & Location1.Bearing & \"'\")";
_data.Put((Object)("cog"),(Object)("'"+BA.NumberToString(_location1.getBearing())+"'"));
 //BA.debugLineNum = 107;BA.debugLine="data.Put(\"rot\", \"0\")";
_data.Put((Object)("rot"),(Object)("0"));
 //BA.debugLineNum = 108;BA.debugLine="data.Put(\"heading\", \"0\")";
_data.Put((Object)("heading"),(Object)("0"));
 //BA.debugLineNum = 109;BA.debugLine="data.Put(\"lat\", \"'\" & Location1.Latitude & \"'\")";
_data.Put((Object)("lat"),(Object)("'"+BA.NumberToString(_location1.getLatitude())+"'"));
 //BA.debugLineNum = 110;BA.debugLine="data.Put(\"lng\",  \"'\" & Location1.Longitude & \"'\")";
_data.Put((Object)("lng"),(Object)("'"+BA.NumberToString(_location1.getLongitude())+"'"));
 //BA.debugLineNum = 111;BA.debugLine="json.Initialize(data)";
_json.Initialize(_data);
 //BA.debugLineNum = 113;BA.debugLine="If lbl_UserId.Text <> \"\" Then";
if (true) break;

case 1:
//if
this.state = 16;
if ((parent.mostCurrent._lbl_userid.getText()).equals("") == false) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 114;BA.debugLine="Private str As String = \"http://aec45b1e527ea46d";
_str = "http://aec45b1e527ea46d394dacdbe444e19c-806738025.ap-southeast-1.elb.amazonaws.com:3838/insert";
 //BA.debugLineNum = 116;BA.debugLine="Try";
if (true) break;

case 4:
//try
this.state = 15;
this.catchState = 14;
this.state = 6;
if (true) break;

case 6:
//C
this.state = 7;
this.catchState = 14;
 //BA.debugLineNum = 117;BA.debugLine="Job.PostString(str, json.ToString)";
_job._poststring /*String*/ (_str,_json.ToString());
 //BA.debugLineNum = 119;BA.debugLine="Job.GetRequest.SetHeader(\"accept\",\"application/";
_job._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetHeader("accept","application/json");
 //BA.debugLineNum = 120;BA.debugLine="Job.GetRequest.SetContentType(\"application/json";
_job._getrequest /*anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest*/ ().SetContentType("application/json");
 //BA.debugLineNum = 122;BA.debugLine="Wait For (Job) JobDone(Job As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_job));
this.state = 17;
return;
case 17:
//C
this.state = 7;
_job = (b4a.mobilenav.httpjob) result[0];
;
 //BA.debugLineNum = 124;BA.debugLine="If Job.Success Then";
if (true) break;

case 7:
//if
this.state = 12;
if (_job._success /*boolean*/ ) { 
this.state = 9;
}else {
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 125;BA.debugLine="lbl_Api.Text = \"Success! - \"";
parent.mostCurrent._lbl_api.setText(BA.ObjectToCharSequence("Success! - "));
 //BA.debugLineNum = 126;BA.debugLine="Dim result As String = Job.GetString";
_result = _job._getstring /*String*/ ();
 //BA.debugLineNum = 128;BA.debugLine="lbl_Api.Text = result";
parent.mostCurrent._lbl_api.setText(BA.ObjectToCharSequence(_result));
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 131;BA.debugLine="lbl_Api.Text = \"Fail! - \" & Job.ErrorMessage";
parent.mostCurrent._lbl_api.setText(BA.ObjectToCharSequence("Fail! - "+_job._errormessage /*String*/ ));
 if (true) break;

case 12:
//C
this.state = 15;
;
 if (true) break;

case 14:
//C
this.state = 15;
this.catchState = 0;
 //BA.debugLineNum = 134;BA.debugLine="lbl_Api.Text = \"[error]::\" & LastException.Mess";
parent.mostCurrent._lbl_api.setText(BA.ObjectToCharSequence("[error]::"+anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage()));
 if (true) break;
if (true) break;

case 15:
//C
this.state = 16;
this.catchState = 0;
;
 if (true) break;

case 16:
//C
this.state = -1;
;
 //BA.debugLineNum = 138;BA.debugLine="Job.Release";
_job._release /*String*/ ();
 //BA.debugLineNum = 140;BA.debugLine="End Sub";
if (true) break;
}} 
       catch (Exception e0) {
			
if (catchState == 0)
    throw e0;
else {
    state = catchState;
processBA.setLastException(e0);}
            }
        }
    }
}
public static void  _jobdone(b4a.mobilenav.httpjob _job) throws Exception{
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
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public static String  _qrcrv_result_found(String _retval) throws Exception{
 //BA.debugLineNum = 150;BA.debugLine="Sub qrcrv_result_found(retval As String)";
 //BA.debugLineNum = 151;BA.debugLine="qrcrv.stopScan";
mostCurrent._qrcrv.stopScan();
 //BA.debugLineNum = 152;BA.debugLine="qrcrv.Visible = False";
mostCurrent._qrcrv.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 153;BA.debugLine="lbl_QR.Text = retval";
mostCurrent._lbl_qr.setText(BA.ObjectToCharSequence(_retval));
 //BA.debugLineNum = 154;BA.debugLine="End Sub";
return "";
}
}
