package rango.tool.androidtool.falling;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseHelper {

    private static final String TAG = "PARSE_HELPER";

    private static final Pattern PATTERN_CMD_NUM = Pattern.compile("(\\s*)(M|m|Z|z|L|l|H|h|V|v|C|c|S|s|Q|q|T|t)(((-?\\d+)(\\.\\d+)?)((,|\\s*)(-?\\d+)(\\.\\d+)?)*)*");
    private static final Pattern PATTERN_CMD = Pattern.compile("(M|m|Z|z|L|l|H|h|V|v|C|c|S|s|Q|q|T|t)");
    private static final Pattern PATTERN_NUM = Pattern.compile("(-?\\d+)(\\.\\d+)?");

    public static class PathDataUnit {
        char cmd;
        List<Float> data = new ArrayList<>();

        @Override
        public String toString() {
            return "cmd = " + cmd + " " + data;
        }
    }

    private String pathData;
    private List<PathDataUnit> pathDataUnitList = new ArrayList<>();

    public ParseHelper(String pathData) {
        this.pathData = pathData;
    }

    public List<PathDataUnit> getPathDataUnitList() {
        return pathDataUnitList;
    }

    public void parserToPathDataUnits() {
        if (TextUtils.isEmpty(pathData)) {
            return;
        }

        pathDataUnitList.clear();

        List<String> pathDataUnitStrList = new ArrayList<>();

        Matcher matcherUnit = PATTERN_CMD_NUM.matcher(pathData);
        while (matcherUnit.find()) {
            String pathDataUnitStr = matcherUnit.group();
            pathDataUnitStrList.add(pathDataUnitStr);

            Log.d(TAG, "find: " + pathDataUnitStr);
        }

        for (String pathDataUnitStr : pathDataUnitStrList) {
            Matcher matcherCmd = PATTERN_CMD.matcher(pathDataUnitStr);
            if (!matcherCmd.find()) {
                continue;
            }

            PathDataUnit pathDataUnit = new PathDataUnit();
            pathDataUnit.cmd = matcherCmd.group().charAt(0);

            Matcher matcherNum = PATTERN_NUM.matcher(pathDataUnitStr);
            while (matcherNum.find()) {
                pathDataUnit.data.add(Float.parseFloat(matcherNum.group()));
            }

            pathDataUnitList.add(pathDataUnit);

            Log.d(TAG, "pathDataUnit: " + pathDataUnit.toString());
        }
    }
}
