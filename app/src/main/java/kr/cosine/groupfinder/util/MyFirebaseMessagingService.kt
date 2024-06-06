package kr.cosine.groupfinder.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kr.cosine.groupfinder.GroupFinderApplication
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.enums.Lane
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import org.json.JSONObject
import java.util.UUID

class MyFirebaseMessagingService: FirebaseMessagingService() {


    override fun onMessageReceived(message: RemoteMessage) {
        val myApp = applicationContext as GroupFinderApplication
        Log.d("FCM", "onMessageReceived: Work, $message")
        if (myApp.isForeground()) {
            message.data.let { data ->
                val messageType = data["type"]
                when (messageType) {
                    "join_request" -> showJoinRequestDialog(myApp.getCurrentActivity(),data)
                    "join_denied" -> showJoinDeniedDialog()
                    "force_exit" -> showForceExitDialog()
                    // 다른 메시지 유형에 대한 처리 추가시 타입과 함수 추가.
                }
            }
        } else {
            super.onMessageReceived(message)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    fun sendRegistrationToServer(token: String, uuid: UUID) {
        val url = "https://updatetoken-wy3rih3y5a-du.a.run.app"
        val json = JSONObject().apply {
            Log.d("FCM", "sendRegistrationToServer: $token, $uuid")
            put("userToken", token)
            put("UUID", uuid)

        }

        val requestBody = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FCM", "Failed to send token: 1$e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.d("FCM", "Failed to send token: 2${response.message}")
                } else {
                    Log.d("FCM", "Token sent successfully")
                }
            }
        })
    }

    fun sendJoinRequest(targetUUID: UUID, senderUUID: UUID, lane: Lane) {
        val url = "https://joinrequest-wy3rih3y5a-an.a.run.app"
        val json = JSONObject().apply {
            Log.d("FCM", "sendJoinRequest: $targetUUID, $senderUUID, $lane")
            put("targetUUID", targetUUID)
            put("senderUUID", senderUUID)
            put("lane", lane.toString())
        }

        val requestBody = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to send request: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    println("Failed to send request: ${response.message}")
                } else {
                    val responseBody = response.body?.string()
                    println("Success: $responseBody")
                }
            }
        })
    }


    private fun showJoinRequestDialog(context: Context, data: Map<String, String>) {
        Handler(Looper.getMainLooper()).post {
            val participantName = data["name"]
            val participantLane = data["lane"]
            val contextWrapper = ContextThemeWrapper(context, R.style.Theme_GroupFinder) // AppTheme을 사용하거나 알맞은 테마로 변경
            val dialogBuilder = AlertDialog.Builder(contextWrapper)
            dialogBuilder.setTitle("참가 요청")
            dialogBuilder.setMessage("${participantName}님이 ${participantLane}에 참가를 요청 합니다.")
            dialogBuilder.setPositiveButton("수락") { dialog, which ->
                // 수락 시 허가 메세지 전송
            }
            dialogBuilder.setNegativeButton("거부") { dialog, which ->
                // 거부 시 거부 메세지 전송
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }
    }





    private fun showJoinDeniedDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("요청 거부")
        dialogBuilder.setMessage("죄송합니다, 방 참가 요청이 거부되었습니다.")
        dialogBuilder.setPositiveButton("확인")  { dialog, which ->
            dialog.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }
    private fun showForceExitDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("강제 퇴장")
        dialogBuilder.setMessage("죄송합니다, 강퇴되었습니다.")
        dialogBuilder.setPositiveButton("확인")  { dialog, which ->
            dialog.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

}