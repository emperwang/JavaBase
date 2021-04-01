package com.wk.dp.behavior.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 15:41
 * @Description
 */
public abstract class Observerable {
    // 记录观察者
    private List<Observor> observors;

    public Observerable(){
        observors = new ArrayList<>();
    }
    // 通知观察者
    public void notifyObservor(){
        observors.forEach(observor -> {
            observor.updateState(this);
        });
    }

    public void addObservor(Observor observor){
        observors.add(observor);
    }

    public void removeObservor(Observor observor){
        observors.remove(observor);
    }
}
