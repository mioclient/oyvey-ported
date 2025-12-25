package me.alpha432.oyvey.util.inventory.strategy;

import me.alpha432.oyvey.util.inventory.Result;

public interface SwapStrategy {
    boolean swap(Result result);

    boolean swapBack(int last, Result result);
}
