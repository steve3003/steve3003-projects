'''
Created on 02/giu/2013

@author: Stefano
'''
from fractions import gcd
n=int(raw_input())
for i in range(0,n):
    a,b=[int(j) for j in raw_input().strip().split()]
    d=gcd(a,b)
    print a*b/(d*d)