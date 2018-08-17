package com.example.taron.imagedownloader.dialogFragment;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.taron.imagedownloader.provider.ImgProvider;
import com.example.taron.imagedownloader.R;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

public class ImageDialogFragment extends DialogFragment {
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_dialog_fragment, container, false);
        imageView = view.findViewById(R.id.img_fr_id);
        httpLoader();
        return view;
    }

    private void httpLoader() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String root = Environment.getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                    File file = new File(root + "/" +
                            ImgProvider.getPosition().getImgNum() + ".jpg");
                    FileInputStream fileInputStream = new FileInputStream(file);
                    final Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                    fileInputStream.close();
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
