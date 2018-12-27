package home.stanislavpoliakov.meet7_practice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.util.Log;

import java.util.List;

public class DiffCall extends DiffUtil.Callback {
    private static final String TAG = "meet7_logs";
    private List<DataItem> mOldList, mNewList;


    /**
     * Констурктор
     * @param oldList "старые" данные
     * @param newList данные с измениниями
     */
    public DiffCall(List<DataItem> oldList, List<DataItem> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return this.mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return this.mNewList.size();
    }

    /**
     * Метод проверяет одинаковые ли элементы в указанных позициях в списках. Сравнение по Id будет
     * давать true в случае изменения содержимого в одинаковых типах элемнтов
     * @param oldItemPosition
     * @param newItemPosition
     * @return результат сравнения по id
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).getId() == mNewList.get(newItemPosition).getId();
    }

    /**
     * Если метод areItemsTheSame вернул true, то выполняется проверка содержимого элементов.
     * Эта проверка будет валидна только в случае deep-copy списков. Элементы этих списков должны
     * указывать на разные элементы памяти. Любые попытки косвенного Shallow-copy (newList = new ArrayList(oldList),
     * Collections.copy, newList.addAll(oldList)) не приносят желаемого результата, то есть на выходе
     * всегда true, а нам нужно false для того, чтобы зайти в getChangePayLoad!
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).equals(mNewList.get(newItemPosition));
    }

    /**
     * Этот методы вызывается тогда, когда элементы на позиции совпадают (areItemsTheSame = true),
     * но содержимое этих элементов поменялось (areContentsTheSame = false). Метод необходим для того,
     * чтобы отслеживать конкретные изменения, которые были сделаны, например, поля, которые были
     * изменены. В данном случае, мониторим измененения в элементах списков, заносим эти изменения в
     * Bundle и бросаем в onBindViewHolder(, , payload)
     */

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        DataItem oldDataItem = mOldList.get(oldItemPosition);
        DataItem newDataItem = mNewList.get(newItemPosition);
        Bundle diff = new Bundle();
        if (oldDataItem.getText() != newDataItem.getText())
            diff.putString("itemText", newDataItem.getText());
        if (oldDataItem.getImageId() != newDataItem.getImageId())
            diff.putInt("itemImageId", newDataItem.getImageId());
        if (diff.size() == 0) return null;
        return diff;
    }
}
