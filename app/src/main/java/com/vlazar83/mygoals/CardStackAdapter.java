package com.vlazar83.mygoals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import static java.util.Collections.emptyList;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private List<CardShape> cards;

    CardStackAdapter(){
        cards = emptyList();
    }

    public void setCards(List<CardShape> cards) {
        this.cards = cards;
    }

    public List<CardShape> getCards(){
        return this.cards;
    }

    @NonNull
    @Override
    public CardStackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listItemView = inflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardStackAdapter.ViewHolder holder, int position) {
        CardShape spot = cards.get(position);
        holder.name.setText(spot.getCardGoal());
        //holder.city.setText(spot.getCity());
        //Glide.with(holder.image)
        //        .load(spot.getUrl())
        //         .into(holder.image);
        holder.itemView.setOnClickListener(v ->
                Toast.makeText(v.getContext(), spot.getCardGoal(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(@NonNull View view) {
            super(view);
        }

        TextView name = super.itemView.findViewById(R.id.item_name);
        //TextView city = super.itemView.findViewById(R.id.item_city);
        //ImageView image = super.itemView.findViewById(R.id.item_image);
    }

}
