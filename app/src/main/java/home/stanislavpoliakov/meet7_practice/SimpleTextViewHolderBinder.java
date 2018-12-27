package home.stanislavpoliakov.meet7_practice;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class SimpleTextViewHolderBinder extends ViewHolderBinder {
    private static final String TAG = "meet7_logs";

    /**
     * Конструтор для создания Binder'-а для типа SIMPLE_TEXT
     * Основная реализация описана в родителе.
     * @param item элемент списка, для которого делаем Bind.
     */
    public SimpleTextViewHolderBinder(DataItem item) {
        super(item);
    }

    /**
     * Метод привязки соответствующего ViewHoler'-а и сеттинга его UI-компонентов
     * @param viewHolder Holder, который мы передаем (логика выбора Holder'-а реализована
     *                   в MyAdapter
     */
    @Override
    public void bindViewHolder(RecyclerView.ViewHolder viewHolder) {
        MyAdapter.SimpleTextViewHolder simpleTextViewHolder = (MyAdapter.SimpleTextViewHolder) viewHolder;
        simpleTextViewHolder.simpleTextView.setText(dataItem.getText());
    }
}
