package kr.cosine.groupfinder.util

import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kr.cosine.groupfinder.enums.TestGlobalUserData.uuID
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
        if (isAppInForeground()) {
            message.data.let { data ->
                val messageType = data["type"]
                when (messageType) {
                    "join_request" -> showJoinRequestDialog(data)
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
        sendRegistrationToServer(token, uuID.toString())
    }

    private fun sendRegistrationToServer(token: String, uuid: String) {
        val url = "https://asia-northeast3-groupfinder-b2f8e.cloudfunctions.net/registerToken"
        val json = JSONObject().apply {
            put("token", token)
            put("uuid", uuid)
        }

        val requestBody = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FCM", "Failed to send token: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.d("FCM", "Failed to send token: ${response.message}")
                } else {
                    Log.d("FCM", "Token sent successfully")
                }
            }
        })
    }

    private fun isAppInForeground(): Boolean {
        // 포그라운드 상태일 때 다이얼로그를, 보고있지 않다면 notification을 통한 알림을. 사용시 수정필요
        return true
    }

    private fun showJoinRequestDialog(data: Map<String,String>) {//참가자의 닉네임, 라인이 넘어옴
        val participantName = data["name"]
        val participantLane = data["lane"]
        val dialogBuilder = AlertDialog.Builder(this)
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