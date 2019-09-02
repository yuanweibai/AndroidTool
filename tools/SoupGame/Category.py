import os
import sys
from bs4 import BeautifulSoup, Comment

def result():
	soup = BeautifulSoup(open('Game.html'))
	divList = soup.findAll('div')
	result = open('result.txt', "a")

	for div in divList:
		a = div.find('a')
		if not a is None:
			msg = a.get('href')
			c = msg.count('.')
			l = len(msg)
			if c > 1 and l < 80 and ('+' not in msg):
				splits = msg.split('=')
				result.write(splits[1]+'\n')
				print splits[1]


if __name__ == '__main__':
	result()