package com.example.santorini_engine_gui;

public class HeightEvaluation implements Evaluation{
    @Override
    public double evaluate(Board b) {
        if(b.winner==null){
            return 0.1*((double)b.getActivePlayer().heightSum() - (double)b.getNextPlayer().heightSum()) ;
        } else if(b.winner == b.getActivePlayer()){
            return 1.0;
        } else {
            return -1.0;
        }
    }
}
