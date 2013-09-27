'''
Created on 28/mag/2013

@author: Stefano
'''
from numpy import asarray, dot, transpose
from numpy.linalg import inv

with open("Dota2GamePrediction.txt") as f:
    games=[[p for p in s.strip().split(",")] for s in f.readlines()]
namelist={}
number=0
for i,p in enumerate(games):
    for j,e in enumerate(p):
        if(e!='1' and e!='2'):
            if e not in namelist:
                namelist[e]=number;
                number+=1
            games[i][j]=namelist[e]
        else:
            games[i][j]=int(e)
print namelist
print len(namelist)
y=[]
with open("Dota2GamePredictionInput.txt",'w') as f, open("Dota2GamePredictionTarget.txt",'w') as f2:
    for i,p in enumerate(games):
        y.append([p[10]])
        f2.write(str(p[10]-1)+"\n")
        del games[i][10]
        win1=[-1 for k in range(len(namelist))]
        win2=[-1 for k in range(len(namelist))]
        for j,e in enumerate(games[i]):
            if j<5:
                win1[e]=1
            else:
                win2[e]=1
        for h in win1:
            f.write(str(h)+" ")
        for h in win2:
            f.write(str(h)+" ")
        f.write("\n")       
# y=asarray(y)
# phi=asarray(games)
# phiT=transpose(phi)
# theta=dot(dot(inv(dot(phiT,phi)),phiT),y)
# print "insert data:"
# x=[namelist[e] for e in raw_input().strip().split(",")]
# x=asarray(x)
# print dot(x,theta)