package home.stanislavpoliakov.meet7_practice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {
    private DataItem dataItem;
    private static final String TAG = "meet7_logs";

    public MyService() {
    }

    /**
     * Основной метод работы сервиса. Здесь, в отдельном потоке, мы соберем 20 элементов
     * списка данных. Тип данных будет выбран случайно. Текст сгенерирован. Картинка выбрана случайно
     */
    private void createItemsList() {
        new Thread(new Runnable() {
            private ItemTypes itemType;
            private int stringNumber = 0;
            private List<DataItem> items = new ArrayList<>();

            @Override
            public void run() {
                for (int i = 1; i <= 20; i++) {
                    items.add(makeItem()); // Забиваем список из 20 элементов случайными типами данных
                }
                    if (mClient != null) { // После того, как список готов, отправляем результат в Activity
                        try {
                            Message msg = Message.obtain(null, MSG_PUT_DATA);
                            msg.obj = items; // Список отправляем через msg.obj
                            mClient.send(msg);
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
            }

            /**
             * Метод отдельного потока. Создаем отдельный элемент. Сразу определяем случайным
             * образом тип элемента. И в зависимости от типа вызываем необходимый конструктор DataItem
             * @return Объект элемента данных, который мы хотим отобразить
             */
            private DataItem makeItem() {
                int tmp = (int) Math.round(Math.random() * 2); // Случайное целое от 0 до 2
                switch (tmp) {
                    case 0 :
                        itemType = ItemTypes.SIMPLE_TEXT;
                        return new DataItem(itemType, setItemText());
                    case 1 :
                        itemType = ItemTypes.SIMPLE_IMAGE;
                        return new DataItem(itemType, setItemImageResource());
                    case 2 :
                        itemType = ItemTypes.IMAGE_AND_TEXT;
                        return new DataItem(itemType, setItemText(), setItemImageResource());
                    default :
                        return makeItem();
                }
            }

            /**
             * Нумеруем тексты в RecyclerView (если они есть) по порядку отображения
             * @return текст для элемента данных
             */
            private String setItemText() {
                return "This is a String #" + ++stringNumber;
            }

            /**
             * В нашей папке /res/drawable мы поместили 20 картинок. Получаем случайное число и
             * динамически достаем id через формирование имени ресурса
             * @return resId картинки для imageView.setImageResource(int resId)
             */
            private int setItemImageResource() {
                int randomResourceId = (int) Math.round(Math.random() * 20);
                if (randomResourceId > 0) {
                    Log.d(TAG, "Image = : image_" + randomResourceId);
                    return getResources().getIdentifier("image_" + randomResourceId, "drawable", getPackageName());
                }
                return setItemImageResource();
            }
        }).start();
        stopSelf();
    }

    //Возвращаем Binder по факту привязки для отправки сообщения из Activity
    @Override
    public IBinder onBind(Intent intent) {
        return mService.getBinder();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MyService.class);
    }

    private Messenger mClient;
    private Messenger mService = new Messenger(new IncomingHandler());
    static final int MSG_PUT_DATA = 1;

    private class IncomingHandler extends Handler {

        /**
         * Запуск логики работы осуществляем после регистрации клиента в сервисе
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MainActivity.MSG_REGISTER_CLIENT) {
                mClient = msg.replyTo;
                createItemsList();
            }
        }
    }
}
