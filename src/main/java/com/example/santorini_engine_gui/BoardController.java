package com.example.santorini_engine_gui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class BoardController implements Drawer {
    private Board board;
    private boolean paused;
    @FXML
    private Label welcomeText;
    @FXML
    private Canvas boardCanvas;
    @FXML
    private Button helloButton;
    @FXML
    protected void onHelloButtonClick() {
        if(!paused){
            board.timeline.stop();
            helloButton.setText("restart");
            paused = true;
        } else{
            board.timeline.play();
            helloButton.setText("pause");
            paused = false;
        }

    }

    private Field chosenField;

    public void setBoard(Board board) {
        this.board = board;
    }

    public void init(){
        drawBoard();
        boardCanvas.setOnMouseClicked(e->{
            GraphicsContext gc = boardCanvas.getGraphicsContext2D();
            int i = (int)(e.getX()-2)/100;
            int j = (int)(e.getY()-2)/100;
            Field field = board.map[i][j];
            if(field.worker!=null){
                if(chosenField!=null) {
                    gc.setFill(Color.LIGHTGRAY);
                    gc.fillRect(chosenField.x * 100 + 2, chosenField.y * 100 + 2, 100, 100);
                    drawField(chosenField.x, chosenField.y);
                }
                chosenField=field;
                gc.setFill(Color.BLACK);
                gc.fillRect(chosenField.x*100+2, chosenField.y*100+2, 100, 100);
                drawField(chosenField.x, chosenField.y);
            } else {
                if(chosenField!=null) {
                    Player player = chosenField.worker;
                    player.moveWorker(chosenField, field);
                    drawField(field.x, field.y);
                    gc.setFill(Color.LIGHTGRAY);
                    gc.fillRect(chosenField.x * 100 + 2, chosenField.y * 100 + 2, 100, 100);
                    drawField(chosenField.x, chosenField.y);
                    chosenField = null;
                    if(field.height==3){
                        welcomeText.setText("Player "+player.name+" won");
                    }
                } else {
                    field.height+=1;
                    drawField(field.x, field.y);
                    board.nextPlayer();
                    board.play();
                }
            }
        });
    }
    @Override
    public void drawBoard(){
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, 502, 502);
        gc.setLineWidth(8);
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                drawField(i, j);
            }
        }
    }
    @Override
    public void drawField(int i, int j){
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();
        gc.setLineWidth(8);
        Field field = board.map[i][j];
        double x = i*100+2;
        double y = j*100+2;
        gc.setFill(Color.LAWNGREEN);
        gc.fillRect(x+2, y+2, 96, 96);
        if(field.height>0){
            gc.setFill(Color.DARKGRAY);
            gc.fillRect(x+10, y+10, 80, 80);
            gc.setFill(Color.WHITE);
            gc.fillRect(x+12, y+12, 76, 76);
        }
        if(field.height>1){
            gc.setFill(Color.DARKGRAY);
            gc.fillRect(x+16, y+16, 68, 68);
            gc.setFill(Color.WHITE);
            gc.fillRect(x+18, y+18, 64, 64);
        }
        if(field.height>2){
            gc.setFill(Color.DARKGRAY);
            gc.fillOval(x+22, y+22, 56, 56);
            gc.setFill(Color.WHITE);
            gc.fillOval(x+24, y+24, 52, 52);
        }
        if(field.height>3){
            gc.setFill(Color.BLUE);
            gc.fillOval(x+24, y+24, 52, 52);
        }
        if(field.worker != null){
            gc.setStroke(field.worker.color);
            gc.strokeLine(x+30, y+30, x+70, y+70);
            gc.strokeLine(x+30, y+70, x+70, y+30);
        }
    }

    @Override
    public void drawWin(Player player) {
        welcomeText.setText("Player "+player.name+" won");
    }
}