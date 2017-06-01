
package com.andriodutils.app;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 *
 * 类描述：Activity相关的工具,使用单例模式，一个应用程序只允许有一个Activity堆栈的管理工具
 */
public class ActivityManager {
   /**
     * 保存Activity的栈
     */
    private static Stack<Activity> activitys;

    private static ActivityManager am;//当前类的实例，使用的单例模式


   
    /**
     * 私有化构造函数
     */
    private ActivityManager() {
        if (null == activitys)
            activitys = new Stack<Activity>();
    }

    /**
     * 使用单例模式获取当前类的单一实例
     *
     * @return 当前类的单一实例
     */
    public static synchronized ActivityManager getInstance() {
        if (null == am)
            am = new ActivityManager();
        return am;
    }

    /**
     * 添加一个Activity到堆栈
     *
     * @param activity 需要添加的Activity对象
     */
    public synchronized void addActivity(Activity activity) {
    	if (null == activitys){
            activitys = new Stack<Activity>();
    	}
        activitys.add(activity);
    }

    /**
     * 获取栈顶的Activity
     *
     * @return 如果堆栈中存放有activity，则返回栈顶的Activity对象，如果堆栈中没有存放有activity，则返回null
     */
    public Activity getTopActivity() {
        if (null == activitys || activitys.size() == 0)
            return null;
        return activitys.lastElement();//返回栈里面最后一个元素出去
    }

    /**
     * 关闭存放在堆栈中的Activity
     *
     * @param activity 需要关闭的activity
     */
    public void killActivity(Activity activity) {
        if (null == activity)
            return;
        activitys.remove(activity);
        activity.finish();
    }

    /**
     * 关闭指定名字的activity
     *
     * @param cls Activity的class对象
     */
    public void killActivity(Class<?> cls) {
        for (Activity activity : activitys) {
            if (activity.getClass().equals(cls))
                killActivity(activity);
        }
    }
    
    /**
     * 关闭栈顶的Activity
     */
    public void killTopActivity() {
        Activity topActivity = getTopActivity();
        killActivity(topActivity);
    }

   

    /**
     * 关闭存放在堆栈中所有的activity
     */
    public void killAllActivity() {
        for (Activity a : activitys) {
            if (null != a) {
                a.finish();
            }
        }
        activitys.clear();//清空堆栈
    }
    

    public void AppExit(Context context) {
        try {
        	killAllActivity();
         
            android.os.Process.killProcess(android.os.Process.myPid()); 
            //System.exit(0);
        } catch (Exception e) {

        }
    }

    



}
