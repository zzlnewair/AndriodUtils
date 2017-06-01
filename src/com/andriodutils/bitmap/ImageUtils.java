package com.andriodutils.bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.andriodutils.bitmap.ImageSizeUtil.ImageSize;
import com.andriodutils.file.IOUtils;
import com.andriodutils.network.HttpUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * ImageUtils
 * <ul>
 * convert between Bitmap, byte array, Drawable
 * <li>{@link #bitmapToByte(Bitmap)}</li>
 * <li>{@link #bitmapToDrawable(Bitmap)}</li>
 * <li>{@link #byteToBitmap(byte[])}</li>
 * <li>{@link #byteToDrawable(byte[])}</li>
 * <li>{@link #drawableToBitmap(Drawable)}</li>
 * <li>{@link #drawableToByte(Drawable)}</li>
 * </ul>
 * <ul>
 * get image
 * <li>{@link #getInputStreamFromUrl(String, int)}</li>
 * <li>{@link #getBitmapFromUrl(String, int)}</li>
 * <li>{@link #getDrawableFromUrl(String, int)}</li>
 * </ul>
 * <ul>
 * scale image
 * <li>{@link #scaleImageTo(Bitmap, int, int)}</li>
 * <li>{@link #scaleImage(Bitmap, float, float)}</li>
 * </ul>
 * 
 * 
 */
public class ImageUtils {

    private ImageUtils() {
        throw new AssertionError();
    }

    /**
     * convert Bitmap to byte array
     * 
     * @param b
     * @return
     */
    public static byte[] bitmapToByte(Bitmap b) {
        if (b == null) {
            return null;
        }

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, o);
        return o.toByteArray();
    }

    /**
     * convert byte array to Bitmap
     * 
     * @param b
     * @return
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * convert Drawable to Bitmap
     * 
     * @param d
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable d) {
        return d == null ? null : ((BitmapDrawable)d).getBitmap();
    }

    /**
     * convert Bitmap to Drawable
     * 
     * @param b
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap b) {
        return b == null ? null : new BitmapDrawable(b);
    }

    /**
     * convert Drawable to byte array
     * 
     * @param d
     * @return
     */
    public static byte[] drawableToByte(Drawable d) {
        return bitmapToByte(drawableToBitmap(d));
    }

    /**
     * convert byte array to Drawable
     * 
     * @param b
     * @return
     */
    public static Drawable byteToDrawable(byte[] b) {
        return bitmapToDrawable(byteToBitmap(b));
    }

    /**
     * get input stream from network by imageurl, you need to close inputStream yourself
     * 
     * @param imageUrl
     * @param readTimeOutMillis
     * @return
     * @see ImageUtils#getInputStreamFromUrl(String, int, boolean)
     */
    public static InputStream getInputStreamFromUrl(String imageUrl, int readTimeOutMillis) {
        return getInputStreamFromUrl(imageUrl, readTimeOutMillis, null);
    }

    /**
     * get input stream from network by imageurl, you need to close inputStream yourself
     * 
     * @param imageUrl
     * @param readTimeOutMillis read time out, if less than 0, not set, in mills
     * @param requestProperties http request properties
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static InputStream getInputStreamFromUrl(String imageUrl, int readTimeOutMillis,
            Map<String, String> requestProperties) {
        InputStream stream = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            HttpUtils.setURLConnection(requestProperties, con);
            if (readTimeOutMillis > 0) {
                con.setReadTimeout(readTimeOutMillis);
            }
            stream = con.getInputStream();
        } catch (MalformedURLException e) {
            IOUtils.close(stream);
            throw new RuntimeException("MalformedURLException occurred. ", e);
        } catch (IOException e) {
            IOUtils.close(stream);
            throw new RuntimeException("IOException occurred. ", e);
        }
        return stream;
    }

    /**
     * get drawable by imageUrl
     * 
     * @param imageUrl
     * @param readTimeOutMillis
     * @return
     * @see ImageUtils#getDrawableFromUrl(String, int, boolean)
     */
    public static Drawable getDrawableFromUrl(String imageUrl, int readTimeOutMillis) {
        return getDrawableFromUrl(imageUrl, readTimeOutMillis, null);
    }

    /**
     * get drawable by imageUrl
     * 
     * @param imageUrl
     * @param readTimeOutMillis read time out, if less than 0, not set, in mills
     * @param requestProperties http request properties
     * @return
     */
    public static Drawable getDrawableFromUrl(String imageUrl, int readTimeOutMillis,
            Map<String, String> requestProperties) {
        InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOutMillis, requestProperties);
        Drawable d = Drawable.createFromStream(stream, "src");
        IOUtils.close(stream);
        return d;
    }

    /**
     * get Bitmap by imageUrl
     * 
     * @param imageUrl
     * @param readTimeOut
     * @return
     * @see ImageUtils#getBitmapFromUrl(String, int, boolean)
     */
    public static Bitmap getBitmapFromUrl(String imageUrl, int readTimeOut) {
        return getBitmapFromUrl(imageUrl, readTimeOut, null);
    }

    /**
     * get Bitmap by imageUrl
     * 
     * @param imageUrl
     * @param requestProperties http request properties
     * @return
     */
    public static Bitmap getBitmapFromUrl(String imageUrl, int readTimeOut, Map<String, String> requestProperties) {
        InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOut, requestProperties);
        Bitmap b = BitmapFactory.decodeStream(stream);
        IOUtils.close(stream);
        return b;
    }
    
    
	
	public static Bitmap getSampledBitmapFromLocal(final String path,
			final ImageView imageView)
	{
		Bitmap bm;
		// 加载图片
		// 图片的压缩
		// 1、获得图片需要显示的大小
		ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
		// 2、压缩图片
		bm = getSampledBitmapFromPath(path, imageSize.width,
				imageSize.height);
		return bm;
	}
	
	
    /**
	 * 
	 * 
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	protected static Bitmap getBitmapFromPath(String path)
	{
		
	
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		return bitmap;
	}
	
    
	
	protected static Bitmap getSampledBitmapFromPath(String path, int width,
			int height)
	{
		// 获得图片的宽和高，并不把图片加载到内存中
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options,
				width, height);

		// 使用获得到的InSampleSize再次解析图片
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}
    

    /**
     * scale image 根据比例缩放图片
     * 
     * @param org
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
        return scaleImage(org, (float) newWidth / org.getWidth(), (float) newHeight / org.getHeight());
    }

    /**
     * scale image 缩放图片
     * 
     * @param org
     * @param scaleWidth sacle of width
     * @param scaleHeight scale of height
     * @return
     */
    public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
        if (org == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
    }
    
    
   
    /**
     * 图片圆角处理
     *
     * @param bitmap
     * @param roundPX
     * @return
     */

    public static Bitmap getRCB(Bitmap bitmap, float roundPX) {
        Bitmap dstbmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dstbmp);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectf = new RectF(rect);//矩形
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectf, roundPX, roundPX, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return dstbmp;

    }

}
