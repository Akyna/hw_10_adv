package com.amboiko.wait;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.amboiko.common.Logger;
import com.amboiko.common.WorkerNames;


public class Transporter implements Runnable {
    private final WarHouse warHouseSrc;
    private final WarHouse warHouseDest;
    private final int wheelBarrowCapacity;
    private final int timeSpentOnOneSide;
    private WorkerNames wheelBarrowOwner = WorkerNames.LOADER;

    public Transporter(
            WarHouse warHouseSrc,
            int wheelBarrowCapacity,
            WarHouse warHouseDest,
            int timeSpentOnOneSide
    ) {
        this.warHouseSrc = warHouseSrc;
        this.wheelBarrowCapacity = wheelBarrowCapacity;
        this.warHouseDest = warHouseDest;
        this.timeSpentOnOneSide = timeSpentOnOneSide;

        new Thread(this).start();
    }

    @Override
    public void run() {
        Logger.info(WorkerNames.TRANSPORTER + " start");
        while (warHouseSrc.getSandQuantity() >= wheelBarrowCapacity) {
            Logger.info(WorkerNames.TRANSPORTER + " waiting for " + wheelBarrowOwner + " with a wheelBarrow");

            if (wheelBarrowOwner.equals(WorkerNames.UNLOADER)) {
                synchronized (warHouseDest) {
                    try {
                        warHouseDest.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                synchronized (warHouseSrc) {
                    try {
                        warHouseSrc.notify();
                        warHouseSrc.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


            Logger.info(WorkerNames.TRANSPORTER + " received a wheelbarrow and went to "
                    + (wheelBarrowOwner.equals(WorkerNames.LOADER) ? WorkerNames.UNLOADER : WorkerNames.LOADER)
            );

            try {
                TimeUnit.SECONDS.sleep(timeSpentOnOneSide);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (wheelBarrowOwner.equals(WorkerNames.LOADER)) {
                wheelBarrowOwner = WorkerNames.UNLOADER;
                synchronized (warHouseDest) {
                    warHouseDest.notify();
                }
            } else {
                wheelBarrowOwner = WorkerNames.LOADER;
                synchronized (warHouseSrc) {
                    warHouseSrc.notify();
                }
            }


        }
    }
}
