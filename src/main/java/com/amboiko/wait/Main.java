package com.amboiko.wait;

public class Main {
    public static void main(String[] args) {
        WarHouse warHouseSrc = new WarHouse("EpiCenter", 12);
        WarHouse warHouseDest = new WarHouse("AMB", 0);
        int wheelBarrowCapacity = 6;

        System.out.println("WarHouse EpiCenter");
        System.out.println("Amount of cargo: " + warHouseSrc.getSandQuantity());
        System.out.println("WarHouse AMB");
        System.out.println("Amount of cargo: " + warHouseDest.getSandQuantity());
        System.out.println("--- START ---");

        new Loader(
                warHouseSrc,
                wheelBarrowCapacity,
                2
        );
        new Transporter(
                warHouseSrc,
                wheelBarrowCapacity,
                warHouseDest,
                5
        );
        new Unloader(
                warHouseSrc,
                wheelBarrowCapacity,
                warHouseDest,
                3
        );

    }
}
