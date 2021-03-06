package com.example.flashcardmaker.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardmaker.Data.Card;
import com.example.flashcardmaker.Data.Database.SetDatabase;
import com.example.flashcardmaker.Data.Database.SetItemDao;
import com.example.flashcardmaker.Data.Set;
import com.example.flashcardmaker.Fragments.FragmentSets;
import com.example.flashcardmaker.Fragments.FragmentNewSet;
import com.example.flashcardmaker.R;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> {
    private static final String TAG = "SetAdapter";
    private boolean shouldShow = false;
    private ArrayList<Set> sets = new ArrayList<>();
    private Context context;
    private FragmentManager manager;
    private String type;

    public SetAdapter(Context context, FragmentManager manager, String type) {
        this.context = context;
        this.manager = manager;
        this.type = type;
    }

    public void setSets(ArrayList<Set> sets) {
        this.sets = sets;
        notifyDataSetChanged();
    }

    public void setShouldShow(boolean shouldShow) {
        this.shouldShow = shouldShow;
        notifyDataSetChanged();
    }

    public void selectAll() {
        for (Set s : sets) {
            s.setSelected(true);
            new UpdateFavouritesOrSelectedTask(context, type).execute(-1, 1, 0);
        }
        notifyDataSetChanged();
    }

    public void unselectAll() {
        for (Set s : sets) {
            s.setSelected(false);
            new UpdateFavouritesOrSelectedTask(context, type).execute(-1, 0, 0);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.set_item, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Set boundSet = sets.get(position);
        Log.d(TAG, "onBindViewHolder: " + boundSet.isSelected());
        holder.txtSetName.setText(boundSet.getTitle());
        holder.txtSetDesc.setText(boundSet.getDesc());
        holder.txtNumCards.setText(boundSet.getSetCards().size() + " Cards");

        if (boundSet.isFavourite()) {
            holder.imageViewFavouritesFilled.setVisibility(View.VISIBLE);
            holder.imageViewFavourites.setVisibility(View.INVISIBLE);
        } else {
            holder.imageViewFavouritesFilled.setVisibility(View.INVISIBLE);
            holder.imageViewFavourites.setVisibility(View.VISIBLE);
        }

        if (boundSet.isRecentlyStudied()) {
            holder.imgViewRecent.setVisibility(View.VISIBLE);
            ArrayList<Card> cards = boundSet.getSetCards();
            holder.adapter.setCards(cards);

            if (holder.secondParent.getVisibility() == View.VISIBLE) {
                holder.imgViewDropUp.setVisibility(View.VISIBLE);
                holder.imgViewDropDown.setVisibility(View.GONE);
            } else {
                holder.imgViewDropUp.setVisibility(View.GONE);
                holder.imgViewDropDown.setVisibility(View.VISIBLE);
            }
        } else {
            holder.secondParent.setVisibility(View.GONE);
            holder.imgViewRecent.setVisibility(View.GONE);
            holder.imgViewDropDown.setVisibility(View.GONE);
            holder.imgViewDropUp.setVisibility(View.GONE);
        }

        holder.imageViewFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imageViewFavouritesFilled.setVisibility(View.VISIBLE);
                holder.imageViewFavourites.setVisibility(View.INVISIBLE);
                boundSet.setFavourite(true);
                new UpdateFavouritesOrSelectedTask(context).execute(boundSet.getId(), 1, -1);
            }
        });

        holder.imageViewFavouritesFilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imageViewFavouritesFilled.setVisibility(View.INVISIBLE);
                holder.imageViewFavourites.setVisibility(View.VISIBLE);
                boundSet.setFavourite(false);
                new UpdateFavouritesOrSelectedTask(context).execute(boundSet.getId(), 0, -1);
            }
        });

        if (shouldShow) {

            if (boundSet.isSelected()) {
                holder.imgViewFilledCheckbox.setVisibility(View.VISIBLE);
                holder.imgViewEmptyCheckbox.setVisibility(View.INVISIBLE);
            } else {
                holder.imgViewFilledCheckbox.setVisibility(View.INVISIBLE);
                holder.imgViewEmptyCheckbox.setVisibility(View.VISIBLE);
            }

            holder.imgViewEmptyCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.imgViewFilledCheckbox.setVisibility(View.VISIBLE);
                    holder.imgViewEmptyCheckbox.setVisibility(View.INVISIBLE);
                    boundSet.setSelected(true);
                    new UpdateFavouritesOrSelectedTask(context).execute(boundSet.getId(), 1, 1);
                }
            });

            holder.imgViewFilledCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.imgViewFilledCheckbox.setVisibility(View.INVISIBLE);
                    holder.imgViewEmptyCheckbox.setVisibility(View.VISIBLE);
                    boundSet.setSelected(false);
                    new UpdateFavouritesOrSelectedTask(context).execute(boundSet.getId(), 0, 1);
                }
            });
        } else {
            holder.imgViewFilledCheckbox.setVisibility(View.GONE);
            holder.imgViewEmptyCheckbox.setVisibility(View.GONE);
        }

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

        holder.imgViewDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imgViewDropDown.setVisibility(View.GONE);
                holder.secondParent.setVisibility(View.VISIBLE);
                holder.imgViewDropUp.setVisibility(View.VISIBLE);

                double percentage = Double.parseDouble(calculatePercentage(boundSet.getSetCards()));
                if (percentage >= 60.00) {
                    holder.txtResultPercentage.setTextColor(context.getResources().getColor(R.color.lightGreen));
                } else if (percentage >= 50.00 && percentage < 60.00) {
                    holder.txtResultPercentage.setTextColor(context.getResources().getColor(R.color.lightYellow));
                } else if (percentage >= 0.00 && percentage < 50.00) {
                    holder.txtResultPercentage.setTextColor(context.getResources().getColor(R.color.lightRed));
                }
                holder.txtResultPercentage.setText(String.valueOf(percentage)+"%");
            }
        });

        holder.imgViewDropUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imgViewDropDown.setVisibility(View.VISIBLE);
                holder.secondParent.setVisibility(View.GONE);
                holder.imgViewDropUp.setVisibility(View.GONE);
            }
        });

    }

    private String calculatePercentage(ArrayList<Card> setCards) {
        DecimalFormat format = new DecimalFormat("#00.00");
        double numberOfCorrect = 0.0;
        double totalQuestions = setCards.size();

        for (Card c : setCards) {
            Log.d(TAG, "calculatePercentage: " + c.getGotCorrect());
            if (c.getGotCorrect() == 1) {
                numberOfCorrect += 1;
            }
        }
        Log.d(TAG, "calculatePercentage: " + totalQuestions);
        return format.format((numberOfCorrect / totalQuestions) * 100);
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSetName, txtSetDesc, txtNumCards, txtResultPercentage;
        private ImageView imageViewFavourites, imageViewFavouritesFilled, imgViewFilledCheckbox, imgViewEmptyCheckbox, imgViewRecent, imgViewDropDown, imgViewDropUp;
        private MaterialCardView parent, secondParent;
        private RecyclerView recView;
        private CardNameAdapter adapter;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            txtSetName = itemView.findViewById(R.id.txtSetName);
            txtSetDesc = itemView.findViewById(R.id.txtSetDesc);
            txtNumCards = itemView.findViewById(R.id.txtNumCards);
            txtResultPercentage = itemView.findViewById(R.id.txtResultPercentage);
            imageViewFavourites = itemView.findViewById(R.id.imgViewFavourite);
            imageViewFavouritesFilled = itemView.findViewById(R.id.imgViewFavouriteFilled);
            imgViewFilledCheckbox = itemView.findViewById(R.id.imgViewFilledCheckbox);
            imgViewEmptyCheckbox = itemView.findViewById(R.id.imgViewEmptyCheckbox);
            imgViewRecent = itemView.findViewById(R.id.imgViewRecent);
            imgViewDropDown = itemView.findViewById(R.id.imgViewDropDown);
            imgViewDropUp = itemView.findViewById(R.id.imgViewDropUp);
            secondParent = itemView.findViewById(R.id.secondParent);
            parent = itemView.findViewById(R.id.parent);
            recView = itemView.findViewById(R.id.recView);
            adapter = new CardNameAdapter(context);
            recView.setLayoutManager(new LinearLayoutManager(context));
            recView.setAdapter(adapter);
        }
    }

    private static class UpdateFavouritesOrSelectedTask extends AsyncTask<Integer, Void, Void> {
        private SetItemDao dao;
        private String type = "";

        public UpdateFavouritesOrSelectedTask(Context context) {
            dao = SetDatabase.getInstance(context).setItemDao();
        }

        public UpdateFavouritesOrSelectedTask(Context context, String type) {
            dao = SetDatabase.getInstance(context).setItemDao();
            this.type = type;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            if (integers[2] == -1) {
                int setId = integers[0];
                int boolShowFavourite = integers[1];
                dao.updateFavouritesById(setId, boolShowFavourite);
            } else if (integers[2] == 0) {
                int boolSelectedAll = integers[1];
                switch (type) {
                    case FragmentSets.ALL_SETS:
                        Log.d(TAG, "doInBackground: ?");
                        dao.updateSelectedStatusAll(boolSelectedAll);
                        break;
                    case FragmentSets.FAVOURITE_SETS:
                        dao.updateSelectedStatusFavourites(boolSelectedAll);
                        break;
                    case FragmentSets.RECENTLY_STUDIED_SETS:
                        dao.updateSelectedStatusRecent(boolSelectedAll);
                        break;
                    default:
                        break;
                }
            } else if (integers[2] == 1) {
                int setId = integers[0];
                int boolSelected = integers[1];
                dao.updateSelectedById(setId, boolSelected);
            }
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
