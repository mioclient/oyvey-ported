package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.util.models.Timer;

import java.text.DecimalFormat;
import java.util.Arrays;

public class ServerManager
        extends Feature {
    private final float[] tpsCounts = new float[10];
    private final DecimalFormat format = new DecimalFormat("##.00#");
    private final Timer timer = new Timer();
    private float TPS = 20.0f;
    private long lastUpdate = -1L;
    private String serverBrand = "";

    public void onPacketReceived() {
        this.timer.reset();
    }

    public boolean isServerNotResponding() {
        return this.timer.passedMs(2000);
    }

    public long serverRespondingTime() {
        return this.timer.getPassedTimeMs();
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
        this.TPS = Float.parseFloat(this.format.format(total).replace(",", "."));
        this.lastUpdate = currentTime;
    }

    @Override
    public void reset() {
        Arrays.fill(this.tpsCounts, 20.0f);
        this.TPS = 20.0f;
    }

    public float getTpsFactor() {
        return 20.0f / this.TPS;
    }

    public float getTPS() {
        return this.TPS;
    }

    public String getServerBrand() {
        return this.serverBrand;
    }

    public void setServerBrand(String brand) {
        this.serverBrand = brand;
    }

    public int getPing() {
        if (ServerManager.fullNullCheck()) {
            return 0;
        }
        try {
            return mc.getNetworkHandler().getPlayerListEntry(mc.player.getGameProfile().getName()).getLatency();
        } catch (Throwable e) {
            return 0;
        }
    }
}