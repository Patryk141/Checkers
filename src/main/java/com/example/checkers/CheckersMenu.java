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

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Class opening the application menu and initializing whole app
 * @author Barłomiej Puchała Patryk Piskorski
 */
public class CheckersMenu extends Application {

  private static Stage newStage;
  /**
   * Board obj depending on the game type
   */
  private CheckersBoard board;
  private CheckersApp app;
  private static boolean Bot = true;

  public static boolean isBot() {
    return Bot;
  }

  /**
   * @param stage
   * @throws Exception
   */
  @Override
  public void start(Stage stage) throws Exception {
    initMenu();
  }

  /**
   * Method creting the menu layout, buttons and adding event handlers to these buttons for generating specific board to each game
   * @return root element which builds the whole menu
   */
  private HBox createMenu() {
    HBox root = new HBox();
    root.setPrefSize(600, 600);

    Button game1 = new Button("Warcaby rosyjskie"); // całe zaimplementowane
    Button game2 = new Button("Warcaby klasyczne");
    Button game3 = new Button("Warcaby polskie");
    Button gameBot = new Button("Gra z botem");

    EventHandler<MouseEvent> russianHandler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        app = new CheckersApp();
        Thread th = new Thread(()-> {
          try {
            board = new RussianBoard();
            app.initApp(board, newStage, Bot);
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
            app.initApp(board, newStage, Bot);
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
            app.initApp(board, newStage, Bot);
          } catch(Exception e) {
            System.out.println(e);
          }
        });
        th.start();
      }
    };
    EventHandler<MouseEvent> gameWithBot = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        Bot = false;
      }
    };

    game1.addEventFilter(MouseEvent.MOUSE_CLICKED, russianHandler); // git
    game2.addEventFilter(MouseEvent.MOUSE_CLICKED, classicHandler);
    game3.addEventFilter(MouseEvent.MOUSE_CLICKED, polishHandler);
    gameBot.addEventHandler(MouseEvent.MOUSE_CLICKED, gameWithBot);

    root.getChildren().addAll(game1, game2, game3, gameBot);
    root.setAlignment(Pos.CENTER);
    root.setSpacing(15);
    return root;
  }

  /**
   * Method creating the stage and setting the scene
   */
  public void initMenu() {
    newStage = new Stage();
    Scene scene = new Scene(createMenu());
    newStage.setTitle("Checkers Menu");
    newStage.setScene(scene);
    newStage.show();
  }
}
