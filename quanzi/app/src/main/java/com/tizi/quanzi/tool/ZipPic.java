package com.tizi.quanzi.tool;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.log.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by qixingchen on 15/8/19.
 * 图片压缩工具类
 */
public class ZipPic {

    public static ZipPic getNewInstance() {
        return new ZipPic();
    }

    /**
     * 获取图片的宽高
     *
     * @param filePath 文件地址
     *
     * @return ans[0] : 宽 ans[1] :高
     */
    public static int[] getImageSize(String filePath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        BitmapFactory.decodeFile(filePath, opts);
        int[] ans = new int[2];
        ans[0] = opts.outWidth;
        ans[1] = opts.outHeight;
        return ans;
    }

    /**
     * 通过降低图片的质量来压缩图片
     *
     * @param bitmap  要压缩的图片位图对象
     * @param maxSize 压缩后图片大小的最大值,单位KB
     *
     * @return 压缩后的图片位图对象
     */
    public Bitmap compressByQuality(Bitmap bitmap, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(CompressFormat.JPEG, quality, baos);
        System.out.println("图片压缩前大小：" + baos.toByteArray().length + "byte");
        boolean isCompressed = false;
        while (baos.toByteArray().length / 1024 > maxSize) {
            quality -= 10;
            baos.reset();
            bitmap.compress(CompressFormat.JPEG, quality, baos);
            System.out.println("质量压缩到原来的" + quality + "%时大小为："
                    + baos.toByteArray().length + "byte");
            isCompressed = true;
        }
        System.out.println("图片压缩后大小：" + baos.toByteArray().length + "byte");
        if (isCompressed) {
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(
                    baos.toByteArray(), 0, baos.toByteArray().length);
            recycleBitmap(bitmap);
            return compressedBitmap;
        } else {
            return bitmap;
        }
    }

    public long getSize(String pathName) {
        File file = new File(pathName);
        if (file.exists()) {
            return file.length();
        }
        return 0;
    }

