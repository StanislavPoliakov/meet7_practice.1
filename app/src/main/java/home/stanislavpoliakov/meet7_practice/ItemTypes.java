package home.stanislavpoliakov.meet7_practice;

/**
 * Enum-класс для определения типов данных, используемых в приложении
 */
public enum ItemTypes {
    SIMPLE_TEXT(0), // Простой текст
    SIMPLE_IMAGE(1), // Простая картинка
    IMAGE_AND_TEXT(2);// Картинка и текст

    int type; // Численный идентификатор типа, который мы будем использовать в int viewType

    // Конструктор перечислений
    ItemTypes(int type) {
        this.type = type;
    }
}