package ro.pub.cs.systems.eim.practicaltest01

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01MainActivity : AppCompatActivity() {

    private lateinit var input1:  EditText
    private lateinit var input2: EditText
    private val intentFilter = IntentFilter()

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
            startServiceIfConditionIsMet(leftNumber, rightNumber)
        }

        val pressMeTooButton = findViewById<Button>(R.id.right_button)
        pressMeTooButton.setOnClickListener {
            rightNumber++
            input2.setText(rightNumber.toString())
            startServiceIfConditionIsMet(leftNumber, rightNumber)
        }

        val activityResultsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Toast.makeText(this, "Activity result ok", Toast.LENGTH_LONG).show()
            } else if (result.resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Activity result canceled", Toast.LENGTH_LONG).show()
            }
        }

        val navigateToSecActivityButton = findViewById<Button>(R.id.second_activity_button)
        navigateToSecActivityButton.setOnClickListener {
            val intent = Intent(this, PracticalTest01SecondaryActivity::class.java)
            intent.putExtra(Constants.INPUT1, Integer.valueOf(input1.text.toString()))
            intent.putExtra(Constants.INPUT2, Integer.valueOf(input2.text.toString()))
            activityResultsLauncher.launch(intent)
        }

        Constants.ACTIONS.forEach { action ->
            intentFilter.addAction(action)
        }
    }

    private fun startServiceIfConditionIsMet(leftNumber: Int, rightNumber: Int) {
        if (leftNumber + rightNumber > Constants.NUMBER_OF_CLICKS_THRESHOLD) {
            val intent = Intent(applicationContext, PracticalTest01Service::class.java).apply {
                putExtra(Constants.INPUT1, leftNumber)
                putExtra(Constants.INPUT2, rightNumber)
            }
            applicationContext.startService(intent)
        }
    }

    private val messageBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                Log.d(Constants.BROADCAST_RECEIVER_TAG, it.action.toString())
                Log.d(Constants.BROADCAST_RECEIVER_TAG, it.getStringExtra(Constants.EXTRA_BROADCAST_MESSAGE).toString())
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(messageBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(messageBroadcastReceiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(messageBroadcastReceiver)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        val intent = Intent(applicationContext, PracticalTest01Service::class.java)
        applicationContext.stopService(intent)
        super.onDestroy()
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