package com.j1c1m1b1.udf

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import com.j1c1m1b1.presentation.main.MainState
import com.j1c1m1b1.udf.databinding.ActivityMainBinding
import kotlinx.coroutines.FlowPreview

@FlowPreview
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun inflateBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe(this) { logState(it) }
        binding.errorButton.setOnClickListener { viewModel.sendError() }
        binding.eventsButton.setOnClickListener { viewModel.sendSuccess(getTimeStamp()) }
    }

    private fun getTimeStamp(): String =
        System.currentTimeMillis().toString()

    private fun logState(state: MainState?) {
        when (state) {
            null -> return
            else -> state::class.simpleName
        }?.let {
            binding.textView.appendText(it)
        }
    }

    private fun TextView.appendText(newText: String) {
        buildString {
            appendLine(getString(R.string.state_format, newText, getTimeStamp()))
            text?.let { append(it) }
        }.also { text = it }
    }
}