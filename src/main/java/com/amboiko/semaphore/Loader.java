package com.amboiko.semaphore;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.amboiko.common.Logger;
import com.amboiko.common.WorkerNames;

public class Loader implements Runnable {
    private final int wheelBarrowCapacity;
    private final int performancePerSecond;
    private final WarHouse warHouse;
    private final Semaphore loaderSemaphore;
    private final Semaphore transporterSemaphore;
    private final CountDownLatch countDownLatch;

    public Loader(
            WarHouse warHouse,
            int performancePerSecond,
            int wheelBarrowCapacity,
            Semaphore loaderSemaphore,
            Semaphore transporterSemaphore,
            CountDownLatch countDownLatch
    ) {
        this.wheelBarrowCapacity = wheelBarrowCapacity;
        this.performancePerSecond = performancePerSecond;
        this.warHouse = warHouse;
        this.loaderSemaphore = loaderSemaphore;
        this.transporterSemaphore = transporterSemaphore;
        this.countDownLatch = countDownLatch;

        new Thread(this).start();
    }

    @Override
    public void run() {
        Logger.info(WorkerNames.LOADER + " start");

        while (warHouse.getSandQuantity() >= wheelBarrowCapacity) {
            try {
                loaderSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
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

            transporterSemaphore.release();
        }

        countDownLatch.countDown();
    }
}
