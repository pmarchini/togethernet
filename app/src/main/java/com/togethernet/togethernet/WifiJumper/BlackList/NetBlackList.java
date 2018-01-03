package com.togethernet.togethernet.WifiJumper.BlackList;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
/**
 * Created by pietr_000 on 04/11/2017.
 */


import com.togethernet.togethernet.WifiJumper.BlackList.inbloom.*;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

//Gestione della BlackList
    //Escludo una wifi che da errori ripetutamente fino al prossimo riavvio dell'app
    // TODO -> Creare BlackList usando database SQLite
    //Eventuale TODO -> Aggiungere una gestione remota della BlackList rimuovendo l'AP dopo un tot di errori riscontrati dai vari utenti
public class NetBlackList {

    //BloomFilter by https://github.com/EverythingMe/inbloom
    // TODO -> Assegnare crediti al sopracitato -> libreria integrata all'interno del progetto
    public BloomFilter BloomFilter;
    public ArrayList<HashMap<String, String>> BlackList;
    protected SQLiteDatabase db;
    protected boolean isReady;

    public NetBlackList(Context context){

        isReady = false;
        BlackList = new ArrayList<>();
        //Inizializzo il BloomFilter
        BloomFilter = new BloomFilter(100, 0.01);
        //Apro o creo il database
        db = openOrCreateDatabase( context.getFilesDir() + "/TogetherNetDB.db", null );
        //Creo, se non esiste, tabella blackList
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                    "blackList ( "+
                            "BSSID VARCHAR(100), " +
                            "SSID VARCHAR(100), " +
                            "PRIMARY KEY (BSSID) ) "
                    );
    }

    //Potenzialmente richiede tempo
    public void fillBloomFilter(){
        Cursor cursor =  this.db.rawQuery("SELECT * FROM blackList", null);
        if (cursor.moveToFirst()) {
            do {
                this.BloomFilter.add(cursor.getString(0)); //Aggiungo BSSID
            } while (cursor.moveToNext());
        }
    }

    public void fillBlackListArray(){
        HashMap<String, String> temp = new HashMap<>();
        Cursor cursor =  this.db.rawQuery("SELECT * FROM blackList", null);
        if (cursor.moveToFirst()) {
            do {
                temp.clear();
                temp.put("BSSID", cursor.getString(0));
                temp.put("SSID", cursor.getString(1));
                this.BlackList.add(temp);
            } while (cursor.moveToNext());
        }
    }


    public boolean blackListVerifyBSSID (String BSSID){
        //Controllo il risultato del BloomFilter
        //Se statisticamente contiene l'elemento controllo il db
        if(this.BloomFilter.contains(BSSID)){
            Cursor cursor =  this.db.rawQuery("SELECT * FROM blackList WHERE BSSID = '" + BSSID + "'", null);
            return cursor.getCount() == 0;
        }else{
            return false;
        }
    }

    //TODO -> Inserire controllo dell'errore
    public void addNetToBlackList(String BSSID, String SSID){
        db.execSQL("INSERT or REPLACE INTO blackList (BSSID, SSID)" +
                    "VALUES(\"" + BSSID + "\",\"" + SSID + "\")"
                );
    }

}
