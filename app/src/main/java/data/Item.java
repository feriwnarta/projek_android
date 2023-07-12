package data;

public class Item {
    private  String id;
    private String title;
    private String imgPath;

    private String produser;

    private String durasi;

    private String category;

    public Item(String id, String title, String imgPath, String produser, String durasi, String category) {
        this.id = id;
        this.title = title;
        this.imgPath = imgPath;
        this.produser = produser;
        this.durasi = durasi;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getProduser() {
        return produser;
    }

    public void setProduser(String produser) {
        this.produser = produser;
    }

    public String getDurasi() {
        return durasi;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
