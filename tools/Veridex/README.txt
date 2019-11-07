Veridex ---- Android 扫描非法 API 方法

1. 切换到 veridex-mac 目录，把 result.txt 删除，因为结果会保存在此文件中；
2. 执行脚本：./appcompat.sh --dex-file={apk 路径} >> result.txt，如：./appcompat.sh --dex-file=/Users/yuanweibai/Documents/douyin.apk >> result.txt
3. 可以在 result.txt 中查看结果；

imprecise 标志会导致输出更加详细，但也有可能是误报，酌情使用，如：./appcompat.sh --dex-file={apk 路径} --imprecise >> result.txt