package com.codecool.dungeoncrawl.game.map;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.game.Cell;
import com.codecool.dungeoncrawl.game.Items.*;
import com.codecool.dungeoncrawl.game.controller.LoadGameController;
import com.codecool.dungeoncrawl.game.creatures.*;
import com.codecool.dungeoncrawl.game.controller.GameController;
import com.codecool.dungeoncrawl.game.controller.NameController;
import com.codecool.dungeoncrawl.game.map.generator.MapConfig;
import com.codecool.dungeoncrawl.game.map.generator.MapGenerator;
import com.codecool.dungeoncrawl.game.map.generator.MapGeneratorImpl;
import com.codecool.dungeoncrawl.game.utils.Utils;
import com.codecool.dungeoncrawl.model.ItemModel;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;


public class MapLoader {
    private static boolean isPentagramOnMap = false;
    private static String generateMap() {
        char[] items = MapConfig.ITEMS.getItems();
        MapGenerator mapGenerator = new MapGeneratorImpl(
                MapConfig.WIDTH.getNumber(),
                MapConfig.HEIGHT.getNumber(),
                MapConfig.MAX_ROOMS.getNumber(),
                MapConfig.MIN_ROOM_XY.getNumber(),
                MapConfig.MAX_ROOM_XY.getNumber(),
                MapConfig.ROOM_OVERLAP.isRoomOverlap(),
                MapConfig.RANDOM_CONNECTIONS.getNumber(),
                MapConfig.RANDOM_SPURS.getNumber(),
                MapConfig.SKELETONS.getNumber(),
                MapConfig.VAMPIRES.getNumber(),
                MapConfig.MEDUSAS.getNumber(),
                MapConfig.NPCS.getNumber(),
                items
        );
        mapGenerator.genLevel();
        return mapGenerator.genTilesLevel();
    }

