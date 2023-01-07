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

public class CheckersMenu extends Application {

  Stage newStage;

  public CheckersMenu() {

  }

  @Override
  public void start(Stage stage) throws Exception {
    initMenu();
  }

  private HBox createMenu() {
    HBox root = new HBox();
    root.setPrefSize(450, 120);
    CheckersApp app = new CheckersApp();

    Button game1 = new Button("Warcaby klasyczne");
    Button game2 = new Button("Warcaby polskie");
    Button game3 = new Button("Warcaby rosyjskie");

    EventHandler<MouseEvent> eventHandler1 = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        Thread th = new Thread(()-> {
          try {
            int Size = 8;
            app.initApp(Size, newStage, true);
          } catch(Exception e) {
            System.out.println(e);
          }
        });
        th.start();
      }
    };
    EventHandler<MouseEvent> eventHandler2 = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
          Thread th = new Thread(()-> {
            try {
              int Size = 10;
              app.initApp(Size, newStage, true);
            } catch(Exception e) {
              System.out.println(e);
            }
          });
          th.start();
      }
    };
    EventHandler<MouseEvent> eventHandler3 = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        Thread th = new Thread(()-> {
          try {
            int Size = 8;
            app.initApp(Size, newStage, false);
          } catch(Exception e) {
            System.out.println(e);
          }
        });
        th.start();
      }
    };

    game1.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler1);
    game2.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler2);
    game3.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler3);
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
