package com.wk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: wk
 * @Date: 2021/1/19 18:13
 * @Description
 */
public class Solution_summaryRanges {
    /*
    返回 恰好覆盖数组中所有数字 的 最小有序 区间范围列表。也就是说，nums 的每个元素都恰好被某个区间范围所覆盖，
    并且不存在属于某个范围但不属于 nums 的数字 x 。
    列表中的每个区间范围 [a,b] 应该按如下格式输出：
    "a->b" ，如果 a != b
    "a" ，如果 a == b

    示例 1：
    输入：nums = [0,1,2,4,5,7]
    输出：["0->2","4->5","7"]
    解释：区间范围是：
    [0,2] --> "0->2"
    [4,5] --> "4->5"
    [7,7] --> "7"

    示例 2：
    输入：nums = [0,2,3,4,6,8,9]
    输出：["0","2->4","6","8->9"]
    解释：区间范围是：
    [0,0] --> "0"
    [2,4] --> "2->4"
    [6,6] --> "6"
    [8,9] --> "8->9"
     */
    // 有瑕疵
    public List<String> summaryRanges(int[] nums) {
        List<String> lists = new ArrayList<>();
        if (nums == null || nums.length<=0) return lists;
        if (nums.length == 1){
            lists.add(nums[0]+"");
            return lists;
        }
        Arrays.sort(nums);
        int start = nums[0];
        int pre = nums[0];
        for (int i=1;i<nums.length;i++){
            if ((nums[i] - pre) == 1){
                pre = nums[i];
                continue;
            }
            lists.add(start==pre ? (pre+""):(start+"->"+pre));
            start = pre = nums[i];
            if (i == nums.length-1) lists.add(nums[i]+"");
        }
        return lists;
    }

    public List<String> summaryRanges2(int[] nums) {
        int i = 0,len = nums.length;
        List<String> lists = new ArrayList<>();
        while (i < len){
            int low = i;
            i++;
            while (i < len && nums[i] == nums[i-1]+1){
                i++;
            }
            int high = i-1;
            final StringBuilder builder = new StringBuilder(Integer.toString(nums[low]));
            if (low < high){
                builder.append("->");
                builder.append(Integer.toString(nums[high]));
            }
            lists.add(builder.toString());
        }
        return lists;
    }
    public List<String> summaryRanges3(int[] nums) {
        List<String> lists = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i=0,j=i+1; j<nums.length;i++,j++){
            if (nums[j] - 1 > nums[i]){
                sb.append(nums[i]);
                lists.add(sb.toString());
                sb = new StringBuilder();
            }else{
                if (sb.length()==0){
                    sb.append(nums[i]);
                    sb.append("->");
                }
            }
        }
        lists.add(nums[nums.length-1]+"");
        return lists;
    }

    public static void main(String[] args) {
        final Solution_summaryRanges solutionSummaryRanges = new Solution_summaryRanges();
        System.out.println(solutionSummaryRanges.summaryRanges3(new int[]{0,1,2,4,5,7}));
        System.out.println(solutionSummaryRanges.summaryRanges3(new int[]{0,2,3,4,6,8,9}));
    }
}
