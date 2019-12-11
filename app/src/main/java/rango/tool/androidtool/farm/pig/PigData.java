package rango.tool.androidtool.farm.pig;

import android.support.annotation.DrawableRes;

import rango.tool.androidtool.R;

public class PigData {
    private int grade;
    private @DrawableRes
    int drawableId;

    public void initData(int grade) {
        this.grade = grade;
        this.drawableId = getDrawableIdByGrade(grade);
    }

    public void upgrade() {
        this.grade++;
        this.drawableId = getDrawableIdByGrade(grade);
    }

    public int getGrade() {
        return grade;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public static @DrawableRes
    int getDrawableIdByGrade(int grade) {
        switch (grade) {
            default:
            case 0:
                return R.drawable.pig_1;
            case 1:
                return R.drawable.pig_2;
        }
    }
}
