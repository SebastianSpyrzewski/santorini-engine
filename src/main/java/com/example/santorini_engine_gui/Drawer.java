package com.example.santorini_engine_gui;

public interface Drawer {
    void drawBoard();
    void drawField(int i, int j);
    void drawWin(Player player);
}
