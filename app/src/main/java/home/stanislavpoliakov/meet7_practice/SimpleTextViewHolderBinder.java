package home.stanislavpoliakov.meet7_practice;

import android.support.v7.widget.RecyclerView;

public class SimpleTextViewHolderBinder extends ViewHolderBinder {
    private final DataItem item;

    public SimpleTextViewHolderBinder(DataItem item, int viewType) {
        super(viewType);
        this.item = item;
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder viewHolder) {
        MyAdapter.SimpleTextViewHolder simpleTextViewHolder = (MyAdapter.SimpleTextViewHolder) viewHolder;
        simpleTextViewHolder.simpleTextView.setText(item.getText());
    }
}
