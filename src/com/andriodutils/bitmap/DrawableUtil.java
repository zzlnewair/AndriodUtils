package com.andriodutils.bitmap;

import android.graphics.drawable.GradientDrawable;

/**
 
 * 类描述：Drawable相关的工具类，提供静态方法调用，禁止实例化该类
 */
public class DrawableUtil {
    
    /**
     * 禁止实例化该类
     */
    private DrawableUtil() {
        throw new UnsupportedOperationException(
                "The class " + getClass().getSimpleName() + " can not be instantiated!");
    }

  
    /**
     * 使用代码创建指定圆角大小的矩形shape形状
     *
     * @param rgb               形状颜色
     * @param topLeftRadius     左上角的圆角尺寸
     * @param topRightRadius    右上角的圆角尺寸
     * @param bottomLeftRadius  左下角的圆角尺寸
     * @param bottomRightRadius 右下角的圆角尺寸
     * @return 指定形状的Drawable对象
     */
    public static GradientDrawable createRectangleDrawable(int rgb, float topLeftRadius, float topRightRadius,
                                                           float bottomLeftRadius, float bottomRightRadius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);//设置矩形
        //创建圆角
        float[] radius = {
                topLeftRadius, topLeftRadius,
                topRightRadius, topRightRadius,
                bottomRightRadius, bottomRightRadius,
                bottomLeftRadius, bottomLeftRadius};
        drawable.setCornerRadii(radius);
        drawable.setColor(rgb);
        return drawable;
    }

    /**
     * 创建一个没有圆角的指定颜色的矩形Drawble
     *
     * @param rgb 矩形形状的颜色
     * @return 指定颜色的矩形的Drawanle对象
     */
    public static GradientDrawable createRectangleDrawable(int rgb) {
        return createRectangleDrawable(rgb, 0, 0, 0, 0);
    }

//    //===Desc:代码创建椭圆形==========================================================================================
////    GradientDrawable.OVAL
//
//    public static GradientDrawable createOvalDrawable(int rgb) {
//        GradientDrawable drawable = new GradientDrawable();
//        drawable.setShape(GradientDrawable.OVAL);//设置椭圆形
//        drawable.set
//        drawable.setColor(rgb);
//        return drawable;
//    }

}
