#!/usr/bin/env bash
echo "Please enter file folder path"
read fileFolderPath
mkdir $fileFolderPath/0_webp/
for file in $fileFolderPath/*
do
	if [[ -d $file ]]; then
		echo $file "是一个文件夹"
	else
		echo $file
		fullname=$(basename $file)
		filename=$(echo $fullname | cut -d . -f1)
		echo $filename
		./bin/gif2webp -q 75 -lossy -m 6 $file -o $fileFolderPath/0_webp/$filename.webp
	fi
done