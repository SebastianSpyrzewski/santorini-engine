package com.example.santorini_engine_gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Stack;

public class Board {
    Field [][] map;
    Player [] players;
    int playersNumber;
    int playerToMove;
    Player winner;
    static String[] names = {"Red", "Purple", "Green", "Orange"};
    static Color[] colors = {Color.RED, Color.PURPLE, Color.GREEN, Color.ORANGE};
    Drawer drawer;
    Timeline timeline;

    private void setup(){
        map = new Field[5][5];
        players = new Player[playersNumber];
        playerToMove=0;
        for(int i = 0; i< playersNumber; i++){
            players[i] = new Player(names[i], colors[i]);
        }
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                map[i][j] = new Field(i, j);
            }
        }
        startPosition();
    }

    private void startPosition(){
        if(this.playersNumber ==2){
            putWorker(0, 0, 1, 1);
            putWorker(0, 1, 3, 3);
            putWorker(1, 0, 1, 3);
            putWorker(1, 1, 3, 1);
        }
    }

    private void putWorker(int player, int worker, int x, int y){
        players[player].workers[worker] = map[x][y];
        map[x][y].worker = players[player];
    }

    void setDrawer(Drawer drawer){
        this.drawer=drawer;
    }

    Board(int players_number){
        this.playersNumber =players_number;
        setup();
    }
    Board(){
        this.playersNumber =2;
        setup();
    }
    Player getActivePlayer(){
        return players[playerToMove];
    }
    Player getPreviousPlayer(){
        return players[(playerToMove+playersNumber-1)%playersNumber];
    }
    Player getNextPlayer(){
        return players[(playerToMove+1)%playersNumber];
    }
    void nextPlayer(){
        playerToMove = (playerToMove+1)%playersNumber;
    }
    void previousPlayer(){
        playerToMove = (playerToMove+playersNumber-1)%playersNumber;
    }
    void play(){
         this.timeline = new Timeline(new KeyFrame(Duration.millis(1200), a -> {
             if(getActivePlayer().engine==null || winner!=null)
                 this.timeline.stop();
             else{
                 boolean moved = getActivePlayer().engine.makeMove(this);
                 if(!moved){
                     winner = getPreviousPlayer();
                     drawer.drawWin(winner);
                 }
             }
        }));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
         this.timeline.play();
    }
    void makeMove(Move m, boolean display){
        Player player = getActivePlayer();
        Field f = player.workers[m.worker];
        Field f1 = map[f.x+m.moveX][f.y+m.moveY];
        player.moveWorker(f, f1);
        if(display){
            drawer.drawField(f.x, f.y);
            drawer.drawField(f1.x, f1.y);
        }
        nextPlayer();
        Field fb = map[f1.x+ m.buildX][f1.y+ m.buildY];
        fb.height++;
        if(display)
            drawer.drawField(fb.x, fb.y);
        if(f1.height==3){
            winner = player;
            if(display)
                drawer.drawWin(player);
        }
    }

    void reverseMove(Move m, boolean display){
        previousPlayer();
        winner = null;
        Player player = getActivePlayer();
        Field f1 = player.workers[m.worker];
        Field f = map[f1.x-m.moveX][f1.y-m.moveY];
        player.moveWorker(f1, f);
        Field fb = map[f1.x+ m.buildX][f1.y+ m.buildY];
        fb.height--;
        if(display){
            drawer.drawField(f.x, f.y);
            drawer.drawField(f1.x, f1.y);
            drawer.drawField(fb.x, fb.y);
        }
    }

    Player simulateRandom(){
        Player outcome;
        Stack<Move> S = new Stack<>();
        while(winner == null){
            Move m = getActivePlayer().getRandomMove(this);
            if(m==null){
                winner = getPreviousPlayer();
                break;
            }
            makeMove(m, false);
            S.push(m);
        }
        outcome = winner;
        while(!S.isEmpty()){
            Move m = S.pop();
            reverseMove(m, false);
        }
        return outcome;
    }
}
