package com.codecool.dungeoncrawl.game.creatures;

import com.codecool.dungeoncrawl.game.Cell;
import com.codecool.dungeoncrawl.game.Drawable;

public abstract class Npc extends Creature {
    protected Cell cell;
    public Npc(Cell cell) {
        super(cell);
    }
    @Override
    public boolean checkIfMovePossible(int x, int y) {
        return false;
    }

    public void npcTalk(){
        System.out.println("npc");
    };


}