package com.example.InsubriApp

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant

class Message @RequiresApi(Build.VERSION_CODES.O) constructor(testo: String? = null, ora : String?= Instant.now().toString(),
    mit : String? = null) {
    public var messaggio = testo
    @RequiresApi(Build.VERSION_CODES.O)
    public var orario = ora
    var mittente = mit
}