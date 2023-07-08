package com.heroku.java;

public class GCOverhead {
    public static void main(String[] args) throws Exception {
        while (true) {
            new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000 * 60 * 60 * 24);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            ).start();
        }
    }
}
