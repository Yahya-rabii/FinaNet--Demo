package com.android.FinaNet.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.FinaNet.R;
import com.android.FinaNet.model.FinaModel;
import com.bumptech.glide.Glide;
import java.util.List;



public class FinaListAdapter extends RecyclerView.Adapter<FinaListAdapter.MyViewHolder> {

    private List<FinaModel> finaModelList;
    private final FinaListClickListener clickListener;

    public FinaListAdapter(List<FinaModel> finaModelList, FinaListClickListener clickListener) {
        this.finaModelList = finaModelList;
        this.clickListener = clickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<FinaModel> finaModelList) {
        this.finaModelList = finaModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FinaListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return  new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FinaListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.BrandName.setText(finaModelList.get(position).getName());
        holder.BrandAddress.setText("Address: "+ finaModelList.get(position).getAddress());
        holder.listbrHours.setText("Today's hours: " + finaModelList.get(position).getHours().getTodaysHours());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(finaModelList.get(position));
            }
        });
        Glide.with(holder.thumbImage)
                .load(finaModelList.get(position).getImage())
                .into(holder.thumbImage);

    }

    @Override
    public int getItemCount() {
        return finaModelList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView BrandName;
        TextView BrandAddress;
        TextView listbrHours;
        ImageView thumbImage;

        public MyViewHolder(View view) {
            super(view);
            BrandName = view.findViewById(R.id.brandName);
            BrandAddress = view.findViewById(R.id.brandAddress);
            listbrHours = view.findViewById(R.id.brandhrHours);
            thumbImage = view.findViewById(R.id.thumbImage);

        }
    }

    public interface FinaListClickListener {
        public void onItemClick(FinaModel finaModel);
    }
}
