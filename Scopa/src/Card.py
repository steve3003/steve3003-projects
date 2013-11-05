'''
Created on 04/nov/2013

@author: Stefano
'''

class Card(object):
    SEEDS = {0:"d", 1:"b", 2:"s", 3:"c"}
    
    def __init__(self, number, seed):
        self.number = number
        self.seed = seed
        self.id = Card.get_id(self.number, self.seed)
        
    def __repr__(self, *args, **kwargs):
        return str(self.number+1) + self.SEEDS[self.seed]

    @classmethod
    def get_id(cls, number, seed):
        return seed * 10 + number
    
    @classmethod
    def get_Card(cls, idn):
        return Card(idn % 10, int(idn / 10))
    
    @classmethod
    def toCards(cls, l):
        return [Card.get_Card(i) for i in l]
