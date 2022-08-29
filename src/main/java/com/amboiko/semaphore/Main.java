package com.amboiko.semaphore;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import com.amboiko.common.Logger;

public class Main {
    public static void main(String[] args) {
        WarHouse warHouseSrc = new WarHouse("EpiCenter", 12);
        WarHouse warHouseDest = new WarHouse("AMB", 0);
        int wheelBarrowCapacity = 6;

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Semaphore loaderSemaphore = new Semaphore(1);
        Semaphore transporterSemaphore = new Semaphore(0);
        Semaphore unloaderSemaphore = new Semaphore(0);


        Logger.info("WarHouse EpiCenter");
        Logger.info("Amount of cargo: " + warHouseSrc.getSandQuantity());
        Logger.info("WarHouse AMB");
        Logger.info("Amount of cargo: " + warHouseDest.getSandQuantity());
        Logger.info("--- START ---");

        new Loader(
                warHouseSrc,
                2,
                wheelBarrowCapacity,
                loaderSemaphore,
                transporterSemaphore,
                countDownLatch
        );
        Transporter transporter = new Transporter(
                5,
                loaderSemaphore,
                transporterSemaphore,
                unloaderSemaphore
        );
        Unloader unloader = new Unloader(
                warHouseDest,
                3,
                wheelBarrowCapacity,
                transporterSemaphore,
                unloaderSemaphore
        );


        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        transporter.stopWork();
        unloader.stopWork();
    }
}
