package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    EditText editNumbers;
    Button btnCalculate, btnGoToHTML;
    TextView textMax, textMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Tham chiếu các thành phần giao diện
        editNumbers = findViewById(R.id.nhapso);
        btnCalculate = findViewById(R.id.btn_tinhtoan);
        textMax = findViewById(R.id.text_max);
        textMin = findViewById(R.id.text_min);
        btnGoToHTML = findViewById(R.id.btn_go_to_html); // Thêm nút chuyển đến WebView

        // Xử lý khi nhấn nút "Tính toán"
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy chuỗi nhập vào từ EditText
                String input = editNumbers.getText().toString();

                // Kiểm tra nếu chuỗi nhập vào trống
                if (input.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập dữ liệu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    // Chuyển chuỗi thành mảng số nguyên
                    String[] strNumbers = input.split(",");
                    int[] numbers = new int[strNumbers.length];

                    for (int i = 0; i < strNumbers.length; i++) {
                        numbers[i] = Integer.parseInt(strNumbers[i].trim());
                    }

                    // Tính giá trị lớn nhất và nhỏ nhất
                    int max = Tinhtoan.findMax(numbers);
                    int min = Tinhtoan.findMin(numbers);

                    // Hiển thị kết quả
                    textMax.setText("Giá trị lớn nhất: " + max);
                    textMin.setText("Giá trị nhỏ nhất: " + min);

                } catch (NumberFormatException e) {
                    // Xử lý khi nhập sai định dạng
                    Toast.makeText(MainActivity.this, "Dữ liệu nhập vào không hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý khi nhấn nút "Chuyển sang HTML"
        btnGoToHTML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang Activity WebView
                goToWebView();
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void goToWebView() {
        setContentView(R.layout.activity_webview);  // Set layout for WebView activity

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Bật JavaScript
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        // Thêm JavaScript Interface
        webView.addJavascriptInterface(this, "Android");

        // Load file HTML từ thư mục assets
        webView.loadUrl("file:///android_asset/index.html");
    }

    // Phương thức này sẽ được gọi từ JavaScript trong HTML
    @android.webkit.JavascriptInterface
    public void processInput(String input) {
        // Kiểm tra log để xem input nhận từ JavaScript có đúng không
        Log.d("Android", "Input received from JavaScript: " + input);

        // Chuyển chuỗi nhập vào thành mảng số
        String[] strNumbers = input.split(",");
        int[] numbers = new int[strNumbers.length];

        try {
            for (int i = 0; i < strNumbers.length; i++) {
                numbers[i] = Integer.parseInt(strNumbers[i].trim());
            }

            // Tính toán Max và Min
            int max = Tinhtoan.findMax(numbers);
            int min = Tinhtoan.findMin(numbers);

            // Gửi kết quả lại cho HTML để hiển thị
            String script = "javascript:showResult(" + max + ", " + min + ");";

            // Đảm bảo thực hiện trên main thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Thực thi JavaScript trong WebView
                    webView.evaluateJavascript(script, null);
                }
            });

        } catch (NumberFormatException e) {
            // Lỗi khi dữ liệu không hợp lệ
            Log.e("Android", "Error parsing numbers: " + e.getMessage());
            Toast.makeText(MainActivity.this, "Dữ liệu nhập vào không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }
}
