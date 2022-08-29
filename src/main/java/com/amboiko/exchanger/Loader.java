package com.amboiko.exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

import com.amboiko.common.Logger;
import com.amboiko.common.WorkerNames;

public class Loader implements Runnable {
    private final int wheelBarrowCapacity;
    private final WarHouse warHouse;
    private final int performancePerSecond;
    private final Exchanger<Integer> loaderExchanger;
    private final Exchanger<Integer> transporterExchanger;

    public Loader(
            WarHouse warHouse,
            int wheelBarrowCapacity,
            int performancePerSecond,
            Exchanger<Integer> loaderExchanger,
            Exchanger<Integer> transporterExchanger
    ) {
        this.wheelBarrowCapacity = wheelBarrowCapacity;
        this.performancePerSecond = performancePerSecond;
        this.warHouse = warHouse;
        this.loaderExchanger = loaderExchanger;
        this.transporterExchanger = transporterExchanger;

        new Thread(this).start();
    }

    @Override
    public void run() {
        Logger.info(WorkerNames.LOADER + " start");
        while (true) {
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

            try {
                transporterExchanger.exchange(wheelBarrowCapacity);
                if (warHouse.getSandQuantity() < wheelBarrowCapacity) {
                    break;
                }
                loaderExchanger.exchange(wheelBarrowCapacity);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
