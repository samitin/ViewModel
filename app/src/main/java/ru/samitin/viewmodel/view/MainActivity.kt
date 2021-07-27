package ru.samitin.viewmodel.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.samitin.viewmodel.R
import ru.samitin.viewmodel.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var bainding:MainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bainding= MainActivityBinding.inflate(layoutInflater)
        setContentView(bainding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(bainding.container.id, MainFragment.newInstance())
                    .commitNow()
        }
    }
}