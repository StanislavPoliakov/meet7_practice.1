package home.stanislavpoliakov.meet7_practice;

import android.util.Log;

/**
 * Класс элемента данных. Эта реализация несколько нарушает первый принцип SOLID, так как
 * внутри себя описывает три разновидности данных. Надо сделать общий интерфейс данных и три
 * реализации для каждого типа.
 * Три различных конструктора, каждый из которых вызывается в зависимости от типа данных, описанного
 * в Enum. К сожалению, реализация не по SOLID дает возможность дотянуться до полей, значение которых
 * будет инициализировано default (для String = "", для int = 0), в то время как этих полей для определенного
 * типа данных вообще не должно существовать. Надо переделать.
 *
 * Добавил id-элемента данных. Для DiffUtils
 */
public class DataItem implements Cloneable{
    private static final String TAG = "meet7_logs";
    private static int count;
    private String text;
    private int imageId = 0;
    private ItemTypes itemType;
    private int id;
    //TODO Переделать объекты данных

    /**
     * Конструктор для данных типа "Простой текст"
     * @param itemType тип данных Enum (ItemTypes.SIMPLE_TEXT)
     * @param text текстовое содержимое для отображения
     */

    DataItem(ItemTypes itemType, String text) {
        this.itemType = itemType;
        this.text = text;
        this.id = ++count;
    }

    /**
     * Конструктор для данных типа "Простая картинка"
     * @param itemType тип данных Enum (ItemTypes.SIMPLE_IMAGE)
     * @param imageId идентификатор ресурса (package:drawable/)
     */
    DataItem(ItemTypes itemType, int imageId) {
        this.itemType = itemType;
        this.imageId = imageId;
        this.id = ++count;
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
        this.id = ++count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public ItemTypes getItemType() {
        return itemType;
    }

    public int getId() {
        return id;
    }


    /**
     *  Переопределяем метод Clone для того, чтобы сделать дубликат нашего списка элемнтов
     *  для внесения изменений (чтобы изменения регистрировались в DiffCall
     * @return дубликат
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        DataItem cloneData = (DataItem) super.clone();
        cloneData.text = this.text;
        cloneData.imageId = this.imageId;
        return cloneData;
    }
}
