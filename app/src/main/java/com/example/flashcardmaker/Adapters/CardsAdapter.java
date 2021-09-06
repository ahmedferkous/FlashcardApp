package com.example.flashcardmaker.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardmaker.DataObjects.Card;
import com.example.flashcardmaker.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {
    public interface onRemovedCard {
        void onRemovedCardResult();
    }
    private onRemovedCard onRemovedCard;
    private ArrayList<Card> cards = new ArrayList<>();
    private Context context;

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        cards.add(card);
        notifyItemInserted(cards.indexOf(card));
    }

    public CardsAdapter(CardsAdapter.onRemovedCard onRemovedCard, Context context) {
        this.onRemovedCard = onRemovedCard;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card boundCard = cards.get(position);
        holder.edtTxtFront.setText(boundCard.getFront());
        holder.edtTxtBack.setText(boundCard.getBack());
        holder.txtRemoveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setMessage("Remove card?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cards.remove(boundCard);
                                notifyDataSetChanged();
                                onRemovedCard.onRemovedCardResult();
                            }
                        });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private EditText edtTxtFront, edtTxtBack;
        private TextView txtRemoveCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edtTxtFront = itemView.findViewById(R.id.edtTxtFront);
            edtTxtBack = itemView.findViewById(R.id.edtTxtBack);
            txtRemoveCard = itemView.findViewById(R.id.txtRemoveCard);
        }
    }
}
