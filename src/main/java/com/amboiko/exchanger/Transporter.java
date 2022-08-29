package com.amboiko.exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

import com.amboiko.common.Logger;
import com.amboiko.common.WorkerNames;

public class Transporter implements Runnable {
    private final WarHouse warHouse;
    private final int wheelBarrowCapacity;
    private final int timeSpentOnOneSide;
    private final Exchanger<Integer> loaderExchanger;
    private final Exchanger<Integer> transporterExchanger;
    private final Exchanger<Integer> unloaderExchanger;
    private WorkerNames wheelBarrowOwner = WorkerNames.LOADER;

    public Transporter(
            WarHouse warHouse,
            int wheelBarrowCapacity,
            int timeSpentOnOneSide,
            Exchanger<Integer> loaderExchanger,
            Exchanger<Integer> transporterExchanger,
            Exchanger<Integer> unloaderExchanger
    ) {
        this.warHouse = warHouse;
        this.wheelBarrowCapacity = wheelBarrowCapacity;
        this.timeSpentOnOneSide = timeSpentOnOneSide;
        this.loaderExchanger = loaderExchanger;
        this.transporterExchanger = transporterExchanger;
        this.unloaderExchanger = unloaderExchanger;

        new Thread(this).start();
    }

    @Override
    public void run() {
        Logger.info(WorkerNames.TRANSPORTER + " start");
        while (warHouse.getSandQuantity() >= wheelBarrowCapacity) {
            Logger.info(WorkerNames.TRANSPORTER + " waiting for " + wheelBarrowOwner + " with a wheelBarrow");

            try {
                transporterExchanger.exchange(0);
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
                try {
                    unloaderExchanger.exchange(6);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                wheelBarrowOwner = WorkerNames.LOADER;
                try {
                    loaderExchanger.exchange(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
