package com.zybooks.fibonacci

import android.media.tv.TvContract.Programs
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var numberEditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var NumberFormat: NumberFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)
        numberEditText = findViewById(R.id.numberEditText)
        resultTextView = findViewById(R.id.resultTextView)

    }

    private fun fibonacci(n: Long): Long {
        return if (n <= 1) n else fibonacci(n - 1) + fibonacci(n - 2)
    }

    fun fibonacciClick(view: View) {
        // Display progress bar
        progressBar.visibility = View.VISIBLE

        // Find the nth Fibonacci number using the given number
        val num = numberEditText.text.toString().toLong()
        val fibNumber = fibonacci(num)

        // Show the result with commas in the right place
        resultTextView.text = "Result:" +
                NumberFormat.getNumberInstance(Locale.US).format(fibNumber)

        // Hide progress bar
        progressBar.visibility = View.INVISIBLE
    }

}