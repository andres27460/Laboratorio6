package com.example.lab6.ui.Login.CreateLogin

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.example.lab6.DeudoresApp
import com.example.lab6.DeudoresApp.Companion.database
import com.example.lab6.data.dao.DebtorSessionDao
import com.example.lab6.data.entities.DebtorSession

import com.example.lab6.databinding.*
import com.example.lab6.ui.Login.Login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.sql.Types.NULL
import java.util.regex.Pattern


    class CreateLoginActivity : AppCompatActivity() {

    private lateinit var binding: CreateLoginActivityBinding
    var database = FirebaseDatabase.getInstance().reference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateLoginActivityBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)

        binding.button2.setOnClickListener(){
            if(binding.editPassText.text.toString() == binding.editPassText2.text.toString()) {

                val mail = binding.mailEditText.text.toString()
                val userPass = binding.editPassText.text.toString()

                if(Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                   // database.child("Users").push()  .setValue(DatabaseFirebaseLogin(userName,userPass))
                    if(userPass.length > 7){

                        //createDebtorLogin(mail,userPass)
                        createFirebaseAccount(mail,userPass)


                        val i = Intent(this, LoginActivity::class.java)
                        startActivity(i)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        finish()
                    }
                    else{
                        Toast.makeText(applicationContext, "La contraseña de contener minimo 8 caracteres", Toast.LENGTH_SHORT).show()

                    }
                }
                else{
                    Toast.makeText(applicationContext, "Ingrese un correo valido", Toast.LENGTH_SHORT).show()

                }
            }
            else{
                Toast.makeText(applicationContext, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show()

            }
/*
            var getdata = object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var sb = StringBuilder()
                    for(i in p0.children){
                        var userName = i.child("userName").getValue()
                        var userPass = i.child("userPass").getValue()
                        sb.append("${i.key}  $userName $userPass")
                    }
                }
            }
            database.addValueEventListener(getdata)
            database.addListenerForSingleValueEvent(getdata)
*/

        }
    }

    private fun createFirebaseAccount(mail: String, userPass: String) {
        auth.createUserWithEmailAndPassword(mail, userPass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(baseContext, "Usuario creado con exito",
                        Toast.LENGTH_SHORT).show()
                    var tmail = mail.replace(oldChar = '@', newChar = '0')
                    tmail = tmail.replace(oldChar = '.', newChar = '1')
                    database.child("Users").child(tmail).child("numberDebtors").child("nextDebtorId").setValue(0)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Usuario ya existe",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }

    }




  //  private fun createDebtorLogin(userName: String, userPass: String) {
      //  val debtorSession = DebtorSession(id = NULL, user_login = userName, pass_login = userPass)
      //  val debtorSessionDAO : DebtorSessionDao = DeudoresApp.database2.DebtorSessionDao()
      //  debtorSessionDAO.createUserLogin(debtorSession)


   // }

}