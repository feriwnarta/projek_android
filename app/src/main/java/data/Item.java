package data;

public class Item {
    private String title;
    private String imgPath;

    public Item(String title, String imgPath) {
        this.title = title;
        this.imgPath = imgPath;
    }

    public String getTitle() {
        return title;
    }

    public String getImgPath() {
        return imgPath;
    }
}
