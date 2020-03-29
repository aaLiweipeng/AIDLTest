package com.lwp.aidltest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }

    MyService.MyBinder mb = new MyBinder();

    private int i;

    //无论使用start还是bind启动服务，创建时都回调onCreate，并开始数数
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("MyService","服务创建了");

        //开启一个线程（从1数到100，每秒输一次），用于模拟耗时的任务
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    for (i = 1; i <= 100; i++) {
                        sleep(1000);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //启动
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("MyService","服务启动了");
        return super.onStartCommand(intent, flags, startId);
    }

    //绑定
    //IBinder：在android中用于远程操作对象的一个基本接口
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.e("MyService","服务绑定了");

        //实现AIDL接口
        // 注意 Stub类既是IMyAidlInterface的子类，也是Binder的子类！！！！
        return new IMyAidlInterface.Stub(){
            @Override
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            }

            @Override
            public void showProgress() throws RemoteException {
                Log.e("TAG","当前进度是" + i);
            }
        };

//        return mb;
    }

    //对于onBind方法而言，要求返回IBinder对象
    //实际上，我们会自己定义一个内部类，集成Binder类

    class MyBinder extends Binder {

        //定义自己需要的方法（实现进度监控）
        public int getProcess(){
            return i;
        }
    }

    //解绑
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("MyService","服务解绑了");
        return super.onUnbind(intent);

    }

    //摧毁
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("MyService","服务销毁了");

    }
}