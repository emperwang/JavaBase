package com.wk;
/**
 * 在一个有序的二维数组中查找对应的value.
 * 二维数组的行: 从左到右 递增
 *          列: 从上到下递增
 */
public class FindNumInTwoDimArray {

    int[][] arr = {
            {1,3,4,5,6,7,9},
            {2,5,7,8,9,10,11},
            {4,5,6,8,9,13,14},
            {5,8,9,10,11,14,15}
    };

    public void searchNum(int num){
        /**
         * 查找时从右上角开始,如果右上角的值小于目标值 num,那么就可以排除第一行
         * 如果右上角的值 大于目标值,那么就排除最后一列
         */
        int row = 0;
        int colum = arr[0].length-1;
        //System.out.printf("a[0] length: %d, a length: %d\n",arr[0].length, arr.length);
        while (row < arr.length-1 && colum >=0){
            if (arr[row][colum] == num){
                System.out.printf("find num at row:%d, column: %d,val=%d \n",row, colum,arr[row][colum]);
                return;
            }else if (arr[row][colum] > num){
                colum--;
            }else if(arr[row][colum] < num){
                row++;
            }
        }
        System.out.printf("can't find num :%d\n", num);
    }

    public static void main(String[] args) {
        FindNumInTwoDimArray dimArray = new FindNumInTwoDimArray();
        dimArray.searchNum(10);
        dimArray.searchNum(100);
    }

}
