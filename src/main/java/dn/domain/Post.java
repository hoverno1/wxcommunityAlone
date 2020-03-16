package dn.domain;

public class Post {
    private Integer postId;
    private String date;
    private String disease;
    private String avatar;
    private String imgSrc;
    private String detail;
    private String aid;

    public Post() {
    }

    public Post(Integer postId,String aid, String date, String disease, String avatar, String imgSrc,
                String detail) {
        this.postId = postId;
        this.date = date;
        this.disease = disease;
        this.avatar = avatar;
        this.imgSrc = imgSrc;
        this.detail = detail;
        this.aid = aid;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", date='" + date + '\'' +
                ", disease='" + disease + '\'' +
                ", avatar='" + avatar + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                ", detail='" + detail + '\'' +
                ", aid=" + aid +
                '}';
    }
}
