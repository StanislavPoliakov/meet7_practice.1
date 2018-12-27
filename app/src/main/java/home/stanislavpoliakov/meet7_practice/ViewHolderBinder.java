package home.stanislavpoliakov.meet7_practice;

import android.support.v7.widget.RecyclerView;

/**
 * Абстрактный класс для наследующих его ..ViewHolderBinder'-ов
 * Не использую viewType, потому что не достаю в коде тип из Binder'-ов, использую getItemType
 * для каждого элемента списка отображения. Забираю из конструктора элемент, чтобы менять состояние
 * изнутри связанного с ним Binder'-а
 */
public abstract class ViewHolderBinder {
    protected final DataItem dataItem;

    public ViewHolderBinder(DataItem dataItem) {
        this.dataItem = dataItem;
    }

    public abstract void bindViewHolder(RecyclerView.ViewHolder viewHolder);

}
