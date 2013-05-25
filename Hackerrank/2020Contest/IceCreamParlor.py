'''
Created on 13/apr/2013

@author: Stefano
'''
n = int(raw_input())
for t in range(n):
    c = int(raw_input())
    l = int(raw_input())
    flavors = [(int(e), i + 1) for i, e in enumerate(raw_input().strip().split()) if int(e) < c]
    flavors.sort()
    start = 0
    end = len(flavors) - 1
    s = flavors[start][0] + flavors[end][0]
    while  s != c:
        if s < c:
            start += 1
        else:
            end -= 1
        s = flavors[start][0] + flavors[end][0]
    if flavors[start][1] < flavors[end][1]:
        print str(flavors[start][1]) + " " + str(flavors[end][1])
    else:
        print str(flavors[end][1]) + " " + str(flavors[start][1])
