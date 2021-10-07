package com.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.fury.instafull.R;
import com.bumptech.glide.Glide;
import com.instagram.data.Utils;

import java.util.ArrayList;
import java.util.List;

public class MySavedVideosAdapter extends Adapter<MySavedVideosAdapter.MyViewHolder> {
    Context f16c;
    int f17h;
    boolean isAllSelected;
    private List<DownloadModel> listItems;
    onItemClickListener mClickListener;
    onItemLongClickListener mLongClickListener;
    LayoutParams params;
    int selectedCount;
    int totalItemsCount;
    int visibility;
    int f18w;

    /* renamed from: com.adapters.MySavedVideosAdapter.1 */
    class C03181 implements OnClickListener {
        private final /* synthetic */ int val$position;

        C03181(int i) {
            this.val$position = i;
        }

        public void onClick(View v) {
            MySavedVideosAdapter.this.mClickListener.onItemClick(this.val$position);
        }
    }

    /* renamed from: com.adapters.MySavedVideosAdapter.2 */
    class C03192 implements OnLongClickListener {
        private final /* synthetic */ int val$position;

        C03192(int i) {
            this.val$position = i;
        }

        public boolean onLongClick(View v) {
            MySavedVideosAdapter.this.mLongClickListener.onItemLongClick(this.val$position);
            return true;
        }
    }

    public class MyViewHolder extends ViewHolder {
        ImageView ivImage;

        public MyViewHolder(View view) {
            super(view);
            this.ivImage = (ImageView) view.findViewById(R.id.ivImage);
            ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            this.ivImage.setLayoutParams(MySavedVideosAdapter.this.params);
            view.setLayoutParams(MySavedVideosAdapter.this.params);
        }
    }

    public interface onItemClickListener {
        void onItemClick(int i);
    }

    public interface onItemLongClickListener {
        void onItemLongClick(int i);
    }

    public MySavedVideosAdapter(Context c, List<DownloadModel> listItems) {
        this.f18w = Utils.f100w;
        this.f17h = Utils.f99h;
        this.visibility = View.GONE;
        this.listItems = listItems;
        this.f16c = c;
        this.f18w = this.f18w < 1 ? 720 : this.f18w;
        this.f17h = this.f17h < 1 ? 1280 : this.f17h;
        this.params = new LayoutParams((this.f18w / 2) - 4, (this.f18w / 2) - 4);
        this.params.setMargins(1, 1, 1, 1);
        this.params.gravity = Gravity.CENTER;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_video_item_main, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        DownloadModel model = (DownloadModel) this.listItems.get(position);
        Glide.with(this.f16c).load(model.filePath).override(this.params.width, this.params.height).error((int) R.drawable.error_video).into(holder.ivImage);
        holder.ivImage.setOnClickListener(new C03181(position));
        holder.ivImage.setOnLongClickListener(new C03192(position));
    }

    public int getItemCount() {
        this.totalItemsCount = this.listItems.size();
        return this.totalItemsCount;
    }

    public void setOnItemClickListener(onItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public void setOnItemLongClickListener(onItemLongClickListener mListener) {
        this.mLongClickListener = mListener;
    }

    public void removeItem(int pos) {
        this.listItems.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, this.listItems.size());
    }

    public void removeItems(ArrayList<DownloadModel> listDeleted) {
        this.listItems.removeAll(listDeleted);
        notifyDataSetChanged();
    }

    public void setVisible(int selectedPos, int visiblility) {
        this.visibility = visiblility;
        if (selectedPos != -1) {
            ((DownloadModel) this.listItems.get(selectedPos)).isChecked = true;
        }
        notifyDataSetChanged();
    }

    public ArrayList<DownloadModel> getSelectedModel() {
        ArrayList<DownloadModel> model = new ArrayList();
        for (DownloadModel im : this.listItems) {
            if (im.isChecked) {
                model.add(im);
            }
        }
        return model;
    }

    public void resetAdapter() {
        for (DownloadModel model : this.listItems) {
            model.isChecked = false;
        }
        this.isAllSelected = false;
        this.visibility = View.GONE;
        this.selectedCount = 0;
        notifyDataSetChanged();
    }

    public void setAllSelected() {
        int i = 0;
        boolean doCheck = !this.isAllSelected;
        this.isAllSelected = doCheck;
        for (DownloadModel model : this.listItems) {
            model.isChecked = doCheck;
        }
        if (doCheck) {
            i = this.totalItemsCount;
        }
        this.selectedCount = i;
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectedItemsPathOnly() {
        ArrayList<String> list = new ArrayList();
        for (DownloadModel model : this.listItems) {
            if (model.isChecked) {
                list.add(model.filePath);
            }
        }
        return list;
    }
}
