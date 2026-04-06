package org.vivi.framework.factory.strategy;

public class SortedDemo {

    public static int sortedRemoved(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int slow = 0;

        for (int fast = 1; fast < nums.length; fast++) {
            if (nums[fast] != nums[slow]) {
                slow++;
                nums[slow] = nums[fast];
            }
        }

        return slow + 1;
    }

    public static void main(String[] args) {
        int[] nums = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};

        int newLength = sortedRemoved(nums);

        System.out.println("新长度: " + newLength);
        System.out.print("新数组: ");
        for (int i = 0; i < newLength; i++) {
            System.out.print(nums[i] + " ");
        }
    }
}
