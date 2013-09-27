'''
Created on 27/set/2013

@author: Stefano
'''
import re, fileinput

n=raw_input()
for line in fileinput.input():
    pass
    if re.search('^(hackerrank)$', line):
        print 0
    elif re.search('(hackerrank)$', line):
        print 2
    elif re.search('^(hackerrank)', line):
        print 1
    else:
        print -1