package com.example.santorini_engine_gui;

import java.util.ArrayList;
import java.util.List;

public class MonteCarloEngine implements Engine{
    static class Node {
        List<Move> moves;
        List<Node> children;
        int visited;
        int won;
        Player player;
        Node(Player player){
            this.player=player;
            this.visited=0;
            this.won=0;
        }
        Player explore(Board b){
            visited += 1;
            if(b.winner!=null){
                if(player == b.winner)
                    won+=1;
                return b.winner;
            }
            if(moves==null || moves.isEmpty()){
                moves = player.getPossibleMoves(b);
                int n = moves.size();
                if(n==0){
                    return b.getPreviousPlayer();
                }
                children = new ArrayList<>(n);
                for(int i=0; i<n; i++){
                    children.add(new Node(b.getNextPlayer()));
                }
                int i = (int)(Math.random()*n);
                Node child = children.get(i);
                child.visited += 1;
                Move m = moves.get(i);
                b.makeMove(m, false);
                Player winner = b.simulateRandom();
                b.reverseMove(m, false);
                if(winner == child.player){
                    child.won+=1;
                } else if(winner == player){
                    won += 1;
                }
                return winner;
            } else {
                int i = findBestUCT();
                Move m = moves.get(i);
                b.makeMove(m, false);
                Player winner = children.get(i).explore(b);
                if(winner == player) {
                    won+=1;
                }
                b.reverseMove(m, false);
                return winner;
            }
        }
        double UCT(double parentVisited){
            if(visited==0){
                return Double.MAX_VALUE;
            } else {
                return (double)won/(double)visited + Math.sqrt(2.0* Math.log(parentVisited)/(double)visited);
            }
        }
        int findBestUCT(){
            double max = 0;
            int argmax = 0;
            int n = children.size();
            for(int i=0; i<n; i++){
                double uct = children.get(i).UCT(visited);
                if(uct>max){
                    max=uct;
                    argmax = i;
                }
            }
            return argmax;
        }
        Move findBestMove(){
            double min = 0;
            int argmin = 0;
            int n = children.size();
            for(int i=0; i<n; i++){
                Node child = children.get(i);
                //double score = (double)child.won/(double)child.visited;
                double score = child.visited;
                if(score<min){
                    min=score;
                    argmin = i;
                }
            }
            return moves.get(argmin);
        }
    }
    @Override
    public boolean makeMove(Board b) {
        Node root = new Node(b.getActivePlayer());
        if(root.player.getPossibleMoves(b).isEmpty())
            return false;
        long end = System.currentTimeMillis() + 3000;
        while(System.currentTimeMillis()<end){
            root.explore(b);
        }
        Move m = root.findBestMove();
        b.makeMove(m, true);
        m.print();
        return true;
    }
}
