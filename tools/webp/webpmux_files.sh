#!/usr/bin/env bash
duration=45
if [[ $1 -gt 0 ]]; then
	duration=$1
fi

echo "Please enter file folder path"
read fileFolderPath
mkdir $fileFolderPath/0_webp
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
			./webp-bin/cwebp -q 75 $file -o $fileFolderPath/0_webp/$filename.webp
			anim=$anim"-frame "$fileFolderPath/0_webp/$filename.webp" +$duration+0+0+0-b "
		else
			anim=$anim"-frame "$file" +$duration+0+0+0-b "
		fi
	fi
done
mkdir $fileFolderPath/0_webp/0_anim_webp
echo "---------webpmux command: " $anim
./webp-bin/webpmux $anim -loop 0 -o $fileFolderPath/0_webp/0_anim_webp/anim.webp

