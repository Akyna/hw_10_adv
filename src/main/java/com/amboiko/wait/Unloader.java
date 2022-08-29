package com.amboiko.wait;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.amboiko.common.Logger;
import com.amboiko.common.WorkerNames;

public class Unloader implements Runnable {
    private final WarHouse warHouseSrc;
    private final int wheelBarrowCapacity;
    private final int performancePerSecond;
    private final WarHouse warHouseDest;

    public Unloader(
            WarHouse warHouseSrc,
            int wheelBarrowCapacity,
            WarHouse warHouseDest,
            int performancePerSecond
    ) {
        this.warHouseSrc = warHouseSrc;
        this.wheelBarrowCapacity = wheelBarrowCapacity;
        this.warHouseDest = warHouseDest;
        this.performancePerSecond = performancePerSecond;
        new Thread(this).start();
    }

    @Override
    public void run() {
        Logger.info(WorkerNames.UNLOADER + " start");
        while (warHouseSrc.getSandQuantity() >= wheelBarrowCapacity) {
            Logger.info(WorkerNames.UNLOADER + " waiting for " + WorkerNames.TRANSPORTER + " with a wheelBarrow");
            synchronized (warHouseDest) {
                try {
                    warHouseDest.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Logger.info(WorkerNames.UNLOADER + " received a wheelbarrow and start to unload");
            for (int i = 0; i < wheelBarrowCapacity / performancePerSecond; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                warHouseDest.increaseAmountOfSandOn(performancePerSecond);
            }
            synchronized (warHouseDest) {
                warHouseDest.notify();
            }
        }
    }

}
