package com.amboiko.wait;

import java.util.concurrent.TimeUnit;

import com.amboiko.common.Logger;
import com.amboiko.common.WorkerNames;

public class Loader implements Runnable {
    private final WarHouse warHouse;
    private final int wheelBarrowCapacity;
    private final int performancePerSecond;

    public Loader(
            WarHouse warHouse,
            int wheelBarrowCapacity,
            int performancePerSecond
    ) {
        this.wheelBarrowCapacity = wheelBarrowCapacity;
        this.performancePerSecond = performancePerSecond;
        this.warHouse = warHouse;

        new Thread(this).start();
    }

    @Override
    public void run() {
        Logger.info(WorkerNames.LOADER + " start");
        while (warHouse.getSandQuantity() >= wheelBarrowCapacity) {
            synchronized (warHouse) {
                try {
                    warHouse.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Logger.info(WorkerNames.LOADER + " start to load the wheelbarrow");

            for (int i = 0; i < wheelBarrowCapacity / performancePerSecond; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                warHouse.reduceAmountOfSandOn(performancePerSecond);
            }
            Logger.info(WorkerNames.LOADER + " loaded the wheelbarrow and handed it over to " + WorkerNames.TRANSPORTER);

            if (warHouse.getSandQuantity() >= wheelBarrowCapacity) {
                Logger.info(WorkerNames.LOADER + " waiting for the wheelbarrow to back");
            }


            synchronized (warHouse) {
                try {
                    warHouse.notify();
                    if (warHouse.getSandQuantity() < wheelBarrowCapacity) {
                        break;
                    }
                    warHouse.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
