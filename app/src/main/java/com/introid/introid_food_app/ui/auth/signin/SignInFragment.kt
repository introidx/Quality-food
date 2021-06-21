package com.introid.introid_food_app.ui.auth.signin

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
import com.introid.introid_food_app.util.Constants.TAG
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    private lateinit var auth : FirebaseAuth
    private lateinit var email : String
    private lateinit var password : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        tv_join_now.setOnClickListener{
            findNavController().navigate(R.id.action_signInFragment_to_signupFragment2)
        }

        btn_login.setOnClickListener {
            email = et_email.text.toString()
            password = et_password.text.toString()
            Log.d(TAG, "onViewCreated: Sign in Started")

            signInUser(email , password)

        }
    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email , password)
            .addOnCompleteListener {task ->
                if ( task.isSuccessful){
                    Log.d(TAG, "signInWithEmail:success")

                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }else{
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Authentication failed. $it", Toast.LENGTH_SHORT).show()
            }

    }

}