package com.step.example_back_splashapi_datastore

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    private val Context.dataStorePrefrence: DataStore<Preferences> by preferencesDataStore(name = "USER")

    private val USER_FIRST_NAME = stringPreferencesKey("user_first_name")
    private val USER_LAST_NAME = stringPreferencesKey("user_last_name")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)


//        onBackPressedDispatcher.onBackPressed()


        saveData()
        Handler(Looper.getMainLooper()).postDelayed({

            getData()
        }, 3000)






        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                Toast.makeText(this@MainActivity, "Back Pressed", Toast.LENGTH_SHORT).show()

            }
        })


        /*if (Build.VERSION.SDK_INT >= 33) {
        onBackInvokedDispatcher.registerOnBackInvokedCallback(
            OnBackInvokedDispatcher.PRIORITY_DEFAULT
        ) {

            exitOnBackPressed()
        }
    } else {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    Log.i("TAG", "handleOnBackPressed: Exit")
                    exitOnBackPressed()
                }
            })
    }*/


    }

    private fun getData() {

        var data: Flow<String> = dataStorePrefrence.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map {
                it[USER_FIRST_NAME] ?: ""
            }
        GlobalScope.launch {
            data.collect {
                Log.d("``TAG``", "getData: ")
            }
        }

    }

    private fun saveData() {

        GlobalScope.launch {
            dataStorePrefrence.edit {
                it[USER_FIRST_NAME] = "SAmple USER_FIRST_NAME"
                it[USER_LAST_NAME] = "SAmple USER_LAST_NAME"

            }
        }


    }


}