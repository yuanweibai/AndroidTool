echo '查询进程占用文件句柄数'
set `adb shell ps |grep com.Android56 |grep -v channel |grep -v Daemon`
pidnum=$2
index=0
while true
do
index=$[index+1]
echo '##################'
echo '第'$index'次查询'
echo '总句柄'
adb shell ls -l /proc/$pidnum/fd |grep "" -c
echo 'anon句柄'
adb shell ls -l /proc/$pidnum/fd |grep anon -c
echo '##################'
sleep 2
done