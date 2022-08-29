package com.amboiko.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.amboiko.common.Logger;
import com.amboiko.common.WorkerNames;

public class Unloader implements Runnable {
    private final int performancePerSecond;
    private final WarHouse warHouse;
    private final Semaphore transporterSemaphore;
    private final Semaphore unloaderSemaphore;
    private volatile boolean isWorkGoing = true;
    private final int wheelBarrowCapacity;

    public Unloader(
            WarHouse warHouse,
            int performancePerSecond,
            int wheelBarrowCapacity,
            Semaphore transporterSemaphore,
            Semaphore unloaderSemaphore
    ) {
        this.performancePerSecond = performancePerSecond;
        this.wheelBarrowCapacity = wheelBarrowCapacity;
        this.warHouse = warHouse;
        this.transporterSemaphore = transporterSemaphore;
        this.unloaderSemaphore = unloaderSemaphore;
        new Thread(this).start();
    }

    @Override
    public void run() {
        Logger.info(WorkerNames.UNLOADER + " start");
        while (isWorkGoing) {
            Logger.info(WorkerNames.UNLOADER + " waiting for " + WorkerNames.TRANSPORTER + " with a wheelBarrow");
            try {
                unloaderSemaphore.acquire();
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
                warHouse.increaseAmountOfSandOn(performancePerSecond);
            }
            transporterSemaphore.release();
        }
    }

    public void stopWork() {
        isWorkGoing = false;
    }
}
