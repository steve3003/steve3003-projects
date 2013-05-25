'''
Created on 24/apr/2013

@author: Stefano
''' 
from math import ceil
import sys

print sys.maxint
t = int(raw_input())
for i in range(t):
    n, m = [ int(j) for j in raw_input().strip().split() ]
    nf = int(ceil(n / 2.0))
    ns = n - nf
    frontier = [jr for jr in range(ns + 1, n + 1)]
    '''Number of numbers that can follow a number in position i<=n/2: n - 2 i + 1'''
    #print frontier
    for j in range(m - 1):
        newFrontier = []
        for f in frontier:
            if 2 * f > n:
                newFrontier.extend(range(1, n + 1))
            else:
                newFrontier.extend([jr for jr in range(2 * f, n + 1)])
        frontier = newFrontier
        #print frontier
    print len(frontier) % 100000007
    
