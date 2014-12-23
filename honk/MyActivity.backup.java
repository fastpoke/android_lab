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

import java.io.*;
import java.util.*;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

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
                    temp.append(line).append('\n');
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
        final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        WifiManager wifiMan = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipaddress = wifiInf.getIpAddress();
        int gateway = wifiMan.getDhcpInfo().gateway;
        String ip = String.format("%d.%d.%d.%d", (ipaddress & 0xff),(ipaddress >> 8 & 0xff),(ipaddress >> 16 & 0xff),(ipaddress >> 24 & 0xff));
        String gw = String.format("%d.%d.%d.%d", (gateway & 0xff),(gateway >> 8 & 0xff),(gateway >> 16 & 0xff),(gateway >> 24 & 0xff));

        //TODO parse this shit

        NetworkInfo stateMobile = cm.getAllNetworkInfo()[0];
        ((TextView) findViewById(R.id.title)).setText(
                "Network state:         " + stateMobile.getTypeName() + "\n" +
                "Network available:     " + Boolean.toString(stateMobile.isAvailable()) + "\n" +
                "Network type:          " + stateMobile.getSubtypeName() + "\n" +
                "Network connection:    " + Boolean.toString(stateMobile.isConnected()) + "\n" +
                "Connection status:     " + stateMobile.getState()
        );
        NetworkInfo stateWireless = cm.getAllNetworkInfo()[1];
        ((TextView) findViewById(R.id.title)).setText(
                "Network state:         " + stateWireless.getTypeName() + "\n" +
                "Network available:     " + Boolean.toString(stateWireless.isAvailable()) + "\n" +
                "Network connection:    " + Boolean.toString(stateWireless.isConnected()) + "\n" +
                "Connection status:     " + stateWireless.getState() + "\n" +
                "SSID:                  " + wifiInf.getSSID() + "\n" +
                "IP Address:            " + ip + "\n" +
                "MAC Address:           " + wifiInf.getMacAddress() + "\n" +
                "Gateway:               " + gw + "\n" +
                "Link speed:            " + wifiInf.getLinkSpeed() + " Mbit/s"
        );

       /* try {
            Process prs = Runtime.getRuntime().exec("netcfg");
            InputStream is = prs.getInputStream();
            byte[] b = new byte[1024];
            int size;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((size = is.read(b)) != -1) {
                System.out.println(size);
                baos.write(b, 0, size);
            }
            //((TextView) findViewById(R.id.network)).setText(new String(baos.toByteArray()));
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
        }*/
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


    public void reload (View button){
        Intent intent = new Intent(MyActivity.this, MyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }


}


