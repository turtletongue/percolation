### PercolationStats.java

This class estimates the value of the percolation threshold.

## How to run program

You can get the 95% confidence interval for the percolation threshold by running program these ways:
```
java PercolationStats 200 100
java PercolationStats 2 10000
java PercolationStats 2 100000
```

PercolationStats takes two arguments: n and T, performs T independent computational experiments on an n-by-n grid.
