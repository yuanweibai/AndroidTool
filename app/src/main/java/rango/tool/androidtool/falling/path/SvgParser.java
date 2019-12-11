package rango.tool.androidtool.falling.path;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.XmlRes;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class SvgParser {

    private static final String TAG = "SVG_PARSER";

    private static final String SVG_TAG_VECTOR = "vector";
    private static final String SVG_TAG_PATH = "path";
    private static final String SVG_ATTR_VIEWPORT_WIDTH = "viewportWidth";
    private static final String SVG_ATTR_VIEWPORT_HEIGHT = "viewportHeight";
    private static final String SVG_ATTR_FILL_COLOR = "fillColor";
    private static final String SVG_ATTR_PATH_DATA = "pathData";
    private static final String SVG_ATTR_NAME = "name";

    @IntDef({ PATH_MOVE, PATH_LINE, PATH_ARC, PATH_CUBIC })
    @Retention(RetentionPolicy.SOURCE)
    public @interface PathType {
    }

    public static final int PATH_MOVE = 0;
    public static final int PATH_LINE = 1;
    public static final int PATH_ARC = 2;
    public static final int PATH_CUBIC = 3;

    public static class PathUnit {

        @PathType
        public final int pathKey;
        public final PointF pointEnd;

        public PathUnit(@PathType int pathKey, PointF pointEnd) {
            this.pathKey = pathKey;
            this.pointEnd = pointEnd;
        }
    }

    public static class PathMoveData extends PathUnit {
        public final PointF pointMove;

        public PathMoveData(PointF pointMove) {
            super(PATH_MOVE, pointMove);

            this.pointMove = pointMove;
        }

        @Override
        public String toString() {
            return "PathMoveData " + pointMove.x + ", " + pointMove.y;
        }
    }

    public static class PathLineData extends PathUnit {
        public final PointF pointLineStart;
        public final PointF pointLineEnd;

        public PathLineData(PointF pointLineStart, PointF pointLineEnd) {
            super(PATH_LINE, pointLineEnd);

            this.pointLineStart = pointLineStart;
            this.pointLineEnd = pointLineEnd;
        }

        @Override
        public String toString() {
            return "PathLineData " + pointLineStart.x + ", " + pointLineStart.y
                + " " + pointLineEnd.x + ", " + pointLineEnd.y;
        }
    }

    public static class PathCubicData extends PathUnit {
        public final PointF pointCubicStart;
        public final PointF pointCubicCtr1;
        public final PointF pointCubicCtr2;
        public final PointF pointCubicEnd;

        public PathCubicData(PointF pointCubicStart, PointF pointCubicCtr1,
                             PointF pointCubicCtr2, PointF pointCubicEnd) {

            super(PATH_CUBIC, pointCubicEnd);

            this.pointCubicStart = pointCubicStart;
            this.pointCubicCtr1 = pointCubicCtr1;
            this.pointCubicCtr2 = pointCubicCtr2;
            this.pointCubicEnd = pointCubicEnd;
        }

        @Override
        public String toString() {
            return "PathCubicData "
                + pointCubicStart.x + ", " + pointCubicStart.y + " "
                + pointCubicCtr1.x + ", " + pointCubicCtr1.y + " "
                + pointCubicCtr2.x + ", " + pointCubicCtr2.y + " "
                + pointCubicEnd.x + ", " + pointCubicEnd.y;
        }
    }

    public static class PathArcData extends PathUnit {

        public final PointF pointArcStart;
        public final PointF pointArcEnd;

        public double rx;
        public double ry;
        public double angle;
        public boolean largeArcFlag;
        public boolean sweepFlag;

        public final RectF oval = new RectF();
        public float angleStart;
        public float angleExtent;

        public PathArcData(PointF pointArcStart, PointF pointArcEnd, double rx, double ry,
                           double angle, boolean largeArcFlag, boolean sweepFlag) {

            super(PATH_ARC, pointArcEnd);

            this.pointArcStart = pointArcStart;
            this.pointArcEnd = pointArcEnd;

            this.rx = rx;
            this.ry = ry;
            this.angle = angle;
            this.largeArcFlag = largeArcFlag;
            this.sweepFlag = sweepFlag;

            transform();
        }

        public void transform() {

            double x0 = pointArcStart.x;
            double y0 = pointArcStart.y;
            double x = pointArcEnd.x;
            double y = pointArcEnd.y;
            double rx = this.rx;
            double ry = this.ry;
            double angle = this.angle;
            boolean largeArcFlag = this.largeArcFlag;
            boolean sweepFlag = this.sweepFlag;

            double dx2 = (x0 - x) / 2.0;
            double dy2 = (y0 - y) / 2.0;

            angle = Math.toRadians(angle % 360.0);
            double cosAngle = Math.cos(angle);
            double sinAngle = Math.sin(angle);

            double x1 = (cosAngle * dx2 + sinAngle * dy2);
            double y1 = (-sinAngle * dx2 + cosAngle * dy2);
            rx = Math.abs(rx);
            ry = Math.abs(ry);

            double Prx = rx * rx;
            double Pry = ry * ry;
            double Px1 = x1 * x1;
            double Py1 = y1 * y1;

            // check that radii are large enough
            double radiiCheck = Px1 / Prx + Py1 / Pry;
            if (radiiCheck > 1) {
                rx = Math.sqrt(radiiCheck) * rx;
                ry = Math.sqrt(radiiCheck) * ry;
                Prx = rx * rx;
                Pry = ry * ry;
            }

            // Step 2 : Compute (cx1, cy1)
            double sign = (largeArcFlag == sweepFlag) ? -1 : 1;
            double sq = ((Prx * Pry) - (Prx * Py1) - (Pry * Px1))
                / ((Prx * Py1) + (Pry * Px1));
            sq = (sq < 0) ? 0 : sq;
            double coef = (sign * Math.sqrt(sq));
            double cx1 = coef * ((rx * y1) / ry);
            double cy1 = coef * -((ry * x1) / rx);

            double sx2 = (x0 + x) / 2.0;
            double sy2 = (y0 + y) / 2.0;
            double cx = sx2 + (cosAngle * cx1 - sinAngle * cy1);
            double cy = sy2 + (sinAngle * cx1 + cosAngle * cy1);

            // Step 4 : Compute the angleStart (angle1) and the angleExtent (dangle)
            double ux = (x1 - cx1) / rx;
            double uy = (y1 - cy1) / ry;
            double vx = (-x1 - cx1) / rx;
            double vy = (-y1 - cy1) / ry;
            double p, n;

            // Compute the angle start
            n = Math.sqrt((ux * ux) + (uy * uy));
            p = ux; // (1 * ux) + (0 * uy)
            sign = (uy < 0) ? -1.0 : 1.0;
            double angleStart = Math.toDegrees(sign * Math.acos(p / n));

            // Compute the angle extent
            n = Math.sqrt((ux * ux + uy * uy) * (vx * vx + vy * vy));
            p = ux * vx + uy * vy;
            sign = (ux * vy - uy * vx < 0) ? -1.0 : 1.0;
            double angleExtent = Math.toDegrees(sign * Math.acos(p / n));
            if (!sweepFlag && angleExtent > 0) {
                angleExtent -= 360f;
            } else if (sweepFlag && angleExtent < 0) {
                angleExtent += 360f;
            }
            angleExtent %= 360f;
            angleStart %= 360f;

            oval.set((float) (cx - rx), (float) (cy - ry), (float) (cx + rx), (float) (cy + ry));
            this.angleStart = (float) angleStart;
            this.angleExtent = (float) angleExtent;
        }
    }

    public static class PathDataInfo {
        public final List<PointF> pointDataField = new ArrayList<>();

        public String name;
        public int fillColor;
        public final List<PathUnit> pathUnits = new ArrayList<>();
        public final Path path = new Path();

        public void updatePath() {
            path.reset();

            for (PathUnit pathUnit : pathUnits) {

                switch (pathUnit.pathKey) {

                    case PATH_MOVE:

                        PathMoveData pathMoveData = (PathMoveData) pathUnit;
                        path.moveTo(pathMoveData.pointMove.x, pathMoveData.pointMove.y);

                        break;

                    case PATH_LINE:

                        PathLineData pathLineData = (PathLineData) pathUnit;
                        path.lineTo(pathLineData.pointLineEnd.x, pathLineData.pointLineEnd.y);

                        break;

                    case PATH_CUBIC:

                        PathCubicData pathCubicData = (PathCubicData) pathUnit;
                        path.cubicTo(pathCubicData.pointCubicCtr1.x, pathCubicData.pointCubicCtr1.y,
                            pathCubicData.pointCubicCtr2.x, pathCubicData.pointCubicCtr2.y,
                            pathCubicData.pointCubicEnd.x, pathCubicData.pointCubicEnd.y);

                        break;

                    case PATH_ARC:

                        PathArcData pathArcData = (PathArcData) pathUnit;
                        path.addArc(pathArcData.oval, pathArcData.angleStart, pathArcData.angleExtent);

                        break;

                    default:
                }
            }
        }
    }

    public static class SvgDataInfo {

        public final List<PointF> pointDataField = new ArrayList<>();

        public float viewportWidth;
        public float viewportHeight;
        public final List<PathDataInfo> pathDataInfoList = new ArrayList<>();

        public void transform(Matrix matrix) {

            float[] matrixArray = new float[9];
            matrix.getValues(matrixArray);

            viewportWidth = matrixArray[Matrix.MSCALE_X];
            viewportHeight = matrixArray[Matrix.MSCALE_Y];

            for (PointF pointF : pointDataField) {
                pointF.set(matrixArray[Matrix.MSCALE_X] * pointF.x + matrixArray[Matrix.MSKEW_X] * pointF.y + matrixArray[Matrix.MTRANS_X],
                    matrixArray[Matrix.MSKEW_Y] * pointF.x + matrixArray[Matrix.MSCALE_Y] * pointF.y + matrixArray[Matrix.MTRANS_Y]);
            }
        }

        public void updatePath() {
            for (PathDataInfo pathDataInfo : pathDataInfoList) {
                pathDataInfo.updatePath();
            }
        }
    }

    public SvgDataInfo createSvgInfo(Context context, @XmlRes int xmlRes) {
        SvgDataInfo svgDataInfo = new SvgDataInfo();

        XmlResourceParser xmlParser = context.getResources().getXml(xmlRes);
        try {
            int event = xmlParser.getEventType();

            while (event != XmlResourceParser.END_DOCUMENT) {

                switch (event) {

                    case XmlResourceParser.START_DOCUMENT:

                        Log.d(TAG, "createSvgInfo(), XmlResourceParser.START_DOCUMENT");

                        break;

                    case XmlResourceParser.START_TAG: {

                        Log.d(TAG, "createSvgInfo(), XmlResourceParser.START_DOCUMENT, name = " + xmlParser.getName());

                        switch (xmlParser.getName()) {

                            case SVG_TAG_VECTOR: {

                                float viewportWidth = 0;
                                float viewportHeight = 0;

                                for (int i = 0; i < xmlParser.getAttributeCount(); i++) {
                                    final String attributeName = xmlParser.getAttributeName(i);

                                    Log.d(TAG, "createSvgInfo(), SVG_TAG_VECTOR, i = " + i + ", attributeName = " + attributeName);

                                    switch (attributeName) {

                                        case SVG_ATTR_VIEWPORT_WIDTH:

                                            viewportWidth = xmlParser.getAttributeFloatValue(i, 0);

                                            break;

                                        case SVG_ATTR_VIEWPORT_HEIGHT:

                                            viewportHeight = xmlParser.getAttributeFloatValue(i, 0);

                                            break;
                                    }
                                }

                                svgDataInfo.viewportHeight = viewportHeight;
                                svgDataInfo.viewportWidth = viewportWidth;

                                Log.d(TAG, "createSvgInfo(), SVG_TAG_VECTOR, viewportWidth = " + svgDataInfo.viewportWidth
                                    + ", viewportHeight = " + svgDataInfo.viewportHeight);

                                break;
                            }

                            case SVG_TAG_PATH: {

                                String name = "";
                                String pathData = "";
                                int fillColor = 0xFF000000;

                                for (int i = 0; i < xmlParser.getAttributeCount(); i++) {
                                    final String attributeName = xmlParser.getAttributeName(i);

                                    Log.d(TAG, "createSvgInfo(), SVG_TAG_PATH, i = " + i + ", attributeName = " + attributeName);


                                    switch (attributeName) {

                                        case SVG_ATTR_FILL_COLOR:

                                            fillColor = xmlParser.getAttributeIntValue(i, 0xFF000000);

                                            break;

                                        case SVG_ATTR_PATH_DATA:

                                            pathData = xmlParser.getAttributeValue(i);

                                            break;

                                        case SVG_ATTR_NAME:

                                            name = xmlParser.getAttributeValue(i);

                                            break;
                                    }
                                }

                                Log.d(TAG, "createSvgInfo(), name = " + name);
                                Log.d(TAG, "createSvgInfo(), fillColor = 0x" + Integer.toHexString(fillColor));
                                Log.d(TAG, "createSvgInfo(), pathData = " + pathData);

                                PathDataInfo pathDataInfo = parserPathInfo(name, fillColor, pathData);
                                svgDataInfo.pathDataInfoList.add(pathDataInfo);
                                svgDataInfo.pointDataField.addAll(pathDataInfo.pointDataField);

                                break;
                            }

                            default:
                                break;
                        }

                        break;
                    }

                    case XmlResourceParser.TEXT:

                        Log.d(TAG, "createSvgInfo(), XmlResourceParser.TEXT, text = " + xmlParser.getText());

                        break;

                    case XmlResourceParser.END_TAG:

                        break;

                    default:
                        break;
                }

                event = xmlParser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();

            Log.d(TAG, "createSvgInfo(), exception = " + e.getMessage());
        }

        return svgDataInfo;
    }

    public PathDataInfo parserPathInfo(@NonNull String pathData) {
        return parserPathInfo("", 0xffffffff, pathData);
    }

    private PathDataInfo parserPathInfo(String name, int fillColor, @NonNull String pathData) {
        Log.d(TAG, "parserPathInfo(), start");

        PathDataInfo pathDataInfo = new PathDataInfo();
        pathDataInfo.name = name;
        pathDataInfo.fillColor = fillColor;

        PointF pPointSectionStart = null;
        PointF pPointLast = null;
        PointF pPointSecondLast = null;

        ParseHelper parseHelper = new ParseHelper(pathData);
        parseHelper.parserToPathDataUnits();
        List<ParseHelper.PathDataUnit> pathDataUnitList = parseHelper.getPathDataUnitList();

        label:
        for (ParseHelper.PathDataUnit parsedPathData : pathDataUnitList) {
            Log.d(TAG, "parserPathInfo(), cmd = " + parsedPathData.cmd);

            switch (parsedPathData.cmd) {

                case 'M':
                case 'm': {

                    if (parsedPathData.data.size() < 2) {
                        Log.e(TAG, "parserPathInfo(), cmd = " + parsedPathData.cmd);

                        break label;
                    }

                    final float x = parsedPathData.data.get(0);
                    final float y = parsedPathData.data.get(1);

                    Log.d(TAG, "parserPathInfo(), x = " + x + ", y = " + y);

                    PointF point = (pPointLast == null || parsedPathData.cmd == 'M') ? new PointF(x, y)
                        : new PointF(pPointLast.x + x, pPointLast.y + y);

                    pathDataInfo.pointDataField.add(point);
                    pathDataInfo.pathUnits.add(new PathMoveData(point));

                    pPointLast = pPointSectionStart = point;

                    break;
                }

                case 'Z':
                case 'z': {

                    if (pPointLast == null) {
                        Log.e(TAG, "parserPathInfo(), cmd = " + parsedPathData.cmd);

                        break label;
                    }

                    pathDataInfo.pathUnits.add(new PathLineData(pPointLast, pPointSectionStart));

                    pPointLast = pPointSectionStart;

                    break;
                }

                case 'L':
                case 'l': {

                    if (parsedPathData.data.isEmpty() || parsedPathData.data.size() % 2 != 0 || pPointLast == null) {
                        Log.e(TAG, "parserPathInfo(), cmd = " + parsedPathData.cmd);

                        break label;
                    }

                    for (int i = 0; i < parsedPathData.data.size(); i += 2) {
                        final float x = parsedPathData.data.get(i);
                        final float y = parsedPathData.data.get(i + 1);

                        Log.d(TAG, "createPathInfo(), x = " + x + ", y = " + y);

                        PointF point = (parsedPathData.cmd == 'l')
                            ? new PointF(pPointLast.x + x, pPointLast.y + y) : new PointF(x, y);

                        pathDataInfo.pointDataField.add(point);
                        pathDataInfo.pathUnits.add(new PathLineData(pPointLast, point));

                        pPointSecondLast = pPointLast;
                        pPointLast = point;

                    }

                    break;
                }

                case 'H':
                case 'h': {

                    if (parsedPathData.data.size() < 1 || pPointLast == null) {
                        Log.e(TAG, "parserPathInfo(), cmd = " + parsedPathData.cmd);

                        break label;
                    }

                    final float x = parsedPathData.data.get(0);

                    Log.d(TAG, "createPathInfo(), x = " + x);

                    PointF point = (parsedPathData.cmd == 'h')
                        ? new PointF(pPointLast.x + x, pPointLast.y) : new PointF(x, pPointLast.y);

                    pathDataInfo.pointDataField.add(point);
                    pathDataInfo.pathUnits.add(new PathLineData(pPointLast, point));

                    pPointSecondLast = pPointLast;
                    pPointLast = point;

                    break;
                }

                case 'V':
                case 'v': {

                    if (parsedPathData.data.size() < 1 || pPointLast == null) {
                        Log.e(TAG, "parserPathInfo(), cmd = " + parsedPathData.cmd);

                        break label;
                    }

                    final float y = parsedPathData.data.get(0);

                    Log.d(TAG, "createPathInfo(), y = " + y);

                    PointF point = (parsedPathData.cmd == 'v')
                        ? new PointF(pPointLast.x, pPointLast.y + y) : new PointF(pPointLast.x, y);

                    pathDataInfo.pointDataField.add(point);
                    pathDataInfo.pathUnits.add(new PathLineData(pPointLast, point));

                    pPointSecondLast = pPointLast;
                    pPointLast = point;

                    break;
                }

                case 'C':
                case 'c': {

                    if (parsedPathData.data.size() < 6 || pPointLast == null) {
                        Log.e(TAG, "parserPathInfo(), cmd = " + parsedPathData.cmd);

                        break label;
                    }

                    final float x1 = parsedPathData.data.get(0);
                    final float y1 = parsedPathData.data.get(1);
                    final float x2 = parsedPathData.data.get(2);
                    final float y2 = parsedPathData.data.get(3);
                    final float x3 = parsedPathData.data.get(4);
                    final float y3 = parsedPathData.data.get(5);

                    Log.d(TAG, "createPathInfo(), x1 = " + x1 + ", y1 = " + y1
                        + ", x2 = " + x2 + ", y2 = " + y2 + ", x3 = " + x3 + ", y3 = " + y3);

                    final PointF point1 = new PointF();
                    final PointF point2 = new PointF();
                    final PointF point3 = new PointF();

                    if (parsedPathData.cmd == 'c') {
                        point1.set(pPointLast.x + x1, pPointLast.y + y1);
                        point2.set(pPointLast.x + x2, pPointLast.y + y2);
                        point3.set(pPointLast.x + x3, pPointLast.y + y3);
                    } else {
                        point1.set(x1, y1);
                        point2.set(x2, y2);
                        point3.set(x3, y3);
                    }

                    pathDataInfo.pointDataField.add(point1);
                    pathDataInfo.pointDataField.add(point2);
                    pathDataInfo.pointDataField.add(point3);
                    pathDataInfo.pathUnits.add(new PathCubicData(pPointLast, point1, point2, point3));

                    pPointSecondLast = point2;
                    pPointLast = point3;

                    break;
                }

                case 'S':
                case 's': {

                    if (parsedPathData.data.size() < 4 || pPointSecondLast == null) {
                        Log.e(TAG, "parserPathInfo(), cmd = " + parsedPathData.cmd);

                        break label;
                    }

                    final float x1 = parsedPathData.data.get(0);
                    final float y1 = parsedPathData.data.get(1);
                    final float x2 = parsedPathData.data.get(2);
                    final float y2 = parsedPathData.data.get(3);

                    Log.d(TAG, "createPathInfo(), x1 = " + x1 + ", y1 = " + y1
                        + ", x2 = " + x2 + ", y2 = " + y2);

                    final PointF point1 = new PointF(
                        2 * pPointLast.x - pPointSecondLast.x,
                        2 * pPointLast.y - pPointSecondLast.y);

                    final PointF point2 = new PointF();
                    final PointF point3 = new PointF();

                    if (parsedPathData.cmd == 's') {
                        point2.set(pPointLast.x + x1, pPointLast.y + y1);
                        point3.set(pPointLast.x + x2, pPointLast.y + y2);
                    } else {
                        point2.set(x1, y1);
                        point3.set(x2, y2);
                    }

                    pathDataInfo.pointDataField.add(point1);
                    pathDataInfo.pointDataField.add(point2);
                    pathDataInfo.pointDataField.add(point3);
                    pathDataInfo.pathUnits.add(new PathCubicData(pPointLast, point1, point2, point3));

                    pPointSecondLast = point2;
                    pPointLast = point3;

                    break;
                }

                case 'A':
                case 'a': {

                    if (parsedPathData.data.size() < 7 || pPointLast == null) {
                        Log.e(TAG, "parserPathInfo(), cmd = " + parsedPathData.cmd);

                        break label;
                    }

                    final float rx = parsedPathData.data.get(0);
                    final float ry = parsedPathData.data.get(1);
                    final float theta = parsedPathData.data.get(2);
                    final float largeArc = parsedPathData.data.get(3);
                    final float sweepArc = parsedPathData.data.get(4);
                    final float x = parsedPathData.data.get(5);
                    final float y = parsedPathData.data.get(6);

                    Log.d(TAG, "createPathInfo(), rx = " + rx + ", ry = " + ry
                        + ", theta = " + theta + ", largeArc = " + largeArc
                        + ", sweepArc = " + sweepArc + ", x = " + x + ", y = " + y);

                    final PointF point = new PointF();

                    if (parsedPathData.cmd == 'a') {
                        point.set(pPointLast.x + x, pPointLast.y + y);
                    } else {
                        point.set(x, y);
                    }

                    pathDataInfo.pointDataField.add(point);
                    pathDataInfo.pathUnits.add(new PathArcData(pPointLast, point,
                        rx, ry, theta, largeArc == 1, sweepArc == 1));

                    pPointSecondLast = pPointLast;
                    pPointLast = point;

                    break;
                }

                case 'T':
                case 't': {

                    if (parsedPathData.data.size() < 2 || pPointSecondLast == null) {
                        Log.e(TAG, "parserPathInfo(), cmd = " + parsedPathData.cmd);

                        break label;
                    }

                    final float x = parsedPathData.data.get(0);
                    final float y = parsedPathData.data.get(1);

                    Log.d(TAG, "createPathInfo(), x = " + x + ", y = " + y);

                    final PointF point2 = new PointF(
                        2 * pPointLast.x - pPointSecondLast.x,
                        2 * pPointLast.y - pPointSecondLast.y);

                    final PointF point3 = new PointF();

                    if (parsedPathData.cmd == 't') {
                        point3.set(pPointLast.x + x, pPointLast.y + y);
                    } else {
                        point3.set(x, y);
                    }

                    pathDataInfo.pointDataField.add(point2);
                    pathDataInfo.pointDataField.add(point3);
                    pathDataInfo.pathUnits.add(new PathCubicData(pPointLast, pPointLast, point2, point3));

                    pPointSecondLast = point2;
                    pPointLast = point3;

                    break;
                }

                case 'Q':
                case 'q': {

                    if (parsedPathData.data.size() < 4 || pPointLast == null) {
                        Log.e(TAG, "parserPathInfo(), cmd = " + parsedPathData.cmd);

                        break label;
                    }

                    final float x1 = parsedPathData.data.get(0);
                    final float y1 = parsedPathData.data.get(1);
                    final float x2 = parsedPathData.data.get(2);
                    final float y2 = parsedPathData.data.get(3);

                    Log.d(TAG, "createPathInfo(), x1 = " + x1 + ", y1 = " + y1
                        + ", x2 = " + x2 + ", y2 = " + y2);

                    final PointF point2 = new PointF();
                    final PointF point3 = new PointF();

                    if (parsedPathData.cmd == 'q') {
                        point2.set(pPointLast.x + x1, pPointLast.y + y1);
                        point2.set(pPointLast.x + x2, pPointLast.y + y2);
                    } else {
                        point2.set(x1, y1);
                        point3.set(x2, y2);
                    }

                    pathDataInfo.pointDataField.add(point2);
                    pathDataInfo.pointDataField.add(point3);
                    pathDataInfo.pathUnits.add(new PathCubicData(pPointLast, pPointLast, point2, point3));

                    pPointSecondLast = point2;
                    pPointLast = point3;

                    break;
                }

                default:
                    Log.d(TAG, "Invalid path command: " + parsedPathData.cmd);
                    break label;
            }
        }

        return pathDataInfo;
    }
}
