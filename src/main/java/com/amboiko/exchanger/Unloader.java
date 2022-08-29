package com.amboiko.exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

import com.amboiko.common.Logger;
import com.amboiko.common.WorkerNames;

public class Unloader implements Runnable {
    private final WarHouse warHouseSrc;
    private final int wheelBarrowCapacity;
    private final int performancePerSecond;
    private final WarHouse warHouseDest;
    private final Exchanger<Integer> transporterExchanger;
    private final Exchanger<Integer> unloaderExchanger;

    public Unloader(
            WarHouse warHouseSrc,
            int wheelBarrowCapacity,
            WarHouse warHouseDest,
            int performancePerSecond,
            Exchanger<Integer> transporterExchanger,
            Exchanger<Integer> unloaderExchanger
    ) {
        this.warHouseSrc = warHouseSrc;
        this.wheelBarrowCapacity = wheelBarrowCapacity;
        this.warHouseDest = warHouseDest;
        this.performancePerSecond = performancePerSecond;
        this.transporterExchanger = transporterExchanger;
        this.unloaderExchanger = unloaderExchanger;
        new Thread(this).start();
    }

    @Override
    public void run() {
        Logger.info(WorkerNames.UNLOADER + " start");
        while (true) {
            Logger.info(WorkerNames.UNLOADER + " waiting for " + WorkerNames.TRANSPORTER + " with a wheelBarrow");

            try {
                unloaderExchanger.exchange(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
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

            if (warHouseSrc.getSandQuantity() < wheelBarrowCapacity) {
                break;
            }

            try {
                transporterExchanger.exchange(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
