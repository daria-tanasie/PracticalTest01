package ro.pub.cs.systems.eim.practicaltest01

import android.content.Context
import android.content.Intent
import java.util.Date
import kotlin.math.sqrt
import kotlin.random.Random

class ProcessingThread(private val context: Context, private val num1: Int, private val num2: Int) : Thread() {
    private var isRunning : Boolean = true

    override fun run() {
        while (isRunning) {
            try {
                sleep(1000)
                val currentDate = Date().toString()
                val arithmeticMean = (num1 + num2) / 2.0
                val geometricMean = sqrt((num1 * num2).toDouble())

                val message = "Data: $currentDate, Media Aritmetică: $arithmeticMean, Media Geometrică: $geometricMean"
                sendMessage(message)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                isRunning = false
            }
        }
    }

    private fun sendMessage(message: String) {
        val broadcastIntent = Intent()

        val randomAction = Constants.ACTIONS[Random.nextInt(Constants.ACTIONS.size)]
        broadcastIntent.action = randomAction

        broadcastIntent.putExtra(Constants.EXTRA_BROADCAST_MESSAGE, message)

        context.sendBroadcast(broadcastIntent)
    }

    fun stopThread() {
        isRunning = false
        interrupt()
    }

}