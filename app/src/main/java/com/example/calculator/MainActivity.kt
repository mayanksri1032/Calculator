package com.example.calculator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var inputValue1: Double = 0.0
    private var inputValue2: Double? = null
    private var currentOperator: Operator? = null
    private var result: Double? = null
    private val equation: StringBuilder = StringBuilder("0")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setListeners() {
        for (button in getNumericButtons()) {
            button.setOnClickListener { onNumberClicked(button.text.toString()) }
        }
        with(binding) {
            zero.setOnClickListener { onZeroClicked() }
            doubleZero.setOnClickListener { onDoubleZeroClicked() }
            decimal.setOnClickListener { onDecimalPointClicked() }
            btnAdd.setOnClickListener { onOperatorClicked(Operator.ADDITION) }
            btnSubstraction.setOnClickListener { onOperatorClicked(Operator.SUBTRACTION) }
            btnDivision.setOnClickListener { onOperatorClicked(Operator.DIVISION) }
            btnMultiply.setOnClickListener { onOperatorClicked(Operator.MULTIPLICATION) }
            btnEqual.setOnClickListener { onEqualsClicked() }
            btnAllClear.setOnClickListener { onAllClearClicked() }
            btnPlusMinus.setOnClickListener { onPlusMinusClicked() }

        }
    }


    private fun onPlusMinusClicked() {
        if (equation.startsWith(MINUS)) {
            equation.deleteCharAt(0)
        } else {
            equation.insert(0, MINUS)
        }
        updateInputOnDisplay()
    }

    private fun onAllClearClicked() {
        inputValue1 = 0.0
        inputValue2 = null
        currentOperator = null
        result = null
        equation.clear().append("0")
        clearDisplay()
    }


    private fun onOperatorClicked(operator: Operator) {
        currentOperator = operator
        if (equation.isNotEmpty()) {
            inputValue1 = equation.toString().toDouble()
            equation.clear().append("0")
        }
    }

    private fun onEqualsClicked() {
        if (currentOperator != null && equation.isNotEmpty()) {
            inputValue2 = equation.toString().toDouble()
            result = calculate()
            binding.textInput.text = result.toString()
            inputValue1 = result!!
            equation.clear().append("0")
        }
    }

    private fun calculate(): Double {
        return when (currentOperator) {
            Operator.ADDITION -> inputValue1 + (inputValue2 ?: 0.0)
            Operator.SUBTRACTION -> inputValue1 - (inputValue2 ?: 0.0)
            Operator.MULTIPLICATION -> inputValue1 * (inputValue2 ?: 1.0)
            Operator.DIVISION -> inputValue1 / (inputValue2 ?: 1.0)
            else -> throw IllegalStateException("Operator is null")
        }
    }

    private fun onDecimalPointClicked() {
        if (!equation.contains(".")) {
            equation.append(".")
        }
        updateInputOnDisplay()
    }

    private fun onZeroClicked() {
        if (equation.toString() == "0") {
            return  // No need to clear if already zero
        }
        equation.append("0")
        updateInputOnDisplay()
    }

    private fun onDoubleZeroClicked() {
        if (equation.startsWith("0.")) {
            return  // No need to clear if already starts with "0."
        }
        equation.append("00")
        updateInputOnDisplay()
    }



    private fun getNumericButtons() = with(binding) {
        arrayOf(
            one, two, three, four, five, six, seven, eight, nine
        )
    }

    private fun onNumberClicked(numberText: String) {
        if (equation.toString() == "0" || equation.startsWith("0.")) {
            equation.clear()
        }
        equation.append(numberText)
        updateInputOnDisplay()
        updateEquationOnDisplay()
    }

    private fun updateInputOnDisplay() {
        binding.textInput.text = equation.toString()
    }

    private fun updateEquationOnDisplay() {
        binding.textInput.text = equation.toString()
        binding.textEquation.text = equation.toString()
    }

    private fun clearDisplay() {
        binding.textInput.text = ""
    }

    enum class Operator(val symbol: String) {
        ADDITION("+"), SUBTRACTION("-"), MULTIPLICATION("*"), DIVISION("/")
    }



    private companion object {
        const val MINUS = "-"
    }
}
