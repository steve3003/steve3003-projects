'''
Created on 04/nov/2013

@author: Stefano
'''
from GameState import GameState

g = GameState()
print g
for i in range(6):
    print g.move(i%2, 0)[0]
print g