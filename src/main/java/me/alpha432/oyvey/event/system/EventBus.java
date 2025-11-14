package me.alpha432.oyvey.event.system;

import me.alpha432.oyvey.event.Event;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private final Map<Class<?>, CopyOnWriteArrayList<Listener>> listeners = new ConcurrentHashMap<>();

    public void register(Object host) {
        for (Method method : host.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1) {
                    Class<?> eventType = params[0];

                    listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                            .add(Listener.of(host, method));
                }
            }
        }
    }

    public void unregister(Object host) {
        for (CopyOnWriteArrayList<Listener> list : listeners.values()) {
            list.removeIf(listener -> listener.getHost().equals(host));
        }
    }

    public void post(Event event) {
        List<Listener> list = listeners.get(event.getClass());
        if (list == null) return;

        for (Listener listener : list) {
            if (event.isCancelled()) return;
            listener.invoke(event);
        }
    }
}