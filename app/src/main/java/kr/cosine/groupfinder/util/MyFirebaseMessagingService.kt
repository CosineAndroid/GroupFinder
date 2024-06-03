package kr.cosine.groupfinder.util

import androidx.appcompat.app.AlertDialog
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

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
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {

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