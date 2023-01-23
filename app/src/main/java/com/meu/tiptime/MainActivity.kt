package com.meu.tiptime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lemonade.ui.theme.TipTimeTheme
import com.meu.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContent {
            TipTimeTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ){
                    TipTimeApp()
                }
            }
        }

        binding.calculateButton.setOnClickListener { calculateTip() }
//        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode)}
    }
    //Criando a tela em compose
    @Preview(showBackground = true)
    @Composable
    fun TipTimeApp(){
        TipTimeTheme {
            Screen()
        }
    }
    
    //Colocando as views na tela
    @Composable
    fun Screen(){
        Column(
            Modifier.padding(32.dp), Arrangement.spacedBy(8.dp)
        ) {
            Text(text = stringResource(R.string.calculate_tip),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(16.dp))
            editNumberField()

        }
    }

    @Composable
    private fun editNumberField() {
        var amountInput by remember { mutableStateOf("") }
        TextField(value = amountInput, onValueChange = { amountInput = it })
    }


    private fun calculateTip() {
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        val cost = stringInTextField.toDoubleOrNull()

        //Verificando a porcentagem da gorgeta


        if (cost == null) {
            binding.tipResult.text = ""
            return
        }

        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }
        //calculando a gorjeta
        var tip = tipPercentage * cost

        //atribuindo o Switch
        if (binding.roundUpSwitch.isChecked) {
            tip = kotlin.math.ceil(tip)
        }
        /** Aqui eu estou falando --
         * Se roundUp estiver ativada, tip vai terá seu valor arredondado, usando o método
         * ceil. **/
        //convertendo o formato de formattecTip para a moeda local.

        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)

    }
}