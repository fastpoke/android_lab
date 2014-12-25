package org.fastpoke.dafuq;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.*;

public class MainActivity extends Activity {

    public EditText editTextInput;
    public Button button;
    public EditText test;
    public EditText test2;
    final String LOG_TAG = "myLogs";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        editTextInput = (EditText) findViewById(R.id.editTextInput);
        test = (EditText) findViewById(R.id.test);
        test2 = (EditText) findViewById(R.id.test2);
        setContentView(R.layout.main);
    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void onSearchClick(View button) throws IOException {
        try {
            //TODO fix getText
//            String url = "http://google.com/complete/search?output=toolbar&q=derp";
            button.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View view) {
                            try {
                                System.out.println(editTextInput.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );

            new HttpAsyncTask().execute("http://google.com/complete/search?output=toolbar&q=derp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String GET(String url) {
        InputStream inputStream;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    public String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        inputStream.close();
        return result;
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received", Toast.LENGTH_LONG).show();
            XmlParser();
            test = (EditText) findViewById(R.id.test);
            test.setText(result, TextView.BufferType.EDITABLE);
        }

        public void XmlParser() {
            try {
                XmlPullParser xpp = prepareXpp();

                while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                    switch (xpp.getEventType()) {
                        case XmlPullParser.START_DOCUMENT:
                            Log.d(LOG_TAG, "START_DOCUMENT");
                            break;
                        case XmlPullParser.START_TAG:
                            Log.d(LOG_TAG, "START_TAG: name = " + xpp.getName()
                                    + ", depth = " + xpp.getDepth() + ", attrCount = "
                                    + xpp.getAttributeCount());
                            String tmp = "";
                            for (int i = 0; i < xpp.getAttributeCount(); i++) {
                                tmp = tmp + xpp.getAttributeName(i) + " = "
                                        + xpp.getAttributeValue(i) + ", ";
                            }
                            if (!TextUtils.isEmpty(tmp))
                                Log.d(LOG_TAG, "Attributes: " + tmp);
                            break;
                        case XmlPullParser.END_TAG:
                            Log.d(LOG_TAG, "END_TAG: name = " + xpp.getName());
                            break;
                        case XmlPullParser.TEXT:
                            Log.d(LOG_TAG, "text = " + xpp.getText());
                            break;

                        default:
                            break;
                    }
                    xpp.next();
                }
                Log.d(LOG_TAG, "END_DOCUMENT");

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public XmlPullParser prepareXpp() throws XmlPullParserException, IOException {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            //TODO replace string to convertInputStreamToString(result)
            xpp.setInput(new StringReader("derp"));
            return xpp;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
