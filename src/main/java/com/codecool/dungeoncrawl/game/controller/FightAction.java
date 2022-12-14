package com.codecool.dungeoncrawl.game.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum FightAction {
    ATTACK, BLOCK, SPECIAL;

    private static final Map<FightAction, List<FightAction>> winMap = new HashMap<>();

    static {
        List<FightAction> attack = new ArrayList<>();
        List<FightAction> block = new ArrayList<>();
        List<FightAction> allActions = new ArrayList<>();
        attack.add(ATTACK);
        block.add(BLOCK);
        allActions.add(ATTACK);
        allActions.add(BLOCK);

        winMap.put(ATTACK, allActions);
        winMap.put(SPECIAL, allActions);
        winMap.put(BLOCK, attack);
    }

    ActionResult checkAgainst(FightAction action) {
        if (this == action)
            return ActionResult.DRAW;
        return winMap.get(this).contains(action) ? ActionResult.WIN : ActionResult.LOSE;
    }

    public enum ActionResult {
        WIN, LOSE, DRAW
    }
}
