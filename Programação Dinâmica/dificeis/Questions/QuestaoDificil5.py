# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def maxPathSum(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: int
        """

        max_path = [float('-inf')]

        def get_max_gain(node):

            if not node:
                return 0

 
            left_gain = max(get_max_gain(node.left), 0)
            right_gain = max(get_max_gain(node.right), 0)
  
            new_path_sum = node.val + left_gain + right_gain
            max_path[0] = max(max_path[0], new_path_sum)

            return node.val + max(left_gain, right_gain)

        get_max_gain(root)
        return max_path[0]