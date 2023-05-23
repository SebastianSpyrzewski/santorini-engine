package com.example.santorini_engine_gui;

public class Move {
    int worker;
    int moveX;
    int moveY;
    int buildX;
    int buildY;

    public Move(int worker, int moveX, int moveY, int buildX, int buildY) {
        this.worker = worker;
        this.moveX = moveX;
        this.moveY = moveY;
        this.buildX = buildX;
        this.buildY = buildY;
    }
    public void print(){
        System.out.println(worker+ " "+moveX+ " "+moveY+ " "+buildX+ " "+buildY+ " ");
    }
}
