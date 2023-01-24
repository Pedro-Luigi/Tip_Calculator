package com.meu.tiptime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lemonade.ui.theme.TipTimeTheme
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        var amountInput by remember { mutableStateOf("") }
        val amount = amountInput.toDoubleOrNull() ?: 0.0
        var tipInput by remember { mutableStateOf("") }
        val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
        var roundUp by remember { mutableStateOf(false) }
        val tip = calculateTip(amount, tipPercent, roundUp)

        val focusManager = LocalFocusManager.current

        Column(
            Modifier.padding(32.dp), Arrangement.spacedBy(8.dp)
        ) {
            Text(text = stringResource(R.string.calculate_tip),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally))

            //Adiciona espaço
            Spacer(modifier = Modifier.height(16.dp))

            //Adicionando EditText.
            editNumberField(
                label = R.string.bill_amount,
                value = amountInput,
                onValueChange = {amountInput = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(FocusDirection.Down)})
            )
            editNumberField(
                label = R.string.how_was_the_service,
                value = tipInput,
                onValueChange = {tipInput = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()})
            )
            //Adicionando Switch
            roundTheTipRow(roundUp = roundUp, onRoundUpChange = {roundUp = it})

            //Adiciona espaço
            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.tip_amount, tip),
                modifier = Modifier.align(Alignment.End),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

        }
    }

    @Composable
    private fun editNumberField(
        @StringRes label: Int,
        value: String,
        onValueChange: (String) -> Unit,
        keyboardOptions: KeyboardOptions,
        keyboardActions: KeyboardActions,
        modifier: Modifier = Modifier
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = stringResource(id = label), Modifier.fillMaxWidth())}   ,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true
        )


    }

    @Composable
    private fun roundTheTipRow(
        roundUp:Boolean,
        onRoundUpChange: (Boolean) -> Unit,
        modifier: Modifier = Modifier
    ){
        Row(
            modifier = modifier
                .fillMaxWidth()
                .size(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.round_up_tip))
            Switch(checked = roundUp, onCheckedChange = onRoundUpChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End),
                colors = SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray)
            )
        }
    }


    private fun calculateTip(
        amount: Double,
        tipPercent: Double = 15.0,
        roundUp: Boolean
    ): String {
        var tip = tipPercent / 100 * amount
        if (roundUp) tip = kotlin.math.ceil(tip)
        return NumberFormat.getCurrencyInstance().format(tip)
    }
}