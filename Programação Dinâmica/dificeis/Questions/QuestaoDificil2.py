class Solution:
    def profitableSchemes(self, n, minProfit, group, profit):
        MOD = 10**9 + 7
        dp = [[0] * (minProfit + 1) for _ in range(n + 1)]
        dp[0][0] = 1
        m = len(group)

        for k in range(m):
            g = group[k]
            p = profit[k]
            for people in range(n, g - 1, -1):
                for pr in range(minProfit, -1, -1):
                    npf = pr + p
                    if npf > minProfit:
                        npf = minProfit
                    dp[people][npf] = (dp[people][npf] + dp[people - g][pr]) % MOD

        total = 0
        for people in range(n + 1):
            total = (total + dp[people][minProfit]) % MOD
        return total