package com.greenovator.padc_login.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.login.*
import com.greenovator.padc_login.R
import com.greenovator.padc_login.Utils.RC_SIGN_IN
import com.greenovator.padc_login.mvp.presenter.LoginPresenter
import com.greenovator.padc_login.mvp.view.LoginView
import kotlinx.android.synthetic.main.login.view.*


class LoginActivity : BaseActivity(), LoginView {
    override fun loginSuccess() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    private var googleSignInClient: GoogleSignInClient? = null
    val callbackManager = CallbackManager.Factory.create()
    private lateinit var mLoginPresenter: LoginPresenter

    override fun facebookLoginAction() {
        f_login_button.setReadPermissions("email")
        f_login_button.setText("Facebook")
        f_login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {

            }

        })
    }

    override fun googleLoginAction() {
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        val textView: TextView = g_sign_in_button.getChildAt(0) as TextView
        textView.text = "Google"

        g_sign_in_button.setSize(SignInButton.SIZE_STANDARD)
        g_sign_in_button.setOnClickListener {
            signIn()
        }
    }

    override fun loginWithInputData(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(this.toString(), "createUserWithEmail:success")
                    val user = auth.currentUser
                    mLoginPresenter.loginSuccess()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(this.toString(), "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        FacebookSdk.sdkInitialize(getApplicationContext())
        mLoginPresenter = LoginPresenter()
        mLoginPresenter.initPresenter(this)

        mLoginPresenter.loginWithFacebook()
        mLoginPresenter.loginWithGoogle()
        login_button.setOnClickListener {
            mLoginPresenter.loginWtihInputData(
                text_input_mail.toString(),
                text_input_password.toString()
            )
        }

        //AppEventsLogger.activateApp(this)


    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(this.toString(), "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(this.toString(), "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(this.toString(), "signInWithCredential:success")
                    val user = auth.currentUser
                    mLoginPresenter.loginSuccess()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(this.toString(), "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Already Login with Google",
                        Toast.LENGTH_LONG
                    ).show()
                    mLoginPresenter.loginSuccess()
                }

            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient?.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(this.toString(), "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(this.toString(), "signInWithCredential:success")
                    Toast.makeText(baseContext, "Login Success", Toast.LENGTH_LONG).show()
                    val user = auth.currentUser
                    mLoginPresenter.loginSuccess()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(this.toString(), "signInWithCredential:failure", task.exception)
                }

            }
    }


}