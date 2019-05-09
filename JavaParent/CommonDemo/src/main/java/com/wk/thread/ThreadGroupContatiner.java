package com.wk.thread;

import com.wk.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ThreadUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

public class ThreadGroupContatiner {
    private volatile ThreadGroup defaultGroup;
    private Map<String,ThreadFactory> factories;

    public ThreadGroupContatiner(){
        this.factories = CollectionUtil.newConcurrentHashMap();
    }

    public ThreadGroup getDefaultGroup(){
        if(defaultGroup == null){
            initDefault(null);
        }
        return defaultGroup;
    }

    /**
     * 初始化线程组
     * @param groupName
     * @return
     */
    public ThreadGroup initDefault(String groupName) {
        if(defaultGroup != null){
            return defaultGroup;
        }
        synchronized (this){
            if(defaultGroup != null){
                return defaultGroup;
            }
            defaultGroup = findGroup(groupName);
            if(defaultGroup == null){
                defaultGroup = new ThreadGroup(groupName);
            }
        }
        return defaultGroup;
    }

    /**
     * 这里的ThreadUtils  类需要熟悉一下功能
     * @param groupName
     * @return
     */
    public ThreadGroup findGroup(String groupName) {
        if(StringUtils.isBlank(groupName)){
            return Thread.currentThread().getThreadGroup();
        }
        Collection<ThreadGroup> groups = ThreadUtils.findThreadGroupsByName(groupName);
        return groups.isEmpty() ? null : groups.iterator().next();
    }

    //把线程组中的所有线程停止下来
    public void stop(){
        //activeCount 线程组中的线程活跃数量
        Thread[] threads = new Thread[defaultGroup.activeCount()];
        //这里的enumerate: 将此线程组中每个活动线程复制到指定的数组中
        int c = defaultGroup.enumerate(threads);
        for(int i=0; i <c; i++){
            Thread thread = threads[i];
            //interrupt:中断此线程s
            thread.interrupt();
        }
    }

    /**
     * 此线程组和其子线程组中的所有线程都会被停止
     */
    public void destory(){
        //销毁此线程组及其所有子组
        defaultGroup.destroy();
    }


    public ThreadFactory defaultFactory(){
        return cachedFactory(defaultGroup.getName());
    }

    private ThreadFactory cachedFactory(String prefix) {
        if(StringUtils.isBlank(prefix)){
            prefix = defaultGroup.getName();
        }
        ThreadFactory factory = factories.get(prefix);
        return (factories == null)?createFactory(prefix) : factory;
    }

    private synchronized ThreadFactory createFactory(String prefix) {
        ThreadFactory factory = factories.get(prefix);
        if(factory == null){
            factory =  ThreadUtil.createFactory(defaultGroup,prefix);
            factories.put(prefix,factory);
        }
        return factory;
    }

    public static ThreadGroupContatiner getInstance(){
        return Holder.instance;
    }

    private static final class Holder{
        public static ThreadGroupContatiner instance = new ThreadGroupContatiner();
    }


}
