package com.togethernet.togethernet.RvAdapter;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.togethernet.togethernet.GlobalApp.GlobalApp;
import com.togethernet.togethernet.R;
import com.togethernet.togethernet.WifiJumper.utilities.wifiUtilities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pietr_000 on 07/11/2017.
 */


//Adapter per AvNetsActivity

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.AvNetsCard>{

    public ArrayList<HashMap<String, String>> AvNets;
    protected Activity activity;

    public static class AvNetsCard extends RecyclerView.ViewHolder {


        CardView cv;
        TextView netName;
        TextView netUserName;
        ImageView netImage;
        FloatingActionButton netButton;

        AvNetsCard(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            netName = (TextView)itemView.findViewById(R.id.netName);
            netUserName = (TextView) itemView.findViewById(R.id.netUserName);
            netImage = (ImageView)itemView.findViewById(R.id.netImage);
            netButton = (FloatingActionButton)itemView.findViewById(R.id.netButton);
        }
    }

    public RVAdapter(Activity activity){
        GlobalApp globalApp = ( GlobalApp ) activity.getApplication();
        this.activity = activity;
        this.AvNets = globalApp.GetAvNetsList();
    }

    @Override
    public int getItemCount() {
        return AvNets.size();
    }

    public AvNetsCard onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_reti_disponibili, viewGroup, false);
        AvNetsCard cards = new AvNetsCard(v);
        return cards;
    }

    @Override
    public void onBindViewHolder(AvNetsCard card, final int i) {
        card.netName.setText(AvNets.get(i).get("wifi_ssid"));
        card.netUserName.setText(AvNets.get(i).get("user"));
        card.netImage.setBackgroundResource(R.drawable.man);
        card.netButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Vuoi connettere?
                wifiUtilities.builAlertMessageGoWifi(activity, AvNets.get(i));
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (this.AvNets.isEmpty()){
            recyclerView.setBackgroundResource(R.drawable.sad_cloud);
        }else{
            recyclerView.setBackgroundResource(R.color.colorTransparent);
        }
        super.onAttachedToRecyclerView(recyclerView);
    }
}