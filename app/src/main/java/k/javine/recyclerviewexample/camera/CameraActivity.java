package k.javine.recyclerviewexample.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import k.javine.recyclerviewexample.R;

/**
 * Created by Administrator on 2016/3/24.
 * 还需完善
 * 1.surfaceView的尺寸调整
 * 2.ImageView尺寸调整和个数
 * 3.异步处理文件保存与读取操作
 *
 */
public class CameraActivity extends Activity implements SurfaceHolder.Callback{

    private Camera mCamera;

    @Bind(R.id.preview_iv)
    ImageView preview_iv;
    @Bind(R.id.camera_surface_view)
    SurfaceView surfaceView;
    private SurfaceHolder mHolder;
    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File tempFile = new File("/sdcard/temp.jpg");
            try {
                FileOutputStream fo = new FileOutputStream(tempFile);
                fo.write(data);
                fo.close();
                Message msg = Message.obtain();
                msg.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("path",tempFile.getAbsolutePath());
                msg.setData(bundle);
                handler.sendMessage(msg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                //显示照片到ImageView
                String path = msg.getData().getString("path");
                try {
                    ExifInterface exifInterface = new ExifInterface(path);
                    int result = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
                    int rotate = 0;
                    switch (result){
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                    }
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inSampleSize = 4;
                    Bitmap bitmap =  BitmapFactory.decodeFile(path,options);
                    if (rotate > 0){
                        Matrix matrix = new Matrix();
                        matrix.setRotate(rotate);
                        Bitmap rotateBitmap = Bitmap.createBitmap(bitmap,0,0
                                ,bitmap.getWidth(), bitmap.getHeight(),matrix,true);
                        if (rotateBitmap != null){
                            bitmap.recycle();
                            bitmap = rotateBitmap;
                        }
                    }
                    preview_iv.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
    }

    /**
     * onclick
     * @param view
     */
    public void capture(View view){
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setPreviewSize(800, 400);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success){
                    mCamera.takePicture(null,null,mPictureCallback);
                }
            }
        });
    }

    private Camera getCamera(){
        Camera camera;
        try{
            camera = Camera.open();
        }catch (Exception e){
            e.printStackTrace();
            camera = null;
        }
        return camera;
    }

    /**
     * 预览相机的功能
     */
    private void setStartPreview(Camera camera,SurfaceHolder holder){
        try {
            camera.setPreviewDisplay(holder);
            //将Camera相机角度进行调整
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera(){
        if (mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null){
            mCamera = getCamera();
            if (mHolder != null){
                setStartPreview(mCamera,mHolder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera,mHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        setStartPreview(mCamera,mHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }
}
