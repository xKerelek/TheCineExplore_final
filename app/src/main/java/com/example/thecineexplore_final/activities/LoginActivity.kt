package com.example.thecineexplore_final.activities

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.thecineexplore_final.R

class LoginActivity : AppCompatActivity() {

    private lateinit var loginInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        loginInput = findViewById(R.id.loginInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            if (isInternetAvailable()) {
                val login = loginInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()

                if (login == "admin" && password == "root") {
                    Log.d("LoginActivity", "Login successful")
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Log.d("LoginActivity", "Invalid login or password")
                    Toast.makeText(this@LoginActivity, "Invalid login or password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d("LoginActivity", "No internet connection")
                Toast.makeText(this@LoginActivity, "No internet connection. Please try again later.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let {
            val network: Network? = connectivityManager.activeNetwork
            network?.let {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                return networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            }
        }
        return false
    }
}