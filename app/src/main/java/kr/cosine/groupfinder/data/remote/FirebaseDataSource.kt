package kr.cosine.groupfinder.data.remote

import com.google.firebase.firestore.FirebaseFirestore

interface FirebaseDataSource {

    val firestore: FirebaseFirestore
}