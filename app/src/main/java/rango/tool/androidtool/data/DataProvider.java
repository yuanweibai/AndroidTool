package rango.tool.androidtool.data;

import java.util.ArrayList;
import java.util.List;

import rango.tool.androidtool.base.list.adapter.BaseItemData;
import rango.tool.androidtool.base.list.adapter.BaseItemType;

public class DataProvider {

    private static List<String> imageList = new ArrayList<>();

    static {
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/transformer.png");
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/emoji.png");
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/istyle.png");
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/magicandroid.png");
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/magicluxury.png");
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/applex.png");
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/s7.png");
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/s8.png");
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/thor/banner.png");
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/miniworld.png");
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/aircraft3d.png");
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/androidm.png");
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/ballpoint.png");
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/bluewolf.png");
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/business.png");
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/butterfly.png");
        imageList.add("http://cdn.appcloudbox.net/launcherapps/apps/launcher/themes/themes_hot_themebanner/chalk.png");

    }

    public static List<BaseItemData> getImageListActivityData() {
        List<BaseItemData> dataList = new ArrayList<>();
        for (String string : imageList) {
            dataList.add(new BaseItemData(string, BaseItemType.TYPE_IMAGE_LIST_NROMAL));
        }
        return dataList;
    }

    public static String getMsg() {
        return "kflajsdkjlkjfalksdjflkjlkjsflakjsdflkajsdfl;aksdjflas;dkfjasldkfjas" +
                "ldkfjalkflajsdkjlkjfalksdjflkjlkjsflakjsdflkajsdfl;aksdjflas;dkfjasldkf" +
                "jasldkfjalkflajsdkjlkjfalksdjflkjlkjsflakjsdflkajsdfl;aksdjflas;dkfjasldkf" +
                "jasldkfjalkflajsdkjlkjfalksdjflkjlkjsflakjsdflkajsdfl;aksdjflas;dkfjasldkfjasl" +
                "dkfjalkflajsdkjlkjfalksdjflkjlkjsflakjsdflkajsdfl;aksdjflas;dkfjasldkfjasldkfjalk" +
                "flajsdkjlkjfalksdjflkjlkjsflakjsdflkajsdfl;aksdjflas;dkfjasldkfjasldkfjalkflajsdkjlkj" +
                "falksdjflkjlkjsflakjsdflkajsdfl;aksdjflas;dkfjasldkfjasldkfjalkflajsdkjlkjfalksdjflkjlkjsfla" +
                "kjsdflkajsdfl;aksdjflas;dkfjasldkfjasldkfjalkflajsdkjlkjfalksdjflkjlkjsflakjsdflkajsdfl;aksdjfla" +
                "s;dkfjasldkfjasldkfjalkflajsdkjlkjfalksdjflkjlkjsflakjsdflkajsdfl;aksdjflas;dkfjasldkfjasldkfjal" +
                "kflajsdkjlkjfalksdjflkjlkjsflakjsdflkajsdfl;aksdjflas;dkfjasldkfjasldkfjalkflajsdkjlkjfalksdjf" +
                "lkjlkjsflakjsdflkajsdfl;aksdjflas;dkfjasldkfjasldkfjal";
    }
}
