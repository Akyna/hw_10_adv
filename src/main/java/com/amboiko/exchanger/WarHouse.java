package com.amboiko.exchanger;

import com.amboiko.common.Logger;

public class WarHouse {
    private volatile int sandQuantity;
    private final String name;

    public WarHouse(String name, int sandQuantity) {
        this.sandQuantity = sandQuantity;
        this.name = name;
    }

    public synchronized void increaseAmountOfSandOn(int quantity) {
        sandQuantity += quantity;
        Logger.info(sandQuantity + " kg of sand appeared in warehouse: " + name);
    }

    public synchronized void reduceAmountOfSandOn(int quantity) {
        sandQuantity -= quantity;
        Logger.info(sandQuantity + " kg of sand remained in warehouse: " + name);
    }

    public synchronized int getSandQuantity() {
        return sandQuantity;
    }
}
