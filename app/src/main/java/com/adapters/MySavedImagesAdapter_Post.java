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
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.fury.instafull.R;
import com.bumptech.glide.Glide;
import com.instagram.data.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MySavedImagesAdapter_Post extends Adapter<MySavedImagesAdapter_Post.MyViewHolder> {
    Context f13c;
    int f14h;
    boolean isAllSelected;
    private List<DownloadModel_Post> listItems;
    onItemClickListener mClickListener;
    onItemLongClickListener mLongClickListener;
    LayoutParams params;
    int selectedCount;
    int totalItemsCount;
    int visibility;
    int f15w;

    /* renamed from: com.adapters.MySavedImagesAdapter.1 */
    class C03151 implements OnClickListener {
        private final /* synthetic */ int val$position;

        C03151(int i) {
            this.val$position = i;
        }

        public void onClick(View v) {
           mClickListener.onItemClick(val$position);
        }
    }

    /* renamed from: com.adapters.MySavedImagesAdapter.2 */
    class C03162 implements OnLongClickListener {
        private final /* synthetic */ int val$position;

        C03162(int i) {
            val$position = i;
        }

        public boolean onLongClick(View v) {
            mLongClickListener.onItemLongClick(val$position);
            return true;
        }
    }

    public class MyViewHolder extends ViewHolder {
        public ImageView imageView;

        /* renamed from: com.adapters.MySavedImagesAdapter.MyViewHolder.1 */
        class C03171 implements OnClickListener {
            C03171() {
            }

            public void onClick(View arg0) {
                boolean isChecked;
                MySavedImagesAdapter_Post access$0;
                boolean z = true;
                ((FrameLayout) arg0.getParent()).startAnimation(AnimationUtils.loadAnimation(f13c, R.anim.scale_btn));
                int pos = ((Integer) arg0.getTag()).intValue();
                if (((DownloadModel_Post) listItems.get(pos)).isChecked) {
                    isChecked = false;
                } else {
                    isChecked = true;
                }
                ((DownloadModel_Post) listItems.get(pos)).isChecked = isChecked;
                if (isChecked) {
                    access$0 = MySavedImagesAdapter_Post.this;
                    access$0.selectedCount++;
                } else {
                    access$0 = MySavedImagesAdapter_Post.this;
                    access$0.selectedCount--;
                }
                access$0 = MySavedImagesAdapter_Post.this;
                if (selectedCount != totalItemsCount) {
                    z = false;
                }
                access$0.isAllSelected = z;
            }
        }

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.ivAlbumThumb);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageView.setLayoutParams(params);
            view.setLayoutParams(params);
        }
    }

    public interface onItemClickListener {
        void onItemClick(int i);
    }

    public interface onItemLongClickListener {
        void onItemLongClick(int i);
    }

    public MySavedImagesAdapter_Post(Context c, List<DownloadModel_Post> listItems) {
        this.f15w = Utils.f100w;
        this.f14h = Utils.f99h;
        this.visibility = View.GONE;
        this.listItems = listItems;
        this.f13c = c;
        this.f15w = f15w < 1 ? 720 : f15w;
        this.f14h = f14h < 1 ? 1280 : f14h;
        this.params = new LayoutParams((f15w / 3) - 4, (f15w / 3) - 4);
        this.params.gravity = Gravity.CENTER;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_item_images, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        DownloadModel_Post model = (DownloadModel_Post) listItems.get(position);
        Glide.with(f13c).load(new File(model.filePath)).override(params.width, params.width).error((int) R.drawable.error_image).into(holder.imageView);
        holder.imageView.setOnClickListener(new C03151(position));
        holder.imageView.setOnLongClickListener(new C03162(position));
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

    public void removeItems(ArrayList<DownloadModel_Post> listDeleted) {
        this.listItems.removeAll(listDeleted);
        notifyDataSetChanged();
    }

    public void setVisible(int selectedPos, int visiblility) {
        this.visibility = visiblility;
        if (selectedPos != -1) {
            ((DownloadModel_Post) this.listItems.get(selectedPos)).isChecked = true;
        }
        notifyDataSetChanged();
    }

    public ArrayList<DownloadModel_Post> getSelectedModel() {
        ArrayList<DownloadModel_Post> model = new ArrayList();
        for (DownloadModel_Post im : this.listItems) {
            if (im.isChecked) {
                model.add(im);
            }
        }
        return model;
    }

    public void resetAdapter() {
        for (DownloadModel_Post model : this.listItems) {
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
        for (DownloadModel_Post model : this.listItems) {
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
        for (DownloadModel_Post model : this.listItems) {
            if (model.isChecked) {
                list.add(model.filePath);
            }
        }
        return list;
    }
}
