package home.stanislavpoliakov.meet7_practice;

public class DataItem {
    private String text;
    private int imageId;
    private ItemTypes itemType;

    DataItem(ItemTypes itemType, String text) {
        this.itemType = itemType;
        this.text = text;
    }

    DataItem(ItemTypes itemType, int imageId) {
        this.itemType = itemType;
        this.imageId = imageId;
    }

    DataItem(ItemTypes itemType, String text, int imageId) {
        this.itemType = itemType;
        this.text = text;
        this.imageId = imageId;
    }

    public String getText() {
        return this.text;
    }

    public int getImageId() {
        return this.imageId;
    }

    public ItemTypes getItemType() {
        return this.itemType;
    }
}
