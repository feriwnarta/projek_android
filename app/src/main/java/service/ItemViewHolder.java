package service;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tugakelompok.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewTitle;
    public ImageView imageViewIcon;

    public ItemViewHolder(View itemView) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
    }
}

