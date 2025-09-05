import java.util.HashMap;

class Solution {
    public int[] twoSum(int[] nums, int target) {
        // Create a HashMap to store the numbers and their indices
        HashMap<Integer, Integer> numMap = new HashMap<>();

        // Iterate through the array
        for (int i = 0; i < nums.length; i++) {
            int currentNum = nums[i];
            
            // Calculate the complement
            int complement = target - currentNum;
            
            // Check if the complement exists in the map
            if (numMap.containsKey(complement)) {
                // If it does, we found the pair. Return their indices.
                return new int[] { numMap.get(complement), i };
            }
            
            // If the complement is not in the map, add the current number and its index to the map
            numMap.put(currentNum, i);
        }

        // This part won't be reached due to the problem constraints
        // (exactly one solution exists), but it's good practice.
        return new int[] {};
    }
}
