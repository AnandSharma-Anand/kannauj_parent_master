package app.added.kannauj.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import app.added.kannauj.R;
import app.added.kannauj.Utils.Prefs;

public class ReportsActivity extends AppCompatActivity {

    WebView web;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        String token = new Prefs(ReportsActivity.this).getToken();
        String student_id = new Prefs(ReportsActivity.this).getStudentId();
        String domain = new Prefs(ReportsActivity.this).getDomain();
        //  String url = "http://saigal.addedschools.com/admin/app_login.php?Token=" + token + "&FileName=examination/student_report_card.php?SelectedStudents=1";

        //vxplore 18/7/19

        String finalDomail = domain.replace("mobile_app_services/parents_app/", "");
        String url = finalDomail + "admin/app_login.php?Token=" + token + "&FileName=examination/student_report_card.php?SelectedStudents=" + student_id;
        Log.i("viewReportURL",url);
        setContentView(R.layout.activity_report);

        web = findViewById(R.id.webview);
        WebSettings settings=web.getSettings();
        settings.setJavaScriptEnabled(true);
        web.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                //getWindow().setTitle(title); //Set Activity tile to page title.
            }
        });

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        web.loadUrl(url);
    }
}
