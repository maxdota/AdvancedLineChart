package com.maxdota.advancedlinechart.firebase;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.maxdota.advancedlinechart.R;
import com.maxdota.advancedlinechart.model.Portfolio;

import java.util.ArrayList;

/**
 * Created by Abc on 25/06/2017.
 */

public class FirebaseHelper {
    private static final String PORTFOLIO_PATH = "framData/portfolios";
    private static final FirebaseHelper ourInstance = new FirebaseHelper();
    private FirebaseDatabase mDatabase;

    public static FirebaseHelper getInstance() {
        return ourInstance;
    }

    private FirebaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.setPersistenceEnabled(true);
    }

    public void getPortfolios(final Context context,
                              final OnFirebaseDataChangeListener<ArrayList<Portfolio>> onFirebaseDataChangeListener) {
        if (onFirebaseDataChangeListener == null) {
            return;
        }

        DatabaseReference reference = mDatabase.getReference(PORTFOLIO_PATH);
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || dataSnapshot.getValue() == null) {
                    onFirebaseDataChangeListener.onError(context.getString(R.string.firebase_empty_data_message));
                } else {
                    GenericTypeIndicator<ArrayList<Portfolio>> type = new GenericTypeIndicator<ArrayList<Portfolio>>() {
                    };
                    ArrayList<Portfolio> portfolios = dataSnapshot.getValue(type);
                    onFirebaseDataChangeListener.onDataChanged(portfolios);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onFirebaseDataChangeListener.onError(databaseError.getMessage());
            }
        });
    }
}
