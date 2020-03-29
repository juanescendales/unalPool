package com.example.unalpool

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity  : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        cuenta_no_existe_textView_log_in.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        login_button.setOnClickListener {
            val email = email_editText_login.text.toString()
            val password = password_editText_login.text.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {

                Log.d("Sing in", "signInWithEmail:success")
                Toast.makeText(baseContext, "Ingreso satisfactorio",
                    Toast.LENGTH_SHORT).show()
                val intent = Intent(this,TripList::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {

                Log.w("Sing in", "signInWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Fallo al iniciar sesion: ${task.exception?.message}",
                    Toast.LENGTH_SHORT).show()

            }

        }
        }

    }


}