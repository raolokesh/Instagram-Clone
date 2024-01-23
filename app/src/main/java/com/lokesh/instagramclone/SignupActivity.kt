package com.lokesh.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.lokesh.instagramclone.databinding.ActivitySignupBinding
import com.lokesh.instagramclone.models.UserRegisterModel

class SignupActivity : AppCompatActivity() {
    val binding by lazy { ActivitySignupBinding.inflate(layoutInflater)}

    lateinit var user:UserRegisterModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        user = UserRegisterModel()
        binding.signupButton.setOnClickListener {
            if(binding.usernameField.editText?.text.toString().equals("") or
                binding.emailField.editText?.text.toString().equals("") or
                binding.passwordField.editText?.text.toString().equals("")){

                Toast.makeText(this@SignupActivity,"Please fill the all Information", Toast.LENGTH_SHORT).show()
            }
            else{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.emailField.editText?.text.toString(),
                    binding.passwordField.editText?.text.toString()
                ).addOnCompleteListener {
                    result ->
                    if (result.isSuccessful){
                        user.name = binding.usernameField.editText?.text.toString()
                        user.email = binding.emailField.editText?.text.toString()
                        user.password =  binding.passwordField.editText?.text.toString()

                        Firebase.firestore.collection("User").document(Firebase.auth.currentUser!!.uid).set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this@SignupActivity,"User register Successfully",Toast.LENGTH_SHORT).show()
                            }

                    }
                    else{
                        Toast.makeText(this@SignupActivity,result.exception?.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }
}