'''
Created on 04/nov/2013

@author: Stefano
'''
from Card import Card
from State import State
from itertools import combinations
from random import randint

REPR = """GameState:
    deck: %s
    me: %s
    you: %s
    me_hand: %s
    you_hand: %s
    field: %s
"""

class GameState(object):   
    def __init__(self):
        self.initGame()
        
    def initGame(self):
        self.deck = self.genDeck()
        self.me = []
        self.you = []
        self.distribute_cards()
        self.field = sorted([self.deck.pop() for _ in range(4)])
        self.last_to_pick = 0

    def move(self, player, index_hand, picked_cards=None):
        if player == 0:
            card = self.me_hand.pop(index_hand)
        else:
            card = self.you_hand.pop(index_hand)
        if picked_cards:
            if not (Card.get_Card(card).can_pick(Card.toCards(picked_cards)) and
                    next([False for i in picked_cards if i not in self.field], True)):
                picked_cards = self.can_pick(card)
        else:
            picked_cards = self.can_pick(card)
        if picked_cards:
            self.last_to_pick = player
            if player == 0:
                self.sorted_insert(self.me, card)
            else:
                self.sorted_insert(self.you, card)
            for p in picked_cards:
                if player == 0:
                    self.sorted_insert(self.me, p)
                else:
                    self.sorted_insert(self.you, p)
                self.field.remove(p)
        else:
            self.sorted_insert(self.field, card)
        if len(self.me_hand) + len(self.you_hand) == 0:
            if self.deck:
                self.distribute_cards()
            else:
                for c in self.field:
                    if self.last_to_pick == 0:
                        self.sorted_insert(self.me, c)
                    else:
                        self.sorted_insert(self.you, c)
                self.field = []
        return (State(self.me, self.you, self.me_hand, self.field), State(self.you, self.me, self.you_hand, self.field))
        
    def can_pick(self, c):
        card = Card.get_Card(c)
        field_cards = Card.toCards(self.field)
        for l in range(1, len(self.field) + 1):
            for picked_cards in combinations(field_cards, l):
                if card.can_pick(picked_cards):
                    return Card.toIds(picked_cards)
        return False
    
    def distribute_cards(self):
        self.me_hand = sorted([self.deck.pop() for _ in range(3)])
        self.you_hand = sorted([self.deck.pop() for _ in range(3)])
    
    def genDeck(self):
        deck = []
        for _ in range(40):
            d = randint(0, 39)
            while d in deck:
                d = randint(0, 39)
            deck.append(d)
        return deck
    
    def __repr__(self, *args, **kwargs):
        return REPR % (Card.toCards(self.deck), Card.toCards(self.me), Card.toCards(self.you),
                            Card.toCards(self.me_hand), Card.toCards(self.you_hand), Card.toCards(self.field))
        
    def sorted_insert(self, l, value):
        index = next((i for i in range(len(l)) if l[i] >= value), len(l))
        l.insert(index, value)
