'''
Created on 27/set/2013

@author: Stefano
'''
import re

n = int(raw_input())
lines = [raw_input() for i in range(0, n)]
n = int(raw_input())
tests = [raw_input() for i in range(0, n)]
for t in tests:
    s = t[:-2]
    c = 0
    for l in lines:
        res = re.findall(s + '(se|ze)', l)
        if res:
            c += len(res)
    print c
