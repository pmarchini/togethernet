package com.togethernet.togethernet.WifiJumper.BlackList.AppBlackListGlobalizer;

import android.content.Context;

import com.togethernet.togethernet.WifiJumper.BlackList.NetBlackList;

/**
 * Created by pietr_000 on 05/11/2017.
 */

//Globalizzo classe BlackList
    //Posso accedere a questa classe da qualsiasi punto dell'applicazione
    //Evito abuso di memoria e di tempo
public class GlobalBlackList{

    public NetBlackList GlobalBlackList;

    public GlobalBlackList(Context context){
        //Istanzio la BlackList
        this.GlobalBlackList = new NetBlackList(context);
        //Estraggo i valori salvati
        this.GlobalBlackList.fillBloomFilter();
        //Estraggo l'array dal db
        this.GlobalBlackList.fillBlackListArray();
    }

}
