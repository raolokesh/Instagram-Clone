package com.lokesh.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.lokesh.instagramclone.databinding.ActivitySignupBinding
import com.lokesh.instagramclone.models.UserRegisterModel
import com.lokesh.instagramclone.utils.USER_NODE
import com.lokesh.instagramclone.utils.USER_PROFILE_FOLDER
import com.lokesh.instagramclone.utils.uploadImage
import com.squareup.picasso.Picasso

class SignupActivity : AppCompatActivity() {
    val binding by lazy { ActivitySignupBinding.inflate(layoutInflater)}

    lateinit var user:UserRegisterModel
    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        uri?.let {
            uploadImage(uri, USER_PROFILE_FOLDER){
                if( it == null){

                }else{
                    user.image= it
                    binding.profileimage.setImageURI(uri)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        user = UserRegisterModel()
        if (intent.hasExtra("MODE")){
            if(intent.getIntExtra("MODE",-1) == 1){
                binding.signupButton.text = "Update Profile"
                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                    user  = it.toObject<UserRegisterModel>()!!

                    if(!user.image.isNullOrEmpty()){
                        Picasso.get().load(user.image).into(binding.profileimage)
                    }
                    binding.usernameField.editText?.setText(user.name)
                    binding.emailField.editText?.setText(user.email)
                    binding.passwordField.editText?.setText(user.password)
                }
            }
        }
        binding.signupButton.setOnClickListener {
            if (intent.hasExtra("MODE")){
                if(intent.getIntExtra("MODE",-1) == 1){
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            startActivity(Intent(this@SignupActivity,HomeActivity::class.java))
                            finish()
                        }
                }
            }
            else{
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

                        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this@SignupActivity,"User register Successfully",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@SignupActivity,HomeActivity::class.java))
                                finish()
                            }

                    }
                    else{
                        Toast.makeText(this@SignupActivity,result.exception?.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }

            }
            }
        }
        binding.addimage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.loginTextView.setOnClickListener {
            startActivity(Intent(this@SignupActivity,LoginActivity::class.java))
            finish()
        }
    }
}