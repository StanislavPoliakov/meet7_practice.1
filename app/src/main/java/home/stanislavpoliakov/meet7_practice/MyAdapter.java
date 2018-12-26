package home.stanislavpoliakov.meet7_practice;

import android.support.annotation.NonNull;
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderBinder binder = mBinders.get(position);
        if (binder != null) {
            Log.d(TAG, "onBindViewHolder: " + binder.getClass().getSimpleName());
            Log.d(TAG, "onBindViewHolder: holder = " + holder.getClass().getSimpleName());
            binder.bindViewHolder(holder);
        }
    }

    private void setData(List<DataItem> items) {
        mBinders.clear();
        Log.d(TAG, "setData: mBinders cleared");
        for (DataItem item : items) {
            mBinders.add(generateBinder(item));
            Log.d(TAG, "setData: mBinders.size = " + mBinders.size() +
                    "; mBinder.item = " + mBinders.get(0).getClass().getSimpleName());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
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
                Log.d(TAG, "return new Simple Text View Holder Binder");
                return new SimpleTextViewHolderBinder(item, item.getItemType().type);
            case SIMPLE_IMAGE:
                Log.d(TAG, "return new Simple Image View Holder Binder");
                return new SimpleImageViewHolderBinder(item, item.getItemType().type);
            case IMAGE_AND_TEXT:
                Log.d(TAG, "return new Image and Text Text View Holder Binder");
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
            simpleImageView.setImageResource(R.drawable.image_4);
        }
    }

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
