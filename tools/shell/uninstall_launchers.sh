#!/usr/bin/env bash
PACKAGES=("com.honeycomb.launcher" 
	"com.homescreen.phone.theme" 
	"com.smart.color.phone.emoji" 
	"com.home.screen.live.HD.wallpaper" 
	"com.home.HD.phone.theme.free" 
	"com.phone.fast.boost.cleaner" 
	"com.callerscreen.color.phone.ringtone.flash" 
	"com.emoji.face.sticker.home.screen" 
	"com.easy.cool.next.home.screen" 
	"com.speed.theme.personalized.home" 
    "com.emoticon.screen.home.launcher"
    "com.wallpaper.live.launcher"
    "com.fortune.zodiac.launcher"
    "com.sticker.gif.crazy.launcher"
	)

for package in ${PACKAGES[*]}
do
	adb uninstall $package
done
