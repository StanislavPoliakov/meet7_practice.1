package home.stanislavpoliakov.meet7_practice;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class ImageCascadeViewHolderBinder extends ViewHolderBinder {
    private static final String TAG = "meet7_logs";

    /**
     * Конструтор для создания Binder'-а для типа IMAGE_CASCADE
     * Основная реализация описана в родителе.
     * @param item элемент списка, для которого делаем Bind.
     */
    public ImageCascadeViewHolderBinder(DataItem item) {
        super(item);
    }

    /**
     * Метод привязки соответствующего ViewHoler'-а и сеттинга вложенного RecyclerView
     * @param viewHolder Holder, который мы передаем (логика выбора Holder'-а реализована
     *                   в MyAdapter
     */
    @Override
    public void bindViewHolder(RecyclerView.ViewHolder viewHolder) {
        MyAdapter.ImageCascadeViewHolder imageCascadeViewHolder = (MyAdapter.ImageCascadeViewHolder) viewHolder;
        imageCascadeViewHolder.recyclerViewHorizontal.setAdapter(new HorizontalRecyclerViewAdapter(dataItem.getImageItems()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(imageCascadeViewHolder.itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        imageCascadeViewHolder.recyclerViewHorizontal.setLayoutManager(layoutManager);
        //simpleImageViewHolder.simpleImageView.setImageResource(dataItem.getImageId());
    }
}
