package ro.pub.cs.systems.eim.practicaltest01

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01MainActivity : AppCompatActivity() {

    private lateinit var input1:  EditText
    private lateinit var input2: EditText

    private var leftNumber = 0
    private var rightNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        input1 = findViewById(R.id.left_edit_text)
        input2 = findViewById(R.id.right_edit_text)
        input1.setText("0")
        input2.setText("0")

        val pressMeButton = findViewById<Button>(R.id.left_button)
        pressMeButton.setOnClickListener {
            leftNumber++
            input1.setText(leftNumber.toString())
        }

        val pressMeTooButton = findViewById<Button>(R.id.right_button)
        pressMeTooButton.setOnClickListener {
            rightNumber++
            input2.setText(rightNumber.toString())
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(Constants.INPUT1, input1.text.toString())
        outState.putString(Constants.INPUT2, input2.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        if (savedInstanceState.containsKey(Constants.INPUT1)
            && savedInstanceState.containsKey(Constants.INPUT2)) {
            input1.setText(savedInstanceState.getString(Constants.INPUT1))
            input2.setText(savedInstanceState.getString(Constants.INPUT2))
        }
    }
}