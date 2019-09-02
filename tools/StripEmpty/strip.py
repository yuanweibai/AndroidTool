import os
import sys

def main():
	result = open('result.txt','r')
	for line in result.readlines():
		line.strip('\n')

if __name__ == '__main__':
	main()