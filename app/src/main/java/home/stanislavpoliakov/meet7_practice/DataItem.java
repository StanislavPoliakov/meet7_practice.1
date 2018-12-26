package home.stanislavpoliakov.meet7_practice;

/**
 * Класс элемента данных. Эта реализация несколько нарушает первый принцип SOLID, так как
 * внутри себя описывает три разновидности данных. Надо сделать общий интерфейс данных и три
 * реализации для каждого типа.
 * Три различных конструктора, каждый из которых вызывается в зависимости от типа данных, описанного
 * в Enum. К сожалению, реализация не по SOLID дает возможность дотянуться до полей, значение которых
 * будет инициализировано default (для String = "", для int = 0), в то время как этих полей для определенного
 * типа данных вообще не должно существовать. Надо переделать.
 */
public class DataItem {
    private String text;
    private int imageId;
    private ItemTypes itemType;
    //TODO Переделать объекты данных

    /**
     * Конструктор для данных типа "Простой текст"
     * @param itemType тип данных Enum (ItemTypes.SIMPLE_TEXT)
     * @param text текстовое содержимое для отображения
     */
    DataItem(ItemTypes itemType, String text) {
        this.itemType = itemType;
        this.text = text;
    }

    /**
     * Конструктор для данных типа "Простая картинка"
     * @param itemType тип данных Enum (ItemTypes.SIMPLE_IMAGE)
     * @param imageId идентификатор ресурса (package:drawable/)
     */
    DataItem(ItemTypes itemType, int imageId) {
        this.itemType = itemType;
        this.imageId = imageId;
    }

    /**
     * Конструктор для данных типа "Картинка и текст"
     * @param itemType тип данных Enum (ItemTypes.IMAGE_AND_TEXT)
     * @param text текстовое содержимое для отображения
     * @param imageId идентификатор ресурса (package:drawable/)
     */
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
