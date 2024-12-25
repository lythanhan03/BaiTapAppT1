package com.example.myapplication;

public class Tinhtoan {
    /**
     * Tìm giá trị lớn nhất trong mảng
     * @param arr Mảng các số nguyên
     * @return Giá trị lớn nhất
     */
    public static int findMax(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Mảng không được rỗng");
        }
        int max = arr[0];
        for (int num : arr) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    /**
     * Tìm giá trị nhỏ nhất trong mảng
     * @param arr Mảng các số nguyên
     * @return Giá trị nhỏ nhất
     */
    public static int findMin(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Mảng không được rỗng");
        }
        int min = arr[0];
        for (int num : arr) {
            if (num < min) {
                min = num;
            }
        }
        return min;
    }
}
