# Ứng dụng Android - Tính toán số và WebView

## Giới thiệu  
Ứng dụng Android này cho phép người dùng nhập danh sách số, tính giá trị lớn nhất và nhỏ nhất, đồng thời hiển thị kết quả trong giao diện Android hoặc trang HTML nhúng trong WebView.

## Tính năng chính  
- **Tính toán giá trị lớn nhất và nhỏ nhất** từ danh sách số nhập vào.  
- **Giao diện** người dùng có thể nhập liệu qua EditText,.  
- **Hỗ trợ WebView**, cho phép hiển thị nội dung HTML và nhận dữ liệu từ JavaScript.  
- **Tích hợp đa phương tiện**  phát voice nhạc và video trên trang HTML.  

## Công nghệ sử dụng  
- **Ngôn ngữ:** Java  
- **Framework:** Android SDK  
- **WebView:** Hiển thị trang HTML trong ứng dụng Android  
- **HTML, CSS, JavaScript:** Giao diện và xử lý logic trên WebView  

### 1. **MainActivity.java**  
File chính của ứng dụng Android, xử lý giao diện và logic tính toán.  

#### **Các thành phần chính:**  
- `EditText editNumbers` - Ô nhập số  
- `Button btnCalculate` - Nút tính toán  
- `TextView textMax, textMin` - Hiển thị kết quả  
- `Button btnGoToHTML` - Chuyển sang WebView  

#### **Xử lý sự kiện tính toán số lớn nhất và nhỏ nhất:**  
```java
btnCalculate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String input = editNumbers.getText().toString();
        if (input.isEmpty()) {
            Toast.makeText(MainActivity.this, "Vui lòng nhập dữ liệu!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            String[] strNumbers = input.split(",");
            int[] numbers = new int[strNumbers.length];

            for (int i = 0; i < strNumbers.length; i++) {
                numbers[i] = Integer.parseInt(strNumbers[i].trim());
            }

            int max = Tinhtoan.findMax(numbers);
            int min = Tinhtoan.findMin(numbers);

            textMax.setText("Giá trị lớn nhất: " + max);
            textMin.setText("Giá trị nhỏ nhất: " + min);
        } catch (NumberFormatException e) {
            Toast.makeText(MainActivity.this, "Dữ liệu nhập vào không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }
});
```
### 1. **Tinhtoan.java**  

Xử lý thuật toán tìm số lớn nhất và nhỏ nhất trong mảng.
```java
public class Tinhtoan {
    public static int findMax(int[] numbers) {
        int max = numbers[0];
        for (int num : numbers) {
            if (num > max) max = num;
        }
        return max;
    }

    public static int findMin(int[] numbers) {
        int min = numbers[0];
        for (int num : numbers) {
            if (num < min) min = num;
        }
        return min;
    }
}
```
### 3. **WebView - Nhúng HTML vào ứng dụng**

Ứng dụng sử dụng WebView để hiển thị trang HTML từ thư mục assets.

Kích hoạt WebView trong Android:
```java
@SuppressLint("SetJavaScriptEnabled")
private void goToWebView() {
    setContentView(R.layout.activity_webview);

    webView = findViewById(R.id.webView);
    WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true); // Bật JavaScript
    webView.setWebViewClient(new WebViewClient());
    webView.setWebChromeClient(new WebChromeClient());

    // Thêm JavaScript Interface để giao tiếp với WebView
    webView.addJavascriptInterface(this, "Android");

    webView.loadUrl("file:///android_asset/index.html");
}
```

Giao tiếp giữa JavaScript và Android:
Từ trang HTML, JavaScript gọi phương thức Android.processInput(input) để gửi dữ liệu về Android.
```java
@android.webkit.JavascriptInterface
public void processInput(String input) {
    String[] strNumbers = input.split(",");
    int[] numbers = new int[strNumbers.length];

    try {
        for (int i = 0; i < strNumbers.length; i++) {
            numbers[i] = Integer.parseInt(strNumbers[i].trim());
        }

        int max = Tinhtoan.findMax(numbers);
        int min = Tinhtoan.findMin(numbers);

        // Gửi kết quả về JavaScript để hiển thị trên HTML
        String script = "javascript:showResult(" + max + ", " + min + ");";
        runOnUiThread(() -> webView.evaluateJavascript(script, null));

    } catch (NumberFormatException e) {
        Toast.makeText(MainActivity.this, "Dữ liệu nhập vào không hợp lệ!", Toast.LENGTH_SHORT).show();
    }
}
```

### 4. **index.html - Giao diện WebView**

Trang HTML cho phép nhập số, gọi hàm JavaScript để gửi dữ liệu đến Android, và hiển thị kết quả.
```java
<script>
    function processNumbers() {
        var input = document.getElementById('numbersInput').value;
        if (input.trim() !== "") {
            Android.processInput(input);
        } else {
            alert("Vui lòng nhập dữ liệu!");
        }
    }
</script>
```
Nhận kết quả từ Android và hiển thị trên HTML:
```java
<script>
    function showResult(max, min) {
        document.getElementById('result').innerHTML =
            "Giá trị lớn nhất: <strong>" + max + "</strong><br>Giá trị nhỏ nhất: <strong>" + min + "</strong>";
    }
</script>
```
