package com.xll.gif.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.xll.gif.MainApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okio.BufferedSource;
import okio.Okio;
import okio.Sink;

/**
 * @author xuliangliang
 * @date 2021/1/21
 * copyright(c) 浩鲸云计算科技股份有限公司
 */
public class Android10SaveFile {
    /**
     * 需要依赖Okio
     */
    private static final String VIDEO_BASE_URI = "content://media/external/video/media";

    /***
     *
     * @param videoPath
     * @param context
     */
    private void insertVideo(String videoPath, Context context) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        int nVideoWidth = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        int nVideoHeight = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        int duration = Integer
                .parseInt(retriever
                        .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        long dateTaken = System.currentTimeMillis();
        File file = new File(videoPath);
        String title = file.getName();
        String filename = file.getName();
        String mime = "video/mp4";
        ContentValues mCurrentVideoValues = new ContentValues(9);
        mCurrentVideoValues.put(MediaStore.Video.Media.TITLE, title);
        mCurrentVideoValues.put(MediaStore.Video.Media.DISPLAY_NAME, filename);
        mCurrentVideoValues.put(MediaStore.Video.Media.DATE_TAKEN, dateTaken);
        mCurrentVideoValues.put(MediaStore.MediaColumns.DATE_MODIFIED, dateTaken / 1000);
        mCurrentVideoValues.put(MediaStore.Video.Media.MIME_TYPE, mime);
        mCurrentVideoValues.put(MediaStore.Video.Media.DATA, videoPath);
        mCurrentVideoValues.put(MediaStore.Video.Media.WIDTH, nVideoWidth);
        mCurrentVideoValues.put(MediaStore.Video.Media.HEIGHT, nVideoHeight);
        mCurrentVideoValues.put(MediaStore.Video.Media.RESOLUTION, Integer.toString(nVideoWidth) + "x" + Integer.toString(nVideoHeight));
        mCurrentVideoValues.put(MediaStore.Video.Media.SIZE, new File(videoPath).length());
        mCurrentVideoValues.put(MediaStore.Video.Media.DURATION, duration);
        ContentResolver contentResolver = context.getContentResolver();
        Uri videoTable = Uri.parse(VIDEO_BASE_URI);
        Uri uri = contentResolver.insert(videoTable, mCurrentVideoValues);
        writeFile(videoPath, mCurrentVideoValues, contentResolver, uri);
    }

