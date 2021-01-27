package com.xll.gif.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.highlands.common.dialog.DialogManager;
import com.xll.gif.MainApplication;
import com.xll.gif.activity.EditGif2Activity;
import com.xll.gif.activity.MainActivity;
import com.xll.gif.util.GifMaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class GifMakeService extends IntentService {
    private String TAG = "GifMakeService";
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_MAKE_GIF_FROM_VIDEO = "com.xll.gif.action.MAKE_GIF_VIDEO";
    public static final String ACTION_MAKE_GIF_FROM_PHOTO = "com.xll.gif.action.MAKE_GIF_PHOTO";
    public static final String ACTION_MAKE_GIF_FROM_BITMAP = "com.xll.gif.action.MAKE_GIF_BITMAP";

    private static final String EXTRA_FROM_FILE = "com.xll.gif.extra.FROM_FILE";
    private static final String EXTRA_TO_FILE = "com.xll.gif.extra.TO_FILE";
    private static final String EXTRA_SOURCE = "com.xll.gif.extra.SOURCE";
    private static final String EXTRA_FROM_POSITION = "com.xll.gif.extra.FROM_POSITION";
    private static final String EXTRA_TO_POSITION = "com.xll.gif.extra.TO_POSITION";
    private static final String EXTRA_PERIOD = "com.xll.gif.extra.PERIOD";

    public static final String EXTRA_FILE = "file", EXTRA_SUCCESS = "success";

    public GifMakeService() {
        super("GifMakeService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startMaking(Context context, String fromFile, String toFile, int fromPosition, int toPosition, int period) {
        Intent intent = new Intent(context, GifMakeService.class);
        intent.setAction(ACTION_MAKE_GIF_FROM_VIDEO);
        intent.putExtra(EXTRA_FROM_FILE, fromFile);
        intent.putExtra(EXTRA_TO_FILE, toFile);
        intent.putExtra(EXTRA_FROM_POSITION, fromPosition);
        intent.putExtra(EXTRA_TO_POSITION, toPosition);
        intent.putExtra(EXTRA_PERIOD, period);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startMaking(Context context, ArrayList<String> sourcePathList, String toFile) {
        Intent intent = new Intent(context, GifMakeService.class);
        intent.setAction(ACTION_MAKE_GIF_FROM_PHOTO);
        intent.putStringArrayListExtra(EXTRA_SOURCE, sourcePathList);
        intent.putExtra(EXTRA_TO_FILE, toFile);
        context.startService(intent);
    }

    public static void startMaking2(Context context, ArrayList<Bitmap> bitmapArrayList, String toFile) {
        Intent intent = new Intent(context, GifMakeService.class);
        intent.setAction(ACTION_MAKE_GIF_FROM_PHOTO);
        intent.putParcelableArrayListExtra(EXTRA_SOURCE, bitmapArrayList);
        intent.putExtra(EXTRA_TO_FILE, toFile);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startMaking(Context context, float delay, String toFile) {
        Intent intent = new Intent(context, GifMakeService.class);
        intent.setAction(ACTION_MAKE_GIF_FROM_BITMAP);
        intent.putExtra(EXTRA_TO_FILE, toFile);
        intent.putExtra(EXTRA_PERIOD, delay);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_MAKE_GIF_FROM_VIDEO.equals(action)) {
                final String fromFile = intent.getStringExtra(EXTRA_FROM_FILE);
                final String toFile = intent.getStringExtra(EXTRA_TO_FILE);
                final int fromPosition = intent.getIntExtra(EXTRA_FROM_POSITION, 0);
                final int toPosition = intent.getIntExtra(EXTRA_TO_POSITION, 0);
                final int period = intent.getIntExtra(EXTRA_PERIOD, 200);
                handleTask(fromFile, toFile, fromPosition, toPosition, period);
            } else if (ACTION_MAKE_GIF_FROM_PHOTO.equals(action)) {
                final ArrayList<String> sourcePathList = intent.getStringArrayListExtra(EXTRA_SOURCE);
                final String toFile = intent.getStringExtra(EXTRA_TO_FILE);
                handlePhotoTask(sourcePathList, toFile);
            } else if (ACTION_MAKE_GIF_FROM_BITMAP.equals(action)) {
                final String toFile = intent.getStringExtra(EXTRA_TO_FILE);
                final float delay = intent.getFloatExtra(EXTRA_PERIOD, 100f);
                handlePhotoTask2(toFile, delay);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleTask(String fromFile, String toFile, int fromPosition, int toPosition, int period) {
        GifMaker maker = new GifMaker(2);
        maker.setOnGifListener(new GifMaker.OnGifListener() {
            @Override
            public void onMake(int current, int total) {
                Timber.tag(TAG).i("current = " + current + "   total = " + total);
            }
        });
        long startAt = System.currentTimeMillis();
        final boolean success = maker.makeGifFromVideo(fromFile, fromPosition, toPosition, period, toFile);
        long now = System.currentTimeMillis();
        DialogManager.getInstance().dismissProgressDialog();
        Timber.tag(TAG).i("Done! " + (success ? " success " : " failed ") + " cost time=" + ((now - startAt) / 1000) + " seconds " + " \nsave at=" + toFile);
        Intent intent = new Intent(this, EditGif2Activity.class);
        intent.putExtra("filePath", toFile);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void handlePhotoTask(List<String> sourcePathList, String toFile) {
        GifMaker maker = new GifMaker(2);
        maker.setOnGifListener(new GifMaker.OnGifListener() {
            @Override
            public void onMake(int current, int total) {
                Timber.tag(TAG).i("current = " + current + "   total = " + total);
            }
        });
        long startAt = System.currentTimeMillis();
        boolean success = false;
        try {
            success = maker.makeGifFromPath(sourcePathList, toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long now = System.currentTimeMillis();
        DialogManager.getInstance().dismissProgressDialog();
        Timber.tag(TAG).i("Done! " + (success ? " success " : " failed ") + " cost time=" + ((now - startAt) / 1000) + " seconds " + " \nsave at=" + toFile);
        Intent intent = new Intent(this, EditGif2Activity.class);
        intent.putExtra("filePath", toFile);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void handlePhotoTask2(String toFile, float fps) {
        GifMaker maker = new GifMaker(2);
        maker.setFrameRate(fps);
        maker.setOnGifListener(new GifMaker.OnGifListener() {
            @Override
            public void onMake(int current, int total) {
                Timber.tag(TAG).i("current = " + current + "   total = " + total);
            }
        });
        long startAt = System.currentTimeMillis();
        boolean success = false;
        try {
            success = maker.makeGif(MainApplication.getBitmaps(), toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long now = System.currentTimeMillis();
        DialogManager.getInstance().dismissProgressDialog();
        Timber.tag(TAG).i("Done! " + (success ? " success " : " failed ") + " cost time=" + ((now - startAt) / 1000) + " seconds " + " \nsave at=" + toFile);
        DialogManager.getInstance().dismissProgressDialog();
        if (success) {
            //通知系统相册刷新
            try {
                MediaStore.Images.Media.insertImage(getContentResolver(), toFile, "", "");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Uri uri = Uri.fromFile(new File(toFile));
            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            sendBroadcast(localIntent);
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
