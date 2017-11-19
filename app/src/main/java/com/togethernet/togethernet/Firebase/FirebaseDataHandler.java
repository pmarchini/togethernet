package com.togethernet.togethernet.Firebase;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

import com.togethernet.togethernet.GlobalApp.GlobalApp;
import com.togethernet.togethernet.WifiJumper.AvNetConnect;

/**
 * Created by pietr_000 on 03/11/2017.
 */

public class FirebaseDataHandler {

    public ArrayList<HashMap<String , String>> AvTogetherNets;

    private  FirebaseDatabase Database;
    private DatabaseReference mDatabase;
    public int counter;


    public FirebaseDataHandler(){
        AvTogetherNets = new ArrayList();
        // Prendo istanza del DB
        this.Database = FirebaseDatabase.getInstance();
        // Referenzio il DB
        this.mDatabase = Database.getReferenceFromUrl("https://togethernet-8221b.firebaseio.com/");
    }

    //Passato in ingresso una lista di reti le analizzo e restituisco una lista di reti con i vari dettagli
    // Utente , Nome , eventualmente password
    // I dati vengono salvati in modo asincrono all'interno della lista AvTogetherNets
    // Posso usufruire dei dati durante l'intervallo dello scan
    public void getAVTogetherNets(final List<ScanResult> ApList, final Context context) {

        this.counter = 0;

        for (final ScanResult scanElement : ApList){
            //Creo il child per la query -> SELECT users WHERE wifi_bssid eq @BSSID (ABAP)
            Query query = this.mDatabase.child("APs").child(scanElement.BSSID);
            //Creo evento per leggere i valori
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                if(dataSnapshot.getValue() != null){ // -> la rete fa parte di togetherNet
                                    //Creo elemento della lista
                            HashMap<String, String> AvTogetherNetElement = new HashMap<>();
                            //Aggiungo primo elemento -> chiave unica -> BSSID
                            AvTogetherNetElement.put("wifi_bssid", scanElement.BSSID);
                            //Ciclo sui vari elementi della rete
                            for (DataSnapshot obj : dataSnapshot.getChildren()) {
                                //Aggiungo ogni elemento da DB
                                AvTogetherNetElement.put(obj.getKey().toString(), obj.getValue().toString());
                            }
                            //Aggiungo elementi dallo scan -> mescolo dati da db e device
                            AvTogetherNetElement.put("level", String.valueOf(scanElement.level));
                            AvTogetherNetElement.put("security", scanElement.capabilities);
                            //Pusho elemento nella lista
                            AvTogetherNets.add(AvTogetherNetElement);
                        }else{

                        }
                    }
                    counter += 1;
                    //Ho controllato tutte le reti, vado oltre
                    if( counter == ApList.size()){
                        Log.i("TogetherNet", "Scanned every net, time to go on, found nr : " + AvTogetherNets.size() + " TogetherNets");
                        Log.i("TogetherNet", "TogetherNets details :");
                        for (HashMap<String, String> net : AvTogetherNets){
                            Log.i("TogetherNet detail", net.get("wifi_ssid") + " / " + net.get("wifi_bssid"));
                        }
                        //Aggiungo le reti alla lista globale di reti disponibili
                        //TODO -> to be tested[altro controllo] -> Immagazzinamento reti disponibili
                        GlobalApp globalApp = (GlobalApp)context.getApplicationContext();
                        globalApp.SetAvNetsList(AvTogetherNets);
                        //Controllo le reti , se una TogetherNet è migliore aggiungo e connetto
                        AvNetConnect.WifiSeekAndConnect(AvTogetherNets, context);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("TogetherNet DB error", databaseError.toString());
                }
            });
        }
    }
}
