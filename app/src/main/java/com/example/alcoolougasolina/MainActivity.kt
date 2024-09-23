package com.example.alcoolougasolina

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var etPrecoAlcool: EditText
    lateinit var etPrecoGasolina: EditText
    lateinit var btCalc: Button
    lateinit var textMsg: TextView
    lateinit var btHistorico: Button
    lateinit var textHistorico: TextView

    var swPercentual: Int = 70
    lateinit var db: SQLiteDatabase

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPrecoAlcool = findViewById(R.id.edAlcool)
        etPrecoGasolina = findViewById(R.id.edGasolina)
        btCalc = findViewById(R.id.btCalcular)
        textMsg = findViewById(R.id.textMsg)
        btHistorico = findViewById(R.id.btHistorico)
        textHistorico = findViewById(R.id.textHistorico)

        db = SQLiteDatabase.openOrCreateDatabase(this.getDatabasePath("combustivel.db"), null)

        db.execSQL(
            "CREATE TABLE IF NOT EXISTS historico_precos (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "preco_alcool REAL, " +
                    "preco_gasolina REAL, " +
                    "data_hora DATETIME DEFAULT CURRENT_TIMESTAMP);"
        )

        val switch = findViewById<Switch>(R.id.swPercentual)
        switch.setOnCheckedChangeListener { _, isChecked ->
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

                // Salva os preços no histórico
                salvarPrecos(precoAlcool, precoGasolina)

                val percentual = precoAlcool / precoGasolina * 100

                textMsg.text = if (percentual <= swPercentual) {
                    "Álcool é mais vantajoso"
                } else {
                    "Gasolina é mais vantajoso"
                }

                Log.d("PDM24", "No btCalcular, $percentual")
            } else {
                textMsg.text = "Insira os valores de preço da gasolina e do álcool"
            }
        }

        btHistorico.setOnClickListener {
            carregarHistorico()
        }
    }

    private fun salvarPrecos(precoAlcool: Double, precoGasolina: Double) {
        val values = ContentValues()
        values.put("preco_alcool", precoAlcool)
        values.put("preco_gasolina", precoGasolina)
        db.insert("historico_precos", null, values)

        Toast.makeText(this, "Preços salvos no histórico", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("Range")
    private fun carregarHistorico() {
        val cursor: Cursor = db.rawQuery("SELECT * FROM historico_precos ORDER BY data_hora DESC", null)
        val historico = StringBuilder()

        if (cursor.moveToFirst()) {
            do {
                val precoAlcool = cursor.getDouble(cursor.getColumnIndex("preco_alcool"))
                val precoGasolina = cursor.getDouble(cursor.getColumnIndex("preco_gasolina"))
                val dataHora = cursor.getString(cursor.getColumnIndex("data_hora"))

                historico.append("Data/Hora: $dataHora\n")
                historico.append("Álcool: $precoAlcool, Gasolina: $precoGasolina\n\n")
            } while (cursor.moveToNext())
        }
        cursor.close()

        // Exibe o histórico no TextView
        textHistorico.text = historico.toString()
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
