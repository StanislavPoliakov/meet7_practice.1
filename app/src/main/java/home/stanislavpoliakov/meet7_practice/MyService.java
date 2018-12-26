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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createDataItem();
        return START_NOT_STICKY;
    }

    private void createDataItem() {
        new Thread(new Runnable() {
            private ItemTypes itemType;
            private String itemText;
            private int itemImageResource;
            private int stringNumber = 0;
            private List<DataItem> items = new ArrayList<>();

            @Override
            public void run() {
                //Log.d(TAG, "run: Run");
                //itemType = setItemType();
                //if (itemType == Ite)
                // itemText
                //if (setItemType() == ItemTypes.)
                //makeItem();
                for (int i = 1; i <= 20; i++) {
                    items.add(makeItem());
                }
                    if (mClient != null) {
                        try {
                            Message msg = Message.obtain(null, MSG_PUT_DATA);
                            msg.obj = items;
                            mClient.send(msg);
                            //Log.d(TAG, "run: sent");
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }

                //setItemImageResource();
            }


            /*private DataItem makeItem(ItemTypes itemType) {
                if (itemType == ItemTypes.SIMPLE_TEXT)
                else if (itemType == ItemTypes.SIMPLE_IMAGE) return new DataItem(itemType, setItemImageResource());
                else return new DataItem(itemType, setItemText(), setItemImageResource());
            }*/

            private DataItem makeItem() {
                int tmp = (int) Math.round(Math.random() * 3);
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

            private String setItemText() {
                return "This is a String #" + ++stringNumber;
            }

            private int setItemImageResource() {
                int randomResourceId = (int) Math.round(Math.random() * 20);
                //Log.d(TAG, "setItemImageResource: " + getResources().getIdentifier("image_" + randomResourceId, "drawable", getPackageName()));
                if (randomResourceId > 0) {
                    Log.d(TAG, "Image = : image_" + randomResourceId);
                    return getResources().getIdentifier("image_" + randomResourceId, "drawable", getPackageName());
                }
                //Log.d(TAG, "setItemImageResource: " + res);
                return setItemImageResource();
            }
        }).start();
        stopSelf();
    }
    private Messenger mClient;
    private Messenger mService = new Messenger(new IncomingHandler());
    static final int MSG_PUT_DATA = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return mService.getBinder();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MyService.class);
    }



    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MainActivity.MSG_REGISTER_CLIENT) {
                mClient = msg.replyTo;
                createDataItem();
            }
        }
    }
}
