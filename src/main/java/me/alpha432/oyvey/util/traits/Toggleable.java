package me.alpha432.oyvey.util.traits;

public interface Toggleable {
    boolean isToggled();

    void enable();

    void disable();

    default void toggle() {
        if (isToggled()) disable();
        else enable();
    }

    default void setToggled(boolean toggled) {
        if(isToggled() != toggled) toggle();
    }
}
