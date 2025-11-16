package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.util.models.Timer;

import java.util.Arrays;

public class ServerManager
        extends Feature {
    private final float[] tpsCounts = new float[10];
    private final Timer timer = new Timer();
    private float tps = 20.0f;
    private long lastUpdate = -1L;
    private String serverBrand = "";

    public void onPacketReceived() {
        this.timer.reset();
    }

    public void update() {
        float tps;
        long currentTime = System.currentTimeMillis();
        if (this.lastUpdate == -1L) {
            this.lastUpdate = currentTime;
            return;
        }
        long timeDiff = currentTime - this.lastUpdate;
        float tickTime = (float) timeDiff / 20.0f;
        if (tickTime == 0.0f) {
            tickTime = 50.0f;
        }
        if ((tps = 1000.0f / tickTime) > 20.0f) {
            tps = 20.0f;
        }
        System.arraycopy(this.tpsCounts, 0, this.tpsCounts, 1, this.tpsCounts.length - 1);
        this.tpsCounts[0] = tps;
        double total = 0.0;
        for (float f : this.tpsCounts) {
            total += f;
        }
        if ((total /= this.tpsCounts.length) > 20.0) {
            total = 20.0;
        }
        this.tps = (float) total;
        this.lastUpdate = currentTime;
    }

    @Override
    public void reset() {
        Arrays.fill(this.tpsCounts, 20.0f);
        this.tps = 20.0f;
    }

    public boolean isServerNotResponding() {
        return this.timer.passedMs(2000);
    }

    public long serverRespondingTime() {
        return this.timer.getPassedTimeMs();
    }

    public float getTpsFactor() {
        return 20.0f / this.tps;
    }

    public float getTps() {
        return this.tps;
    }

    public String getServerBrand() {
        return this.serverBrand;
    }

    public void setServerBrand(String brand) {
        this.serverBrand = brand;
    }

    public int getPing() {
        if (ServerManager.nullCheck()) {
            return 0;
        }
        try {
            return mc.getConnection().getPlayerInfo(mc.player.getGameProfile().name()).getLatency();
        } catch (Throwable e) {
            return 0;
        }
    }
}