    /**
     * 通过压缩图片的尺寸来压缩图片大小，仅仅做了缩小，如果图片本身小于目标大小，不做放大操作
     *
     * @param pathName    图片的完整路径
     * @param targetWidth 缩放的目标宽度
     *
     * @return 缩放后的图片
     */
    private Bitmap compressByWidth(String pathName, int targetWidth) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
        // 得到图片的宽度、高度；
        int imgWidth = opts.outWidth;
        int imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        if (widthRatio > 1) {
            opts.inSampleSize = widthRatio;
        }
        if (imgWidth > targetWidth) {
            imgHeight = (int) (imgHeight * (1.0 * (targetWidth) / imgWidth));
            imgWidth = targetWidth;
        }
        // 设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeFile(pathName, opts);
        if (widthRatio > 1) {
            bitmap = Bitmap.createScaledBitmap(bitmap, imgWidth, imgHeight, true);
        }
        return bitmap;
    }

    public String compressByWidth(String filepath, int targetWidth, int quality) {
        Bitmap bitmap = compressByWidth(filepath, targetWidth);
        return saveMyBitmap(bitmap, quality, Tool.getFileName(filepath));
    }

    /**
     * 通过压缩图片的尺寸来压缩图片大小
     *
     * @param bitmap       要压缩图片
     * @param targetWidth  缩放的目标宽度
     * @param targetHeight 缩放的目标高度
     * @return 缩放后的图片
     */
    //    public static Bitmap compressBySize(Bitmap bitmap, int targetWidth,
    //                                        int targetHeight) {
    //        ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //        bitmap.compress(CompressFormat.JPEG, 100, baos);
    //        BitmapFactory.Options opts = new BitmapFactory.Options();
    //        opts.inJustDecodeBounds = true;
    //        bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0,
    //                baos.toByteArray().length, opts);
    //        // 得到图片的宽度、高度；
    //        int imgWidth = opts.outWidth;
    //        int imgHeight = opts.outHeight;
    //        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于该比例的最小整数；
    //        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
    //        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
    //        if (widthRatio > 1 || heightRatio > 1) {
    //            if (widthRatio > heightRatio) {
    //                opts.inSampleSize = widthRatio;
    //            } else {
    //                opts.inSampleSize = heightRatio;
    //            }
    //        }
    //        // 设置好缩放比例后，加载图片进内存；
    //        opts.inJustDecodeBounds = false;
    //        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(
    //                baos.toByteArray(), 0, baos.toByteArray().length, opts);
    //        recycleBitmap(bitmap);
    //        return compressedBitmap;
    //    }

    /**
     * 通过压缩图片的尺寸来压缩图片大小，通过读入流的方式，可以有效防止网络图片数据流形成位图对象时内存过大的问题；
     *
     * @param is           要压缩图片，以流的形式传入
     * @param targetWidth  缩放的目标宽度
     * @param targetHeight 缩放的目标高度
     * @return 缩放后的图片
     * @throws IOException 读输入流的时候发生异常
     */
    //    public static Bitmap compressBySize(InputStream is, int targetWidth,
    //                                        int targetHeight) throws IOException {
    //        ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //        byte[] buff = new byte[1024];
    //        int len = 0;
    //        while ((len = is.read(buff)) != -1) {
    //            baos.write(buff, 0, len);
    //        }
    //
    //        byte[] data = baos.toByteArray();
    //        BitmapFactory.Options opts = new BitmapFactory.Options();
    //        opts.inJustDecodeBounds = true;
    //        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
    //                opts);
    //        // 得到图片的宽度、高度；
    //        int imgWidth = opts.outWidth;
    //        int imgHeight = opts.outHeight;
    //        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于该比例的最小整数；
    //        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
    //        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
    //        if (widthRatio > 1 || heightRatio > 1) {
    //            if (widthRatio > heightRatio) {
    //                opts.inSampleSize = widthRatio;
    //            } else {
    //                opts.inSampleSize = heightRatio;
    //            }
    //        }
    //        // 设置好缩放比例后，加载图片进内存；
    //        opts.inJustDecodeBounds = false;
    //        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
    //        return bitmap;
    //    }

    /**
     * 旋转图片摆正显示
     *
     * @param srcPath
     * @param bitmap
     * @return
     */
    //    public static Bitmap rotateBitmapByExif(String srcPath, Bitmap bitmap) {
    //        ExifInterface exif;
    //        Bitmap newBitmap = null;
    //        try {
    //            exif = new ExifInterface(srcPath);
    //            if (exif != null) { // 读取图片中相机方向信息
    //                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
    //                        ExifInterface.ORIENTATION_NORMAL);
    //                int digree = 0;
    //                switch (ori) {
    //                    case ExifInterface.ORIENTATION_ROTATE_90:
    //                        digree = 90;
    //                        break;
    //                    case ExifInterface.ORIENTATION_ROTATE_180:
    //                        digree = 180;
    //                        break;
    //                    case ExifInterface.ORIENTATION_ROTATE_270:
    //                        digree = 270;
    //                        break;
    //                }
    //                if (digree != 0) {
    //                    Matrix m = new Matrix();
    //                    m.postRotate(digree);
    //                    newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    //                    recycleBitmap(bitmap);
    //                    return newBitmap;
    //                }
    //            }
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //        return bitmap;
    //    }

    /**
     * 回收位图对象
     *
     * @param bitmap
     */
    private void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            System.gc();
            bitmap = null;
        }
    }


    /**
     * 储存图像
     *
     * @param mBitmap 要储存的图像
     * @param quality 图像质量
     *
     * @return 储存路径
     */

    public String saveMyBitmap(Bitmap mBitmap, int quality) {
        String imageFileName = UUID.randomUUID().toString();
        return saveMyBitmap(mBitmap, quality, imageFileName);

    }

    public String saveMyBitmap(Bitmap mBitmap, int quality, String fileName) {
        fileName = fileName.substring(0, fileName.indexOf("."));
        File file = new File(Tool.getCacheCacheDir().getAbsolutePath()
                + "/image/" + AppStaticValue.getUserID(), fileName + ".jpg");

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        String FilePath = file.getAbsolutePath();
        saveMyBitmap(FilePath, mBitmap, quality);
        return FilePath;

    }

    public void saveMyBitmap(String FilePath, Bitmap mBitmap, int quality) {
        File f = new File(FilePath);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            Log.e("在保存图片时出错：", e.toString());
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(CompressFormat.JPEG, quality, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
