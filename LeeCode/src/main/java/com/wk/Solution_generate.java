package com.wk;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wk
 * @Date: 2021/1/11 13:16
 * @Description
 */
public class Solution_generate {
    /*
    在杨辉三角中，每个数是它左上方和右上方的数的和。
    示例:
    输入: 5
    输出:
    [
         [1],       1       1
        [1,1],      2      2 3
       [1,2,1],     3     4 5 6
      [1,3,3,1],    4    7 8 9 10
     [1,4,6,4,1]    5  11 12 13 14 15
    ]
    */
    /*
    1. 行数从0开始,每行元素个数为 row+1
    2. 元素idx从0开始,每行最后一个元素的idx = row
    3. 除去开头和结尾,每个元素为 ret[row][i] = ret[row-1][i-1] + ret[row-1][i]
     */
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> rows = new ArrayList<>();
        for (int i = 0; i<numRows; i++){
            // 存储行数据
            List<Integer> row = new ArrayList<>();
            for (int j=0; j<=i; j++){
                if (j == 0 || j==i){
                    row.add(1);
                    continue;
                }
                row.add(rows.get(i-1).get(j-1)+rows.get(i-1).get(j));
            }
            rows.add(row);
        }
        return rows;
    }

    public static void main(String[] args) {
        final Solution_generate solutionGenerate = new Solution_generate();
        final List<List<Integer>> generate = solutionGenerate.generate(3);
        System.out.println(generate);
    }
}
