package home.stanislavpoliakov.meet7_practice;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    //private LinearLayoutManager mManager;
    private ChessLayoutManager mManager;
    private List<DataItem> items = new ArrayList<>();
    private List<DataItem> newList = new ArrayList<>();
    private static final String TAG = "meet7_logs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Делаем привязку к сервису, запуск логики по onBind()
        bindService(MyService.newIntent(MainActivity.this), serviceConnection, BIND_AUTO_CREATE);
        initButton();
    }

    /**
     * После успешной привязки (onBind()) и передачи ненулевого Binder'-а отправляем в методе
     * onServiceConnected сообщение в сервис для инициализации первичного списка
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
                mService = new Messenger(service);
                sendMessage(MSG_INIT_LIST);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Messenger mService;
    private Messenger mClient = new Messenger(new IncomingHandler());
    static final int MSG_INIT_LIST = 1; // Инициализация первичного списка
    static final int MSG_ADD_ITEMS = 2; // Добавить элементы в начало списка (два элемента)
    static final int MSG_CHANGE_PAIR = 3; // Изменить первые два элемента списка
    static final int MSG_REMOVE_ODD = 4; // Удалить из списка нечетные позиции

    private class IncomingHandler extends Handler {

        /**
         * Получаем сгенерированные сервисом данные (ArrayList<DataItem>),
         * которые передаем как obj, а не Bundle. Если получаем MSG_INIT_LIST - инициализируем
         * RecyclerView, если что-либо другое - это значит, что мы получаем измененный список.
         * В таком случае, просим адаптер посчитать и применить изменения
         * @param msg сообщение сервиса
         */
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MyService.MSG_INIT_LIST) {
                items = (List<DataItem>) msg.obj; // Не проверяем Cast, доверяем себе :)
                initRecyclerView(); //инициализируем RecyclerView после получения данных
            } else {
                newList = (List<DataItem>) msg.obj; // Получаем измененный список
                mAdapter.onNewData(items, newList); // Просим адаптер проверить и принять изменения
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
        //mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mManager = new ChessLayoutManager();
        recyclerView.setLayoutManager(mManager);
    }

    /**
     * Метод инициализации кнопок и установки обработчиков на них
     */
    private void initButton() {
        Button addButton = findViewById(R.id.addButton);
        Button changeButton = findViewById(R.id.changeButton);
        Button removeButton = findViewById(R.id.removeButton);
        addButton.setOnClickListener(this);
        changeButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);
    }

    /**
     * Метод обработки нажатий на кнопки. В зависимости от того, на какую кнопку было произведено
     * нажатие, посылает в сервис сообщение для дальнейших действий
     * @param v собственно, сами кнопки
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                sendMessage(MSG_ADD_ITEMS);
                break;
            case R.id.changeButton:
                sendMessage(MSG_CHANGE_PAIR);
                break;
            case R.id.removeButton:
                sendMessage(MSG_REMOVE_ODD);
                break;
        }
    }

    /**
     * Сделал отдельный метод для отправки сообщений сервису
     * @param MESSAGE_CONSTANT константы-"команды" для сервиса
     */
    private void sendMessage(final int MESSAGE_CONSTANT) {
        Message messageToService = Message.obtain(null, MESSAGE_CONSTANT);
        messageToService.replyTo = mClient;
        if (mService != null) {
            try {
                mService.send(messageToService);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }
}
