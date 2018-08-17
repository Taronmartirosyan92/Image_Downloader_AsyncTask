package com.example.taron.imagedownloader.provider;

import android.content.Context;

import com.example.taron.imagedownloader.R;
import com.example.taron.imagedownloader.model.ImgDataModel;

import java.util.ArrayList;
import java.util.List;

public class ImgProvider {
    private static List<ImgDataModel> list = new ArrayList<>();
    public static int position;

    public static List<ImgDataModel> getList(Context context) {
        if (!list.isEmpty()) {
            list.clear();
        }
        for (int i = 0; i <context.getResources().getStringArray(R.array.imageUrls).length; i++) {
            list.add(new ImgDataModel (context.getResources().getStringArray(R.array.nameImg)[i],
                    context.getResources().getStringArray(R.array.imageUrls)[i], false));
        }
        return list;
    }
    public static ImgDataModel getPosition() {
        return list.get(position);
    }
}
