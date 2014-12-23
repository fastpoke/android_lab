package org.fastpoke.honk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        //TODO set light theme
//      setTheme(android.R.style.Theme_Holo_Light_Panel);

        TabHost tabs = (TabHost) findViewById(R.id.tabHost);

        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("tag1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("CPU");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Network");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Uptime");
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        loadVersion();
        loadCpuInfo();
        loadNetInfo();
        startUptimeUpdateTimer();

    }

    private void loadVersion() {
        try {
            BufferedReader mounts = new BufferedReader(new FileReader("/proc/version"));
            try {
                String line;
                StringBuilder temp = new StringBuilder();
                while ((line = mounts.readLine()) != null) {
                    temp.append(line).append("");
                }
                ((TextView) findViewById(R.id.version)).setText(temp.toString());
            } finally {
                try {
                    mounts.close();
                } catch (IOException ignore) {
                }
            }
        } catch (IOException ignore) {
        }
    }

    private void loadCpuInfo() {
        try {
            BufferedReader mounts = new BufferedReader(new FileReader("/proc/cpuinfo"));
            try {
                String line;
                StringBuilder temp = new StringBuilder();
                while ((line = mounts.readLine()) != null) {
                    temp.append(line).append('\n');
                }
                ((TextView) findViewById(R.id.cpuinfo)).setText(temp.toString());
            } finally {
                try {
                    mounts.close();
                } catch (IOException ignore) {
                }
            }
        } catch (IOException ignore) {
        }
    }

    private void loadNetInfo() {
        /*
        Class that answers queries about the state of network connectivity.
        It also notifies applications when network connectivity changes.
        Get an instance of this class by calling Context.getSystemService(Context.CONNECTIVITY_SERVICE).
         */
        final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final WifiManager wifiMan = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipaddress = wifiInf.getIpAddress();
        int gateway = wifiMan.getDhcpInfo().gateway;

        String ip = String.format("%d.%d.%d.%d", (ipaddress & 0xff),(ipaddress >> 8 & 0xff),(ipaddress >> 16 & 0xff),(ipaddress >> 24 & 0xff));
        String gw = String.format("%d.%d.%d.%d", (gateway & 0xff),(gateway >> 8 & 0xff),(gateway >> 16 & 0xff),(gateway >> 24 & 0xff));

        //TODO parse this shit
        NetworkInfo stateMobile = cm.getAllNetworkInfo()[0];
        ((TextView) findViewById(R.id.MobileStateValue)).setText(stateMobile.getTypeName());
        ((TextView) findViewById(R.id.MobileAvailableValue)).setText(Boolean.toString(stateMobile.isAvailable()));
        ((TextView) findViewById(R.id.MobileTypeValue)).setText(stateMobile.getSubtypeName());
        ((TextView) findViewById(R.id.MobileConnectionValue)).setText(Boolean.toString(stateMobile.isConnected()));
        ((TextView) findViewById(R.id.MobileStatusValue)).setText(stateMobile.getState().toString());

        NetworkInfo stateWireless = cm.getAllNetworkInfo()[1];
        ((TextView) findViewById(R.id.WirelessStateValue)).setText(stateWireless.getTypeName());
        ((TextView) findViewById(R.id.WirelessAvailableValue)).setText(Boolean.toString(stateWireless.isAvailable()));
        ((TextView) findViewById(R.id.WirelessConnectionValue)).setText(stateWireless.getState().name());
        ((TextView) findViewById(R.id.WirelessStatusValue)).setText(Boolean.toString(stateWireless.isConnected()));
        ((TextView) findViewById(R.id.WirelessSSIDValue)).setText(wifiInf.getSSID());
        ((TextView) findViewById(R.id.WirelessIPValue)).setText(ip);
        ((TextView) findViewById(R.id.WirelessMACValue)).setText(wifiInf.getMacAddress());
        ((TextView) findViewById(R.id.WIrelessGatewayValue)).setText(gw);
        ((TextView) findViewById(R.id.WirelessSpeedValue)).setText(Integer.toString(wifiInf.getLinkSpeed()) + " Mbit/s");

       /*
       try {
            Process prs = Runtime.getRuntime().exec("netcfg");
            InputStream is = prs.getInputStream();
            byte[] b = new byte[1024];
            int size;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((size = is.read(b)) != -1) {
                System.out.println(size);
                baos.write(b, 0, size);
            }
            ((TextView) findViewById(R.id.network)).setText(new String(baos.toByteArray()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try {
                AtomicReference<InputStream> is = new AtomicReference<InputStream>();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if(is.get() != null) is.get().close();
                if(baos != null) baos.close();
            } catch (Exception ignored){}
        }
        */
    }

    private void startUptimeUpdateTimer() {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) findViewById(R.id.uptime)).setText(Long.toString(SystemClock.uptimeMillis() / 1000) + " sec");
                            }
                        });
                    }
                },
                0,
                1000
        );
    }

    //TODO load 'network' tab after reset activity
    public void reload (View button){
        Intent intent = new Intent(MyActivity.this, MyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

}
