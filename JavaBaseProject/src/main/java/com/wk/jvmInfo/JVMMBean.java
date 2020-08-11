package com.wk.jvmInfo;

import javax.management.*;
import java.io.IOException;
import java.lang.management.*;
import java.util.List;

public class JVMMBean {
    /**
     * JVM 虚拟机的 运行时 系统
     */
    public static void showJVMINfo(){
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String vendor = runtimeMXBean.getVmVendor();

        System.out.println("jvm name: " + runtimeMXBean.getVmName());
        System.out.println("jvm version: " + runtimeMXBean.getVmVersion());
        System.out.println("jvm bootClasspath: " + runtimeMXBean.getBootClassPath());
        System.out.println("jvm start time: " + runtimeMXBean.getStartTime());
    }

    /**
     * java 虚拟机的内存系统 信息
     */
    public static void showMemoryInfo(){
        MemoryMXBean mxBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = mxBean.getHeapMemoryUsage();
        System.out.println("Heap committed: " + heapMemoryUsage.getCommitted()
                +"--Init:"+heapMemoryUsage.getInit()+"--max:"+ heapMemoryUsage.getMax()
                +"-- used: " + heapMemoryUsage.getUsed());
    }
    /**
     * java 虚拟机在其上运行的操作系统
     */
    public static void showSystem(){
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        System.out.println("Architecture: " + operatingSystemMXBean.getArch());
        System.out.println("Processors: " + operatingSystemMXBean.getAvailableProcessors());
        System.out.println("System name: " + operatingSystemMXBean.getName());
        System.out.println("System version: " + operatingSystemMXBean.getVersion());
        System.out.println("Last minute load: " + operatingSystemMXBean.getSystemLoadAverage());
    }
    /**
     * java 虚拟机的类加载系统
     */
    public static void showClassLoading(){
        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
        System.out.println("TotalLoadedClassCount: " + classLoadingMXBean.getTotalLoadedClassCount());
        System.out.println("LoadedClassCount : " + classLoadingMXBean.getLoadedClassCount());
        System.out.println("UnloadedClassCount:"+classLoadingMXBean.getUnloadedClassCount());
    }
    /**
     * java 虚拟机的编译系统
     */
    public static void showCompilation(){
        CompilationMXBean compilationMXBean = ManagementFactory.getCompilationMXBean();
        System.out.println("name: " + compilationMXBean.getName());
        System.out.println("TotalCompilationTime: " + compilationMXBean.getTotalCompilationTime());
    }
    /**
     * java虚拟机的线程系统
     */
    public static void showThread(){
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        System.out.println("ThreadCount: " + threadMXBean.getThreadCount());
        System.out.println("AllThreadIds: "+ threadMXBean.getAllThreadIds());
        System.out.println("CurrentThreadUserTime: " + threadMXBean.getCurrentThreadUserTime());
    }
    /**
     * java 虚拟机中的垃圾回收器
     */
    public static void showGarbageCollector(){
        List<GarbageCollectorMXBean> mxBeans = ManagementFactory.getGarbageCollectorMXBeans();
        System.out.println("**************************************************************");
        for (GarbageCollectorMXBean mxBean : mxBeans) {
            System.out.println("name: " + mxBean.getName());
            System.out.println("CollectionCount: " + mxBean.getCollectionCount());
            System.out.println("CollectionTime: " + mxBean.getCollectionTime());
        }
        System.out.println("**************************************************************");
    }
    /**
     * java 虚拟机中的内存管理器
     */
    public static void showMemoryManager(){
        List<MemoryManagerMXBean> memoryManagerMXBeans = ManagementFactory.getMemoryManagerMXBeans();
        for (MemoryManagerMXBean mxBean : memoryManagerMXBeans) {
            System.out.println("name: " + mxBean.getName());
            System.out.println("MemoryPoolNames: "+mxBean.getMemoryPoolNames().toString());
        }
    }

    /**
     * java 虚拟机中的内存池
     */
    public static void showMemoryPool(){
        List<MemoryPoolMXBean> poolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean mxBean : poolMXBeans) {
            System.out.println("name: " + mxBean.getName());
            System.out.println("CollectionUsage: " + mxBean.getCollectionUsage());
            System.out.println("type: " + mxBean.getType());
            //System.out.println("CollectionUsageThreshold:" + mxBean.getCollectionUsageThreshold());
        }
    }

    /**
     * 访问MXBean的方法的三种方式
     */
    public static void visitMBean(){
        // 第一种方式
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        System.out.println("vendor: " + runtimeMXBean.getVmVendor());

        // 第二种方式 通过一个连接到正在运行的虚拟机平台 MBeanServer 的MBeanServerConnection
        MBeanServerConnection mbs=  null;

        try{
            ObjectName name = new ObjectName(ManagementFactory.RUNTIME_MXBEAN_NAME);
            Object vendor = mbs.getAttribute(name, "VmVendor");
            System.out.println("vendor: " + vendor);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ReflectionException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (AttributeNotFoundException e) {
            e.printStackTrace();
        } catch (MBeanException e) {
            e.printStackTrace();
        }

        // 第三种方式 MXBean 代理
        MBeanServerConnection mbs2 = null;
        RuntimeMXBean proxy;
        try{
            proxy = ManagementFactory.newPlatformMXBeanProxy(mbs2,
                    ManagementFactory.RUNTIME_MXBEAN_NAME, RuntimeMXBean.class);
            String vmVendor = proxy.getVmVendor();
            System.out.println("vendor : " + vmVendor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        System.out.println("*******************showJvmInfo*******************");
        //showJVMINfo();
        System.out.println("*******************showMemoryInfo*******************");
        //showMemoryInfo();
        System.out.println("*******************showSystem*******************");
        //showSystem();
        System.out.println("*******************showClassLoading*******************");
        showClassLoading();
        System.out.println("*******************showCompilation*******************");
        showCompilation();
        System.out.println("*******************showThread*******************");
        showThread();

        System.out.println("*******************showGarbageCollector*******************");
        showGarbageCollector();
        System.out.println("*******************showMemoryManager*******************");
        showMemoryManager();
        System.out.println("*******************showMemoryPool*******************");
        showMemoryPool();
        System.out.println("*******************showJvmInfo*******************");
    }


}
