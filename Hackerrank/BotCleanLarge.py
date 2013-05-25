'''
Created on 03/apr/2013

@author: Stefano
'''
class Action(object):
    def doAction(self, state):
        pass
    
    def applicable(self, state):
        pass
     
class RightA(Action):
    def doAction(self, state):
        ns = state.copy()
        ns.posy += 1
        return ns
    
    def applicable(self, state):
        return state.posy + 1 < len(state.board[0])
    
    def __repr__(self):
        return "RIGHT"
    
class LeftA(Action):
    def doAction(self, state):
        ns = state.copy()
        ns.posy -= 1
        return ns
    
    def applicable(self, state):
        return state.posy - 1 >= 0
    
    def __repr__(self):
        return "LEFT"
    
class DownA(Action):
    def doAction(self, state):
        ns = state.copy()
        ns.posx += 1
        return ns
    
    def applicable(self, state):
        return state.posx + 1 < len(state.board)
    
    def __repr__(self):
        return "DOWN"
    
class UpA(Action):
    def doAction(self, state):
        ns = state.copy()
        ns.posx -= 1
        return ns
    
    def applicable(self, state):
        return state.posx - 1 >= 0
    
    def __repr__(self):
        return "UP"
    
class CleanA(Action):
    def doAction(self, state):
        ns = state.copy()
        ns.board[ns.posx][ns.posy] = '-'
        return ns
    
    def applicable(self, state):
        return state.board[state.posx][state.posy] == 'd'
    
    def __repr__(self):
        return "CLEAN"

class Node(object):    
    def __init__(self, state, action, parent):
        self.state = state
        self.action = action
        self.parent = parent

class State(object):    
    def __init__(self, posx, posy, board):
        self.posx = posx
        self.posy = posy
        self.board = board
        
    def copy(self):
        board = [[j for j in i] for i in self.board]
        return State(self.posx, self.posy, board)
    
    def equals(self, state):
        if(self.posx != state.posx or self.posy != state.posy):
            return False
        for i, e in enumerate(state.board):
            for j, h in enumerate(e):
                if h != self.board[i][j]:
                    return False
        return True

def bfs(posx, posy, board):
    frontier = [Node(State(posx, posy, board), None, None)]
    actions = [CleanA(), RightA(), LeftA(), UpA(), DownA()]
    visited = [frontier[0].state]
    while len(frontier) != 0:
        n = frontier.pop(0)
        if isGoal(n.state):
            return buildSolution(n)
        for i, a in enumerate(actions):
            if a.applicable(n.state):
                ns = Node(a.doAction(n.state), a, n)
                if not(contains(ns.state, visited)):
                    frontier.append(ns)
                    visited.append(ns.state)
                if(i == 0):
                    return buildSolution(ns)
    return None

def contains(el, l):
    for i in l:
        if(i.equals(el)):
            return True
    return False
        
def isGoal(state):
    for i in state.board:
        for j in i:
            if j != '-' and j != 'b':
                return False
    return True

def buildSolution(n):
    solution = []
    while not(n.action is None):
        solution.insert(0, n.action)
        n = n.parent
    return solution


# Head ends here
def next_move(posx, posy, dimx, dimy, board):
    solution = bfs(posx, posy, board)
    print solution[0]

# Tail starts here
if __name__ == "__main__":
    pos = [int(i) for i in raw_input().strip().split()]
    dim = [int(i) for i in raw_input().strip().split()]
    board = [[j for j in raw_input().strip()] for i in range(dim[0])]
    next_move(pos[0], pos[1], dim[0], dim[1], board)
