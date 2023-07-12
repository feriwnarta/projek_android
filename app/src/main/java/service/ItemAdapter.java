package service;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tugakelompok.R;

import java.util.List;

import AppFragment.DetailFragment;
import data.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;
    private FragmentManager fragmentManager;

    public ItemAdapter(List<Item> itemList, FragmentManager fragmentManager) {
        this.itemList = itemList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);

        holder.textViewTitle.setText(item.getTitle());

        Context context = holder.itemView.getContext();
        Glide.with(context)
                .load(item.getImgPath())
                .apply(new RequestOptions().placeholder(R.drawable.baseline_movie_24))
                .into(holder.imageViewIcon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new DetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", item.getId());
                bundle.putString("item_title", item.getTitle());
                bundle.putString("item_image", item.getImgPath());
                bundle.putString("item_produser", item.getProduser());
                bundle.putString("item_durasi", item.getDurasi());
                bundle.putString("category", item.getCategory());

                fragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewIcon;
        TextView textViewTitle;

        ItemViewHolder(View itemView) {
            super(itemView);
            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }
    }
}

