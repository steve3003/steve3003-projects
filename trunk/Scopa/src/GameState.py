'''
Created on 04/nov/2013

@author: Stefano
'''
from Card import Card
from State import State
import random

class GameState(object):
    REPR = """GameState:
    deck: %s
    me: %s
    you: %s
    me_hand: %s
    you_hand: %s
    field: %s
"""
    
    def __init__(self):
        self.initGame()
        
    def initGame(self):
        self.deck = self.genDeck()
        self.me = []
        self.you = []
        self.me_hand = sorted([self.deck.pop() for _ in range(3)])
        self.you_hand = sorted([self.deck.pop() for _ in range(3)])
        self.field = sorted([self.deck.pop() for _ in range(4)])

    def move(self, player, index_hand):
        if player == 0:
            card = self.me_hand[index_hand]
            self.me_hand.remove(card)
        else:
            card = self.you_hand[index_hand]
            self.you_hand.remove(card)
        picked_cards = self.can_pick(card)
        if picked_cards:
            if player == 0:
                self.me.extend([card, picked_cards])
            else:
                self.you.extend([card, picked_cards])
            self.field.remove(picked_cards)
        else:
            self.field.append(card)
        if len(self.me_hand) + len(self.you_hand) == 0:
            self.me_hand = sorted([self.deck.pop() for _ in range(3)])
            self.you_hand = sorted([self.deck.pop() for _ in range(3)])
        return (State(self.me, self.you, self.me_hand, self.field), State(self.you, self.me, self.you_hand, self.field))
        
    def can_pick(self, card):
        number = Card.get_Card(card).number
        for c in self.field:
            n = Card.get_Card(c).number
            if n == number:
                return c
        return False
    
    def genDeck(self):
        deck = []
        for _ in range(40):
            d = random.randint(0, 39)
            while d in deck:
                d = random.randint(0, 39)
            deck.append(d)
        return deck
    
    def __repr__(self, *args, **kwargs):
        return self.REPR % (Card.toCards(self.deck), Card.toCards(self.me), Card.toCards(self.you),
                            Card.toCards(self.me_hand), Card.toCards(self.you_hand), Card.toCards(self.field))
    
