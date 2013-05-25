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
    
def makeGroups(x, y, color, grid):
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
        # it is wrong for sure
        cgroup = []
        for g in state:
            if(g != group):
                cgroup.append(g.copy())
        for i in range(y):
            column = [c for c in group.cells if c.y == i]
            if len(column) > 0:
                maxR = max(column, key=lambda c: c.x).x
                minR = min(column, key=lambda c: c.x).x
                dim = maxR - minR + 1
                foundMiddle = 0
                for g in cgroup:
                    for c in g.cells:
                        if c.x > minR and c.x < maxR and c.y == i:
                            c.x = maxR - foundMiddle
                            foundMiddle += 1
                    for c in g.cells:
                        if c.x < minR and c.y == i:
                            c.x += dim - foundMiddle
        i = 0
        while i < y:
            for g in cgroup:
                column = [c for c in g.cells if c.y == i]
                if len(column) > 0:
                    break
            else:
                j = i + 1
                zero = True
                while zero and j < y:
                    zero = True
                    for g in cgroup:
                        column = [c for c in g.cells if c.y == j]
                        if len(column) > 0:
                            zero = False
                            break
                    else:
                        j += 1
                dim = j - i
                for g in cgroup:
                    for c in g.cells:
                        if c.y > i:
                            c.y -= dim
                if j >= y:
                    i = y
                else:
                    i -= 1
            i += 1
        return mergeGroups(cgroup)
    
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

def isGoal(state):
    # return len([g for g in state if len(g.cells) < 2]) == 0
    return len(state) == 0

def dfs(groups, x, y):
    frontier = [Node(groups, None, None)]
    while len(frontier) != 0:
        n = frontier.pop(0)
        printState(n.state, x, y)
        if isGoal(n.state):
            return buildSolution(n)
        for s in n.state:
            a = Action(s.cells[0].x, s.cells[0].y)
            if a.applicable(n.state, s):
                ns = Node(a.doAction(n.state, s, x, y), a, n)
                frontier.insert(0, ns)
    return None

def printState(state, x, y):
    grid = [['-' for j in range(y)] for i in range(x)]
    for g in state:
        for c in g.cells:
            grid[c.x][c.y] = c.color
    for i in grid:
        s = ""
        for k in i:
            s += k
        print s
    print "***********"
    
import os

def nextMove(x, y, color, grid):
    groups = makeGroups(x, y, color, grid)
    groups.sort(key=lambda g: len(g.cells))
#     solution = dfs(groups, x, y)
#     print solution[0]
    filename = "myfile.txt"
    if not os.path.exists(filename):    
        with open(filename, "w") as f:
            f.write("0\n")
            solution = dfs(groups, x, y)
            for move in solution:
                f.write(str(move) + "\n")
    with open(filename) as f:
        index = f.readlines()
    index[0] = str(int(index[0]) + 1) + "\n"
    with open(filename, "w") as f:
        for move in index:
            f.write(move)
    print index[int(index[0])]
# Tail starts here
x, y, k = [ int(i) for i in raw_input().strip().split() ]

grid = []
for i in xrange(0, x):
    grid.append(raw_input().strip())

nextMove(x, y, k, grid)
