
1. webp_all.sh: 支持转换静态图和gif图
2. webpmux_files.sh: 支持把序列帧(无论是否是webp格式的图)转换成动态的webp图，此命令后可加一个参数，表示帧之间的时间间隔，如果不加，则默认是45ms

例子：webpmux_files.sh
1. 打开终端，切换至“....../App_ColorSMS_Android/tools/webp”目录
2. 输入“./webpmux_files.sh” 或者 “./webpmux_files.sh 30”，并且enter
3. 输入要转换的文件夹，enter