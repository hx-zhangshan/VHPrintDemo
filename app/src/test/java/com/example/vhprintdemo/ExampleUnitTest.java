package com.example.vhprintdemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import com.example.vhprintdemo.utils.QRCodeGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
//    @Test
//    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
//    }

    private Bitmap createText(){
        String txt="a  b c d e f";
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.BLUE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(50);
        Bitmap txtBitmap = Bitmap.createBitmap((int)textPaint.measureText(txt),200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(txtBitmap);
        canvas.drawBitmap(txtBitmap, 0, 0, null);
        StaticLayout sl= new StaticLayout(txt, textPaint, txtBitmap.getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        sl.draw(canvas);
        return txtBitmap;
    }
    @Test
    public void qrCode(){
//        try {
//            generateQRCodeImage("ZCKP20200909100079", 350, 350, QR_CODE_IMAGE_PATH);
//        } catch (WriterException e) {
//            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
//        } catch (IOException e) {
//            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
//        }
//        Bitmap bmpee= QRCodeGenerator.createQRCodeBitmap("wwwww", 200, 200,
//                "UTF-8", "H", "1",
//                Color.BLACK, Color.WHITE);
//        Bitmap bmp=QRCodeGenerator.toConformBitmap(createText(),bmpee);
//        Bitmap text = createText();
//        QRCodeGenerator.saveBitmap(text);
    }
    private static final String QR_CODE_IMAGE_PATH = "C:/Users/HP/Desktop/MyQRCode.png";


    private  void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);

        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

    }

}