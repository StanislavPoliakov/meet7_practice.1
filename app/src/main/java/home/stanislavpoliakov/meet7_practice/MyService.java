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
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static home.stanislavpoliakov.meet7_practice.ItemTypes.SIMPLE_TEXT;

public class MyService extends Service {
    private DataItem dataItem;
    private static final String TAG = "meet7_logs";
    private List<DataItem> items = new ArrayList<>();
    private List<DataItem> changeList = new ArrayList<>();
    private int stringNumber = 0;

    public MyService() {
    }

    /**
     * Основой метод работы сервиса. В зависимости от модификатора, который мы получаем от приходящих
     * запросов от Activity, мы инициализируем начальный список (MSG_INIT_LIST), добавляем элементы в
     * список (MSG_ADD_ITEMS), изменяем элементы списка (MSG_CHANGE_PAIR) или удаляем из списка
     * нечетные позиции (MSG_REMOVE_ODD)
     * @param MODIFIER модификатор запуска
     */
    private void serviceWork(final int MODIFIER) {
        new Thread(new Runnable() {
            private ItemTypes itemType;

            @Override
            public void run() {
                switch (MODIFIER) {
                    case MainActivity.MSG_INIT_LIST:
                        items = initList(20);
                        //Log.d(TAG, "run: " + items.get(0));
                        //Log.d(TAG, "run: " + changeList.get(0));
                        changeList = cloneList(items);
                        sendMessage(items, MODIFIER);
                        break;
                    case MainActivity.MSG_ADD_ITEMS:
                        //changeList = cloneList(items);
                        changeList = addItems(changeList, 2);
                        sendMessage(changeList, MODIFIER);
                        break;
                    case MainActivity.MSG_CHANGE_PAIR:
                        changeList = cloneList(items);
                        changeList = changePair(changeList);
                        sendMessage(changeList, MODIFIER);
                        break;
                    case MainActivity.MSG_REMOVE_ODD:
                        //changeList = cloneList(items);
                        changeList = removeOdds(changeList);
                        sendMessage(changeList, MODIFIER);
                        break;
                }
               // sendMessage(MODIFIER);
            }

            private List<DataItem> cloneList(List<DataItem> oldList) {
                List<DataItem> newList = new ArrayList<>();
                try {
                    for (int i = 0; i < oldList.size(); i++)
                        newList.add((DataItem) oldList.get(i).clone());
                    //changeList.add(items.get(i));
                } catch (CloneNotSupportedException ex) {
                    ex.printStackTrace();
                }
                return newList;
            }

            /**
             * Метод отправки сообщений для Activity
             * @param itemList список элементов для отправки
             * @param MESSAGE_CONSTANT константы ответного сообщения
             */
            private void sendMessage(List<DataItem> itemList, final int MESSAGE_CONSTANT) {
                Message messageToClient = Message.obtain(null, MESSAGE_CONSTANT);
                messageToClient.obj = itemList;
                if (mClient != null) {
                    try {
                        mClient.send(messageToClient);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            /**
             * Делаем начальный список элементов для отображения
             * @param count количество элементов
             * @return список элементов
             */
            private List<DataItem> initList(int count) {
                List<DataItem> initList = new ArrayList<>();
                for (int i = 1; i <= count; i++) initList.add(makeItem()); 
                return initList;
            }

            /**
             * Метод добавления элементов к списку. Добавим два элемента, для примера.
             * @param mainList список, в котороый будем добавлять элементы
             * @param count количество элементов, которое необходимо добавить
             * @return измененный список
             */
            private List<DataItem> addItems(List<DataItem> mainList, int count) {
                List<DataItem> addList = initList(2);
                mainList.addAll(1, addList);
                return mainList;
            }

            /**
             * Меняем первую пару элементов в списке
             * @param mainList список, в который необходимо внести изменения
             * @return измененный список
             *
             * Отмечу, что столь странная реализация обусловлена тем, что сначала проходим по
             * списку итератором и удаляем элементы. Да, можно было вызвать просто mainList.remove()
             * по индексам, но если добавлять логику удалений, а потом вставок элементов, то придется
             * сначала проходить итератором по циклу, чтобы не ловить ConcurrentModificationException :)
             */
            private List<DataItem> changePair(List<DataItem> mainList) {
                /*Iterator<DataItem> itemIterator = mainList.iterator();
                List<ItemTypes> typesList = new ArrayList<>();
                for (int i = 0; i <= 1; i++) {
                    DataItem item = itemIterator.next();
                    typesList.add(i, item.getItemType());
                    itemIterator.remove();
                }

                for (int i = 0; i < typesList.size(); i++) {
                    ItemTypes type = typesList.get(i);
                    switch (type) {
                        case SIMPLE_TEXT:
                            mainList.add(i, new DataItem(type, setItemText()));
                            break;
                        case SIMPLE_IMAGE:
                            mainList.add(i, new DataItem(type, setItemImageResource()));
                            break;
                        case IMAGE_AND_TEXT:
                            mainList.add(i, new DataItem(type, setItemText(), setItemImageResource()));
                            break;
                    }
                }*/
                List<DataItem> changeList = new ArrayList<>(mainList);
                for (int i = 0; i < 2; i++) {
                    //ItemTypes type = typesList.get(i);
                    switch (changeList.get(i).getItemType()) {
                        case SIMPLE_TEXT:
                            changeList.get(i).setText(setItemText());
                            break;
                        case SIMPLE_IMAGE:
                            changeList.get(i).setImageId(setItemImageResource());
                            break;
                        case IMAGE_AND_TEXT: {
                            changeList.get(i).setText(setItemText());
                            changeList.get(i).setImageId(setItemImageResource());
                            break;
                        }
                    }
                }

                return changeList;
            }
            
            private List<DataItem> removeOdds(List<DataItem> mainList) {
                Iterator<DataItem> itemIterator = mainList.iterator();
                int itemCount = 0;
                while (itemIterator.hasNext()) {
                    DataItem item = itemIterator.next();
                    itemCount++;
                    if ((itemCount % 2) != 0) itemIterator.remove();
                }
                return mainList;
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
                        itemType = SIMPLE_TEXT;
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
                    //Log.d(TAG, "Image = : image_" + randomResourceId);
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
    static final int MSG_INIT_LIST = 1;
    static final int MSG_MAKE_CHANGES = 2;
    

    private class IncomingHandler extends Handler {

        /**
         * Запуск логики работы осуществляем после регистрации клиента в сервисе
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            mClient = msg.replyTo;
            serviceWork(msg.what);
            }
            
            /*if (msg.what == MainActivity.MSG_REGISTER_CLIENT) {
                mClient = msg.replyTo;
                createItemsList();
            } else if (msg.what == MainActivity.MSG_CHANGE_LIST) {
                //MyService.this.items.remove(1);
                List<DataItem> newList = new ArrayList<>();
                newList.addAll(items);
                newList.remove(1);
                Log.d(TAG, "handleMessage: " + items.size());
                Log.d(TAG, "handleMessage: " + newList.size());
                if (mClient != null) { // После того, как список готов, отправляем результат в Activity
                    try {
                        Message msgChange = Message.obtain(null, MSG_SOME_CHANGES);
                        msgChange.obj = newList; // Список отправляем через msg.obj
                        mClient.send(msgChange);
                        Log.d(TAG, "handleMessage: sent");
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            }*/

    }
}
