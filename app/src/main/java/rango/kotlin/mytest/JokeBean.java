package rango.kotlin.mytest;

import java.util.List;

public class JokeBean {

    private List<JokeDataBean> joke_data;

    public List<JokeDataBean> getJoke_data() {
        return joke_data;
    }

    public void setJoke_data(List<JokeDataBean> joke_data) {
        this.joke_data = joke_data;
    }

    public static class JokeDataBean {
        /**
         * content : 据说大年初一，给心爱的老公洗完脚，咣咣咣磕仨响头 ，说声爷您辛苦了，磕的越响越好！老公在狗年嗷嗷有钱，嘎嘎听话。老灵了，为了亲爱的老公，身为老婆的女人们都试试吧！同意的男同胞们转起来吧。。。不敢转的喝点酒就敢了！没有一定实力切记不要模仿.. .. .. .. 怕媳妇的就当没看见！.. .. .. .. .. ..[呲牙][呲牙][呲牙][呲牙][偷笑
         * hashId : 02551967c18952e652d1e45b17ed3c63
         * unixtime : 1518746100
         * updatetime : 2018-02-16 09:55:00
         */

        private String content;
        private String hashId;
        private int unixtime;
        private String updatetime;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getHashId() {
            return hashId;
        }

        public void setHashId(String hashId) {
            this.hashId = hashId;
        }

        public int getUnixtime() {
            return unixtime;
        }

        public void setUnixtime(int unixtime) {
            this.unixtime = unixtime;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }
    }
}
