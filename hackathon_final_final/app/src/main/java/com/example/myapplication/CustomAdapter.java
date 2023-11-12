package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {
    private Context context;
    private int resource;
    private List<String> mesaje;
    private LayoutInflater layoutInflater;

    public CustomAdapter(@NonNull Context context, int resource, List<String> mesaje, LayoutInflater layoutInflater) {
        super(context, resource, mesaje);
        this.context = context;
        this.resource = resource;
        this.mesaje = mesaje;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = layoutInflater.inflate(resource, parent, false);
        TextView tvMesaj = view.findViewById(R.id.tv_mesaj);
        String mesaj = mesaje.get(position);

        if (mesaj != null) {
            tvMesaj.setText(mesaj);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone((ConstraintLayout) tvMesaj.getParent());


            if (position % 2 == 0) {

                constraintSet.connect(tvMesaj.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);

                tvMesaj.setBackgroundResource(R.drawable.bubble_sent);
            } else {

                constraintSet.connect(tvMesaj.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);

                tvMesaj.setBackgroundResource(R.drawable.bubble_received);
            }



            constraintSet.applyTo((ConstraintLayout) tvMesaj.getParent());
        }

        return view;
    }
}
