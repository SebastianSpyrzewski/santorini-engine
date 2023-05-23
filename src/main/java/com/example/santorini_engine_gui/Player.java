package com.example.santorini_engine_gui;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Player {
    Field [] workers;
    String name;
    Color color;
    Engine engine=null;
    Player(String name, Color color){
        this.name = name;
        this.color = color;
        workers = new Field[2];
    }
    void moveWorker(Field from, Field to){
        if(workers[0]==from){
            workers[0]=to;
        }
        if(workers[1]==from){
            workers[1]=to;
        }
        from.worker = null;
        to.worker = this;
    }
    List<Move> getPossibleMoves(Board board){
        List<Move> l = new ArrayList<>();
        for(int worker=0; worker<2; worker++){
            Field f = workers[worker];
            int x = f.x;
            int y = f.y;
            for(int moveX=-1; moveX<2; moveX++){
                if(x+moveX<0)
                    continue;
                if (x+moveX>4)
                    continue;
                for(int moveY=-1; moveY<2; moveY++){
                    if(y+moveY<0)
                        continue;
                    if (y+moveY>4)
                        continue;
                    Field f1 = board.map[x+moveX][y+moveY];
                    if(f1.height>f.height+1 || f1.worker!= null){
                        continue;
                    }
                    l.add(new Move(worker, moveX, moveY, -moveX, -moveY));
                    for(int buildX=-1; buildX<2; buildX++){
                        if(x+moveX+buildX<0)
                            continue;
                        if (x+moveX+buildX>4)
                            continue;
                        for(int buildY=-1; buildY<2; buildY++){
                            if(y+moveY+buildY<0)
                                continue;
                            if (y+moveY+buildY>4)
                                continue;
                            if(buildX==0 && buildY==0)
                                continue;
                            Field fb = board.map[x+moveX+buildX][y+moveY+buildY];
                            if(fb.height<4 && fb.worker== null){
                                Move m = new Move(worker, moveX, moveY, buildX, buildY);
                                l.add(m);
                            }
                        }
                    }
                }
            }
        }
        return l;
    }
    Move getRandomMove(Board board){
        List<Move> l = getPossibleMoves(board);
        int n = l.size();
        if(n==0)
            return null;
        int i = (int)(Math.random()*n);
        return l.get(i);
    }
    int heightSum(){
        return workers[0].height+workers[1].height;
    }
    int maxHeight() { return Math.max(workers[0].height, workers[1].height);}
}
