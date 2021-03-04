package com.wk.other;

/**
 * @author: wk
 * @Date: 2020/11/10 14:07
 * @Description
 */
public class SnowFlake {
/**
 * 描述: Twitter的分布式自增ID雪花算法snowflake (Java版)
 *
 * snowflake的结构如下(每部分用-分开):
 *
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * 第一位为未使用，接下来的41位为毫秒级时间(41位的长度可以使用69年)，
 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点） ，
 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 *
 * 一共加起来刚好64位，为一个Long型。(转换成字符串长度为18)
 *
 * snowflake生成的ID整体上按照时间自增排序，
 */
    //起始时间戳
    private final static long START_STMP = 1567267200000L;

    // 每一部分占用的位数
    private final static long SEQUENCE_BIT = 12; // 序列号位数
    private final static long MACHINE_BIT = 8; // 机器标识
    private final static long DATACENTER_BIT = 2; // 数据中心

    // 每一部分的最大值
    private final static long MAX_DATACENTER_NUM = -1l ^ (-1l << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1l ^ (-1l << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1l ^ (-1l <<SEQUENCE_BIT);
    // 每一部分向左的位移
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_LEFT;
    private final static long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long datacenterId;// 数据中心
    private long machineId; // 机器标识
    private long sequence = 0l; // 序列号
    private long lastStmp = 01l;    // 上一次时间戳

    public SnowFlake(long datacenterId, long machineId){
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0){
            System.out.println("Invalid datacenterId.");
            return;
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0){
            System.out.println("Invalid machineId.");
            return;
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    // 产生下一个ID
    public synchronized long nextId(){
        long curStmp = getNewStmp();
        if (curStmp < lastStmp){
            System.out.println("Clock moved backwards.");
            return 0;
        }
        if (curStmp == lastStmp){
            // 相同毫秒内 序列号自增
            sequence = (sequence+1) & MAX_SEQUENCE;
            // 统一毫秒的序列号已经达到最大
            if (sequence == 0L){
                // 多获取几次
                for (int i=0;i<100;i++){
                    curStmp = getNextMill();
                    if (curStmp != lastStmp)
                        break;
                }
            }
        }else {
            // 不同毫秒内  序列号为0
            sequence = 0L;
        }
        lastStmp = curStmp;
        return (curStmp - START_STMP) <<TIMESTAMP_LEFT  // 时间戳部分
                | datacenterId << DATACENTER_LEFT   // 数据中心部分
                | machineId << MACHINE_LEFT // 机器标识
                | sequence;     // 序列号
    }

    private long getNextMill(){
        long mill = getNewStmp();
        while (mill <= lastStmp){
            mill = getNewStmp();
        }
        return mill;
    }
    private long getNewStmp(){
        final long currentTimeMillis = System.currentTimeMillis();
        System.out.println("currentTImeMills :" + currentTimeMillis);
        return currentTimeMillis;
    }

    public static void main(String[] args) {
        final SnowFlake snowFlake = new SnowFlake(99999L, 6666l);
        System.out.println(snowFlake.nextId());
    }
}
