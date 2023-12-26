package b4a.mobilenav.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_layout{

public static void LS_general(anywheresoftware.b4a.BA ba, android.view.View parent, anywheresoftware.b4a.keywords.LayoutValues lv, java.util.Map props,
java.util.Map<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) throws Exception {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 4;BA.debugLine="Panel1.Left = 2%x"[Layout/General script]
views.get("panel1").vw.setLeft((int)((2d / 100 * width)));
//BA.debugLineNum = 5;BA.debugLine="Panel1.Top = 2%y"[Layout/General script]
views.get("panel1").vw.setTop((int)((2d / 100 * height)));
//BA.debugLineNum = 6;BA.debugLine="Panel1.width = 96%x"[Layout/General script]
views.get("panel1").vw.setWidth((int)((96d / 100 * width)));
//BA.debugLineNum = 7;BA.debugLine="Panel1.Height = 96%y"[Layout/General script]
views.get("panel1").vw.setHeight((int)((96d / 100 * height)));
//BA.debugLineNum = 9;BA.debugLine="WebView1.Left = 15"[Layout/General script]
views.get("webview1").vw.setLeft((int)(15d));
//BA.debugLineNum = 10;BA.debugLine="WebView1.Top = 15"[Layout/General script]
views.get("webview1").vw.setTop((int)(15d));
//BA.debugLineNum = 11;BA.debugLine="WebView1.Width = Panel1.width-30"[Layout/General script]
views.get("webview1").vw.setWidth((int)((views.get("panel1").vw.getWidth())-30d));
//BA.debugLineNum = 12;BA.debugLine="WebView1.Height = 0.7 * Panel1.Height"[Layout/General script]
views.get("webview1").vw.setHeight((int)(0.7d*(views.get("panel1").vw.getHeight())));
//BA.debugLineNum = 14;BA.debugLine="qrcrv.Top = WebView1.Top"[Layout/General script]
views.get("qrcrv").vw.setTop((int)((views.get("webview1").vw.getTop())));
//BA.debugLineNum = 15;BA.debugLine="qrcrv.Left = WebView1.Left"[Layout/General script]
views.get("qrcrv").vw.setLeft((int)((views.get("webview1").vw.getLeft())));
//BA.debugLineNum = 16;BA.debugLine="qrcrv.Width = WebView1.Width"[Layout/General script]
views.get("qrcrv").vw.setWidth((int)((views.get("webview1").vw.getWidth())));
//BA.debugLineNum = 17;BA.debugLine="qrcrv.Height = WebView1.Height"[Layout/General script]
views.get("qrcrv").vw.setHeight((int)((views.get("webview1").vw.getHeight())));
//BA.debugLineNum = 19;BA.debugLine="Button2.Top = Panel1.Height - Button2.Height"[Layout/General script]
views.get("button2").vw.setTop((int)((views.get("panel1").vw.getHeight())-(views.get("button2").vw.getHeight())));
//BA.debugLineNum = 20;BA.debugLine="Button2.Left = Panel1.Width - Button2.Width"[Layout/General script]
views.get("button2").vw.setLeft((int)((views.get("panel1").vw.getWidth())-(views.get("button2").vw.getWidth())));
//BA.debugLineNum = 22;BA.debugLine="Button1.Top = WebView1.Height - Button1.Height + 10"[Layout/General script]
views.get("button1").vw.setTop((int)((views.get("webview1").vw.getHeight())-(views.get("button1").vw.getHeight())+10d));
//BA.debugLineNum = 23;BA.debugLine="Button1.Left = WebView1.Left"[Layout/General script]
views.get("button1").vw.setLeft((int)((views.get("webview1").vw.getLeft())));
//BA.debugLineNum = 25;BA.debugLine="Label1.Top = WebView1.Height + 10"[Layout/General script]
views.get("label1").vw.setTop((int)((views.get("webview1").vw.getHeight())+10d));
//BA.debugLineNum = 26;BA.debugLine="lbl_Lat.Top = WebView1.Height + 10"[Layout/General script]
views.get("lbl_lat").vw.setTop((int)((views.get("webview1").vw.getHeight())+10d));
//BA.debugLineNum = 27;BA.debugLine="lbl_Lat.Left = Label1.Left + Label1.Width"[Layout/General script]
views.get("lbl_lat").vw.setLeft((int)((views.get("label1").vw.getLeft())+(views.get("label1").vw.getWidth())));
//BA.debugLineNum = 29;BA.debugLine="Label2.Top = WebView1.Height + Label1.Height + 10"[Layout/General script]
views.get("label2").vw.setTop((int)((views.get("webview1").vw.getHeight())+(views.get("label1").vw.getHeight())+10d));
//BA.debugLineNum = 30;BA.debugLine="lbl_Lon.Top = WebView1.Height + Label1.Height + 10"[Layout/General script]
views.get("lbl_lon").vw.setTop((int)((views.get("webview1").vw.getHeight())+(views.get("label1").vw.getHeight())+10d));
//BA.debugLineNum = 31;BA.debugLine="lbl_Lon.Left = Label2.Left + Label2.Width"[Layout/General script]
views.get("lbl_lon").vw.setLeft((int)((views.get("label2").vw.getLeft())+(views.get("label2").vw.getWidth())));
//BA.debugLineNum = 33;BA.debugLine="Label3.Top = WebView1.Height + Label1.Height + Label2.Height + 10"[Layout/General script]
views.get("label3").vw.setTop((int)((views.get("webview1").vw.getHeight())+(views.get("label1").vw.getHeight())+(views.get("label2").vw.getHeight())+10d));
//BA.debugLineNum = 34;BA.debugLine="lbl_Cog.Top = WebView1.Height + Label1.Height + Label2.Height + 10"[Layout/General script]
views.get("lbl_cog").vw.setTop((int)((views.get("webview1").vw.getHeight())+(views.get("label1").vw.getHeight())+(views.get("label2").vw.getHeight())+10d));
//BA.debugLineNum = 35;BA.debugLine="lbl_Cog.Left = Label2.Left + Label3.Width"[Layout/General script]
views.get("lbl_cog").vw.setLeft((int)((views.get("label2").vw.getLeft())+(views.get("label3").vw.getWidth())));
//BA.debugLineNum = 37;BA.debugLine="Label4.Top = WebView1.Height + Label1.Height + Label2.Height + Label3.Height + 10"[Layout/General script]
views.get("label4").vw.setTop((int)((views.get("webview1").vw.getHeight())+(views.get("label1").vw.getHeight())+(views.get("label2").vw.getHeight())+(views.get("label3").vw.getHeight())+10d));
//BA.debugLineNum = 38;BA.debugLine="lbl_Sog.Top = WebView1.Height + Label1.Height + Label2.Height + Label3.Height + 10"[Layout/General script]
views.get("lbl_sog").vw.setTop((int)((views.get("webview1").vw.getHeight())+(views.get("label1").vw.getHeight())+(views.get("label2").vw.getHeight())+(views.get("label3").vw.getHeight())+10d));
//BA.debugLineNum = 39;BA.debugLine="lbl_Sog.Left = Label2.Left + Label4.Width"[Layout/General script]
views.get("lbl_sog").vw.setLeft((int)((views.get("label2").vw.getLeft())+(views.get("label4").vw.getWidth())));
//BA.debugLineNum = 41;BA.debugLine="Label5.Top = WebView1.Height + Label1.Height + Label2.Height + Label3.Height + Label4.Height + 10"[Layout/General script]
views.get("label5").vw.setTop((int)((views.get("webview1").vw.getHeight())+(views.get("label1").vw.getHeight())+(views.get("label2").vw.getHeight())+(views.get("label3").vw.getHeight())+(views.get("label4").vw.getHeight())+10d));
//BA.debugLineNum = 42;BA.debugLine="lbl_Api.Top = WebView1.Height + Label1.Height + Label2.Height + Label3.Height + Label3.Height + 10"[Layout/General script]
views.get("lbl_api").vw.setTop((int)((views.get("webview1").vw.getHeight())+(views.get("label1").vw.getHeight())+(views.get("label2").vw.getHeight())+(views.get("label3").vw.getHeight())+(views.get("label3").vw.getHeight())+10d));
//BA.debugLineNum = 43;BA.debugLine="lbl_Api.Left = Label2.Left + Label4.Width"[Layout/General script]
views.get("lbl_api").vw.setLeft((int)((views.get("label2").vw.getLeft())+(views.get("label4").vw.getWidth())));
//BA.debugLineNum = 45;BA.debugLine="lbl_UserId.Top = WebView1.Height + 10"[Layout/General script]
views.get("lbl_userid").vw.setTop((int)((views.get("webview1").vw.getHeight())+10d));
//BA.debugLineNum = 46;BA.debugLine="lbl_UserId.Left = Panel1.Width - lbl_UserId.Width - 20"[Layout/General script]
views.get("lbl_userid").vw.setLeft((int)((views.get("panel1").vw.getWidth())-(views.get("lbl_userid").vw.getWidth())-20d));
//BA.debugLineNum = 48;BA.debugLine="lbl_QR.Top = WebView1.Height + lbl_UserId.Height + 10"[Layout/General script]
views.get("lbl_qr").vw.setTop((int)((views.get("webview1").vw.getHeight())+(views.get("lbl_userid").vw.getHeight())+10d));
//BA.debugLineNum = 49;BA.debugLine="lbl_QR.Left = Panel1.Width - lbl_UserId.Width - 20"[Layout/General script]
views.get("lbl_qr").vw.setLeft((int)((views.get("panel1").vw.getWidth())-(views.get("lbl_userid").vw.getWidth())-20d));

}
}