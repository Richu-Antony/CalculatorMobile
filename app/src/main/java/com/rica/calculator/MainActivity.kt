package com.rica.calculator

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.rica.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    // User Variables

    private lateinit var binding: ActivityMainBinding
    private lateinit var expression: Expression
    var lastNumeric = false
    var stateError = false
    var lastDot = false

    // On-create Function

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)
        // binding help to remove R.id calls

        binding.BtnAuthorLink.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("upi://pay?pa=richuantony38@oksbi&pn=Richu%Antony&am&cu=INR&aid=uGICAgIC1yrWyKQ")
                )
            )
        })
    }

    // User Functions ......

    fun onAllClearClick(view: View) {
        binding.TvInput.text = ""
        binding.TvOutput.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        //        binding.TvOutput.visibility = view.GONE
    }

    fun onClearClick(view: View) {
        binding.TvInput.text = ""
        lastNumeric = false
    }

    fun onBackClick(view: View) {
        binding.TvInput.text = binding.TvInput.text.toString().dropLast(1)
        try {
            val lastchar = binding.TvInput.text.toString().last()
            if (lastchar.isDigit()) {
                onEqual()
            }
        } catch (e: Exception) {
            binding.TvOutput.text = ""
            // binding.TvOutput.visibility = View.GONE
            Log.e("Last char error", e.toString())
        }
    }


    fun onDigitClick(view: View) {
        if (stateError) {
            binding.TvInput.text = (view as Button).text
            stateError = false
        } else {
            binding.TvInput.append((view as Button).text)
        }
        lastNumeric = true
        onEqual()
    }


    fun onOperatorClick(view: View) {
        if (!stateError && lastNumeric) {
            binding.TvInput.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }


    fun onEqualClick(view: View) {
        onEqual()
        binding.TvInput.text = binding.TvOutput.text.toString().drop(1)
    }


    fun onEqual() {
        if (lastNumeric && !stateError) {
            val txt = binding.TvInput.text.toString()
            expression = ExpressionBuilder(txt).build()
            try {
                val result = expression.evaluate()
                //binding.TvOutput.visibility = view.VISIBLE
                binding.TvOutput.text = "=" + result.toString()
            } catch (ex: ArithmeticException) {
                Log.e("evaluate error", ex.toString())
                binding.TvOutput.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }


}