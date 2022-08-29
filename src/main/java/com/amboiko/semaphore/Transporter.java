package com.amboiko.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.amboiko.common.Logger;
import com.amboiko.common.WorkerNames;

public class Transporter implements Runnable {
    private final int timeSpentOnOneSide;
    private final Semaphore loaderSemaphore;
    private final Semaphore transporterSemaphore;
    private final Semaphore unloaderSemaphore;
    private volatile boolean isWorkGoing = true;
    private WorkerNames wheelBarrowOwner = WorkerNames.LOADER;

    public Transporter(
            int timeSpentOnOneSide,
            Semaphore loaderSemaphore,
            Semaphore transporterSemaphore,
            Semaphore unloaderSemaphore
    ) {
        this.timeSpentOnOneSide = timeSpentOnOneSide;
        this.loaderSemaphore = loaderSemaphore;
        this.transporterSemaphore = transporterSemaphore;
        this.unloaderSemaphore = unloaderSemaphore;

        new Thread(this).start();
    }

    @Override
    public void run() {
        Logger.info(WorkerNames.TRANSPORTER + " start");
        while (isWorkGoing) {
            Logger.info(WorkerNames.TRANSPORTER + " waiting for " + wheelBarrowOwner + " with a wheelBarrow");
            try {
                transporterSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
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
                unloaderSemaphore.release();
            } else {
                wheelBarrowOwner = WorkerNames.LOADER;
                loaderSemaphore.release();
            }

        }
    }

    public void stopWork() {
        isWorkGoing = false;
    }
}
