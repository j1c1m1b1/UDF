package com.j1c1m1b1.udf

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.j1c1m1b1.presentation.main.MainState
import com.j1c1m1b1.udf.databinding.ActivityMainBinding
import kotlinx.coroutines.FlowPreview

@FlowPreview
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun inflateBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe(this) { it?.render() }
        binding.errorButton.setOnClickListener { viewModel.sendError() }
        binding.eventsButton.setOnClickListener { viewModel.sendSuccess(getTimeStamp()) }
    }

    private fun getTimeStamp(): String =
        System.currentTimeMillis().toString()

    private fun switchButtonState(isEnabled: Boolean) {
        binding.apply {
            eventsButton.isEnabled = isEnabled
            errorButton.isEnabled = isEnabled
        }
    }

    private fun MainState.render() {
        log()
        when (this) {
            is MainState.Loading -> renderLoading()
            is MainState.Error -> renderError()
            is MainState.Complete -> renderComplete()
            else -> return
        }
    }

    private fun MainState.log() {
        this::class.simpleName?.let { binding.textView.appendText(it) }
    }

    private fun renderLoading() {
        switchButtonState(false)
    }

    private fun MainState.Error.renderError() {
        Snackbar.make(
            binding.root,
            getMessage(),
            Snackbar.LENGTH_LONG
        ).onDismissed {
            switchButtonState(true)
        }.show()
    }

    private fun MainState.Error.getMessage(): String =
        this.throwable?.message ?: getString(R.string.unknown_error)

    private fun renderComplete() {
        binding.apply {
            eventsButton.isEnabled = true
            errorButton.isEnabled = true
        }
    }

    private fun TextView.appendText(newText: String) {
        buildString {
            appendLine(getString(R.string.state_format, newText, getTimeStamp()))
            text?.let { append(it) }
        }.also { text = it }
    }
}