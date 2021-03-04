package com.wk.other;

/**
 * @author: wk
 * @Date: 2020/12/4 15:13
 * @Description
 */
public class Coins {
    /*
    如果我们有面值为1元、3元和5元的硬币若干枚，如何用最少的硬币凑够11元？
     d(i)=min{ d(i-vj)+1 }，其中i-vj >=0，vj表示集合V中第j个硬币的面值
     arr = {1,3,5}
     d(11)=min{d(11-1)+1,d(11-3)+1,d(11-5)+1}=min{d(10)+1,d(8)+1,d(6)+1}=min{4,3,3}=3
     d(10)=min{d(10-1)+1,d(10-3)+1,d(10-5)+1}=min{d(9)+1,d(7)+1,d(5)+1}=min{4,4,3}=3
     d(9)=min{d(9-1)+1,d(9-3)+1,d(9-5)+1}=min{d(8)+1,d(6)+1,d(4)+1}=min{3,3,3}=3
     d(8)=min{d(8-1)+1,d(8-3)+1,d(8-5)+1}=min{d(7)+1,d(5)+1,d(3)+1} = min{4,3,2}=2
     d(7)=min{d(7-1)+1,d(7-3)+1,d(7-5)+1}=min{d(6)+1,d(4)+1,d(2)+1} = min{3,3,3}=3
     d(6)=min{d(6-1)+1,d(6-3)+1,d(6-5)+1}=min{d(5)+1,d(3)+1,d(1)+1} = min{3,2,2}=2
     d(5)=min{d(5-1)+1,d(5-3)+1,d(5-5)+1}=min{d(4)+1,d(2)+1,d(0)+1}=min{3,3,2} = 2
     d(4)=min{d(4-1)+1,d(4-3)+1}=min{d(3)+1,d(1)+1} = min{2,2)=2
     d(3)=min{d(3-1)+1,d(3-3)+1}=min{d(2)+1,d(0)+1} = min{3,1}=1
     d(2)=min{d(2-1)+1}=min{d(1)+1} = 2
     d(1)=min{d(1-1)+1}=min{d(0)+1} = 1
     d(3)=min{d(3-1)+1, d(3-3)+1}
     上面的解析是 最少硬币 来 组成 i 这个面值
     */
    public static void getMinCoins(){
        int N = 11;
        int dimes[] = {1,3,5};
        int[] minSV = new int[N+1];
        // 保证minSV[i] 即 d[i] 只被初始化一次
        boolean flog = true;
        // 获取 d(1) 到 (m) 所有的状态值,并保存到状态集合中
        for (int i = 1; i<= N; i++){
            flog = true;
            // d(i) = min{d(i-vj)+1}
            for (int j=0; j<dimes.length; j++){
                // 先假设d(i)为要比较集合min{...}内的第一个
                // 即d(i)初始化为 min(...)内第一个
                if (dimes[j] <= i && flog){
                    minSV[i] = minSV[i-dimes[j]]+1;
                    flog = false;
                }
                /*
                获取集合内最小的一个赋值给 d(i), 即d(i)=min{d(i-vj)+1}
                所用选取的面值vj不能大于要凑够的面值i
                 */
                if (dimes[j] <= i && minSV[i-dimes[j]] + 1 < minSV[i]){
                    minSV[i] = minSV[i-dimes[j]]+1;
                }
            }
        }
        System.out.printf("final value: %d\n", minSV[N]);
    }

    /*
    d(i) = sum{d(i-vj)+1}
    d(5)=sum{d(5-1),d(5-2), d(5-5)} = sum{d(4),d(3),d(0)}=sum{5,3,1}=9
    d(4)=sum{d(4-1),d(4-2)} = sum{d(3),d(2)}=sum{3,2}=5
    d(3)=sum{d(3-1),d(3-2)} = sum{d(2),d(1)}=sum{2,1}=3
    d(2)=sum{d(2-1),d(2-2)} = sum{d(1),d(0)}= sum{1,1}=2
    d(1)=sum{d(1-1)}=sum{d(0)} = 1
    d(0)=1
    --------------------------------------------------------
    i = 0
        j=1
        arr[1] = arr[1]+arr[0] = 1
        j=2
        arr[2] = arr[2] + arr[1]=1
        j=3
        arr[3] = arr[3] + arr[2]=1
        j=4
        arr[4] = arr[4] + arr[3]=1
        j=5
        arr[5] = arr[5] + arr[4]=1
    i = 1
        j=2
        arr[2] = arr[2] + arr[0]=2
        j=3
        arr[3] = arr[3] + arr[1]=2
        j=4
        arr[4] = arr[4] + arr[2]=3
        j=5
        arr[5] = arr[5] + arr[3]=3
    i = 2
        j=5
        arr[5] = arr[5] + arr[0]=4
     */
    // 计算 有dimes中的小面值 组成 N大面值的 所有情况
    public static void getCoins(){
        int N = 5;
        int dimes[] = {1,2,5};
        int[] arr = new int[(N+1)];
        arr[0] = 1;
        for (int i = 0; i<dimes.length; i++){
            // j<=N 表示 不能使用dimes中大于N的值
            for (int j = dimes[i]; j<=N;j++){
                // 这里的 += 表示的累加所有的情况
                arr[j] += arr[j-dimes[i]];
            }
        }
//        for (int i = 1; i<=N; i++){
//            // j<=N 表示 不能使用dimes中大于N的值
//            for (int j = 0; j< dimes.length;j++){
//                // 这里的 += 表示的累加所有的情况
//                if (i>= dimes[j]) {
//                    arr[i] += arr[i - dimes[j]];
//                }
//            }
//        }
        for (int i = 1;i<=N; i++){
            System.out.printf("i=%d \t arr[i]=%d\n",i, arr[i]);
        }
        System.out.printf("N kinds=%d\n", arr[N]);
    }

    public static void main(String[] args) {
        getCoins();
        getMinCoins();
    }
}
