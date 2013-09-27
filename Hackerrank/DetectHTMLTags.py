'''
Created on 27/set/2013

@author: Stefano
'''
import re

n = int(raw_input())
lines = [raw_input() for i in range(0, n)]
tags = []
for line in lines:
    for tag in re.findall('<[^<]*>', line):
        t = tag.replace('<', "").replace('>', "").replace('/', "").split()[0]
        if t not in tags:
            tags.append(t)
tags.sort()
print ';'.join(tags)
