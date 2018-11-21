#!/usr/bin/env bash
echo "Please enter file folder path"
read fileFolderPath
mkdir $fileFolderPath/webp/
for file in $fileFolderPath/*
do
	if [[ -d $file ]]; then
		echo $file "是一个文件夹"
	else
		echo $file
		fullname=$(basename $file)
		filename=$(echo $fullname | cut -d . -f1)
		echo $filename
		./bin/cwebp -q 75 $file -o $fileFolderPath/webp/$filename.webp
	fi
done

echo "Cwebp images successfully!!!"
