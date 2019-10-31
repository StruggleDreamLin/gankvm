package com.dreamlin.gankvm.ui.girls;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ComponentActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dreamlin.gankvm.App;
import com.dreamlin.gankvm.R;
import com.dreamlin.gankvm.base.ui.NoVMActivity;
import com.dreamlin.gankvm.entity.ResultsEntity;
import com.dreamlin.gankvm.utils.FileUtils;
import com.dreamlin.gankvm.utils.TaskUtils;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;

public class FaceActivity extends NoVMActivity {
    @BindView(R.id.iv_face)
    PhotoView ivFace;
    ResultsEntity resultsEntity;
    public static final String FACE_TAG = "face_results";
    public static final String GANK_PATH = "Gank";
    private static final int PERMISSION_CODE = 0x55;
    private boolean isShare = false;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE
            };
            if (checkSelfPermission(permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                saveOrShare(isShare);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //写权限成功
                saveOrShare(isShare);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        resultsEntity = getIntent().getParcelableExtra(FACE_TAG);
        if (resultsEntity != null && !TextUtils.isEmpty(resultsEntity.getUrl())) {
            Glide.with(this)
                    .load(resultsEntity.getUrl())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .centerCrop()
                    .into(ivFace);
        }
        getSupportActionBar().setTitle(TextUtils.isEmpty(resultsEntity.getDesc())
                ? getText(R.string.app_name) : resultsEntity.getDesc());
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_face;
    }

    @Override
    protected int getMenu() {
        return R.menu.face_setting_menu;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                isShare = false;
                checkPermissions();
                break;
            case R.id.action_share:
                isShare = true;
                checkPermissions();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveOrShare(final boolean share) {
        TaskUtils.executeAsyncTask(new TaskUtils.WeakAsyncTask<FaceActivity, Object, Object, Boolean>(new WeakReference<>(this)) {
            @Override
            protected Boolean doInBackground(Object... objects) {
                Bitmap cache = null;
                try {
                    cache = Glide.with(tWeak.get())
                            .asBitmap()
                            .load(tWeak.get().resultsEntity.getUrl())
                            .submit()
                            .get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String filePath = FileUtils.getExternalPath().concat(File.separator).concat(GANK_PATH);
                File parent = new File(filePath);
                if (!parent.exists()) {
                    boolean mkdir = parent.mkdir();
                    Log.e(FaceActivity.class.getName(), "create Dir parent:" + mkdir);
                }
                String fileName = resultsEntity.getUrl()
                        .substring(resultsEntity.getUrl().lastIndexOf('/') + 1);
                File file = new File(parent, fileName);
                if (!file.exists()) {
                    try {
                        boolean newFile = file.createNewFile();
                        if (!newFile)
                            Log.e(FaceActivity.class.getName(), "create file fail!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    return share;
                }
                Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
                if (fileName.toUpperCase().endsWith("PNG")) {
                    compressFormat = Bitmap.CompressFormat.PNG;
                } else if (fileName.toUpperCase().endsWith("GIF")) {
                    compressFormat = Bitmap.CompressFormat.WEBP;
                }

                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(file);
                    cache.compress(compressFormat, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    MediaStore.Images.Media.insertImage(App.getInstance().getContentResolver(),
                            file.getPath(), fileName, "");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    //通知图库刷新
                    Uri uri = Uri.parse("file://".concat(file.getAbsolutePath()));
                    Intent scanner = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                    App.getInstance().sendBroadcast(scanner);
                }
                return share;
            }

            @Override
            protected void onPostExecute(Boolean share) {
                super.onPostExecute(share);
                String filePath = FileUtils.getExternalPath().concat(File.separator).concat(GANK_PATH);
                File parent = new File(filePath);
                String fileName = resultsEntity.getUrl()
                        .substring(resultsEntity.getUrl().lastIndexOf('/') + 1);
                File file = new File(parent, fileName);
                if (file.exists()) {
                    toastMessageShort(String.format("图片已保存至%s文件夹", file.getPath()));
                } else {
                    toastMessageShort(R.string.save_fail);
                }
                if (share) {
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(FaceActivity.this, "com.dreamlin.gankvm.fileprovider", file);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                    if (uri == null) {
                        toastMessageShort(R.string.share_fail);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        intent.setType("image/*");
                        startActivity(intent);
                    }
                }
            }
        });
    }

}
