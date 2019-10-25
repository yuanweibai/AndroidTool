package rango.tool.androidtool.http.api.bean;

import java.util.List;

public class AllUserThemeBean {

    /**
     * page_index : 1
     * show_list : [{"creator_id":"abcdefg","status":1,"image_url":"","created":1571709225000,"updated":1571709225000,"customize_show_id":5,"file_name":"nnnnnn","video_url":"","audio_url":""},{"creator_id":"abcdefg","status":1,"image_url":"","created":1571706404000,"updated":1571706404000,"customize_show_id":4,"file_name":"nnnnnn","video_url":"","audio_url":""},{"creator_id":"abcdefg","status":1,"image_url":"","created":1571666990000,"updated":1571666990000,"customize_show_id":3,"file_name":"nnnnnn","video_url":"","audio_url":""},{"creator_id":"abcdefg","status":1,"image_url":"","created":1571665232000,"updated":1571665232000,"customize_show_id":2,"file_name":"nnnnnn","video_url":"","audio_url":""},{"creator_id":"abcdefg","status":1,"image_url":"","created":1571664194000,"updated":1571664194000,"customize_show_id":1,"file_name":"nnnnnn","video_url":"","audio_url":""}]
     */

    private int page_index;
    private List<ShowListBean> show_list;

    public int getPage_index() {
        return page_index;
    }

    public void setPage_index(int page_index) {
        this.page_index = page_index;
    }

    public List<ShowListBean> getShow_list() {
        return show_list;
    }

    public void setShow_list(List<ShowListBean> show_list) {
        this.show_list = show_list;
    }

    public static class ShowListBean {
        /**
         * creator_id : abcdefg
         * status : 1
         * image_url :
         * created : 1571709225000
         * updated : 1571709225000
         * customize_show_id : 5
         * file_name : nnnnnn
         * video_url :
         * audio_url :
         */

        private String creator_id;
        private int status;
        private String image_url;
        private long created;
        private long updated;
        private int customize_show_id;
        private String file_name;
        private String video_url;
        private String audio_url;

        public String getCreator_id() {
            return creator_id;
        }

        public void setCreator_id(String creator_id) {
            this.creator_id = creator_id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public long getCreated() {
            return created;
        }

        public void setCreated(long created) {
            this.created = created;
        }

        public long getUpdated() {
            return updated;
        }

        public void setUpdated(long updated) {
            this.updated = updated;
        }

        public int getCustomize_show_id() {
            return customize_show_id;
        }

        public void setCustomize_show_id(int customize_show_id) {
            this.customize_show_id = customize_show_id;
        }

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public String getAudio_url() {
            return audio_url;
        }

        public void setAudio_url(String audio_url) {
            this.audio_url = audio_url;
        }
    }
}
