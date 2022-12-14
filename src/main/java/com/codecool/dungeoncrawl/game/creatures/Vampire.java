package com.codecool.dungeoncrawl.game.creatures;

import com.codecool.dungeoncrawl.game.Cell;
import com.codecool.dungeoncrawl.game.map.GameMap;

import java.util.Random;

public class Vampire extends Monster {

    private final float drainageOfLife = 0.15f;


    public Vampire(Cell cell, int level) {
        super(cell, false);
        super.setHealth((int) (1000 * (1 + 0.25*level)));
        super.setDamage((int) (10 * (1 + 0.25*level)));
        super.setAbilityPower((int) (20 * (1 + 0.25*level)));
        super.setBlockPower((int) (50 * (1 + 0.25*level)));
        super.setExp((int) (250 * (1 + 0.25*level)));
        super.setAttacked(false);
    }

    @Override
    public void lootItems() {

    }


    @Override
    public String getTileName() {
        if (this.isAttacked()){
            return "vampire_attacked";
        } else {
            return "vampire";
        }
    }
    @Override
    public void specialAbility(GameMap map) {


    }

}