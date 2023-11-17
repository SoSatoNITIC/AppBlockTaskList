package com.example.appblocktasklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }



    //  os側の戻るボタンとか横スワイプで画面戻る機能
    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager

        // Check if there are any fragments in the backstack
        if (fragmentManager.backStackEntryCount > 0) {
            // If there are, pop the previous fragment
            fragmentManager.popBackStack()
        } else {
            // If there aren't, execute the default back button behavior
            super.onBackPressed()
        }
    }
}