'''
Created on 04/nov/2013

@author: Stefano
'''
from Card import Card

REPR = """State:
    me: %s
    you: %s
    me_hand: %s
    field: %s
    value: %s
"""

class State(object):
    def __init__(self, me, you, me_hand, field):
        self.me = list(me)
        self.you = list(you)
        self.me_hand = list(me_hand)
        self.field = list(field)
        self.value = 0
        
    def __repr__(self, *args, **kwargs):
        return REPR % (Card.toCards(self.me), Card.toCards(self.you),
                            Card.toCards(self.me_hand), Card.toCards(self.field), self.value)