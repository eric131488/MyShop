package com.hank.myshop

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {
    private val RC_SIGN_IN = 100
    private val authInstance = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val currentUser = FirebaseAuth.getInstance()
            .currentUser

        if (currentUser == null) {
            showSignInUI()
        }
    }

    private fun showSignInUI() {
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(false)
            .setLogo(R.drawable.shop)
            .setAvailableProviders(
                Arrays.asList(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build()
                )
            )
            .build()
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onStart() {
        super.onStart()

        authInstance.addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()

        authInstance.removeAuthStateListener(this)
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        val user = auth.currentUser
        if (user != null) {
            user_info.text = user?.email
        } else {
            user_info.text = "Not Sign In"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_signin -> {
                showSignInUI()
                true
            }
            R.id.action_signout -> {
                authInstance.signOut()
                true
            }
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
