package app.added.kannauj.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import java.net.URLEncoder;

import app.added.kannauj.R;

public class SolutionActivity extends AppCompatActivity {

    WebView webview;
    String postData;
    String token,SubDomainName,StudentID,TestID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        webview = (WebView) findViewById(R.id.webview);
        //webview.loadUrl("https://onlineexam.addedschools.com/mobile_app_services/parents_app/veiw_test_result.php");
        /*token = getIntent().getExtras().getString("TOK");
        SubDomainName = getIntent().getExtras().getString("SUB");
        StudentID = getIntent().getExtras().getString("")*/

        String url = "https://onlineexam.addedschools.com/mobile_app_services/parents_app/veiw_test_result.php";
        try {
            postData = "Token=" + URLEncoder.encode(getIntent().getExtras().getString("TOK"), "UTF-8") + "&SubDomainName=" + URLEncoder.encode(getIntent().getExtras().getString("SUB"), "UTF-8") + "&StudentID=" + URLEncoder.encode(getIntent().getExtras().getString("SID"), "UTF-8") + "&TestID=" + URLEncoder.encode(getIntent().getExtras().getString("PID"), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        webview.postUrl(url, postData.getBytes());
    }
}