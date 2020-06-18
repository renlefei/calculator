package com.thoughtworks.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.calculator_buttons.*
import kotlinx.android.synthetic.main.calculator_result.*
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {

    private var valueOne: Int = 0
    private var valueTwo: Int = 0

    private var operators = arrayListOf<String>("+", "-", "X", "/")

    private lateinit var expression: String

    private var operation_Add: Boolean = false
    private var operation_Minus: Boolean = false
    private var operation_Multiply: Boolean = false
    private var operation_Divide: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BindClickEvent()

    }

    private fun BindClickEvent() {
        btn_One.setOnClickListener {
            tv_userInput.setText(tv_userInput.text.toString() + "1")
        }

        btn_Two.setOnClickListener {
            tv_userInput.setText(tv_userInput.text.toString() + "2")
        }

        btn_Three.setOnClickListener {
            tv_userInput.setText(tv_userInput.text.toString() + "3")
        }

        btn_Four.setOnClickListener {
            tv_userInput.setText(tv_userInput.text.toString() + "4")
        }

        btn_Five.setOnClickListener {
            tv_userInput.setText(tv_userInput.text.toString() + "5")
        }

        btn_Six.setOnClickListener {
            tv_userInput.setText(tv_userInput.text.toString() + "6")
        }

        btn_Seven.setOnClickListener {
            tv_userInput.setText(tv_userInput.text.toString() + "7")
        }

        btn_Eight.setOnClickListener {
            tv_userInput.setText(tv_userInput.text.toString() + "8")
        }

        btn_Nine.setOnClickListener {
            tv_userInput.setText(tv_userInput.text.toString() + "9")
        }

        btn_Zero.setOnClickListener {
            tv_userInput.setText(tv_userInput.text.toString() + "0")
        }

        btn_Add.setOnClickListener {
            if(tv_result.text.isNotEmpty()){
                tv_userInput.setText(tv_result.text)
                tv_result.text = ""
            }
            tv_userInput.setText(tv_userInput.text.toString() + "+")
        }

        btn_Minus.setOnClickListener {
            if(tv_result.text.isNotEmpty()){
                tv_userInput.setText(tv_result.text)
                tv_result.text = ""
            }
            tv_userInput.setText(tv_userInput.text.toString() + "-")
        }

        btn_Multiply.setOnClickListener {
            if(tv_result.text.isNotEmpty()){
                tv_userInput.setText(tv_result.text)
                tv_result.text = ""
            }
            tv_userInput.setText(tv_userInput.text.toString() + "X")
        }

        btn_Divide.setOnClickListener {
            if(tv_result.text.isNotEmpty()){
                tv_userInput.setText(tv_result.text)
                tv_result.text = ""
            }
            tv_userInput.setText(tv_userInput.text.toString() + "/")
        }

        btn_Clear.setOnClickListener {
            tv_userInput.setText(tv_userInput.text.toString().substring(0, tv_userInput.text.toString().length-1))
            tv_result.text = ""
        }

        btn_ClearAll.setOnClickListener {
            tv_userInput.setText("")
            tv_result.text = ""
        }

        btn_Equal.setOnClickListener {

            try {
                var resultExpression = listOf<String>()
                var inPutExpression = handleMinusNumber(tv_userInput.text.toString())
                var expression = convertInPutStringToExpression(inPutExpression)

                resultExpression = convertExpress(expression)
                tv_result.text = computeExpression(resultExpression)
            }catch (e: Exception){
                tv_userInput.setText("Error")
            }
        }
    }

    private fun handleMinusNumber(inPutExpression: String): String{
        var result = ""
        for((index, value) in inPutExpression.withIndex()){
            result += if(value.toString() == "-" && ( index == 0 || operators.contains(inPutExpression[index-1].toString()))){
                "#"
            }else{
                value.toString()
            }
        }
        return result
    }

    private fun computeExpression(resultExpression: List<String>): String{
        var result = Stack<String>()

        for(item in resultExpression){
            if(operators.contains(item)){
                when(item){
                    "+" -> result.push((result.pop().toDouble() + result.pop().toDouble()).toString())
                    "-" -> {
                        var valueTwo = result.pop().toDouble()
                        var valueOne = result.pop().toDouble()
                        result.push((valueOne - valueTwo).toString())
                    }
                    "X" -> result.push((result.pop().toDouble() * result.pop().toDouble()).toString())
                    "/" -> {
                        var valueTwo = result.pop().toDouble()
                        var valueOne = result.pop().toDouble()
                        result.push((valueOne / valueTwo).toString())
                    }
                }

            }else{
                result.push(item)
            }
        }
        return result.pop()
    }

    private fun convertExpress(expression: List<String>): List<String>{

        var operatorStack = Stack<String>()
        var postList = mutableListOf<String>()

        for(item in expression){
            if(operators.contains(item)){
                var operator =  ""
                if(operatorStack.isNotEmpty()){
                    operator = operatorStack.peek()
                }else{
                    operatorStack.push(item)
                    continue
                }

                if((item == "X" || item == "/") && (operator == "+" || operator == "-")){
                    operatorStack.push(item)
                }else{

                    do{
                        postList.add(operatorStack.pop())
                        if(operatorStack.isNotEmpty()){
                            operator = operatorStack.peek()
                        }else{
                            break
                        }
                    }
                    while(!((item == "X" || item == "/") && (operator == "+" || operator == "-")))
                    operatorStack.push(item)
                }

            }else{
                postList.add(item)
            }
        }
        while(operatorStack.isNotEmpty()){
            postList.add(operatorStack.pop())
        }



        return postList
    }

    private fun convertInPutStringToExpression(expression: String): List<String>{
        var startIndex = 0
        var res = mutableListOf<String>()
        var minusFlag = false

        for((index, value) in expression.withIndex()){

            if(value.toString() == "#"){
                minusFlag = true
                startIndex = index
                continue
            }

            if(operators.contains(value.toString())){
                if(minusFlag){
                    res.add(((expression.substring(startIndex + 1, index)).toInt() * (-1)).toString())
                    res.add(value.toString())
                    minusFlag = false
                }else{
                    res.add(expression.substring(startIndex, index))
                    res.add(value.toString())
                }
                startIndex = index + 1
            }
        }
        if(minusFlag){
            res.add(((expression.substring(startIndex + 1, expression.length)).toInt() * (-1)).toString())
        }else{
            res.add(expression.substring(startIndex, expression.length))
        }


        return res
    }
}
