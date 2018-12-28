package home.stanislavpoliakov.meet7_practice;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerViewAdapter.SimpleImageViewHolder> {
    private List<Integer> imageItems;

    public HorizontalRecyclerViewAdapter(List<Integer> imageItems) {
        this.imageItems = imageItems;
    }

    @NonNull
    @Override
    public SimpleImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hrecview_image, parent, false);
        return new SimpleImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleImageViewHolder holder, int position) {
        holder.imageView.setImageResource(imageItems.get(position));
    }

    @Override
    public int getItemCount() {
        return imageItems.size();
    }

    public class SimpleImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public SimpleImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.simpleImageView);
        }
    }
}
