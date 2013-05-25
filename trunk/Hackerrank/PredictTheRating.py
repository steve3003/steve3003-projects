'''
Created on 25/mag/2013

@author: Stefano
'''
from numpy import asarray, dot, transpose
from numpy.linalg import inv

n=int(raw_input())
for i in range(0,n):
    y=[int(j) for j in raw_input().strip().split()]
    na=int(y[0]/2)
    y.pop(0)
    y=y[::-1]
    phi=[]
    yf=[]
    for index in range(1,len(y)-na+1):
        phi.append(y[index:index+na])
        yf.append([y[index-1]])
    phi=asarray(phi)
    yf=asarray(yf)
    phiT=transpose(phi)
    theta=dot(dot(inv(dot(phiT,phi)),phiT),yf)
    yNext=dot(y[0:na],theta)
    if yNext>y[0]:
        print 1
    else:
        print 0