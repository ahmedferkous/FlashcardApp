package com.example.flashcardmaker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardmaker.Data.Card;
import com.example.flashcardmaker.R;

import java.util.ArrayList;

public class CardNameAdapter extends RecyclerView.Adapter<CardNameAdapter.ViewHolder> {
    private ArrayList<Card> cards = new ArrayList<>();
    private Context context;

    public CardNameAdapter(Context context) {
        this.context = context;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_name_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card boundCard = cards.get(position);
        holder.txtCardName.setText(boundCard.getFront());

        if (boundCard.isGotCorrect()) {
            holder.imgViewCorrect.setVisibility(View.VISIBLE);
            holder.imgViewWrong.setVisibility(View.GONE);
        } else {
            holder.imgViewCorrect.setVisibility(View.GONE);
            holder.imgViewWrong.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCardName;
        private ImageView imgViewWrong, imgViewCorrect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCardName = itemView.findViewById(R.id.txtCardName);
            imgViewWrong = itemView.findViewById(R.id.imgViewWrong);
            imgViewCorrect = itemView.findViewById(R.id.imgViewCorrect);
        }
    }
}
