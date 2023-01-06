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
import javafx.stage.Stage;

public class CheckersMenu extends Application implements Runnable {

  Stage newStage;

  public CheckersMenu() {

  }

  @Override
  public void start(Stage stage) throws Exception {
    initMenu();
  }

  private HBox createMenu() {
    HBox root = new HBox();
    root.setPrefSize(600, 600);

    Button game1 = new Button("Warcaby klasyczne");
    Button game2 = new Button("Warcaby polskie");
    Button game3 = new Button("Warcaby tureckie");

    EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent mouseEvent) {
        CheckersApp app = new CheckersApp();
        Thread th = new Thread(()-> {
          try {
            int SIZE = 10;
            app.initApp(SIZE, newStage);
          } catch(Exception e) {
            System.out.println(e);
          }
        });
        th.start();
//        CheckersApp app2 = new CheckersApp();
//        Thread th2 = new Thread(()-> {
//          try {
//            app2.start(stage2);
//          } catch(Exception e) {
//            System.out.println(e);
//          }
//        });
//        th2.start();

      }
    };

    EventHandler<MouseEvent> eventHandler2 = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        Stage stage2 = new Stage();

          CheckersApp app = new CheckersApp();

          Thread th = new Thread(()-> {
            try {
              app.start(stage2);
            } catch(Exception e) {
              System.out.println(e);
            }
          });
          th.start();
      }
    };

    game1.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    game2.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler2);

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

  @Override
  public void run() {

  }
}
