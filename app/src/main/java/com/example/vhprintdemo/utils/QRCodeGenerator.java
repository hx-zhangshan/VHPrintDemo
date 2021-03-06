package com.example.vhprintdemo.utils;


import android.graphics.*;
import android.text.TextUtils;
import androidx.annotation.ColorInt;
import com.example.vhprintdemo.bean.EquiCheckPdDetail;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;

public class QRCodeGenerator {

    private static final String QR_CODE_IMAGE_PATH = "C:/Users/HP/Desktop/MyQRCode.png";

    public static Bitmap addTextWatermark(final Bitmap src,
                                          final EquiCheckPdDetail checkPdDetail, final float textSize,
                                          @ColorInt final int color, final float x, final float y, final boolean recycle) {
        if ( checkPdDetail == null) {
            return null;
        }
        Bitmap ret = src.copy(src.getConfig(), true);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Canvas canvas = new Canvas(ret);
        paint.setColor(color);
        paint.setTextSize(textSize);
        Rect bounds = new Rect();
        paint.getTextBounds(checkPdDetail.toString(), 0, checkPdDetail.toString().length(), bounds);
        canvas.drawText("资产编号:"+checkPdDetail.getEquiArchCode(), x, y , paint);
        canvas.drawText("资产名称:"+checkPdDetail.getEquiName(), x, y +30, paint);
        canvas.drawText("规    格:"+checkPdDetail.getEquiModel(), x, y +60, paint);
        canvas.drawText("使用日期:"+"2020-11-26", x, y +90, paint);
        canvas.drawText("存放地点:"+checkPdDetail.getEquiDictCode(), x, y +120, paint);
        if (recycle && !src.isRecycled()) {
            src.recycle();
        }
        return ret;
    }

    /**
     * 旋转bitmap
     *
     * @param b
     * @param degrees
     * @return
     */
    public static Bitmap rotateBmp(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) b.getWidth() / 2,
                    (float) b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                        b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {


            }
        }
        return b;
    }


    /**
     * 生成简单二维码
     *
     * @param content                字符串内容
     * @param width                  二维码宽度
     * @param height                 二维码高度
     * @param character_set          编码方式（一般使用UTF-8）
     * @param error_correction_level 容错率 L：7% M：15% Q：25% H：35%
     * @param margin                 空白边距（二维码与边框的空白区域）
     * @param color_black            黑色色块
     * @param color_white            白色色块
     * @return BitMap
     */
    public static Bitmap createQRCodeBitmap(String content, int width,int height,
                                            String character_set,String error_correction_level,
                                            String margin,int color_black, int color_white) {
        // 字符串内容判空
        if (TextUtils.isEmpty(content)) {

            return null;
        }
        // 宽和高>=0
        if (width < 0 || height < 0) {
            return null;
        }
        try {
            /** 1.设置二维码相关配置 */
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            // 字符转码格式设置
            if (!TextUtils.isEmpty(character_set)) {
                hints.put(EncodeHintType.CHARACTER_SET, character_set);
            }
            // 容错率设置
            if (!TextUtils.isEmpty(error_correction_level)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, error_correction_level);
            }
            // 空白边距设置
            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin);
            }
            /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象 */
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = color_black;//黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white;// 白色色块像素设置
                    }
                }
            }
            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象 */
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String saveBitmap(Bitmap bitmap) {
        String  imagePath = QR_CODE_IMAGE_PATH;
        File file = new File(imagePath);
        if(file.exists()) {
            file.delete();
        }
        try{
            FileOutputStream out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)){
                out.flush();
                out.close();
            }
        } catch (Exception e) {
                    e.printStackTrace();
        }
        return imagePath;
    }
    public static Bitmap toConformBitmap(Bitmap background, Bitmap foreground,Bitmap logo,
                                         EquiCheckPdDetail checkPdDetail) {

        if( background == null ) {
            return null;
        }
        Bitmap bk=addTextWatermark(background,checkPdDetail,18,0xFF000000,160,90,true);
        int bgWidth = 500;
        int bgHeight = 230;
        //int fgWidth = foreground.getWidth();
        //int fgHeight = foreground.getHeight();
        //create the new blank bitmap 创建一个新的和SRC长度宽度一样的位图
        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);

        Canvas cv = new Canvas(newbmp);
        //draw bg into
        cv.drawBitmap(bk, 0, 0, null);//在 0，0坐标开始画入bg
        cv.drawBitmap(logo, 70, -10, null);//在 0，0坐标开始画入bg
        //draw fg into
        cv.drawBitmap(foreground, 0, 55, null);//在 0，0坐标开始画入fg ，可以从任意位置画入
        //save all clip
        cv.save();//保存
        //store
        cv.restore();//存储


        return rotateBmp(newbmp,90);
    }

}