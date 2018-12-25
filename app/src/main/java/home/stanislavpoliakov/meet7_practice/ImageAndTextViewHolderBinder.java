package home.stanislavpoliakov.meet7_practice;

import android.support.v7.widget.RecyclerView;

public class ImageAndTextViewHolderBinder extends ViewHolderBinder {
    private final DataItem item;

    public ImageAndTextViewHolderBinder(DataItem item, int viewType) {
        super(viewType);
        this.item = item;
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder viewHolder) {
        MyAdapter.ImageAndTextViewHolder imageAndTextViewHolder = (MyAdapter.ImageAndTextViewHolder) viewHolder;
        imageAndTextViewHolder.comboTextView.setText(item.getText());
        imageAndTextViewHolder.comboImageView.setImageResource(item.getImageId());
    }
}
