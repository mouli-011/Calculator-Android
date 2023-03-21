package com.example.calculator

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private var operation:String = ""
    private var viewChanged = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        if(!viewChanged)
            startingScreen()
        else
            displayResult(operation)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("operation",operation)
        outState.putBoolean("viewChanged",viewChanged)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operation = savedInstanceState.getString("operation").toString()
        viewChanged = savedInstanceState.getBoolean("viewChanged")
    }

    private fun startingScreen(){
        viewChanged = false
        with(binding){
            divButton.visibility = View.VISIBLE
            addButton.visibility = View.VISIBLE
            subButton.visibility = View.VISIBLE
            mulButton.visibility = View.VISIBLE
            resetButton.visibility = View.GONE
            resultTextView.visibility = View.GONE
        }
        setListeners()
    }
    private fun setListeners(){
        val getInputIntent = Intent(this,ActivityB::class.java)
        binding.addButton.setOnClickListener {
            operation = "+"
            getInputIntent.putExtra("OPERATION",OPERATIONS.ADD.string)
            startActivityForResult(getInputIntent,1)
        }
        binding.subButton.setOnClickListener {
            operation = "-"
            getInputIntent.putExtra("OPERATION",OPERATIONS.SUB.string)
            startActivityForResult(getInputIntent,1)
        }
        binding.mulButton.setOnClickListener {
            operation = "*"
            getInputIntent.putExtra("OPERATION",OPERATIONS.MUL.string)
            startActivityForResult(getInputIntent,1)
        }
        binding.divButton.setOnClickListener {
            operation = "/"
            getInputIntent.putExtra("OPERATION",OPERATIONS.DIV.string)
            startActivityForResult(getInputIntent,1)
        }
    }
    private fun displayResult(operation: String){
        with(binding){
            divButton.visibility = View.GONE
            addButton.visibility = View.GONE
            subButton.visibility = View.GONE
            mulButton.visibility = View.GONE
            resetButton.visibility = View.VISIBLE
            with(resultTextView){
                visibility = View.VISIBLE
                val resultText = "${viewModel.number1} $operation ${viewModel.number2} is ${viewModel.result}"
                text = resultText
            }
            resetButton.setOnClickListener {
                startingScreen()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewChanged = true
            if(requestCode == 1){
                if(resultCode == RESULT_OK){
                    data?.let{
                        viewModel.number1 = it.getIntExtra("a",0)
                        viewModel.number2 = it.getIntExtra("b",0)
                        viewModel.result = it.getIntExtra("result",0)
                        displayResult(operation)
                    }
                }
            }
    }
}
