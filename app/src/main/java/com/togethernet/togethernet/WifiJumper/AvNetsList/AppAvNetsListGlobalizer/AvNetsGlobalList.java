package com.togethernet.togethernet.WifiJumper.AvNetsList.AppAvNetsListGlobalizer;

import com.togethernet.togethernet.WifiJumper.AvNetsList.AvNetsList;

/**
 * Created by pietr_000 on 06/11/2017.
 */

public class AvNetsGlobalList {
    public AvNetsList GlobalAvNets;

    public AvNetsGlobalList(){
        //Istanzio la AvNetsList
        this.GlobalAvNets = new AvNetsList();
    }
}
