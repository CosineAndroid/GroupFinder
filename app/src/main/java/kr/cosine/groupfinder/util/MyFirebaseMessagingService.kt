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
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry.uniqueId
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.presentation.view.detail.DetailActivity
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

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val myApp = applicationContext as GroupFinderApplication
        Log.d("FCM", "onMessageReceived: ")
        message.data.let { data ->
            val messageType = data["type"]
            when (messageType) {
                "join_request" -> showJoinRequestDialog(myApp.getCurrentActivity(), data)
                "join_denied" -> {
                    val currentActivity = myApp.getCurrentActivity()
                    if(currentActivity is DetailActivity) {
                        currentActivity.dismissProgressDialog()
                    }
                    showJoinDeniedDialog(myApp.getCurrentActivity())
                }
                "join_accept" -> {
                    val currentActivity = myApp.getCurrentActivity()
                    val postUUID = UUID.fromString(data["postUUID"])
                    if(currentActivity is DetailActivity) {
                        currentActivity.dismissProgressDialog()
                        currentActivity.reFreshGroupDetail(postUUID)
                    }
                }
                "force_exit" -> showForceExitDialog(myApp.getCurrentActivity())
                "already_cancel_request" -> showCanceledRequestDialog(myApp.getCurrentActivity())
                "permissionDenied" -> showPermissionDeniedDialog(myApp.getCurrentActivity())
                // 다른 메시지 유형에 대한 처리 추가시 타입과 함수 추가.
            }
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

        val requestBody =
            json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

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
                    Log.e("FCM", "Failed to send token: 2${response.message}")
                } else {
                    Log.d("FCM", "Token sent successfully")
                }
            }
        })
    }

    private fun acceptJoinRequest(senderUUID: String, postUUID: String, lane: Lane) {
        val url = "https://acceptjoinrequest-wy3rih3y5a-dt.a.run.app"
        val json = JSONObject().apply {
            put("ownerUUID", uniqueId)
            put("senderUUID", senderUUID)
            put("lane", lane)
            put("postUUID", postUUID)
        }

        val requestBody =
            json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FCM", "Failed to accept: 1$e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.d("FCM", "Failed to accept: 2${response.message}, ${response.code}")
                } else {
                    Log.d("FCM", "accept Success")
                }
            }
        })
    }

    private fun deniedJoinRequest(senderUUID: String) {
        val url = "https://deniedjoinrequest-wy3rih3y5a-dt.a.run.app"
        val json = JSONObject().apply {
            put("ownerUUID", uniqueId)
            put("senderUUID", senderUUID)
        }

        val requestBody =
            json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FCM", "Failed to denied: 1$e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.d("FCM", "Failed to denied: 2${response.message}, ${response.code}")
                } else {
                    Log.d("FCM", "accept Success")
                }
            }
        })
    }

    fun sendJoinRequest(targetUUID: UUID, senderUUID: UUID, lane: Lane, postUUID: UUID) {
        val url = "https://joinrequest-wy3rih3y5a-an.a.run.app"
        val json = JSONObject().apply {
            Log.d("FCM", "sendJoinRequest: $targetUUID, $senderUUID, $lane")
            put("type", "join_request")
            put("targetUUID", targetUUID)
            put("senderUUID", senderUUID)
            put("lane", lane.displayName)
            put("postUUID", postUUID)
        }

        val requestBody =
            json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

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

    fun sendLeaveGroupRequest(postUUID: UUID, targetUUID: UUID) {
        val url = "https://leavegrouprequest-wy3rih3y5a-dt.a.run.app"
        val json = JSONObject().apply {
            put("postUUID", postUUID)
            put("senderUUID", uniqueId)
            put("targetUUID", targetUUID)
        }

        val requestBody =
            json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

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
            val senderUUID = data["senderUUID"] ?: "error"
            val postUUID = data["postUUID"] ?: "error"
            Log.d("FCM", "showJoinRequestDialog: $senderUUID,$postUUID")
            val contextWrapper = ContextThemeWrapper(
                context,
                R.style.Theme_GroupFinder
            )
            val dialogBuilder = AlertDialog.Builder(contextWrapper)
            dialogBuilder.setTitle("참가 요청")
            dialogBuilder.setMessage("${participantName}님이 ${participantLane}에 참가를 요청 합니다.")
            dialogBuilder.setPositiveButton("수락") { dialog, which ->
                acceptJoinRequest(
                    senderUUID = senderUUID,
                    postUUID = postUUID,
                    lane = Lane.getLaneByDisplayName(participantLane!!)
                )
            }
            dialogBuilder.setNegativeButton("거부") { dialog, which ->
                deniedJoinRequest(
                    senderUUID = senderUUID
                )
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }
    }

    private fun showCanceledRequestDialog(context: Context) {
        showDialog(context, "만료된 요청", "이미 만료된 요청입니다.")
    }

    private fun showJoinDeniedDialog(context: Context) {
        showDialog(context, "참가 거절", "참가 요청이 거절되었습니다.")
    }


    private fun showForceExitDialog(context: Context) {
        showDialog(context,"강제 퇴장", "강제 퇴장되었습니다.")
    }

    private fun showPermissionDeniedDialog(context: Context) {
        showDialog(context, "권한 오류", "잠시 후 다시 시도해주세요.")
    }

    private fun showDialog(context: Context, title: String, message: String) {
        Handler(Looper.getMainLooper()).post {
            val contextWrapper = ContextThemeWrapper(
                context,
                R.style.Theme_GroupFinder
            )
            val dialogBuilder = AlertDialog.Builder(contextWrapper)
            dialogBuilder.setTitle(title)
            dialogBuilder.setMessage(message)
            dialogBuilder.setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }
    }

}