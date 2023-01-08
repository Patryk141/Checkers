package com.example.checkers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CheckersMenu extends Application {

  private static Stage newStage;
  private CheckersBoard board;
  private CheckersApp app;

  @Override
  public void start(Stage stage) throws Exception {
    initMenu();
  }

  private HBox createMenu() {
    HBox root = new HBox();
    root.setPrefSize(600, 600);

    Button game1 = new Button("Warcaby rosyjskie"); // całe zaimplementowane
    Button game2 = new Button("Warcaby klasyczne");
    Button game3 = new Button("Warcaby polskie");

    EventHandler<MouseEvent> russianHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        app = new CheckersApp();
        Thread th = new Thread(()-> {
          try {
            board = new RussianBoard();
            app.initApp(board, newStage);
          } catch(Exception e) {
            System.out.println(e);
          }
        });
        th.start();
      }
    };

    EventHandler<MouseEvent> classicHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        app = new CheckersApp();
        Thread th = new Thread(()-> {
          try {
            board = new ClassicBoard();
            app.initApp(board, newStage);
          } catch(Exception e) {
            System.out.println(e);
          }
        });
        th.start();
      }
    };
    EventHandler<MouseEvent> polishHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        app = new CheckersApp();
        Thread th = new Thread(()-> {
          try {
            board = new PolishBoard();
            app.initApp(board, newStage);
          } catch(Exception e) {
            System.out.println(e);
          }
        });
        th.start();
      }
    };

    game1.addEventFilter(MouseEvent.MOUSE_CLICKED, russianHandler); // git
    game2.addEventFilter(MouseEvent.MOUSE_CLICKED, classicHandler);
    game3.addEventFilter(MouseEvent.MOUSE_CLICKED, polishHandler);

    root.getChildren().addAll(game1, game2, game3);
    root.setAlignment(Pos.CENTER);
    root.setSpacing(15);
    return root;
  }

  public void initMenu() {
    newStage = new Stage();
    Scene scene = new Scene(createMenu());
    newStage.setTitle("Checkers Menu");
    newStage.setScene(scene);
    newStage.show();
  }
}
