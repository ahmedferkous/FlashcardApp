package com.example.flashcardmaker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardmaker.DataObjects.Set;
import com.example.flashcardmaker.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> {
    private ArrayList<Set> sets = new ArrayList<>();
    private Context context;

    public SetAdapter(Context context) {
        this.context = context;
    }

    public void setSets(ArrayList<Set> sets) {
        this.sets = sets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.set_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Set boundSet = sets.get(position);
        holder.txtSetName.setText(boundSet.getTitle());
        holder.txtSetDesc.setText(boundSet.getDesc());
        holder.txtNumCards.setText(boundSet.getSetCards().size() + " Cards");
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 6/09/2021 card fragment
            }
        });
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSetName, txtSetDesc, txtNumCards;
        private RelativeLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSetName = itemView.findViewById(R.id.txtSetName);
            txtSetDesc = itemView.findViewById(R.id.txtSetDesc);
            txtNumCards = itemView.findViewById(R.id.txtNumCards);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
