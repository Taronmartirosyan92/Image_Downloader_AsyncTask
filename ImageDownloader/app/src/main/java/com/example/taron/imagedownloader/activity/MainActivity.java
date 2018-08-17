package com.example.taron.imagedownloader.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taron.imagedownloader.R;
import com.example.taron.imagedownloader.adapter.ImgAdapter;
import com.example.taron.imagedownloader.provider.ImgProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 1001;
    private Button button;
    private TextView editText;
    private ProgressBar bar;
    private String root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.edit_text);
        button = findViewById(R.id.download_id);
        bar = findViewById(R.id.proBar);
        requestPermissions();
        initialisationRecycler(editText);
        root = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }

    private void initialisationRecycler(TextView editText) {
        final LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ImgAdapter imgAdapter = new ImgAdapter(this, ImgProvider.getList(this), editText);
        RecyclerView recyclerView = findViewById(R.id.rec_id);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(imgAdapter);
    }

    private void myImageLoader() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LoaderAsyncTask loaderAsyncTask = new LoaderAsyncTask(MainActivity.this);
                loaderAsyncTask.execute();
            }
        });
    }

    private static class LoaderAsyncTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<MainActivity> activityReference;

        private LoaderAsyncTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        private MainActivity getReference() {
            return activityReference.get();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getReference().bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                httpLoader();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getReference().bar.setVisibility(View.GONE);
            Toast.makeText(getReference(), R.string.upload,
                    Toast.LENGTH_SHORT).show();
        }

        private void httpLoader() throws IOException {
            final String url = getReference().editText.getText().toString();
            URL ul = new URL(url);
            HttpURLConnection ur = (HttpURLConnection) ul.openConnection();
            ur.connect();
            final InputStream in = ur.getInputStream();
            final Bitmap bitmap = BitmapFactory.decodeStream(in);
            final String path = String.format(getReference().root, getReference().getString(R.string.fraction),
                    ImgProvider.getPosition().getImgNum(), getReference().getString(R.string.format_jpg));
            File file = new File(path);
            ImgProvider.getPosition().setDownload(true);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = in.read(buf.clone()))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myImageLoader();
                } else {
                    requestPermissions();
                }
        }
    }
}
