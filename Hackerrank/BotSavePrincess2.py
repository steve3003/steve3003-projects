'''
Created on 05/apr/2013

@author: Stefano
'''
def princessIndex(m,grid):
    for i in range(m):
        for j in range(m):
            if(grid[i][j]=="p"):
                return [i,j]

# Head ends here
def nextMove(n,x,y,grid):
    prin=princessIndex(n,grid)
    bot=[x,y]
    if prin[0]<bot[0]:
        print "UP"
        return
    elif prin[0]>bot[0]:
        print "DOWN"
        return
    if prin[1]<bot[1]:
        print "LEFT"
        return
    elif prin[1]>bot[1]:
        print "RIGHT"
        return
# Tail starts here
n = input()
x,y = [int(i) for i in raw_input().strip().split()]
grid = []
for i in xrange(0, n):
    grid.append(raw_input())

nextMove(n,x,y,grid)