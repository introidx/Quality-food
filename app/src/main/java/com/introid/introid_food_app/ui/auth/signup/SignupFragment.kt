package com.introid.introid_food_app.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.introid.introid_food_app.ui.MainActivity
import com.introid.introid_food_app.R
import kotlinx.android.synthetic.main.fragment_signup.*

class SignupFragment : Fragment(R.layout.fragment_signup) {

    private lateinit var auth : FirebaseAuth
    private lateinit var email : String
    private lateinit var password : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        tv_log_in.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_signInFragment)
        }

        btn_signup.setOnClickListener {
            email = et_email.text.toString()
            password = et_password.text.toString()

            signUp(email , password)
        }
    }

    private fun signUp(email: String, password: String) {
        Toast.makeText(context , "Sign up Started", Toast.LENGTH_SHORT).show()
        Toast.makeText(context , "email : $email and password : $password", Toast.LENGTH_SHORT).show()

        auth.createUserWithEmailAndPassword(email , password)
            .addOnCompleteListener{task ->
                if (task.isSuccessful){
                    Log.d("Signin", "createUserWithEmail:success")
                    Toast.makeText(context , "Sign up success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
            }else{
                    Log.w("Signin", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }


            }
            .addOnFailureListener {
                Toast.makeText(context, "Authentication failed. $it", Toast.LENGTH_SHORT).show()
            }

    }

}