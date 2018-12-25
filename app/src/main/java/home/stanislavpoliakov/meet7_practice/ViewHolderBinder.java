package home.stanislavpoliakov.meet7_practice;

import android.support.v7.widget.RecyclerView;

public abstract class ViewHolderBinder {
    protected final int viewType;

    public ViewHolderBinder(int viewType) {
        this.viewType = viewType;
    }

    public abstract void bindViewHolder(RecyclerView.ViewHolder viewHolder);
}
