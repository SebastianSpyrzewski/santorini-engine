package com.example.santorini_engine_gui;

import java.util.List;

public class SpecialMinimaxEngine implements Engine {
    Evaluation evaluation;
    int depth;
    SpecialMinimaxEngine(Evaluation e, int depth){
        this.evaluation = e;
        this.depth = depth;
    }
    double minimax(Board b, int depth, boolean deeper){
        if(b.winner != null)
            return -1.0;
        if(depth == 0)
            return evaluation.evaluate(b);
        Player player = b.getActivePlayer();
        List<Move> l = player.getPossibleMoves(b);
        double max = -1.0;
        for(Move m : l){
            int initialHeight = player.maxHeight();
            b.makeMove(m, false);
            int finalHeight = player.maxHeight();
            double val;
            if(initialHeight<finalHeight && deeper && depth == 1){
                val = -minimax(b, depth, false);
            } else {
                val = -minimax(b, depth - 1, deeper);
            }
            max = Math.max(max, val);
            b.reverseMove(m, false);
        }
        return max;
    }
    @Override
    public boolean makeMove(Board b) {
        List<Move> l = b.getActivePlayer().getPossibleMoves(b);
        if(l.isEmpty())
            return false;
        double min = 1.0;
        Move mMin = l.get(0);
        for(Move m : l){
            b.makeMove(m, false);
            double val = minimax(b, depth, true);
            if(val<min){
                min = val;
                mMin = m;
            }
            b.reverseMove(m, false);
        }
        b.makeMove(mMin, true);
        return true;
    }
}
