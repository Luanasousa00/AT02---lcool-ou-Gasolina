package com.example.alcoolougasolina

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HistoricoActivity : AppCompatActivity() {
    lateinit var db: SQLiteDatabase
    lateinit var textHistorico: TextView

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)

        textHistorico = findViewById(R.id.textHistorico)

        // Abre o banco de dados
        db = SQLiteDatabase.openOrCreateDatabase(this.getDatabasePath("combustivel.db"), null)

        // Consulta o histórico e exibe
        carregarHistorico()
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
}
