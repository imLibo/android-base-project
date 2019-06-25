package com.cnksi.android.crash;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ScrollView;

import com.cnksi.android.R;
import com.cnksi.android.activity.BaseCoreActivity;
import com.cnksi.android.databinding.CoreActivityCrashBinding;
import com.cnksi.android.executor.ActivityManager;
import com.cnksi.android.log.KLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import es.dmoral.toasty.Toasty;

/**
 * 崩溃日志界面
 *
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/9/8
 * @since 1.0
 */
public class CoreCrashActivity extends BaseCoreActivity {

    public static final String CRASH_MODEL = "crash_model";
    private static final int REQUEST_CODE = 110;
    private CrashModel model;

    @Override
    protected String[] getPermission() {
        return new String[0];
    }

    @Override
    protected void initContentView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoreActivityCrashBinding mBinding = DataBindingUtil.setContentView(this, R.layout.core_activity_crash);
        model = getIntent().getParcelableExtra(CRASH_MODEL);
        if (model == null) {
            return;
        }
        mBinding.coreTvMessage.setText(model.getExceptionMsg());
        mBinding.coreTvClassName.setText(model.getFileName());
        mBinding.coreTvMethodName.setText(model.getMethodName());
        mBinding.coreTvLineNumber.setText(String.valueOf(model.getLineNumber()));
        mBinding.coreTvExceptionType.setText(model.getExceptionType());
        mBinding.coreTvFullException.setText(model.getFullException());
        mBinding.coreTvTime.setText(SpiderMan.df.format(model.getTime()));

        mBinding.coreTvModel.setText(model.getDevice().getModel());
        mBinding.coreTvBrand.setText(model.getDevice().getBrand());
        mBinding.coreTvVersion.setText(model.getDevice().getVersion());

        mBinding.coreIvMore.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(CoreCrashActivity.this, v);
            menu.inflate(R.menu.menu_more);
            menu.show();
            menu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_copy_text) {
                    String crashText = SpiderMan.getShareText(model);
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    if (cm != null) {
                        ClipData mClipData = ClipData.newPlainText("crash", crashText);
                        cm.setPrimaryClip(mClipData);
                        showToast("拷贝成功");
                    }
                } else if (id == R.id.menu_share_text) {
                    String crashText = SpiderMan.getShareText(model);
                    shareText(crashText);
                } else if (id == R.id.menu_share_image) {
                    if (ContextCompat.checkSelfPermission(CoreCrashActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                            ContextCompat.checkSelfPermission(CoreCrashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    } else {
                        shareImage();
                    }
                }
                return true;
            });
        });

        mBinding.coreTvCrashTips.setOnClickListener(v -> {
            mBinding.coreScrollView.setVisibility(View.VISIBLE);
            mBinding.coreIvMore.setVisibility(View.VISIBLE);
            mBinding.coreLlCrashContainer.setVisibility(View.GONE);
        });

        mBinding.coreBtnRestart.setOnClickListener(v -> {
            Intent intent = getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                ActivityManager.instance().popAllActivityExcept(null);
                mActivity.startActivity(intent);
            }
            killCurrentProcess();
        });
    }

    private static void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    private void shareText(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "崩溃信息：");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "分享到"));
    }

    private void requestPermission(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //判断请求码，确定当前申请的权限
        if (requestCode == REQUEST_CODE) {
            //判断权限是否申请通过
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //授权成功
                shareImage();
            } else {
                //授权失败
                showToast("请授予SD卡权限才能分享图片");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public Bitmap getBitmapByView(ScrollView view) {
        if (view == null) {
            return null;
        }
        int height = 0;
        for (int i = 0; i < view.getChildCount(); i++) {
            height += view.getChildAt(i).getHeight();
        }
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRGB(255, 255, 255);
        view.draw(canvas);
        return bitmap;
    }

    private File bitmapToFile(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File imageFile = new File(path, "crash-" + SpiderMan.df.format(model.getTime()) + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            bitmap.recycle();
        } catch (IOException e) {
            KLog.e(e);
        }
        return imageFile;
    }

    private void shareImage() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            showToast("未插入sd卡");
            return;
        }
        File file = bitmapToFile(getBitmapByView(findViewById(R.id.core_scrollView)));
        if (file == null || !file.exists()) {
            showToast("图片文件不存在");
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".corefileprovider", file);
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        } else {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "分享图片"));
    }

    private void showToast(String text) {
        Toasty.info(getApplicationContext(), text).show();
    }

    @Override
    public void onBackPressed() {
        killCurrentProcess();
    }
}
