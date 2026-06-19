public class sudako {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5};
        int sum = 0;

        for (int i = 0; i < nums.length; i++) {
            System.out.println(nums[i]);
            sum = sum + nums[i];
        }
        System.out.println(sum);
    }
}