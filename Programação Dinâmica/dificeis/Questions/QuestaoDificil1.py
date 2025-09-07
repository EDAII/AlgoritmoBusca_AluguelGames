class Solution:
    def jobScheduling(self, startTime, endTime, profit):
        jobs = list(zip(startTime, endTime, profit))
        jobs.sort(key=lambda x: x[1])
        ends = [j[1] for j in jobs]
        n = len(jobs)
        dp = [0] * (n + 1)

        def find(t):
            lo, hi = 0, n
            while lo < hi:
                mid = (lo + hi) // 2
                if ends[mid] <= t:
                    lo = mid + 1
                else:
                    hi = mid
            return lo

        for i in range(1, n + 1):
            s, e, p = jobs[i-1]
            j = find(s)
            dp[i] = dp[i-1] if dp[i-1] > dp[j] + p else dp[j] + p

        return dp[n]