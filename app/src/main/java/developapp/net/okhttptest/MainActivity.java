package developapp.net.okhttptest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textRes;
    private TextView textDes;
    private Button getBtn;
    private Button postBtn;

    private String res ="";
    private String des ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textRes =(TextView)findViewById(R.id.res);
        textDes =(TextView)findViewById(R.id.des);
        getBtn =(Button)findViewById(R.id.btn_get);
        getBtn.setOnClickListener(this);
        postBtn =(Button)findViewById(R.id.btn_post);
        postBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get: getTest(); break;
            case R.id.btn_post: postTest(); break;
            default:break;
        }
    }

    //GET
    private void getTest(){
        Request request = new Request.Builder()
                .url("http://weather.livedoor.com/forecast/webservice/json/v1?city=130010") // 130010->東京
                .get()
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                failMessage();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                res = response.body().string();

                try {
                    JSONObject resJson = new JSONObject(res);
                    JSONArray weathers = resJson.getJSONArray("pinpointLocations"); //pinpointLocations　を取り出す
                    JSONObject weather = weathers.getJSONObject(0); //２番めにアクセスしたいなら１を指定
                    String description = weather.getString("name");  //name を取得
                    des = description;

                    //UI 反映
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textRes.setText(res);
                            textDes.setText(des);
                        }
                    });

                }catch (JSONException e){
                    failMessage();
                    e.printStackTrace();
                }
            }
        });
    }

    //POST
    private void postTest(){
        RequestBody formBody = new FormBody.Builder()
//                .add("tokyo", "130010")
//                .add("osaka", "270000")
//                .add("name", "nanashinogonbei")
//                .add("action", "hoge")
//                .add("value", "fuga")
                .add("id","gest")
                .add("password","12345")
//                .add("name","hoge")
                .build();

        final Request request = new Request.Builder()
//                .url("http://www.muryou-tools.com/test/aaaa.php")
                .url("https://rest-test-snowpooll.c9users.io/rest_test.php")
                .post(formBody)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                failMessage();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                res = response.body().string();

                //UI反映
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textRes.setText(res);
                        textDes.setText("No Data");
                    }
                });

            }
        });
    }

    private void failMessage(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textRes.setText("onFailure");;
                textDes.setText("No Data");
            }
        });
    }

}
