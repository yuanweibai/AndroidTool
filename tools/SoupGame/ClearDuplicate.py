rFile = open("result.txt", "r")
wFile = open("newResult.txt", "w")
allLine = rFile.readlines()
rFile.close()
h = {}
for l in allLine:
	splits = l.split(':')
	key = splits[0]
	if not h.has_key(key):
		h[key]=1
		wFile.write(l)
wFile.close()