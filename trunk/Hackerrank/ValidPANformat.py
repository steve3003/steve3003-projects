'''
Created on 27/set/2013

@author: Stefano
'''
import re, fileinput

n=raw_input()
for line in fileinput.input():
    pass
    if re.search('[A-Z]{5}[0-9]{4}[A-Z]', line):
        print "YES"
    else:
        print "NO"