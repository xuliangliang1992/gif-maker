//package com.xll.gif.view;
//
//import android.content.DialogInterface;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cretin.www.relativelayoutdemo.utils.ImageUtils;
//import com.cretin.www.relativelayoutdemo.view.MyRelativeLayout;
//import com.xll.gif.R;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class MainActivity extends AppCompatActivity implements MyRelativeLayout.MyRelativeTouchCallBack {
//    private MyRelativeLayout rela;
//    public static final String TAG = "MainActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();
//        initUI();
//    }
//
//    public void initUI() {
//        rela = (MyRelativeLayout) findViewById(R.id.id_rela);
//        rela.setMyRelativeTouchCallBack(this);
//    }
//
//    /**
//     * 当时重写这个方法是因为项目中有左右滑动切换不同滤镜的效果
//     *
//     * @param direction
//     */
//    @Override
//    public void touchMoveCallBack(int direction) {
////        if (direction == AppConstants.MOVE_LEFT) {
////            Toast.makeText(MainActivity.this, "你在向左滑动！", Toast.LENGTH_SHORT).show();
////        } else {
////            Toast.makeText(MainActivity.this, "你在向右滑动！", Toast.LENGTH_SHORT).show();
////        }
//    }
//
//    /**
//     * 这个方法可以用来实现滑到某一个地方删除该TextView的实现
//     *
//     * @param textView
//     */
//    @Override
//    public void onTextViewMoving(TextView textView) {
//        Log.d(TAG, "TextView正在滑动");
//    }
//
//    @Override
//    public void onTextViewMovingDone() {
//        Toast.makeText(MainActivity.this, "标签TextView滑动完毕！", Toast.LENGTH_SHORT).show();
//    }
//
//
//
//    public void btnClickLoad(View view) {
//        Bitmap bitmap = ImageUtils.createViewBitmap(rela, rela.getWidth(), rela.getHeight());
//        String fileName = "CRETIN_" + new SimpleDateFormat("yyyyMMdd_HHmmss")
//                .format(new Date()) + ".png";
//        String result = ImageUtils.saveBitmapToFile(bitmap, fileName);
//        Toast.makeText(MainActivity.this, "保存位置:" + result, Toast.LENGTH_SHORT).show();
//    }
//}
