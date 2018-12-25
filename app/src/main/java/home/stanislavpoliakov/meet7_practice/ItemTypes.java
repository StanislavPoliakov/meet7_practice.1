package home.stanislavpoliakov.meet7_practice;

public enum ItemTypes {
    SIMPLE_TEXT(0),
    SIMPLE_IMAGE(1),
    IMAGE_AND_TEXT(2);

    int type;

    ItemTypes(int type) {
        this.type = type;
    }
}