反编译工具

apktool

apktool 不适合查看 java 代码，适合查看一些资源文件，如 res、assets、AndroidManifest.xml等

1. 切换到 apktool 的目录，如：cd /Users/yuanweibai/Documents/tool/apktool
2. 执行命令：./apktool d -o {反编译结果输出路径}  -f {需要反编译的 apk 路径}；如：./apktool d -o /Users/yuanweibai/Documents/tool/apktool/result -f /Users/yuanweibai/Documents/douyin.apk




jadx

反编译工具，适合查看代码、看资源，就是反编译速度较慢

1. 打开 jadx/bin/jadx-gui 即可；
2. 选择要反编译的 apk;
