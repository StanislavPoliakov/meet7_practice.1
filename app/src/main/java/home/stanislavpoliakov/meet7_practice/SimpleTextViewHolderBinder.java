package home.stanislavpoliakov.meet7_practice;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class SimpleTextViewHolderBinder extends ViewHolderBinder {
    private final DataItem item;
    private static final String TAG = "meet7_logs";

    public SimpleTextViewHolderBinder(DataItem item, int viewType) {
        super(viewType);
        this.item = item;
        Log.d(TAG, "SimpleTextViewHolderBinder: Constructor passed, item.text = " + item.getText());
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder viewHolder) {
        Log.d(TAG, "bindViewHolder: " + viewHolder.getClass().getSimpleName());
        MyAdapter.SimpleTextViewHolder simpleTextViewHolder = (MyAdapter.SimpleTextViewHolder) viewHolder;
        Log.d(TAG, "bindViewHolder: passed");
        simpleTextViewHolder.simpleTextView.setText(item.getText());
    }
}
