package home.stanislavpoliakov.meet7_practice;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class SimpleImageViewHolderBinder extends ViewHolderBinder {
    private final DataItem item;
    private static final String TAG = "meet7_logs";

    public SimpleImageViewHolderBinder(DataItem item, int viewType) {
        super(viewType);
        this.item = item;
        Log.d(TAG, "SimpleImageViewHolderBinder: Constructor passed, item.imageid = " + item.getImageId());
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder viewHolder) {
        Log.d(TAG, "bindViewHolder: " + viewHolder.getClass().getSimpleName());
        MyAdapter.SimpleImageViewHolder simpleImageViewHolder = (MyAdapter.SimpleImageViewHolder) viewHolder;
        Log.d(TAG, "bindViewHolder: passed");
        simpleImageViewHolder.simpleImageView.setImageResource(item.getImageId());
    }
}
