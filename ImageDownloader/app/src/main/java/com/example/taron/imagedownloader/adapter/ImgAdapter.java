package com.example.taron.imagedownloader.adapter;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.taron.imagedownloader.model.ImgDataModel;
import com.example.taron.imagedownloader.dialogFragment.ImageDialogFragment;
import com.example.taron.imagedownloader.provider.ImgProvider;
import com.example.taron.imagedownloader.activity.MainActivity;
import com.example.taron.imagedownloader.R;

import java.util.List;

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ImgViewHolder> {
    private final String KEY_FRAG = "tag";
    private Context context;
    private List<ImgDataModel> imgUrlList;
    private TextView urlView;

    public ImgAdapter(Context context, List<ImgDataModel> imgUrlList, TextView urlView) {
        this.context = context;
        this.imgUrlList = imgUrlList;
        this.urlView = urlView;
    }

    @NonNull
    @Override
    public ImgAdapter.ImgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        final View view = inflater.inflate(R.layout.img_item, parent, false);
        return new ImgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImgAdapter.ImgViewHolder holder, final int position) {
        final ImgDataModel model = imgUrlList.get(position);
        holder.image.setText(model.getImgNum());
        final FragmentManager fragmentTransaction = ((MainActivity) context).getFragmentManager();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!model.isDownload()) {
                    urlView.setText(model.getImgUrl());
                    ImgProvider.position = holder.getAdapterPosition();

                } else {
                    urlView.setText(model.getImgNum());
                    DialogFragment dialogFragment = new ImageDialogFragment();
                    dialogFragment.show(fragmentTransaction, KEY_FRAG);
                    ImgProvider.position = holder.getAdapterPosition();
                    holder.relativeLayout.setBackgroundColor(R.drawable.card_gradient);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgUrlList.size();
    }

    class ImgViewHolder extends RecyclerView.ViewHolder {
        private TextView image;
        private RelativeLayout relativeLayout;

        ImgViewHolder(final View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_num_id);
            relativeLayout = itemView.findViewById(R.id.for_card_back);
        }
    }
}
