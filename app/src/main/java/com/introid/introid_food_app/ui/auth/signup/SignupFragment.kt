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
import com.introid.introid_food_app.localDB.prefs.UserManager
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignupFragment : Fragment(R.layout.fragment_signup) {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var name: String
    private lateinit var phone: String

    private lateinit var userManager: UserManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        userManager = UserManager(requireContext())

        tv_log_in.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_signInFragment)
        }

        btn_signup.setOnClickListener {
            email = et_email.text.toString()
            password = et_password.text.toString()
            name = et_name.text.toString()
            phone = et_phone.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty()) {
                signUp(email, password)
            } else {
                Toast.makeText(requireContext(), "Please fill all details", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun signUp(email: String, password: String) {
        Toast.makeText(context, "Sign up Started", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "email : $email and password : $password", Toast.LENGTH_SHORT)
            .show()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Signin", "createUserWithEmail:success")
                    Toast.makeText(context, "Sign up success", Toast.LENGTH_SHORT).show()
                    // store user data to shared prefs/ data store
                    storeUserDataToDataStore()
                } else {
                    Log.w("Signin", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Authentication failed. $it", Toast.LENGTH_SHORT).show()
            }

    }

    private fun storeUserDataToDataStore() {
        GlobalScope.launch {
            userManager.storeUser(name, email, phone)
        }
        // start the main activity
        startMainActivity()
    }

    private fun startMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

}