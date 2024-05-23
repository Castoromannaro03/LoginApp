package com.example.InsubriApp

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant

class Message @RequiresApi(Build.VERSION_CODES.O) constructor(testo: String? = null, ora : String?= Instant.now().toString()) {
    public var messaggio = testo
    @RequiresApi(Build.VERSION_CODES.O)
    public var orario = ora

    //FieldValue.serverTimestamp().
    //Timestamp.now().toInstant().toString()

    // ora : Timestamp? = Timestamp.now()

}