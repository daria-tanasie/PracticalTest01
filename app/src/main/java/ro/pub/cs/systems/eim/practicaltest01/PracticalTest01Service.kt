package ro.pub.cs.systems.eim.practicaltest01

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.SyncStateContract
import android.util.Log

class PracticalTest01Service : Service() {
    private var processingThread : ProcessingThread? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val input1 = intent?.getIntExtra(Constants.INPUT1, 0) ?: 0
        val input2 = intent?.getIntExtra(Constants.INPUT2, 0) ?: 0

        if (processingThread == null || !processingThread!!.isAlive) {
            processingThread = ProcessingThread(this, input1, input2).apply {
                start()
            }
        }
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        processingThread?.stopThread()
    }
}