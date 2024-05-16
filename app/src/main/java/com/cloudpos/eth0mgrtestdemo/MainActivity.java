package com.cloudpos.eth0mgrtestdemo;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cloudpos.utils.Logger;
import com.cloudpos.utils.TextViewUtil;
import com.wizarpos.wizarviewagentassistant.aidl.IEth0MgrService;
import com.wizarpos.wizarviewagentassistant.aidl.IpBean;


public class MainActivity extends AbstractActivity implements OnClickListener, ServiceConnection {

    //    private ISystemManagerService mService;
    private IEth0MgrService mEth0Mgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_run3 = (Button) this.findViewById(R.id.btn_run3);
        Button btn_run4 = (Button) this.findViewById(R.id.btn_run4);
        Button btn_run5 = (Button) this.findViewById(R.id.btn_run5);
        log_text = (TextView) this.findViewById(R.id.text_result);
        log_text.setMovementMethod(ScrollingMovementMethod.getInstance());

        findViewById(R.id.settings).setOnClickListener(this);
        btn_run3.setOnClickListener(this);
        btn_run4.setOnClickListener(this);
        btn_run5.setOnClickListener(this);


        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == R.id.log_default) {
                    log_text.append("\t" + msg.obj + "\n");
                } else if (msg.what == R.id.log_success) {
                    String str = "\t" + msg.obj + "\n";
                    TextViewUtil.infoBlueTextView(log_text, str);
                } else if (msg.what == R.id.log_failed) {
                    String str = "\t" + msg.obj + "\n";
                    TextViewUtil.infoRedTextView(log_text, str);
                } else if (msg.what == R.id.log_clear) {
                    log_text.setText("");
                }
            }
        };
//        bindService();
//        bindSetPropService();
        bindEth0MgrService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEth0Mgr != null) {
            this.mEth0Mgr = null;
        }
        this.unbindService(this);
    }

    public void bindEth0MgrService() {
        try {
            boolean result = startConnectService(MainActivity.this,
                    "com.wizarpos.wizarviewagentassistant",
                    "com.wizarpos.wizarviewagentassistant.Eth0MgrService", this);

            writerInSuccessLog("bing service result " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected boolean startConnectService(Context mContext, String packageName, String className, ServiceConnection connection) {
        boolean isSuccess = startConnectService(mContext, new ComponentName(packageName, className), connection);
        return isSuccess;
    }

    protected boolean startConnectService(Context context, ComponentName comp, ServiceConnection connection) {
        Intent intent = new Intent();
        intent.setPackage(comp.getPackageName());
        intent.setComponent(comp);
        boolean isSuccess = context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        Logger.debug("bind service (%s, %s)", isSuccess, comp.getPackageName(), comp.getClassName());
        return isSuccess;
    }


    @Override
    public void onClick(View arg0) {
        int index = arg0.getId();
        if (mEth0Mgr == null) {
            writerInFailedLog("bind service failed!");
            return;
        }
        if (index == R.id.btn_run3) {
            enableStaticIp();
        } else if (index == R.id.btn_run4) {
            getStaticIp();
        } else if (index == R.id.btn_run5) {
            setStaticIp();
        } else if (index == R.id.settings) {
            log_text.setText("");
        }
    }

    private void enableStaticIp() {
        try {
            mEth0Mgr.enableStaticIp(false);
            writerInLog("enableStaticIp:false", R.id.log_default);
            boolean isStaticIp = mEth0Mgr.isStaticIp();
            writerInLog("isStaticIp:" + isStaticIp, R.id.log_default);

            writerInLog("enableStaticIp:true", R.id.log_default);
            mEth0Mgr.enableStaticIp(true);
            isStaticIp = mEth0Mgr.isStaticIp();
            writerInLog("isStaticIp:" + isStaticIp, R.id.log_default);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void getStaticIp() {
        try {
            IpBean currentIpBean = mEth0Mgr.getStaticIp();
            writerInLog("getStaticIp:" + currentIpBean, R.id.log_default);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setStaticIp() {
        try {
            IpBean ipBean = new IpBean();
            ipBean.setIpAddress("192.168.22.2");
            ipBean.setPrefixLength("25");
            ipBean.setGateway("192.168.22.3");
            ipBean.setDnsServer("192.168.22.4");
            mEth0Mgr.setStaticIp(ipBean);
            writerInLog("setStaticIp:" + ipBean, R.id.log_default);
            IpBean currentIpBean = mEth0Mgr.getStaticIp();
            writerInLog("getStaticIp:" + currentIpBean, R.id.log_default);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
//        IAgentService agentService = IAgentService.Stub.asInterface(service);
//        agentService.setSystemVariables("persist.wp.scan.honeywell", "0");
//        mService = ISystemManagerService.Stub.asInterface(service);
        mEth0Mgr = IEth0MgrService.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
