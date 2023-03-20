package com.engeto.florist;

public class Flower {
    private static int idCounter = 0;
    private String color;
    private String description;
    private String flowerName;
    private int id;
    private boolean isPoisonous;

    public Flower(String flowerName, String color, String description, boolean isPoisonous) {
        id = ++idCounter;
        this.flowerName = flowerName;
        this.color = color;
        this.description = description;
        this.isPoisonous = isPoisonous;
    }

    //region Getters and setters
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFlowerName() {
        return flowerName;
    }

    public void setFlowerName(String flowerName) {
        this.flowerName = flowerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPoisonous() {
        return isPoisonous;
    }

    public void setPoisonous(boolean poisonous) {
        isPoisonous = poisonous;
    }
    //endregion
}
