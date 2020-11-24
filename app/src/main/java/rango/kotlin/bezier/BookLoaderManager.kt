package rango.kotlin.bezier

import rango.tool.androidtool.R
import rango.tool.androidtool.ToolApplication
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.IllegalStateException

object BookLoaderManager {

    private var bookContentList: List<String>? = null
    private var bookContent: String = "《元尊》\n" +
            "\n" +
            "\n" +
            " 第一章蟒雀吞龙\n" +
            "\n" +
            "    灯火通明的内殿之中，金碧辉煌，气势威严，殿内有着长明灯燃烧，其中燃烧着一颗青石，袅袅的青烟升腾而起，盘绕在殿内。r\n" +
            "\n" +
            "    那是青檀石，燃烧起来会释放出异香，有着凝神静心之效，乃是修炼时必备之物，不过此物价格不低，能够当做燃料般来使用，足以说明此地主人颇有地位。r\n" +
            "\n" +
            "    内殿中，一名身着明黄袍服的中年男子负手而立，他面容坚毅，眼目之间有着威严之气，显然久居高位，而其身后，隐有气息升腾，似炎似雷，发出低沉轰鸣之声。r\n" +
            "\n" +
            "    只是若是看向其右臂，却是发现空空荡荡，竟是一只断臂。r\n" +
            "\n" +
            "    在他的身旁，还有着一位宫装美妇，她娇躯纤细，容貌雍容而美丽，不过其脸颊，却是显得分外的苍白与虚弱。r\n" +
            "\n" +
            "    而此时的这对显然地位不低的男女，都是面带着一丝紧张之色的望着前方，只见得在那里的床榻上，有着一名约莫十三四岁的少年盘坐，少年身躯略显单薄，双目紧闭，那张属于少年人本应该朝气蓬勃的脸庞，却是萦绕着一股血气。r\n" +
            "\n" +
            "    那股诡异的血气，在他的皮肤下窜动，隐隐间，仿佛有着怨毒的龙啸声传出。r\n" +
            "\n" +
            "    而伴随着那道龙啸，少年额头上青筋耸动，身体不断的颤抖着，面庞变得狰狞，似乎是承受了难于言语的痛苦。r\n" +
            "\n" +
            "    在少年的身侧，一名白发老者手持一面铜镜，铜镜之上，有着柔和的光芒散发出来，照耀在少年身体上，而在那光芒的照耀下，少年面庞上的诡异血气则是开始渐渐的平复。r\n" +
            "\n" +
            "    血气在持续了一炷香时间后，终是尽数的退去，最后缩回了少年的掌心之中。r\n" +
            "\n" +
            "    白发老者见到这一幕，顿时如释重负的松了一口气，然后转过身来，对着一旁紧张等待的中年男子以及宫装美妇弯身道：“恭喜王上，王后，这三年一道的大坎，殿下总算是熬了过来，接下来的三年，应当都无大碍。”r\n" +
            "\n" +
            "    中年男子与宫装美妇闻言，皆是面露喜色，紧握的拳头都是渐渐的松开。r\n" +
            "\n" +
            "    “秦师，如今元儿已是十三，一般这个年龄的少年，都已八脉成形，可以开始修炼了，那元儿？”身着明黄袍服的威严男子，期待的望着眼前的白发老者，问道。r\n" +
            "\n" +
            "    听到此问，白发老者神色顿时黯淡了一些，他微微摇头，道：“王上，这一次老夫依然没有探测到殿下体内八脉”r\n" +
            "\n" +
            "    威严男子闻言，眼神同样是黯淡了下来。r\n"

    fun getTestData(): String {
        if (bookContent == null) {
            val list = getBookContent()
            val buffer = StringBuffer()
            list.forEach {
                buffer.append(it)
                buffer.append("\n")
            }
            bookContent = buffer.toString()
        }

        return bookContent ?: ""
    }

    fun getBookContent(): List<String> {
        if (bookContentList == null) {
            bookContentList = readFromRaw()
        }
        return bookContentList ?: ArrayList()
    }

    private fun readFromRaw(): List<String>? {
        try {
            val inputStream = ToolApplication.getContext().resources.openRawResource(R.raw.yuanzun)
            return readTextFromStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ArrayList()
    }

    private fun readTextFromStream(inputStream: InputStream): List<String> {
        try {
            val reader = InputStreamReader(inputStream)
            return reader.readLines()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        throw IllegalStateException("InputStream is null content!!!")
    }
}