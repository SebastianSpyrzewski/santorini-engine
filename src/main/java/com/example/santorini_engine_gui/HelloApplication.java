package com.example.santorini_engine_gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        BoardController bc = fxmlLoader.getController();
        Board b = new Board();
        bc.setBoard(b);
        bc.init();
        b.drawer = bc;
        b.players[0].engine = new MinimaxEngine(new HeightEvaluation(), 3);
        b.players[0].name = "Minimax1";
        //b.players[1].engine = new MinimaxEngine(new HeightEvaluation(), 3);
        //b.players[1].name = "Minimax2";
        stage.show();
        b.play();
    }

    public static void main(String[] args) {
        launch();
    }
}