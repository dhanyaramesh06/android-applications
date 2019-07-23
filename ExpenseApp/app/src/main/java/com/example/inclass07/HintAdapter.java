package com.example.inclass07;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.Collections;

public class HintAdapter extends ArrayAdapter<String[]> {


    public HintAdapter(@NonNull Context context, @NonNull String[] objects, int theLayoutResId) {
        super(context, theLayoutResId, Collections.singletonList(objects));
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }
}
