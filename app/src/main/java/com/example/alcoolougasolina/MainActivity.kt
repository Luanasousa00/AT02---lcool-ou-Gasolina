package com.example.alcoolougasolina

import android.annotation.SuppressLint
import android.content.Intent // Import necessário para criar a Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var etPrecoAlcool: EditText
    lateinit var etPrecoGasolina: EditText
    lateinit var btCalc: Button
    lateinit var textMsg: TextView
    var swPercentual: Int = 70
    lateinit var db: SQLiteDatabase
    lateinit var btHistorico: Button

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPrecoAlcool = findViewById(R.id.edAlcool)
        etPrecoGasolina = findViewById(R.id.edGasolina)
        btCalc = findViewById(R.id.btCalcular)
        textMsg = findViewById(R.id.textMsg)
        btHistorico = findViewById(R.id.btHistorico)

        db = SQLiteDatabase.openOrCreateDatabase(this.getDatabasePath("combustivel.db"), null)

        db.execSQL(
            "CREATE TABLE IF NOT EXISTS historico_precos (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "preco_alcool REAL, " +
                    "preco_gasolina REAL, " +
                    "data_hora DATETIME DEFAULT CURRENT_TIMESTAMP);"
        )

        val switch = findViewById<Switch>(R.id.swPercentual)

        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            swPercentual = if (isChecked) {
                75
            } else {
                70
            }
        }

        btCalc.setOnClickListener {
            val precoAlcoolText = etPrecoAlcool.text.toString()
            val precoGasolinaText = etPrecoGasolina.text.toString()

            if (precoAlcoolText.isNotEmpty() && precoGasolinaText.isNotEmpty()) {
                val precoAlcool = precoAlcoolText.toDouble()
                val precoGasolina = precoGasolinaText.toDouble()

                val percentual = precoAlcool / precoGasolina * 100

                if (percentual <= swPercentual) {
                    textMsg.text = "Álcool é mais vantajoso"
                } else {
                    textMsg.text = "Gasolina é mais vantajosa"
                }

                Log.d("PDM24", "No btCalcular, $percentual")
            } else {
                textMsg.text = "Insira os valores de preço da gasolina e do álcool"
            }
        }

        // Adiciona o listener para o botão de histórico
        btHistorico.setOnClickListener {
            // Cria a Intent para iniciar a tela de Histórico
            val intent = Intent(this, HistoricoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("PDM24", "No onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.v("PDM24", "No onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.e("PDM24", "No onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.w("PDM24", "No onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.wtf("PDM24", "No Destroy")
    }
}
