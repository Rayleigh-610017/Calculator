package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private double firstNumber = 0;       // 1つ目の数値
    private String currentOperator = "";    // 選択された演算子
    private boolean isTypingSecond = false; // 2つ目の数字を入力中かどうか

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textViewResult = findViewById(R.id.textViewResult);

        Button btn0 = findViewById(R.id.btn0);
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btnDelete = findViewById(R.id.btnDelete);

        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        Button btn6 = findViewById(R.id.btn6);
        Button btnAdd = findViewById(R.id.btnAdd);

        Button btn7 = findViewById(R.id.btn7);
        Button btn8 = findViewById(R.id.btn8);
        Button btn9 = findViewById(R.id.btn9);
        Button btnSub = findViewById(R.id.btnSub);

        Button btnDiv = findViewById(R.id.btnDiv);
        Button btnMul = findViewById(R.id.btnMul);
        Button btnEqual = findViewById(R.id.btnEqual);

        // --- 1. 数字ボタンの処理 ---
        View.OnClickListener numberListener = v -> {
            Button b = (Button) v;
            String btnText = b.getText().toString();
            String currentText = textViewResult.getText().toString();

            // 0の表示のとき、または新しい数字を入力し始めるとき
            if (currentText.equals("0") || isTypingSecond) {
                textViewResult.setText(btnText);
                isTypingSecond = false;
            } else {
                String combinedText = currentText + btnText;
                textViewResult.setText(combinedText);
            }
        };

        btn0.setOnClickListener(numberListener);
        btn1.setOnClickListener(numberListener);
        btn2.setOnClickListener(numberListener);
        btn3.setOnClickListener(numberListener);
        btn4.setOnClickListener(numberListener);
        btn5.setOnClickListener(numberListener);
        btn6.setOnClickListener(numberListener);
        btn7.setOnClickListener(numberListener);
        btn8.setOnClickListener(numberListener);
        btn9.setOnClickListener(numberListener);

        // --- 2. 演算子ボタンの処理（＋, －, ×, ÷） ---
        View.OnClickListener operatorListener = v -> {
            Button b = (Button) v;
            // 現在画面にある数字を1つ目の数値として記憶
            firstNumber = Double.parseDouble(textViewResult.getText().toString());
            currentOperator = b.getText().toString();

            // 修正後：strings.xmlのプレースホルダーに値を埋め込む
            String expression = getString(R.string.calculation_expression, formatNumber(firstNumber), currentOperator);
            textViewResult.setText(expression);

            // 次に数字が押されたら、画面を新しい数字に書き換えるフラグを立てる
            isTypingSecond = true;
        };

        btnAdd.setOnClickListener(operatorListener);
        btnSub.setOnClickListener(operatorListener);
        btnMul.setOnClickListener(operatorListener);
        btnDiv.setOnClickListener(operatorListener);

        // --- 3. ＝ボタンが押されたときの処理 ---
        btnEqual.setOnClickListener(v -> {
            if (currentOperator.isEmpty()) {
                return; // 演算子が未選択なら何もしない
            }

            // 今画面に出ている数値を「2つ目の数値」として取得
            double secondNumber = Double.parseDouble(textViewResult.getText().toString());
            double result = 0;

            // 四則演算の計算を実行
            switch (currentOperator) {
                case "＋":
                    result = firstNumber + secondNumber;
                    break;
                case "－":
                    result = firstNumber - secondNumber;
                    break;
                case "×":
                    result = firstNumber * secondNumber;
                    break;
                case "÷":
                    if (secondNumber != 0) {
                        result = firstNumber / secondNumber;
                    } else {
                        textViewResult.setText("エラー");
                        currentOperator = "";
                        return;
                    }
                    break;
            }

            // ★＝が押された瞬間に計算結果だけを画面に表示する
            textViewResult.setText(formatNumber(result));

            // 計算が終わったので演算子をクリアし、次に数字を押したらリセットされるようにする
            currentOperator = "";
            isTypingSecond = true;
        });

        // --- 4. DELボタンの処理 ---
        btnDelete.setOnClickListener(v -> {
            String currentText = textViewResult.getText().toString();
            if (currentText.length() > 1 && !currentText.equals("エラー")) {
                textViewResult.setText(currentText.substring(0, currentText.length() - 1));
            } else {
                textViewResult.setText("0");
            }
        });
    }

    // 小数点以下の ".0" を綺麗に消して整えるヘルパーメソッド
    private String formatNumber(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        } else {
            return String.valueOf(value);
        }
    }
}