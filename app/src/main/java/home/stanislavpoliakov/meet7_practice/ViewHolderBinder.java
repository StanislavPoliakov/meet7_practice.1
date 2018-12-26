package home.stanislavpoliakov.meet7_practice;

import android.support.v7.widget.RecyclerView;

/**
 * Абстрактный класс для наследующих его ..ViewHolderBinder'-ов
 * Не использую viewType, потому что не достаю в коде тип из Binder'-ов, использую getItemType
 * для каждого элемента списка отображения. Оставляю закомментированными из-за SBOL Best Practice
 */
public abstract class ViewHolderBinder {
    //protected final int viewType;

    /*public ViewHolderBinder(int viewType) {
        this.viewType = viewType;
    }*/

    public abstract void bindViewHolder(RecyclerView.ViewHolder viewHolder);
}
