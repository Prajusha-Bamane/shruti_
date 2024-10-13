package com.example.myapplication.event

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.man_side.mam_side
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class EventNotificationService : FirebaseMessagingService() {

  private val CHANNEL_ID = "event_channel"

  override fun onCreate() {
    super.onCreate()
    createNotificationChannel()
  }

  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        CHANNEL_ID,
        "Event Notifications",
        NotificationManager.IMPORTANCE_HIGH
      )
      val notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.createNotificationChannel(channel)
    }
  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    Log.d(TAG, "From: ${remoteMessage.from}")

    // Check if message contains a data payload.
    if (remoteMessage.data.isNotEmpty()) {
      Log.d(TAG, "Message data payload: ${remoteMessage.data}")
      val eventTitle = remoteMessage.data["eventTitle"]
      val eventDescription = remoteMessage.data["eventDescription"]

      if (eventTitle != null && eventDescription != null) {
        // Create and show the notification
        createAndShowNotification(eventTitle, eventDescription)

        //Send notification to registered users using FCM
      } else {
        Log.w(
          "EventNotificationService",
          "Event title or description is missing in data payload"
        )
      }
    }

    // Check if message contains a notification payload.
    remoteMessage.notification?.let {
      Log.d(TAG, "Message Notification Body: ${it.body}")
      sendNotification(it.title, it.body)
    }
  }

  private fun createAndShowNotification(eventTitle: String, eventDescription: String) {
    val intent = Intent(this, mam_side::class.java).apply {
      putExtra("fragment_to_show", "event_mam")
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }
    val pendingIntent = PendingIntent.getActivity(
      this,
      0,
      intent,
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
      .setSmallIcon(R.drawable.img)
      .setContentTitle(eventTitle)
      .setContentText(eventDescription)
      .setAutoCancel(true)
      .setContentIntent(pendingIntent)

    val notificationManager =
      getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        CHANNEL_ID,
        "Event Notifications",
        NotificationManager.IMPORTANCE_HIGH
      )
      notificationManager.createNotificationChannel(channel)
    }
    notificationManager.notify(0, notificationBuilder.build())
  }

  private fun sendNotification(title: String?, messageBody: String?) {
    val intent = Intent(this, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val pendingIntent = PendingIntent.getActivity(
      this,
      0, /* Request code */
      intent,
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val channelId = getString(R.string.event_channel) // Use your channel ID string resource
    val notificationBuilder = NotificationCompat.Builder(this, channelId)
      .setSmallIcon(R.drawable.img) // Use your notification icon
      .setContentTitle(title)
      .setContentText(messageBody)
      .setAutoCancel(true)
      .setContentIntent(pendingIntent)

    val notificationManager =
      getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Since android Oreo notification channel is needed.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        channelId,
        "Channel human readable title",
        NotificationManager.IMPORTANCE_DEFAULT
      )
      notificationManager.createNotificationChannel(channel)
    }

    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
  }



  companion object {
    private const val TAG = "MyFirebaseMsgService"
  }
}