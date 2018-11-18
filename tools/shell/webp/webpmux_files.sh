#!/usr/bin/env bash
echo "Please enter file folder path"
read fileFolderPath
mkdir $fileFolderPath/webp
anim=""
for file in $fileFolderPath/*
do
	if [[ -d $file ]]; then
		echo $file "是一个文件夹"
	else
		echo $file
		fullname=$(basename $file)
		filename=$(echo $fullname | cut -d . -f1)
		echo $filename
		if [[ ${file##*.} != "webp" ]]; then
			cwebp -q 75 $file -o $fileFolderPath/webp/$filename.webp
			anim=$anim"-frame "$fileFolderPath/webp/$filename.webp" +45+0+0+0-b "
		else 
			anim=$anim"-frame "$file" +45+0+0+0-b "
		fi
	fi
done
mkdir $fileFolderPath/webp/anim
echo "---------webpmux command: " $anim
webpmux $anim -loop 0 -o $fileFolderPath/webp/anim/anim.webp