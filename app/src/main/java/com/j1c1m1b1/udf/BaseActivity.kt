package com.j1c1m1b1.udf

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    private lateinit var realBinding: T

    protected val binding: T
        get() = realBinding

    protected abstract fun inflateBinding(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realBinding = inflateBinding().apply {
            setContentView(root)
        }
    }
}