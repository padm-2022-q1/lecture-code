package br.edu.ufabc.helloperson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        // 1. "puxar" os componentes da interface pelos seus id's
        val personNameEditText = findViewById<EditText>(R.id.editTextPersonName)
        val sendButton = findViewById<Button>(R.id.buttonSend)
        val messageTextView = findViewById<TextView>(R.id.textViewMessage)

        // 2. capturar o evento de click do botao
        sendButton.setOnClickListener {
            // 3. implementar o tratador do click para atualizar o textview
            val message = "Hello, ${personNameEditText.text}. Welcome to UFABC!"
            messageTextView.text = message
        }
    }
}