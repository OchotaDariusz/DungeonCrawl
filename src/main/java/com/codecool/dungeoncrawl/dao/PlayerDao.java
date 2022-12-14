package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PlayerModel;

import java.util.List;

public interface PlayerDao {
    void add(PlayerModel player);
    void update(PlayerModel player);
    PlayerModel get(String player_name);
    List<PlayerModel> getAll();
    List<String>getSaveNames();
}
