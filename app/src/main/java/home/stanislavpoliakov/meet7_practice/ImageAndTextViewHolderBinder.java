package home.stanislavpoliakov.meet7_practice;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class ImageAndTextViewHolderBinder extends ViewHolderBinder {
    private final DataItem item;
    private static final String TAG = "meet7_logs";

    /**
     * Конструтор для создания Binder'-а для типа IMAGE_AND_TEXT
     * @param item элемент данных отображения, состояние которого используем для bindViewHolder
     * @param viewType оставляю, но не использую, потому что возвращаю viewType не из
     *                 Binder, а из item.getItemType()
     */
    public ImageAndTextViewHolderBinder(DataItem item, int viewType) {
        // Поскольку мы не определяем default-реализацию конструктора в абстрактном
        // классе, но в SBOL используется эта конструкция - просто комментирую строку
        // super(viewType);
        this.item = item;
        //Log.d(TAG, "ImageAndTextViewHolderBiner: Constructor passed, item.text = " + item.getText() + "; item.imageid = " + item.getImageId());
    }

    /**
     * Метод привязки соответствующего ViewHoler'-а и сеттинга его UI-компонентов
     * @param viewHolder Holder, который мы передаем (логика выбора Holder'-а реализована
     *                   в MyAdapter
     */
    @Override
    public void bindViewHolder(RecyclerView.ViewHolder viewHolder) {
        //Log.d(TAG, "bindViewHolder: " + viewHolder.getClass().getSimpleName());
        MyAdapter.ImageAndTextViewHolder imageAndTextViewHolder = (MyAdapter.ImageAndTextViewHolder) viewHolder;
        //Log.d(TAG, "bindViewHolder: passed");
        imageAndTextViewHolder.comboTextView.setText(item.getText());
        imageAndTextViewHolder.comboImageView.setImageResource(item.getImageId());
    }
}
