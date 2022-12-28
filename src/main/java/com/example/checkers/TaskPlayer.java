package com.example.checkers;

import javafx.concurrent.Task;

public class TaskPlayer extends Task<Player> {
  private final Player player;

  public TaskPlayer(Player player) {
    this.player = player;
  }

  @Override
  protected Player call() throws Exception {
    return player;
  }
}
