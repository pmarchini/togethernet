package com.togethernet.togethernet.WifiJumper.AvNetsList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pietr_000 on 06/11/2017.
 */

//RamStored list
    //Contiene le reti in Lista
    // TODO -> Aggiungere storage per località(GPS) per utilizzo applicazione offline
public class AvNetsList {

    public ArrayList<HashMap<String, String>> AvNetsList;

    //Costruttore
    public AvNetsList(){
        AvNetsList = new ArrayList<>();
    }

    public void clearList(){
        AvNetsList.clear();
    }

    public ArrayList<HashMap<String, String>> getAvNetsList(){
        return this.AvNetsList;
    }

    public void ClearAndSetAvNetsList(ArrayList<HashMap<String,String>> NetsListToSet){
        AvNetsList.clear();
        for (HashMap<String, String> obj : NetsListToSet){
            AvNetsList.add(obj);
        }
    }

    public void AddToAvNetsList(ArrayList<HashMap<String,String>> NetsListToAdd){
        for (HashMap<String, String> obj : NetsListToAdd){
            AvNetsList.add(obj);
        }
    }
}
