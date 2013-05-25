#!/bin/python
'''
Created on 15/apr/2013

@author: Stefano
'''
# Head ends here

class Group(object):
    
    def __init__(self, cell):
        self.color = cell.color
        self.cells = []
        self.cells.append(cell)

    def isIn(self, cell):
        if self.color != cell.color:
            return False
        for c in self.cells:
            if c.isNear(cell):
                self.cells.append(cell)
                return True
        return False
    
    def merge(self, group):
        if(group.color != self.color):
            return False
        for c1 in self.cells:
            for c2 in group.cells:
                if c1.isNear(c2):
                    self.cells.extend(group.cells)
                    return True
        return False
    
    def copy(self):
        g = Group(self.cells[0].copy())
        for c in range(1, len(self.cells)):
            g.cells.append(self.cells[c].copy())
        return g
    
    def __repr__(self):
        return str(self.cells)

class Cell(object):
    def __init__(self, x, y, color):
        self.x = x
        self.y = y
        self.color = color
        
    def isNear(self, cell):
        return (self.x == cell.x and (self.y == cell.y + 1 or self.y == cell.y - 1)) or (self.y == cell.y and (self.x == cell.x + 1 or self.x == cell.x - 1))
    
    def __repr__(self):
        return str(self.x) + " " + str(self.y) + " " + str(self.color)
    
    def copy(self):
        return Cell(self.x, self.y, self.color)
    
def makeGroups(x, y, grid):
    groups = []
    for i, e in enumerate(grid):
        for j, k in enumerate(e):
            if k != '-':
                c = Cell(i, j, k)
                for g in groups:
                    if g.isIn(c):
                        break
                else:
                    groups.append(Group(c))                   
    return mergeGroups(groups)

def mergeGroups(groups):
    merge = True
    while merge:
        merge = False
        i = 0
        while i < len(groups):
            j = i + 1
            while j < len(groups):
                if groups[i].merge(groups[j]):
                    groups.remove(groups[j])
                    j -= 1
                    merge = True
                j += 1
            i += 1
    return groups

class Node(object):    
    def __init__(self, state, action, parent):
        self.state = state
        self.action = action
        self.parent = parent
        
class Action(object):
    def __init__(self, x, y):
        self.x = x
        self.y = y
    
    def doAction(self, state, group, x, y):
        nextState = []
        for i in state:
            k = []
            for j in i:
                k.append(j)
            nextState.append(k)
        for c in group.cells:
            nextState[c.x][c.y] = '-'
        for j in range(y):
            for i in range(x)[::-1]:
                if nextState[i][j] == '-':
                    k = i
                    while nextState[k][j] == '-':
                        k -= 1
                        if k < 0:
                            break
                    else:
                        for z in range(0, k + 1):
                            nextState[i - z][j] = nextState[k - z][j]
                            nextState[k - z][j] = '-'
        for j in range(y):
            if nextState[x - 1][j] == '-':
                k = j
                while nextState[x - 1][k] == '-':
                    k += 1
                    if k >= y:
                        break
                else:
                    for z in range(0, y - k):
                        for i in range(x):
                            nextState[i][j + z] = nextState[i][k + z]
                            nextState[i][k + z] = '-'
        return nextState
    
    def applicable(self, state, group):
        return len(group.cells) > 1

    def __repr__(self):
        return str(self.x) + " " + str(self.y)
        
def buildSolution(n):
    solution = []
    while not(n.action is None):
        solution.insert(0, n.action)
        n = n.parent
    return solution

def isGoal(groups):
    # return len([g for g in state if len(g.cells) < 2]) == 0
    return len(groups) == 0

def dfs(grid, x, y, color):
    frontier = [Node(grid, None, None)]
    minNode = frontier[0]
    maxScore = -1
    while len(frontier) != 0:
        n = frontier.pop(0)
        # printState(n.state, x, y)
        groups = makeGroups(x, y, n.state)
        score = (1 - sum([len(g.cells) for g in groups]) / 20.0) * 5 * color
        if maxScore < score:
            minNode = n
            maxScore = score
        groups.sort(key=lambda g: len(g.cells), reverse=True)
        # print groups
        if isGoal(groups):
            return buildSolution(n)
        for s in groups:
            a = Action(s.cells[0].x, s.cells[0].y)
            if a.applicable(n.state, s):
                ns = Node(a.doAction(n.state, s, x, y), a, n)
                frontier.insert(0, ns)
    return buildSolution(minNode)

def printState(state, x, y):
    for i in state:
        s = ""
        for k in i:
            s += k + " "
        print s
    print "***********"
    
def printGroups(groups, x, y):
    grid = [['-' for i in range(y)] for j in range(x)]
    for i, g in enumerate(groups):
        for c in g.cells:
            grid[c.x][c.y] = str(i)
    printState(grid, x, y)
    
import random
x = 5
y = 10
color = ['O', 'B']
grid = [[color[random.randint(0, len(color) - 1)] for i in range(y)] for j in range(x)]
grid = [
'RbRbRbRbRb',
'bRbRbRbRbR',
'RbRbRbRbRb',
'bRbRbRbRbR',
'RbRbRbRbRb']
# x, y, k = [ int(i) for i in raw_input().strip().split() ]
# 
# grid = []
# for i in xrange(0, x):
#     grid.append(raw_input().strip())
printState(grid, x, y)
groups = makeGroups(x, y, grid)
groups.sort(key=lambda g: len(g.cells))
print groups
printGroups(groups, x, y)
state = Action(1, 1).doAction(grid, groups[random.randint(0, len(groups) - 1)], x, y)
printState(state, x, y)
groups = makeGroups(x, y, state)
printGroups(groups, x, y)

print 'V' in "VIBGYOR"
