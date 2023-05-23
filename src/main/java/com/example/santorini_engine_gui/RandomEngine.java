package com.example.santorini_engine_gui;

public class RandomEngine implements Engine{

    @Override
    public boolean makeMove(Board b) {
        Player player = b.getActivePlayer();
        Move m = player.getRandomMove(b);
        if(m==null)
            return false;
        b.makeMove(m, true);
        return true;
    }
}
