import os
import sys
from bs4 import BeautifulSoup, Comment

def category(name):
	soup = BeautifulSoup(open('App.html'))
	lis = soup.findAll('li')
	category = open('category.json', "a")

	for li in lis:
		msg = li.find('a').get('href')
		splits = msg.split('=');

		print splits[1]
		category.write('"'+splits[1]+'": "' + name+'",'+'\n')

if __name__ == '__main__':
	category(sys.argv[1])