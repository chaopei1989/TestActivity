package com.zero;

import java.lang.reflect.Method;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

public class SystemUtil {

	public static final int MIN_BRIGHTNESS = 30;

	public static final int MAX_BRIGHTNESS = 255;

	private final static String[] hms = new String[]{
	    "hm",
        "2012022","2012023",
        "2013022","2013023","2013028",
        "2014011","2014017"
	};
	
	static public boolean isHongMi(){
		boolean value = false;
		for (String str : hms) {
			if (Build.MODEL.toLowerCase().contains(str)) {
				value = true;
				break;
			}
		}
		return value;
	}
	
	static public int getBrightnessMode(Context context) {
		int brightnessMode = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
		try {
			brightnessMode = Settings.System.getInt(
					context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return brightnessMode;
	}

	static public void setBrightnessMode(Context context, int brightnessMode) {
		try {
			Settings.System.putInt(context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE, brightnessMode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public int getSysScreenBrightness(Context context) {
		int screenBrightness = MAX_BRIGHTNESS;
		try {
			screenBrightness = Settings.System.getInt(
					context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return screenBrightness;
	}

	static public void setSysScreenBrightness(Context context, int brightness) {
		try {
			ContentResolver resolver = context.getContentResolver();
			Uri uri = Settings.System
					.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
			Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS,
					brightness);
			resolver.notifyChange(uri, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public boolean isMIUIV5() {
		boolean isV5 = false;

		try {
			Class<?> SystemProperties = Class
					.forName("android.os.SystemProperties");
			Method getProperty = SystemProperties.getDeclaredMethod("get",
					String.class, String.class);

			// Maybe can use ro.miui.ui.version.code ???
			String verName = (String) getProperty.invoke(SystemProperties,
					"ro.miui.ui.version.name", "default");
			String verCode = (String) getProperty.invoke(SystemProperties,
					"ro.miui.ui.version.code", "default");
			String verIMEI = (String) getProperty.invoke(SystemProperties,
					"ro.ril.miui.imei", "default");
			if (verName != null && !verName.equals("default")) {// 优先判定versionName，如果没有versionName，有以下两个属性默认为miuiV5+
				int verNum = Integer.parseInt(verName.toLowerCase()
						.substring(1));
				if (verNum >= 5) {
					return true;
				}
			} else if (verCode != null && !verCode.equals("default")) {
				return true;
			} else if (verIMEI != null && !verIMEI.equals("default")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isV5;
	}

	private static boolean isMiuiFloatWindowEnablePreV6(Context context) {
		PackageManager pm = context.getPackageManager();
		boolean isEnable = true;

		try {
			PackageInfo packageInfo = pm.getPackageInfo(
					context.getPackageName(), 0);
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;

			int floatWindowAllowFlag = 0;
			if (Build.VERSION.SDK_INT < 19) {
				floatWindowAllowFlag = 0x8000000;
			} else { // android 4.4后的MIUI系统，判断允许应用开启悬浮窗的标志位变化了
				floatWindowAllowFlag = 0x2000000;
			}
			isEnable = (applicationInfo.flags & floatWindowAllowFlag) != 0;
		} catch (Exception e) {
		}
		return isEnable;
	}

	/**
     * 小米悬浮窗是否可用
     * @return
     */
    private static boolean isMiuiFloatWindowEnable(Context context) {
        if (isMiuiV6Rom()) {
            return isMiuiFloatWindowEnable4V6(context);
        } else {
            return isMiuiFloatWindowEnablePreV6(context);
        }
    }
    
    /**
     * 判断MIUI6的悬浮窗设置是否开启
     * 
     * @param context
     * @return
     */
    private static boolean isMiuiFloatWindowEnable4V6(Context context) {
        try {
            int userAccept = getMiuiV6UserAcceptValue(context);
            return (userAccept & 0x2000000) != 0;
        } catch (Exception e) {
        }
        return true;
    }
    
    private static int getMiuiV6UserAcceptValue(Context context) throws IllegalStateException {
        Cursor c = null;
        String myPkg = context.getPackageName();
        try {
            c = context.getContentResolver().query(Uri.parse("content://com.lbe.security.miui.permmgr/active"), null,
                    "pkgName=?", new String[] { myPkg }, null);
            if (c.moveToFirst()) {
                int userAccept = c.getInt(c.getColumnIndex("userAccept"));
                return userAccept;
            }
        } catch (Exception e) {
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e2) {
                }
            }
        }
        throw new IllegalStateException();
    }
    
    /**
     * 判断是否是MIUI6的rom
     * 
     * @return
     */
    public static boolean isMiuiV6Rom() {
    	boolean isV6 = "V6".equalsIgnoreCase(getVersionName());
        return isV6;
    }
    
    /**
     * 返回ro.miui.ui.version.name
     * @return
     */
    public static String getVersionName() {
    	String miuiVersionName = "";
    	try {
    		Class<?> SystemProperties = Class
    				.forName("android.os.SystemProperties");
    		Method getProperty = SystemProperties.getDeclaredMethod("get",
    				String.class, String.class);

    		// Maybe can use ro.miui.ui.version.code ???
    		miuiVersionName = (String) getProperty.invoke(SystemProperties,
    				"ro.miui.ui.version.name", "default");
		} catch (Exception e) {
			e.printStackTrace();
		}
        return miuiVersionName;
    }
    
    /**
     * 判断是否应该显示引导页面
     * 
     * @param context
     * @return
     */
    public static boolean shouldShow(Context context) {
		if (!isMIUIV5() && !isMiuiV6Rom()) {//既不是MIUI5也不是MIUI6
			return false;
		}
		return !isMiuiFloatWindowEnable(context);
	}
}
