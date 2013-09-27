'''
Created on 27/set/2013

@author: Stefano
'''
from scipy import special
import math
n=int(raw_input())
for i in range(0,n):
    k=int(raw_input())-1
    s=0.25*(math.pi+(-1)**k*(-special.psi(0.75+0.5*k)+special.psi(1.25+0.5*k)))
    print "%.15f" % s