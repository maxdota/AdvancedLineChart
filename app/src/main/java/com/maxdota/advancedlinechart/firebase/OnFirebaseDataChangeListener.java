package com.maxdota.advancedlinechart.firebase;

/**
 * Created by Abc on 25/06/2017.
 */

public interface OnFirebaseDataChangeListener<T> {
    void onDataChanged(T data);
    void onError(String message);
}
