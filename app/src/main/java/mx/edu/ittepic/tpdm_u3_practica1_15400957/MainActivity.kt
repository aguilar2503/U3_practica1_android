package mx.edu.ittepic.tpdm_u3_practica1_15400957

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    var descripcion : EditText?=null
    var monto : EditText?=null
    var fechaVencimiento : EditText?=null
    var pagado : EditText?=null
    var insertar : Button?=null
    var listView : ListView?=null

    //Devlarando objeto Firestone
    var baseRemota = FirebaseFirestore.getInstance()
    //declarar objetos tipo arreglo dinamico
    var registrosRemotos = ArrayList<String>()
    var keys = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        descripcion = findViewById(R.id.editDescrip)
        monto = findViewById(R.id.editMonto)
        fechaVencimiento = findViewById(R.id.editVencimiento)
        pagado = findViewById(R.id.editPagado)
        insertar = findViewById(R.id.btnInsertar)
        listView = findViewById(R.id.listaRegitrados)

        insertar?.setOnClickListener {
            var datoInsertar = hashMapOf(
                "descripcion" to descripcion?.text.toString(),
                "monto" to monto?.text.toString(),
                "fechaVencimiento" to fechaVencimiento?.text.toString(),
                "pagado" to pagado?.text.toString()
            )
            baseRemota.collection("recibopagos")
                .add(datoInsertar as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(this,"Se inserto correctamente", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Nose pudo insertar correctamente"+it.message, Toast.LENGTH_SHORT).show()
                }
            limpiarCampos()
        }

            listView?.setOnItemClickListener{parent, view, position, id ->
                if (keys.size==0){
                    return@setOnItemClickListener
                }
                AlertDialog.Builder(this).setTitle("Atencion")
                    .setMessage("Que desea hacer con "+registrosRemotos.get(position)+"?")
                    .setPositiveButton("Eliminar"){dialog, which->
                        baseRemota.collection("recibopagos")
                            .document(keys.get(position))
                            .delete()
                            .addOnSuccessListener {
                                Toast.makeText(this,"Si se borrro correctamente", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this,"No se borro correctamente",Toast.LENGTH_SHORT).show()
                            }
                    }
                    .setNegativeButton("Actualizar"){dialog, which ->}
                    .setNeutralButton("Cancelar"){dialog,which ->
                        return@setNeutralButton
                    }

            }

    }
    fun limpiarCampos(){
        descripcion?.setText("");monto?.setText("");fechaVencimiento?.setText(""); pagado?.setText("")
    }
}
