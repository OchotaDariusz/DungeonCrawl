package com.codecool.dungeoncrawl.game.Items;


import com.codecool.dungeoncrawl.game.Cell;
import com.codecool.dungeoncrawl.game.Drawable;

public class Item implements Drawable {
    private Cell cell;
    private String itemName;
    private String itemDescription;
    private int itemValue;

    public Item(Cell cell, String itemName, String itemDescription, int itemValue) {
        this.cell = cell;
        this.cell.setItem(this);
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemValue = itemValue;
    }


    public Item(String itemName, String itemDescription){
        this.itemName = itemName;
        this.itemDescription = itemDescription;
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getItemValue() {
        return itemValue;
    }

    public void setItemValue(int itemValue) {
        this.itemValue = itemValue;
    }

    @Override
    public String getTileName() {
        return null;
    }
}
