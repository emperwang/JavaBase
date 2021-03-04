package com.wk.other;

/**
 * @author: wk
 * @Date: 2020/10/19 13:52
 * @Description
 */
/*
在一个 n * m 的二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个函数，
输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。

示例:
现有矩阵 matrix 如下：
[
  [1,   4,  7, 11, 15],
  [2,   5,  8, 12, 19],
  [3,   6,  9, 16, 22],
  [10, 13, 14, 17, 24],
  [18, 21, 23, 26, 30]
]
 */
public class FindNumber {
    // 创建二维数组
    static int[][] matrix = {{1,   4,7,11,15},
            {2,5,8,12,19},
            {3,6,9,16,22},
            {10,13,14,17,24},
            {18,21,23,26,30},
            };
    static int[][] nums={{-5}};

    public static void printMartix(){
        System.out.println(matrix[1].length); // 列数
        System.out.println(matrix.length);    // 行数
    }

    /*
      先判断在哪一行  n
      在使用二分查找在行中查找  logN
      时间复杂度: nlogN
     */
    public static void findNumInMartax(int[][] matrix, int target){
        if (matrix == null || matrix.length <= 0){
            return;
        }
        // 先判断在哪一行
        for (int i=0; i < matrix.length; i++){
            int len = matrix[i].length-1;
            // 如果在这一行,则在这一行进行查找
            if (target >= matrix[i][0] && target<=matrix[i][len]){
                int left = 0;
                int right = len;
                while (left <= right){
                    int mid = (left+right) / 2;
                    if (matrix[i][mid] == target){
                        System.out.println("find number : "+target+",i="+i+", mid="+mid);
                        return;
                    }else if (matrix[i][mid] < target){
                        left=mid+1;
                    }else if (matrix[i][mid] > target){
                        right=mid-1;
                    }
                }
            }
        }
        System.out.println("不存在");
    }

    public static boolean findNumberIn2DArray(int[][] matrix, int target) {
        if (matrix == null || matrix.length <= 0){
            return false;
        }
        // 先判断在哪一行
        for (int i=0; i < matrix.length; i++){
            int len = matrix[i].length-1;
            if (matrix[i].length <= 0){
                continue;
            }
            // 如果在这一行,则在这一行进行查找
            if (target >= matrix[i][0] && target<=matrix[i][len]){
                int left = 0;
                int right = len;
                while (left <= right){
                    int mid = (left+right) / 2;
                    if (matrix[i][mid] == target){
                        return true;
                    }else if (matrix[i][mid] < target){
                        left=mid+1;
                    }else if (matrix[i][mid] > target){
                        right=mid-1;
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        //printMartix();
        //findNumInMartax(nums,-5);
        System.out.println(findNumberIn2DArray(nums,-5));
    }
}
