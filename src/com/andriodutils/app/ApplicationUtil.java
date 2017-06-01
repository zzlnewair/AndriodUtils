
package com.andriodutils.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * 
 * 类描述：于应用程序相关的工具类，提供静态方法调用，禁止实例化该类
 */
public class ApplicationUtil {
    
    private static final String TAG = "ApplicationUtil";

    
    /**
     * 禁止实例化该类
     */
    private ApplicationUtil() {
        throw new UnsupportedOperationException(
                "The class " + getClass().getSimpleName() + " can not be instantiated!");
    }

   
    /**
     * 获取应用程序的版本名称
     *
     * @param context 上下文对象
     * @return 程序对应的本本名称，用于显示当前版本
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();// 获取包的管理者
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;// 获取应用程序的版本名称
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.toString());
        }
        return "";
    }

    /**
     * 获取应用程序的内部版本号
     *
     * @param context 上下文对象
     * @return 程序的内部版本号，用于比对升级用
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();// 获取包的管理者
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;// 获取应用程序的版本名称
        } catch (PackageManager.NameNotFoundException e) {
        	   Log.e(TAG, e.toString());
        }
        return 0;
    }

}
