package com.andriodutils.file;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 类描述：sd卡相关的工具类，提供静态方法调用，禁止实例化该类
 */
public class SdCardUtil {
    //===Desc:成员变量======================================================================================

    //===Desc:构造函数======================================================================================

    /**
     * 禁止实例化该类
     */
    private SdCardUtil() {
        throw new UnsupportedOperationException(
                "The class " + getClass().getSimpleName() + " can not be instantiated!");
    }

    //===Desc:提供给外界使用的静态方法==========================================================================================

    /**
     * 判断sd卡是否可用
     *
     * @return true：sd卡可用。false：sd卡不可用
     */
    public static boolean sdCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取sd卡的根路径
     *
     * @return sd卡不可用就返回“” 否则返回sd卡的根路径
     */
    public static String getSDCardPath() {
        //先判断SD卡是否可用
        if (sdCardEnable()) {
            String separator = File.separator;
            //可用
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            //不可用
            return "";
        }
    }

    /**
     * 获取sdcard的总共大小
     *
     * @return 获取sdcard的总共大小
     */
    public static long getSDCardAllSize() {
        if (sdCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = stat.getAvailableBlocks();
            // 获取单个数据块的大小（byte）
            long blockSize = stat.getBlockSize();
            return blockSize * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath 需要获取剩余空间大小的路径
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return 系统存储路径
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }


    /** 读文件buffer.*/
    private static final int FILE_BUFFER = 1024;
    private static String TAG="DbUtils";

    /**
     * 数据库导出到sdcard.
     * @param context
     * @param dbName 数据库名字 例如 xx.db
     */
    public static void exportDb2Sdcard(Context context,String dbName) {
        String filePath = context.getDatabasePath(dbName).getAbsolutePath();
        byte[] buffer = new byte[FILE_BUFFER];
        int length;
        OutputStream output;
        InputStream input;
        try {
            input = new FileInputStream(new File((filePath)));
            output = new FileOutputStream(context.getExternalCacheDir() + File.separator + dbName);
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            input.close();
            Log.i(TAG, "mv success!");
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }
    
    
}
