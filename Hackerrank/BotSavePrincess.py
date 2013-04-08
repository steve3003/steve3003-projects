'''
Created on 05/apr/2013

@author: Stefano
'''
def princessIndex(m,grid):
    for i in range(m):
        for j in range(m):
            if(grid[i][j]=="p"):
                return [i,j]

def botIndex(m,grid):
    for i in range(m):
        for j in range(m):
            if(grid[i][j]=="m"):
                return [i,j]

def displayPathtoPrincess(m,grid):
    prin=princessIndex(m,grid)
    bot=botIndex(m,grid)
    if prin[0]<bot[0]:
        for i in range(bot[0]-prin[0]):
            print "UP"
    elif prin[0]>bot[0]:
        for i in range(prin[0]-bot[0]):
            print "DOWN"
    if prin[1]<bot[1]:
        for i in range(bot[1]-prin[1]):
            print "LEFT"
    elif prin[1]>bot[1]:
        for i in range(prin[1]-bot[1]):
            print "RIGHT"
# Tail starts here
m = input()

grid = []
for i in xrange(0, m):
    grid.append(raw_input().strip())

displayPathtoPrincess(m,grid)