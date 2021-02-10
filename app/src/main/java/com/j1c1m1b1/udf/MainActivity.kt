package com.j1c1m1b1.udf

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.j1c1m1b1.udf.components.MainState
import com.j1c1m1b1.udf.databinding.ActivityMainBinding
import kotlinx.coroutines.FlowPreview
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@FlowPreview
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainActivityViewModel by viewModels()

    private val dateFormat: DateFormat
        get() = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())

    override fun inflateBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe(this) { it?.render() }
        binding.errorButton.setOnClickListener { viewModel.sendError() }
        binding.eventsButton.setOnClickListener {
            viewModel.sendSuccess(
                Random.nextLong().toString()
            )
        }
    }

    private fun MainState.render() {
        log()
        when (this) {
            is MainState.Loading -> renderLoading()
            is MainState.Error -> render()
            is MainState.Complete -> render()
            else -> return
        }
    }

    private fun renderLoading() {
        binding.progressBar.isVisible = true
        switchButtonState(false)
    }

    private fun MainState.Error.render() {
        binding.progressBar.isGone = true
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

    private fun MainState.Complete.render() {
        showAlertDialog(result)
        binding.progressBar.isGone = true
        switchButtonState(true)
    }

    private fun switchButtonState(isEnabled: Boolean) {
        binding.apply {
            eventsButton.isEnabled = isEnabled
            errorButton.isEnabled = isEnabled
        }
    }

    private fun MainState.log() {
        this::class.simpleName?.let { binding.textView.appendText(it) }
    }

    private fun TextView.appendText(newText: String) {
        buildString {
            appendLine(getString(R.string.state_format, newText, getTimeStamp()))
            text?.let { append(it) }
        }.also { text = it }
    }

    private fun getTimeStamp(): String =
        Date(System.currentTimeMillis()).let { dateFormat.format(it) }

    private companion object {
        const val DATE_PATTERN = "HH:mm:ss"
    }
}