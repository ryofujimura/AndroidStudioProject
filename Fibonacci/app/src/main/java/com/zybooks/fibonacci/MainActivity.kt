package com.zybooks.fibonacci

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var numberEditText: EditText
    private lateinit var resultTextView: TextView

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

    fun fibonacciClickOne(view: View) {
        // Display progress bar
        progressBar.visibility = View.VISIBLE

        // Find the nth Fibonacci number using the given number
        val num = numberEditText.text.toString().toLongOrNull()
        val fibNumber = fibonacci(num)

        // Show the result with commas in the right place
        resultTextView.text = "Result:" +
                NumberFormat.getNumberInstance(Locale.US).format(fibNumber)

        // Hide progress bar
        progressBar.visibility = View.INVISIBLE
    }

    fun fibonacciClickTwo(view: View) {

        // Display progress bar
        progressBar.visibility = View.VISIBLE

        // Get number from UI
        val num = numberEditText.text.toString().toLongOrNull()

        // Clear previous result
        resultTextView.text = ""

        // Create a background thread
        val thread = Thread {

            // Find the Fibonacci number
            val fibNumber = fibonacci(num)

            // UI should only be updated by main thread
            runOnUiThread {
                resultTextView.text = "Result:" +
                        NumberFormat.getNumberInstance(Locale.US).format(fibNumber)

                // Hide progress bar
                progressBar.visibility = View.INVISIBLE
            }
        }

        thread.start()
    }
}
