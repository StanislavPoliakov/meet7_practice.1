package home.stanislavpoliakov.meet7_practice;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {
    private final List<ViewHolderBinder> mBinders;
    private SparseArray<ViewHolderFactory> mFactory;
    private List<DataItem> items;

    public MyAdapter(List<DataItem> items) {
        this.items = items;
        mBinders = new ArrayList<>();
        initFactory();
        setData(items);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolderFactory factory = mFactory.get(viewType);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return factory.createViewHolder(parent, layoutInflater);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderBinder binder = mBinders.get(position);
        if (binder != null) binder.bindViewHolder(holder);
    }

    private void setData(List<DataItem> items) {
        mBinders.clear();
        for (DataItem item : items) mBinders.add(generateBinder(item));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private void initFactory() {
        mFactory = new SparseArray<>();
        mFactory.put(ItemTypes.SIMPLE_TEXT.type, new SimpleTextViewHolderFactory());
        mFactory.put(ItemTypes.SIMPLE_IMAGE.type, new SimpleImageViewHolderFactory());
        mFactory.put(ItemTypes.IMAGE_AND_TEXT.type, new ImageAndTextViewHolderFactory());
    }

    private ViewHolderBinder generateBinder(DataItem item) {
        switch (item.getItemType()) {
            case SIMPLE_TEXT:
                return new SimpleTextViewHolderBinder(item, item.getItemType().type);
            case SIMPLE_IMAGE:
                return new SimpleImageViewHolderBinder(item, item.getItemType().type);
            case IMAGE_AND_TEXT:
                return new ImageAndTextViewHolderBinder(item, item.getItemType().type);
            default:
                return null;
        }
    }


    public static class SimpleTextViewHolder extends RecyclerView.ViewHolder {
        public TextView simpleTextView;

        public SimpleTextViewHolder(View itemView) {
            super(itemView);
            simpleTextView = itemView.findViewById(R.id.simpleTextView);
        }
    }

    public static class SimpleImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView simpleImageView;

        public SimpleImageViewHolder(View itemView) {
            super(itemView);
            simpleImageView = itemView.findViewById(R.id.simpleImageView);
        }
    }

    public static class ImageAndTextViewHolder extends RecyclerView.ViewHolder {
        public TextView comboTextView;
        public ImageView comboImageView;

        public ImageAndTextViewHolder(View itemView) {
            super(itemView);
            comboTextView = itemView.findViewById(R.id.comboTextView);
            comboImageView = itemView.findViewById(R.id.comboImageView);
        }
    }


}
