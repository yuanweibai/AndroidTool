#!/usr/bin/env bash
function webpFile(){
    if [[ ${1##*.} == "gif" ]]; then
        if [[ ! -d ${1%/*}/0_gif_webp ]]; then
            mkdir ${1%/*}/0_gif_webp
        fi
        fullname=$(basename $1)
        filename=$(echo $fullname | cut -d . -f1)
        ./webp-bin/gif2webp -q 75 -lossy -m 6 $1 -o ${1%/*}/0_gif_webp/$filename.webp

    elif [[ ${1##*.} == "jpg" || ${1##*.} == "png" ]]; then
        if [[ ! -d ${1%/*}/0_webp ]]; then
            echo "创建 0_webp文件夹"
            mkdir ${1%/*}/0_webp
        fi
        fullname=$(basename $1)
        filename=$(echo $fullname | cut -d . -f1)
        ./webp-bin/cwebp -q 75 $1 -o ${1%/*}/0_webp/$filename.webp

    else 
        echo "webp 文件不作任何处理！！！"
    fi
}

function read_dir(){
    for file in `ls $1`
        do
           if [[ $file == "0_webp" || $file == "0_gif_webp" ]]
            then
                echo "0_webp 和 0_gif_webp文件夹不作任何处理！！！"
            elif [ -d $1"/"$file ]  
            then
                echo "-----------------------------" $file
                read_dir $1"/"$file
            else
                webpFile $1"/"$file
            fi
         done
}   
echo "Please enter file folder path"
read fileFolderPath
read_dir $fileFolderPath