    public static void writeFile(String imagePath, ContentValues values, ContentResolver contentResolver, Uri item) {
        try (OutputStream rw = contentResolver.openOutputStream(item, "rw")) {
            // Write data into the pending image.
            Sink sink = Okio.sink(rw);
            BufferedSource buffer = Okio.buffer(Okio.source(new File(imagePath)));
            buffer.readAll(sink);
            values.put(MediaStore.Video.Media.IS_PENDING, 0);
            contentResolver.update(item, values, null, null);
            new File(imagePath).delete();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Cursor query = MainApplication.getInstance().getContentResolver().query(item, null, null, null);
                if (query != null) {
                    int count = query.getCount();
                    Log.e("writeFile", "writeFile result :" + count);
                    query.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**需要依赖Okio*/
    /***
     *
     * @param imagePath
     * @param context
     */
    public static void insertImage(String imagePath, Context context) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "IMG1024.JPG");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/gif");
        ContentResolver contentResolver = context.getContentResolver();
        Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        Uri item = contentResolver.insert(collection, values);
        writeFile(imagePath, values, contentResolver, item);
        contentResolver.update(item, values, null, null);
    }

    static String  TAG = "Android10SaveFile";

    /**
     *
     * @param context
     * @param filePath relative path in Q, such as: "DCIM/" or "DCIM/dir_name/"
     *                 absolute path before Q
     * @return
     */
    public static Cursor searchImageFromPublic(Context context, String filePath, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            Log.e(TAG, "searchImageFromPublic: fileName is null");
            return null;
        }
        if (TextUtils.isEmpty(filePath)) {
            filePath = "DCIM/";
        } else {
            if (!filePath.endsWith("/")) {
                filePath = filePath + "/";
            }
        }

        //兼容androidQ和以下版本
        String queryPathKey = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q ? MediaStore.Images.Media.RELATIVE_PATH : MediaStore.Images.Media.DATA;
        String selection = queryPathKey + "=? and " + MediaStore.Images.Media.DISPLAY_NAME + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID, queryPathKey, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.DISPLAY_NAME},
                selection,
                new String[]{filePath, fileName},
                null);

        return cursor;
    }

    /** 读写普通文件，例如txt
     *
     * @param context
     * @param fileName just file name, not include path
     * @param image
     * @param subDir sub direction name, not absolute path
     */
    public static void saveTxt2Public(Context context, String fileName, String content, String subDir) {
        String subDirection;
        if (!TextUtils.isEmpty(subDir)) {
            if (subDir.endsWith("/")) {
                subDirection = subDir.substring(0, subDir.length() - 1);
            } else {
                subDirection = subDir;
            }
        } else {
            subDirection = "Documents";
        }

        Cursor cursor = searchTxtFromPublic(context, subDir, fileName);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                Uri uri = Uri.withAppendedPath(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), "" + id);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), id);
                if (uri != null) {
                    OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                    if (outputStream != null) {
                        outputStream.write(content.getBytes());
                        outputStream.flush();
                        outputStream.close();
                    }
                }
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.put(MediaStore.Files.FileColumns.RELATIVE_PATH, subDirection);
            } else {

            }
            //设置文件类型
            contentValues.put(MediaStore.Files.FileColumns.MEDIA_TYPE, MediaStore.Files.FileColumns.MEDIA_TYPE_NONE);
            Uri uri = context.getContentResolver().insert(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), contentValues);
            if (uri != null) {
                OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                if (outputStream != null) {
                    outputStream.write(content.getBytes());
                    outputStream.flush();
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param context
     * @param filePath relative path in Q, such as: "DCIM/" or "DCIM/dir_name/"
     *                 absolute path before Q
     * @return
     */
    private static Cursor searchTxtFromPublic(Context context, String filePath, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            Log.e(TAG, "searchTxtFromPublic: fileName is null");
            return null;
        }
        if (!filePath.endsWith("/")) {
            filePath = filePath + "/";
        }

        String queryPathKey = MediaStore.Files.FileColumns.RELATIVE_PATH;
        String selection = queryPathKey + "=? and " + MediaStore.Files.FileColumns.DISPLAY_NAME + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL),
                new String[]{MediaStore.Files.FileColumns._ID, queryPathKey, MediaStore.Files.FileColumns.DISPLAY_NAME},
                selection,
                new String[]{filePath, fileName},
                null);

        return cursor;
    }

    /** storage location: sdcard/Android/data/packagename
     *
     * @param context
     * @param fileName
     * @param image
     * @param environmentType
     * @param dirName
     */
    public static void saveImage2SandBox(Context context, String fileName, byte[] image, String environmentType, String dirName) {
        File standardDirectory;
        String dirPath;

        if (TextUtils.isEmpty(fileName) || 0 == image.length) {
            Log.e(TAG, "saveImage2SandBox: fileName is null or image is null!");
            return;
        }

        if (!TextUtils.isEmpty(environmentType)) {
            standardDirectory = context.getExternalFilesDir(environmentType);
        } else {
            standardDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }

        if (!TextUtils.isEmpty(dirName)) {
            dirPath = standardDirectory + "/" + dirName;
        } else {
            dirPath = String.valueOf(standardDirectory);
        }

        File imageFileDirctory = new File(dirPath);
        if (!imageFileDirctory.exists()) {
            if (!imageFileDirctory.mkdir()) {
                Log.e(TAG, "saveImage2SandBox: mkdir failed! Directory: " + dirPath);
                return;
            }
        }

        //        if (queryImageFromSandBox(context, fileName, environmentType, dirName)) {
        //            Log.e(TAG, "saveImage2SandBox: The file with the same name already exists！");
        //            return;
        //        }

        try {
            File imageFile = new File(dirPath + "/" + fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            fileOutputStream.write(image);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** storage location: /data/data/packagename
     *
     * @param filePath
     * @param image
     */
    public static void saveImage2SandBox2(String filePath, byte[] image) {
        try {
            File imageFile = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            fileOutputStream.write(image);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param context
     * @param fileName just file name, not include path
     * @param image
     * @param subDir sub direction name, not absolute path
     */
    public static void saveImage2Public(Context context, String fileName, byte[] image, String subDir) {
        String subDirection;
        if (!TextUtils.isEmpty(subDir)) {
            if (subDir.endsWith("/")) {
                subDirection = subDir.substring(0, subDir.length() - 1);
            } else {
                subDirection = subDir;
            }
        } else {
            subDirection = "DCIM";
        }

        Cursor cursor = searchImageFromPublic(context, subDir, fileName);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));                     // uri的id，用于获取图片
                Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                if (uri != null) {
                    OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                    if (outputStream != null) {
                        outputStream.write(image);
                        outputStream.flush();
                        outputStream.close();
                    }
                }
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            //设置保存参数到ContentValues中
            ContentValues contentValues = new ContentValues();
            //设置文件名
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            //兼容Android Q和以下版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
                //RELATIVE_PATH是相对路径不是绝对路径
                //关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
                contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, subDirection);
                //contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Music/sample");
            } else {
                contentValues.put(MediaStore.Images.Media.DATA, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
            }
            //设置文件类型
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/gif");
            //执行insert操作，向系统文件夹中添加文件
            //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (uri != null) {
                //若生成了uri，则表示该文件添加成功
                //使用流将内容写入该uri中即可
                OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                if (outputStream != null) {
                    outputStream.write(image);
                    outputStream.flush();
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
