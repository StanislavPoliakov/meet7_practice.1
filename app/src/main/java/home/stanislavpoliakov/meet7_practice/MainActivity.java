package home.stanislavpoliakov.meet7_practice;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mManager;
    private List<DataItem> items = new ArrayList<>();
    private static final String TAG = "meet7_logs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Делаем привязку к сервису, запуск логики по onBind()
        bindService(MyService.newIntent(MainActivity.this), serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * После успешной привязки (onBind()) и передачи ненулевого Binder'-а отправляем в методе
     * onServiceConnected сообщение в сервис для регистрации клиента (replyTo = mClient)
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                mService = new Messenger(service);
                Message msgRegisterClient = Message.obtain(null, MSG_REGISTER_CLIENT);
                msgRegisterClient.replyTo = mClient;
                mService.send(msgRegisterClient);
                //Log.d(TAG, "onServiceConnected: sent");
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Messenger mService;
    private Messenger mClient = new Messenger(new IncomingHandler());
    static final int MSG_REGISTER_CLIENT = 1;

    private class IncomingHandler extends Handler {

        /**
         * Получаем сгенерированные сервисом данные (ArrayList<DataItem>),
         * которые передаем как obj, а не Bundle
         * @param msg сообщение сервиса
         */
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MyService.MSG_PUT_DATA) {
                items = (ArrayList<DataItem>) msg.obj; // Не проверяем Cast, доверяем себе :)
                initRecyclerView(); //инициализируем RecyclerView после получения данных
            }
        }
    }

    /**
     * Метод инициализации RecyclerView. Обращу внимание, что необходимые данные для отображение
     * мы передаем в конструктор адаптера (new MyAdapter(items))
     */
    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        mAdapter = new MyAdapter(items);
        recyclerView.setAdapter(mAdapter);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mManager);
    }
}
