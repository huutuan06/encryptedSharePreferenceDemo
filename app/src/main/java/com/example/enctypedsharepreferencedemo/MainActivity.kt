package com.example.enctypedsharepreferencedemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class MainActivity : AppCompatActivity() {
    companion object {
        const val KEY_PLAIN_TEXT = "KEY_DATA_PLAIN_TEXT"
        const val KEY_ENCRYPTED_TEXT = "KEY_DATA_PLAIN_TEXT"
    }

    private var editText: EditText? = null

    private var btnSaveToPrefer: Button? = null
    private var btnSavedToEncryptedPrefer: Button? = null

    private var btnGetFromPrefer: Button? = null
    private var btnGetFromEncryptedPrefer: Button? = null

    var sharedPref: SharedPreferences? = null
    var encryptedSharedPref: SharedPreferences? = null

    var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getView()
        setupButton()
    }

    private fun getView() {
        editText = findViewById(R.id.edt_data)
        btnSaveToPrefer = findViewById(R.id.btn_save_to_prefer)
        btnSavedToEncryptedPrefer = findViewById(R.id.btn_save_to_encrypted_prefer)
        btnGetFromPrefer = findViewById(R.id.btn_get_data_from_prefer)
        btnGetFromEncryptedPrefer = findViewById(R.id.btn_get_data_from_encrypted_prefer)
    }

    private fun setupButton() {
        btnSaveToPrefer?.setOnClickListener {
            saveToSharedPreference()
        }
        btnSavedToEncryptedPrefer?.setOnClickListener {
            saveToEncryptedSharedPreference()
        }
        btnGetFromPrefer?.setOnClickListener {
            getData(sharedPref, R.id.tv_data_from_prefer)
        }
        btnGetFromEncryptedPrefer?.setOnClickListener {
            getData(encryptedSharedPref, R.id.tv_data_from_encrypted_prefer)
        }
    }

    private fun saveToSharedPreference() {
        sharedPref = this.getSharedPreferences(
            getString(R.string.shared_prefs), Context.MODE_PRIVATE
        )
        sharedPref?.edit()?.apply {
            putString(KEY_PLAIN_TEXT, editText?.text.toString())
            apply()
        }
        toast("Saved")
    }

    private fun saveToEncryptedSharedPreference() {
        val masterKey: MasterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        encryptedSharedPref = EncryptedSharedPreferences.create(
            this,
            getString(R.string.secret_shared_prefs),
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        encryptedSharedPref?.edit()?.apply {
            putString(KEY_ENCRYPTED_TEXT, editText?.text.toString())
            apply()
        }
        toast("Saved")
    }

    private fun getData(pref: SharedPreferences?, viewId: Int) {
        val data = pref?.getString(KEY_PLAIN_TEXT, "N/A") ?: "N/A"
        findViewById<TextView>(viewId).text = data
    }

    @SuppressLint("ShowToast")
    private fun toast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast?.show()
    }
}
