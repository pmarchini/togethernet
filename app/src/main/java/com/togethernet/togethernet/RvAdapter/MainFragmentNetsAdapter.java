package com.togethernet.togethernet.RvAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
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
 * Created by pietr_000 on 09/01/2018.
 */

public class MainFragmentNetsAdapter extends RecyclerView.Adapter<MainFragmentNetsAdapter.DistanceNetCard> {

    public ArrayList<HashMap<String, String>> AvNets;
    protected Activity activity;

    public static class DistanceNetCard extends RecyclerView.ViewHolder {


        CardView cv;
        TextView netName;
        TextView netUserName;
        ImageView netImage;
        TextView netDistance;

        DistanceNetCard(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            netName = (TextView)itemView.findViewById(R.id.netName);
            netUserName = (TextView) itemView.findViewById(R.id.netUserName);
            netImage = (ImageView)itemView.findViewById(R.id.netImage);
            netDistance = (TextView) itemView.findViewById(R.id.net_distance);
        }
    }

    public MainFragmentNetsAdapter( android.support.v4.app.Fragment fragment){
        GlobalApp globalApp = ( GlobalApp ) fragment.getActivity().getApplication();
        this.activity = activity;
        this.AvNets = globalApp.getLocationNetsList().GlobalNetsLocation.getLocations();
    }

    @Override
    public int getItemCount() {
        return AvNets.size();
    }

    public MainFragmentNetsAdapter.DistanceNetCard onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_distanza_reti, viewGroup, false);
        DistanceNetCard cards = new DistanceNetCard(v);
        return cards;
    }


    @Override
    public void onBindViewHolder(DistanceNetCard card, final int i) {
        card.netName.setText(AvNets.get(i).get("Name"));
        card.netImage.setBackgroundResource(R.drawable.man);
        //Tolgo visibilità al bottone
        card.netDistance.setText(AvNets.get(i).get("Distance") + " m ");
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (this.AvNets.isEmpty()){
            //recyclerView.setBackgroundResource(R.drawable.sad_cloud);
        }else{
            recyclerView.setBackgroundResource(R.color.cardview_light_background);
        }
        super.onAttachedToRecyclerView(recyclerView);
    }
}
