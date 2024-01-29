package com.lokesh.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.lokesh.instagramclone.databinding.ActivityLoginBinding
import com.lokesh.instagramclone.models.UserRegisterModel

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener{
            if(binding.emailField.editText?.text.toString().equals("") or
                binding.passwordField.editText?.text.toString().equals("") ){
                Toast.makeText(this@LoginActivity,"Please fill all the details",Toast.LENGTH_LONG).show()
            }else{
                var user = UserRegisterModel(binding.emailField.editText?.text.toString(),
                    binding.passwordField.editText?.text.toString())
                Firebase.auth.signInWithEmailAndPassword(user.email!!,user.password!!)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(this@LoginActivity,it.exception?.localizedMessage,Toast.LENGTH_LONG).show()

                        }
                    }
            }
        }
        binding.signupButton.setOnClickListener {
            startActivity(Intent(this@LoginActivity,SignupActivity::class.java))
        }
    }
}