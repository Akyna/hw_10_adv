package com.amboiko.semaphore;

import com.amboiko.common.Logger;

public class WarHouse {
    private int sandQuantity;
    private final String name;

    public WarHouse(String name, int sandQuantity) {
        this.sandQuantity = sandQuantity;
        this.name = name;
    }

    public void increaseAmountOfSandOn(int quantity) {
        sandQuantity += quantity;
        Logger.info(sandQuantity + " kg of sand appeared in warehouse: " + name);
    }

    public void reduceAmountOfSandOn(int quantity) {
        sandQuantity -= quantity;
        Logger.info(sandQuantity + " kg of sand remained in warehouse: " + name);
    }

    public int getSandQuantity() {
        return sandQuantity;
    }
}
