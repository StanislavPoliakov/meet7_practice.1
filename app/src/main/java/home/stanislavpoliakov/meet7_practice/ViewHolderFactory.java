package home.stanislavpoliakov.meet7_practice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Функциональный интерфейс для фабрик ViewHolder'-ов
 */
public interface ViewHolderFactory {
    RecyclerView.ViewHolder createViewHolder(ViewGroup parent, LayoutInflater inflater);
}
