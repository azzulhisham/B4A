package b4a.MyFirstHelloAndroid.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_layoutmain{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 2;BA.debugLine="AutoScaleAll"[LayoutMain/General script]
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
//BA.debugLineNum = 3;BA.debugLine="Label2.HorizontalCenter = 50%x"[LayoutMain/General script]
views.get("label2").vw.setLeft((int)((50d / 100 * width) - (views.get("label2").vw.getWidth() / 2)));
//BA.debugLineNum = 4;BA.debugLine="lblNumber1.Right = Label2.Left"[LayoutMain/General script]
views.get("lblnumber1").vw.setLeft((int)((views.get("label2").vw.getLeft()) - (views.get("lblnumber1").vw.getWidth())));
//BA.debugLineNum = 5;BA.debugLine="lblNumber2.Left = Label2.Right"[LayoutMain/General script]
views.get("lblnumber2").vw.setLeft((int)((views.get("label2").vw.getLeft() + views.get("label2").vw.getWidth())));
//BA.debugLineNum = 6;BA.debugLine="Panel1.HorizontalCenter = 50%x"[LayoutMain/General script]
views.get("panel1").vw.setLeft((int)((50d / 100 * width) - (views.get("panel1").vw.getWidth() / 2)));

}
}