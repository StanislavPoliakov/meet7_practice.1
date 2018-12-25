package home.stanislavpoliakov.meet7_practice;

import android.support.v7.widget.RecyclerView;

public class SimpleImageViewHolderBinder extends ViewHolderBinder {
    private final DataItem item;

    public SimpleImageViewHolderBinder(DataItem item, int viewType) {
        super(viewType);
        this.item = item;
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder viewHolder) {
        MyAdapter.SimpleImageViewHolder simpleImageViewHolder = (MyAdapter.SimpleImageViewHolder) viewHolder;
        simpleImageViewHolder.simpleImageView.setImageResource(item.getImageId());
    }
}
