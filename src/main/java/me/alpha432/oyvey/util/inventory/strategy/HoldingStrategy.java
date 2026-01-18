package me.alpha432.oyvey.util.inventory.strategy;

import me.alpha432.oyvey.util.inventory.Result;

public final class HoldingStrategy implements SwapStrategy{
    public static final HoldingStrategy INSTANCE = new HoldingStrategy();

    private HoldingStrategy() {
    }

    @Override
    public boolean swap(Result result) {
        return result.holding();
    }

    @Override
    public boolean swapBack(int last, Result result) {
        return result.holding();
    }
}
