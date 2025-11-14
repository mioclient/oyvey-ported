package me.alpha432.oyvey.event.system;

import me.alpha432.oyvey.event.Event;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private final Map<Class<?>, CopyOnWriteArrayList<ListenerMethod>> listeners =
            new ConcurrentHashMap<>();

    public void register(Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1) {
                    Class<?> eventType = params[0];

                    listeners
                            .computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                            .add(new ListenerMethod(listener, method));
                }
            }
        }
    }

    public void unregister(Object listener) {
        for (CopyOnWriteArrayList<ListenerMethod> list : listeners.values()) {
            list.removeIf(lm -> lm.owner.equals(listener));
        }
    }

    public void post(Event event) {
        List<ListenerMethod> list = listeners.get(event.getClass());
        if (list == null) return;

        for (ListenerMethod lm : list) {
            if (event.isCancelled()) return;
            try {
                lm.method.setAccessible(true);
                lm.method.invoke(lm.owner, event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private record ListenerMethod(Object owner, Method method) {}
}