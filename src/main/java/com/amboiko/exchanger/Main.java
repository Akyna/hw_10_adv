package com.amboiko.exchanger;

import com.amboiko.common.Logger;

import java.util.concurrent.Exchanger;

public class Main {
    public static void main(String[] args) {
        WarHouse warHouseSrc = new WarHouse("EpiCenter", 12);
        WarHouse warHouseDest = new WarHouse("AMB", 0);
        int wheelBarrowCapacity = 6;

        Exchanger<Integer> loaderExchanger = new Exchanger<>();
        Exchanger<Integer> transporterExchanger = new Exchanger<>();
        Exchanger<Integer> unloaderExchanger = new Exchanger<>();


        Logger.info("WarHouse EpiCenter");
        Logger.info("Amount of cargo: " + warHouseSrc.getSandQuantity());
        Logger.info("WarHouse AMB");
        Logger.info("Amount of cargo: " + warHouseDest.getSandQuantity());
        Logger.info("--- START ---");

        new Loader(
                warHouseSrc,
                wheelBarrowCapacity,
                2,
                loaderExchanger,
                transporterExchanger
        );
        new Transporter(
                warHouseSrc,
                wheelBarrowCapacity,
                5,
                loaderExchanger,
                transporterExchanger,
                unloaderExchanger
        );
        new Unloader(
                warHouseSrc,
                wheelBarrowCapacity,
                warHouseDest,
                3,
                transporterExchanger,
                unloaderExchanger
        );
    }
}