    public static GameMap loadMap(boolean isBossLevel, boolean isQuestLevel) {
        Scanner scanner;
        if (isBossLevel) {
            InputStream mapLevel = MapLoader.class.getResourceAsStream("/com/codecool/dungeoncrawl/levels/boss.txt");
            scanner = new Scanner(mapLevel);
        } else if (isQuestLevel) {
            InputStream mapLevel = MapLoader.class.getResourceAsStream("/com/codecool/dungeoncrawl/levels/quest.txt");
            scanner = new Scanner(mapLevel);
        } else {
            String mapLevel = generateMap();
            scanner = new Scanner(mapLevel);
        }
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            addWalls(cell);
                            break;
                        case '.':
                            addFloor(cell);
                            break;
                        case 's':
                            addFloor(cell);
                            map.addMonsters(new Skeleton(cell, Main.level));
                            break;
                        case 'v':
                            addFloor(cell);
                            map.addMonsters(new Vampire(cell, Main.level));
                            break;
                        case 'm':
                            addFloor(cell);
                            map.addMonsters(new Medusa(cell, Main.level));
                            break;
                        case 'n':
                            addFloor(cell);
                            if(Main.level == 1) {
                                map.addNpc(new Arthur(cell));
                                cell.setType(CellType.NPC);
                            } else if (Main.level == 2) {
                                map.addNpc(new Crudy(cell));
                                cell.setType(CellType.NPC);
                            }
                            break;
                        case 'b':
                            addFloor(cell);
                            map.addMonsters(new FinalBoss(cell));
                            break;
                        case '@':
                            addFloor(cell);
                            if (GameController.isMapLoaded) {
                                putPlayerOnMap(map, cell);
                            } else if (GameController.player == null) {
                                GameController.player = new Player(cell, NameController.getUserName());
                            } else {
                                GameController.player.setCell(cell);
                                GameController.player.getCell().setCreature(GameController.player);
                            }
                            map.setPlayer(GameController.player);
                            break;
                        case '1':
                            addFloor(cell);
                            new Sword(cell);
                            break;
                        case '2':
                            addFloor(cell);
                            new Key(cell);
                            break;
                        case '3':
                            addFloor(cell);
                            new Armor(cell);
                            break;
                        case '4':
                            addFloor(cell);
                            new Helmet(cell, "Helmet", "Test", 100);
                            break;
                        case '5':
                            addFloor(cell);
                            new Shoes(cell);
                            break;
//                        case '6':
//                            addFloor(cell);
//                            new Shield(cell);
//                            break;
//                        case '7':
//                            addFloor(cell);
//                            new Gloves(cell);
//                            break;
                        case 'H':
                            cell.setType(CellType.STAIRS);
                            break;
                        case 'd':
                            cell.setType(CellType.CLOSED_DOORS);
                            break;
                        case ',':
                            cell.setType(CellType.GOLD_1);
                            break;
                        case '/':
                            cell.setType(CellType.GOLD_2);
                            break;
                        case '\\':
                            cell.setType(CellType.GOLD_3);
                            break;
                        case '-':
                            cell.setType(CellType.GOLD_4);
                            break;
                        case '+':
                            cell.setType(CellType.GOLD_5);
                            break;
                        case '*':
                            cell.setType(CellType.GOLD_6);
                            break;
                        case '[':
                            cell.setType(CellType.BLOOD_1);
                            break;
                        case ']':
                            cell.setType(CellType.BLOOD_2);
                            break;
                        case '{':
                            cell.setType(CellType.BLOOD_3);
                            break;
                        case '6':
                            cell.setType(CellType.BLOOD_6);
                            break;
                        case '7':
                            cell.setType(CellType.BLOOD_7);
                            break;
                        case '8':
                            cell.setType(CellType.BLOOD_8);
                            break;
                        case '9':
                            cell.setType(CellType.BLOOD_9);
                            break;
                        case 'p':
                            cell.setType(CellType.PENTAGRAM);
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

    private static void putPlayerOnMap(GameMap map, Cell cell) {
        Player player = new Player(cell, LoadGameController.playerModel.getPlayerName());
        player.setAbilityPower(LoadGameController.playerModel.getAbilityPower());
        player.setBlockPower(LoadGameController.playerModel.getBlockPower());
        player.setDamage(LoadGameController.playerModel.getDamage());
        player.setMana(LoadGameController.playerModel.getMana());
        player.setExp(LoadGameController.playerModel.getExperience());
        player.setHealth(LoadGameController.playerModel.getHp());
        createItemsInstances(LoadGameController.inventory, map,player);
        System.out.println(player.getInventory());
        GameController.player = player;
    }

    public static void addItemsToInventoryEquipment(List<ItemModel> inventory, int index,Player player, Item objectToCheck){
        if(inventory.get(index).isItemWore()){
            player.addToInventory(objectToCheck);
        }else{
            player.addToEquipment(objectToCheck);
        }
    }
    public static void createItemsInstances(List<ItemModel> inventory, GameMap map,Player player){
        for(int i=0; i<inventory.size(); i++){
            String name = inventory.get(i).getItemName();
            switch(name){
                case "Armor":
                    Armor armor = new Armor(map.getCell(inventory.get(i).getX(), inventory.get(i).getY()), inventory.get(i).getItemName(), inventory.get(i).getItemDescription(),inventory.get(i).getItemValue());
                    armor.getCell().setItem(null);
                    addItemsToInventoryEquipment(inventory,i,player,armor);break;
                case "Gem":
                    Gem gem = new Gem(inventory.get(i).getItemName(), inventory.get(i).getItemDescription());
                    addItemsToInventoryEquipment(inventory,i,player,gem);break;
                case "Gloves":
                    Gloves gloves = new Gloves(map.getCell(inventory.get(i).getX(), inventory.get(i).getY()), inventory.get(i).getItemName(), inventory.get(i).getItemDescription(),inventory.get(i).getItemValue());
                    gloves.getCell().setItem(null);
                    addItemsToInventoryEquipment(inventory,i,player,gloves);break;
                case "Helmet":
                    Helmet helmet = new Helmet(map.getCell(inventory.get(i).getX(), inventory.get(i).getY()), inventory.get(i).getItemName(), inventory.get(i).getItemDescription(),inventory.get(i).getItemValue());
                    helmet.getCell().setItem(null);
                    addItemsToInventoryEquipment(inventory,i,player,helmet);break;
                case "Key":
                    Key key = new Key(map.getCell(inventory.get(i).getX(), inventory.get(i).getY()), inventory.get(i).getItemName(), inventory.get(i).getItemDescription(),inventory.get(i).getItemValue());
                    key.getCell().setItem(null);
                    addItemsToInventoryEquipment(inventory,i,player,key);break;
                case "Shield":
                    Shield  shield = new Shield(map.getCell(inventory.get(i).getX(), inventory.get(i).getY()), inventory.get(i).getItemName(), inventory.get(i).getItemDescription(),inventory.get(i).getItemValue());
                    shield.getCell().setItem(null);
                    addItemsToInventoryEquipment(inventory,i,player,shield);break;
                case "Shoes":
                    Shoes shoes = new Shoes(map.getCell(inventory.get(i).getX(), inventory.get(i).getY()), inventory.get(i).getItemName(), inventory.get(i).getItemDescription(),inventory.get(i).getItemValue());
                    shoes.getCell().setItem(null);
                    addItemsToInventoryEquipment(inventory,i,player,shoes);break;
                case "SkeletonSkull":
                    SkeletonSkull skeletonSkull = new SkeletonSkull(map.getCell(inventory.get(i).getX(), inventory.get(i).getY()), inventory.get(i).getItemName(), inventory.get(i).getItemDescription(),inventory.get(i).getItemValue());
                    skeletonSkull.getCell().setItem(null);
                    addItemsToInventoryEquipment(inventory,i,player,skeletonSkull);break;
                case "Sword":
                    Sword sword = new Sword(map.getCell(inventory.get(i).getX(), inventory.get(i).getY()), inventory.get(i).getItemName(), inventory.get(i).getItemDescription(),inventory.get(i).getItemValue());
                    sword.getCell().setItem(null);
                    addItemsToInventoryEquipment(inventory,i,player,sword);break;
            }
        }
    }
   private static void addFloor(Cell cell) {
        int pentagram = Utils.RANDOM.nextInt(50);
        switch (Utils.RANDOM.nextInt( 3)) {
            case 0:
                if (pentagram == 38 && !isPentagramOnMap && Main.level == 2){
                    cell.setType(CellType.PENTAGRAM);
                    isPentagramOnMap = true;
                } else {
                    cell.setType(CellType.FLOOR);
                }
                break;
            case 1:
                cell.setType(CellType.FLOOR_2);
                break;
            default:
                cell.setType(CellType.FLOOR_3);
                break;
        }
    }

    private static void addWalls(Cell cell) {
        switch (Utils.RANDOM.nextInt(3)) {
            case 0:
                cell.setType(CellType.WALL_2);
                break;
            case 1:
                cell.setType(CellType.WALL_3);
                break;
            default:
                cell.setType(CellType.WALL);
                break;
        }
    }

}
