package com.example.flashcardmaker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.flashcardmaker.Fragments.FragmentSets;
import com.example.flashcardmaker.Fragments.FragmentMain;
import com.example.flashcardmaker.Fragments.FragmentNewSet;
import com.example.flashcardmaker.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, new FragmentMain());
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        FragmentSets fragmentSets = new FragmentSets();
        switch (item.getItemId()) {
            case R.id.action_home:
                transaction.replace(R.id.fragmentContainer, new FragmentMain());
                setTitle("Home Menu");
                break;
            case R.id.action_options:
                break;
            case R.id.action_create_flashcards:
                FragmentNewSet fragmentNewSet = new FragmentNewSet();
                bundle.putString(FragmentNewSet.TYPE_SET, FragmentNewSet.NEW_SET);
                fragmentNewSet.setArguments(bundle);
                transaction.replace(R.id.fragmentContainer, fragmentNewSet);
                break;
            case R.id.action_my_flashcards:
                bundle.putString(FragmentSets.TYPE_SET, FragmentSets.ALL_SETS);
                fragmentSets.setArguments(bundle);
                transaction.replace(R.id.fragmentContainer, fragmentSets);
                break;
            case R.id.action_recently_studied:
                bundle.putString(FragmentSets.TYPE_SET, FragmentSets.RECENTLY_STUDIED_SETS);
                fragmentSets.setArguments(bundle);
                transaction.replace(R.id.fragmentContainer, fragmentSets);
                break;
            case R.id.action_favourite:
                bundle.putString(FragmentSets.TYPE_SET, FragmentSets.FAVOURITE_SETS);
                fragmentSets.setArguments(bundle);
                transaction.replace(R.id.fragmentContainer, fragmentSets);
                break;
            default:
                break;
        }
        transaction.commit();
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to quit the application?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                });
        builder.create().show();
    }
}