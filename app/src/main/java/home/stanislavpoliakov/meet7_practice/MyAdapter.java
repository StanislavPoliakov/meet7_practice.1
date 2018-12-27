package home.stanislavpoliakov.meet7_practice;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<ViewHolderBinder> mBinders;
    private SparseArray<ViewHolderFactory> mFactory;
    private List<DataItem> items;
    private static final String TAG = "meet7_logs";

    /**
     * Определяем конструктор адаптера таким образом, чтобы на вход он получал данные
     * для отображения в RecyclerView
     * @param items список данных, сгенерированных сервисом
     */
    public MyAdapter(List<DataItem> items) {
        this.items = items;
        mBinders = new ArrayList<>();
        initFactory();
        setData(items);
    }

    /**
     * Метод создания отедльного ViewHolder'-а, в зависимости от его типа
     * @param parent ViewGroup для отображения RecyclerView, в данном случае LinearLayoutManager
     * @param viewType тип ViewHolder'-а, который необходимо создать. Определяется методом getItemViewType
     * @return возвращет созданный фабрикой (initFactory()) Holder
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolderFactory factory = mFactory.get(viewType);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return factory.createViewHolder(parent, layoutInflater);
    }

    /**
     * Получаем тип данных для отображения в зависимости от позиции элемента DataItem в списке
     * @param position позиция элемента в списке, которую надо Создать-Привязать и, соответственно, отобразить
     * @return устанавливает тип данных (= int type в нашем Enum) который будет определять тип создаваемого ViewHolder'-а
     *
     * Любопытно, что если этот метод не переопределить, то возвращаемое значение будет равно 0, что в нашем
     * случае будет создавать SimpleTextViewHolderBinder (ItemTypes.SIMPLE_TEXT.type = 0)
     */
    @Override
    public int getItemViewType(int position) {
        switch (items.get(position).getItemType()) {
            case SIMPLE_TEXT:
                return ItemTypes.SIMPLE_TEXT.type;
            case SIMPLE_IMAGE:
                return ItemTypes.SIMPLE_IMAGE.type;
            case IMAGE_AND_TEXT:
                return ItemTypes.IMAGE_AND_TEXT.type;
            default:
                return super.getItemViewType(position);
        }
    }

    /**
     * Находим в нашем списке привязок mBinders элемент, соответствующий позиции отображения, и
     * если он не нулевой - отображаем его (реализция метода в классах ..Binder.java)
     * @param holder ViewHolder, который необходимо отобразить
     * @param position Позиция перебора в списке данных для отображения
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderBinder binder = mBinders.get(position);
        if (binder != null) binder.bindViewHolder(holder);
    }


    /**
     * Метод внесения изменений в привязки в зависимости от изменений контента (areContentTheSame = false)
     * в DiffCalls
     * @param holder
     * @param position
     * @param payloads
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        ViewHolderBinder binder = mBinders.get(position);
        if (payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads);
        else {
            Bundle changes = (Bundle) payloads.get(0);
            for (String key : changes.keySet()) {
                if ("itemText".equals(key)) binder.dataItem.setText(changes.getString(key));
                if ("itemImageId".equals(key)) binder.dataItem.setImageId(changes.getInt(key));
            }
            binder.bindViewHolder(holder);
        }
    }

    /**
     * После получения данных из сервиса, мы должны создать список Binder'-ов
     * @param items данные из сервиса
     */
    private void setData(List<DataItem> items) {
        mBinders.clear();
        for (DataItem item : items) mBinders.add(generateBinder(item));
        notifyDataSetChanged(); // Уведомляем Адаптер о том, что изменились данные
    }

    /**
     * Публичный метод для подсчета и отображение изменений
     * @param oldData "Старый" оригинальный список
     * @param newData "Новый" список с изменениями
     * Примечательно то, что для отображения элементов необходим метод setData, чтобы запустить отрисовку
     */
    public void onNewData(List<DataItem> oldData, List<DataItem> newData) {

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCall(oldData, newData));
        diffResult.dispatchUpdatesTo(this);

        setData(newData);
        oldData.clear();
        oldData.addAll(newData);

    }

    // Количество элементов для отображения
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Определяем MAP<Int, T>, в данном случае SparseArray<ViewHolderFactory> для хранения трех типов фабрик
     * по одной для каждого типа данных
     */
    private void initFactory() {
        mFactory = new SparseArray<>();
        mFactory.put(ItemTypes.SIMPLE_TEXT.type, new SimpleTextViewHolderFactory());
        mFactory.put(ItemTypes.SIMPLE_IMAGE.type, new SimpleImageViewHolderFactory());
        mFactory.put(ItemTypes.IMAGE_AND_TEXT.type, new ImageAndTextViewHolderFactory());
    }

    /**
     * Создаем Binder для каждого элемента данных в полученном из сервиса списке. Более того,
     * в зависимости от типа данных (item.getItemType().type) создаем соответствующий Binder
     * @param item элемент данных из списка данных List<DataItem> (метод setData)
     * @return Binder, который будет помещен в общий список Binder'-ов для отобржания
     */
    private ViewHolderBinder generateBinder(DataItem item) {
        switch (item.getItemType()) {
            case SIMPLE_TEXT:
                return new SimpleTextViewHolderBinder(item);
            case SIMPLE_IMAGE:
                return new SimpleImageViewHolderBinder(item);
            case IMAGE_AND_TEXT:
                return new ImageAndTextViewHolderBinder(item);
            default:
                return null;
        }
    }

    /**
     * Статичный класс, описывающий ViewHolder и инициализирующий UI-компоненты
     * для элемнтов типа SIMPLE_TEXT
     */
    public static class SimpleTextViewHolder extends RecyclerView.ViewHolder {
        public TextView simpleTextView;

        public SimpleTextViewHolder(View itemView) {
            super(itemView);
            simpleTextView = itemView.findViewById(R.id.simpleTextView);
        }
    }

    /**
     * Статичный класс, описывающий ViewHolder и инициализирующий UI-компоненты
     * для элемнтов типа SIMPLE_IMAGE
     */
    public static class SimpleImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView simpleImageView;

        public SimpleImageViewHolder(View itemView) {
            super(itemView);
            simpleImageView = itemView.findViewById(R.id.simpleImageView);
            simpleImageView.setImageResource(R.drawable.image_4);
        }
    }

    /**
     * Статичный класс, описывающий ViewHolder и инициализирующий UI-компоненты
     * для элемнтов типа IMAGE_AND_TEXT
     */
    public static class ImageAndTextViewHolder extends RecyclerView.ViewHolder {
        public TextView comboTextView;
        public ImageView comboImageView;

        public ImageAndTextViewHolder(View itemView) {
            super(itemView);
            comboTextView = itemView.findViewById(R.id.comboTextView);
            comboImageView = itemView.findViewById(R.id.comboImageView);
            comboImageView.setImageResource(R.drawable.image_3);
        }
    }


}
