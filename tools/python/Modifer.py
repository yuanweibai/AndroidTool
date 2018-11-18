# coding:utf-8
# 替换指定目录下以及其子目录下的所有文件名字中的指定字符串。

import os
import sys

def modify_prefix(path,old_content,new_content):
	all_file_list = os.listdir(path)
	for file_name in all_file_list:
		current_dir = os.path.join(path,file_name)
		if os.path.isdir(current_dir):
			modify_prefix(current_dir,old_content,new_content)
		fname = os.path.splitext(file_name)[0]
		ftype = os.path.splitext(file_name)[1]
		if old_content in fname:
			fdcount[0]+=1
			replname = fname.replace(old_content,new_content)
			newname = os.path.join(path,replname+ftype)
			os.rename(current_dir,newname)

if __name__ == '__main__':
	fdcount=[0]
	if len(sys.argv)==2 and "-h" in sys.argv[1]:
		print unicode("\n[+] 用法: python Modifer.py  [指定目录] [参数1] [参数2]","utf-8")
		print unicode("[+] [指定目录]:需要修改的文件夹目录     [参数1]: 需要替换的字符      [参数2]:替换后的字符串","utf-8")
		print unicode("[+] 用法示例:python modifer.py /Users/yuanweibai/Desktop/切图 emoji image","utf-8")
		sys.exit()
	elif os.path.isdir(sys.argv[1]) is False:
		print unicode("\n[+] 指定目录并不是一个文件夹，请输入正确的文件夹目录！","utf-8")
		print unicode("[+] 用命令：python Modifer.py -h 来查看Modifer.py的用法","utf-8")
	else:
		modify_prefix(sys.argv[1],sys.argv[2],sys.argv[3])
		print unicode("[+] 修改完成！","utf-8")
		print unicode("[+] 共修改%s个文件名"%fdcount[0],"utf-8")