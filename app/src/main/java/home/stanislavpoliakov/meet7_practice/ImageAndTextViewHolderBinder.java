package home.stanislavpoliakov.meet7_practice;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class ImageAndTextViewHolderBinder extends ViewHolderBinder {
    private final DataItem item;
    private static final String TAG = "meet7_logs";

    public ImageAndTextViewHolderBinder(DataItem item, int viewType) {
        super(viewType);
        this.item = item;
        Log.d(TAG, "ImageAndTextViewHolderBiner: Constructor passed, item.text = " + item.getText() + "; item.imageid = " + item.getImageId());
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder viewHolder) {
        Log.d(TAG, "bindViewHolder: " + viewHolder.getClass().getSimpleName());
        MyAdapter.ImageAndTextViewHolder imageAndTextViewHolder = (MyAdapter.ImageAndTextViewHolder) viewHolder;
        Log.d(TAG, "bindViewHolder: passed");
        imageAndTextViewHolder.comboTextView.setText(item.getText());
        imageAndTextViewHolder.comboImageView.setImageResource(item.getImageId());
    }
}
