package com.lwp.aidltest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    MyService.MyBinder mb;

    //IBinder
    //ServicerConnection:用于绑定客户端和Service
    //进度监控
    private ServiceConnection conn = new ServiceConnection() {

        //当客户端正常连接着服务时，执行服务的绑定操作会被调用
        // 与服务通讯， 调用服务方法

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            Log.e("TAG","onServiceConnected");

            //使用aidl文件后的写法：
            IMyAidlInterface imai = IMyAidlInterface.Stub.asInterface(iBinder);
            try {
                // 调用服务方法
                imai.showProgress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            //一般情况写法
//            mb = (MyService.MyBinder) iBinder;
//            Log.e("TAG","bind 当前进度是：" + mb.getProcess());
        }

        //当客户端和服务的连接丢失了
        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void operate(View v){
        switch (v.getId()){

            case R.id.start:
                //启动服务:创建-->启动-->销毁
                //如果服务已经创建了，后续重复启动，操作的都是同一个服务，不会再重新创建了，除非你先销毁它
                Intent it1 = new Intent(this,MyService.class);
                startService(it1);

                if (mb != null) {
                    Log.e("TAG","start 当前进度是：" + mb.getProcess());
                }

                break;
            case R.id.stop:
                Intent it2 = new Intent(this,MyService.class);
                stopService(it2);

                if (mb != null) {
                    Log.e("TAG","stop 当前进度是：" + mb.getProcess());
                }

                break;

            case R.id.bind:
                //绑定服务：最大的 作用是用来实现对Service执行的任务进行进度监控
                //服务已经存在：
                // 那么bindService方法只能使onBind方法被调用，
                // 而unbindService方法只能使onUnbind被调用
                Intent it3 = new Intent(this,MyService.class);
                bindService(it3, conn,BIND_AUTO_CREATE);

                break;
            case R.id.unbind:
                //解绑服务
                Log.e("TAG","unbind 当前进度是：" + mb.getProcess());
                unbindService(conn);
                break;
        }
    }
}
