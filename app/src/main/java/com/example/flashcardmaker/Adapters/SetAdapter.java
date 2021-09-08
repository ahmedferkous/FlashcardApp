package com.example.flashcardmaker.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardmaker.Activities.MainActivity;
import com.example.flashcardmaker.Data.Database.SetDatabase;
import com.example.flashcardmaker.Data.Database.SetItemDao;
import com.example.flashcardmaker.Data.Set;
import com.example.flashcardmaker.Fragments.FragmentAllSets;
import com.example.flashcardmaker.Fragments.FragmentNewSet;
import com.example.flashcardmaker.R;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> {
    private ArrayList<Set> sets = new ArrayList<>();
    private Context context;
    private FragmentManager manager;

    public SetAdapter(Context context, FragmentManager manager) {
        this.context = context;
        this.manager = manager;
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

        if (boundSet.isFavourite()) {
            holder.imageViewFavouritesFilled.setVisibility(View.VISIBLE);
            holder.imageViewFavourites.setVisibility(View.GONE);
        } else {
            holder.imageViewFavouritesFilled.setVisibility(View.GONE);
            holder.imageViewFavourites.setVisibility(View.VISIBLE);
        }

        holder.imageViewFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imageViewFavouritesFilled.setVisibility(View.VISIBLE);
                holder.imageViewFavourites.setVisibility(View.GONE);
                new UpdateFavouritesSetTask(context).execute(boundSet.getId(), 1);
            }
        });

        holder.imageViewFavouritesFilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imageViewFavouritesFilled.setVisibility(View.GONE);
                holder.imageViewFavourites.setVisibility(View.VISIBLE);
                new UpdateFavouritesSetTask(context).execute(boundSet.getId(), 0);
            }
        });

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setMessage("View/Edit this set?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FragmentTransaction transaction = manager.beginTransaction();
                                FragmentNewSet fragmentNewSet = new FragmentNewSet();
                                Bundle bundle = new Bundle();
                                bundle.putString(FragmentNewSet.TYPE_SET, FragmentNewSet.EDIT_SET);
                                bundle.putString(FragmentNewSet.SET_KEY, new Gson().toJson(boundSet));
                                bundle.putInt(FragmentNewSet.ID_SET_KEY, boundSet.getId());
                                fragmentNewSet.setArguments(bundle);
                                transaction.replace(R.id.fragmentContainer, fragmentNewSet);
                                transaction.commit();
                            }
                        });
                builder.create().show();
            }
        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setMessage("Remove this set?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sets.remove(boundSet);
                                notifyDataSetChanged();
                                new RemoveSetTask(context).execute(boundSet.getId());
                            }
                        });
                builder.create().show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSetName, txtSetDesc, txtNumCards;
        private ImageView imageViewFavourites, imageViewFavouritesFilled;
        private RelativeLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSetName = itemView.findViewById(R.id.txtSetName);
            txtSetDesc = itemView.findViewById(R.id.txtSetDesc);
            txtNumCards = itemView.findViewById(R.id.txtNumCards);
            imageViewFavourites = itemView.findViewById(R.id.imgViewFavourite);
            imageViewFavouritesFilled = itemView.findViewById(R.id.imgViewFavouriteFilled);
            parent = itemView.findViewById(R.id.parent);
        }
    }

    private static class UpdateFavouritesSetTask extends AsyncTask<Integer, Void, Void> {
        private SetItemDao dao;

        public UpdateFavouritesSetTask(Context context) {
            dao = SetDatabase.getInstance(context).setItemDao();
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            int setId = integers[0];
            int boolShowFavourite = integers[1];
            dao.updateFavouritesById(setId, boolShowFavourite);
            return null;
        }
    }

    private static class RemoveSetTask extends AsyncTask<Integer, Void, Void> {
        private SetItemDao dao;
        private WeakReference<Context> reference;

        public RemoveSetTask(Context context) {
            dao = SetDatabase.getInstance(context).setItemDao();
            reference = new WeakReference<>(context);
        }
        @Override
        protected Void doInBackground(Integer... integers) {
            dao.deleteSetById(integers[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(reference.get(), "Set removed successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}
