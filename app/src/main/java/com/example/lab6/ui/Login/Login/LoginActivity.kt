package com.example.lab6.ui.Login.Login


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lab6.DeudoresApp
import com.example.lab6.MainActivity
import com.example.lab6.currentMail
import com.example.lab6.data.dao.DebtorSessionDao
import com.example.lab6.data.entities.DebtorSession

import com.example.lab6.databinding.LoginActivityBinding
import com.example.lab6.ui.Login.CreateLogin.CreateLoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.example.lab6.data.dao.DebtorDao
import com.example.lab6.data.entities.Debtor
import com.example.lab6.ui.create.CreateFragment
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.sql.Types

var flag = true

class LoginActivity : AppCompatActivity()  {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: LoginActivityBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityBinding.inflate(layoutInflater)

        val debtorSessionDao: DebtorDao = DeudoresApp.database.DebtorDao()
        debtorSessionDao.cleanTables()


        setContentView(binding.root)
        auth = Firebase.auth
        binding.button2.setOnClickListener(){
            val i = Intent(this, CreateLoginActivity::class.java)
            startActivity(i)
            //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }

        binding.button.setOnClickListener(){
            //val debtorSessionDao: DebtorSessionDao = DeudoresApp.database2.DebtorSessionDao()
            //val debtorSession: DebtorSession = debtorSessionDao.readUserLogin(binding.mailEditText.text.toString())
            //if(debtorSession != null && debtorSession.pass_login == binding.editTextTextPassword.text.toString()){
              //  val i = Intent(this, MainActivity::class.java)
                //startActivity(i)
                //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                //finish()
            //}
            //else{
              //  Toast.makeText(applicationContext, "Datos incorrectos", Toast.LENGTH_SHORT).show()
            //}
            val mail = binding.mailEditText.text.toString()
            val pass = binding.editTextTextPassword.text.toString()

            if(mail.isEmpty() || pass.isEmpty()){
                Toast.makeText(applicationContext, "Debe llenar el campo de Correo y Contraseña", Toast.LENGTH_SHORT).show()
            }
            else{
                loginFirebaseAccount(mail,pass)
            }


        }
    }

    private fun loginFirebaseAccount(mail: String, pass: String){
        auth.signInWithEmailAndPassword(mail, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "Bienvenido")
                    val i = Intent(this, MainActivity::class.java)
                    currentMail = mail.replace(oldChar = '@', newChar = '0')
                    currentMail = currentMail.replace(oldChar = '.', newChar = '1')

                    initListCreated()

                    startActivity(i)
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                    finish()
                } else {
                    Toast.makeText(baseContext, "Correo o Contraseña son incorrectos.",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun initListCreated() {
        database = FirebaseDatabase.getInstance().getReference("Users/$currentMail/Debtors")

        var getdata = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                for(i in p0.children){
                    var name = i.child("name").getValue()
                    var phone = i.child("phone").getValue()
                    var amount = i.child("amount").getValue()

                    if(!name.toString().isNullOrEmpty() && !phone.toString().isNullOrEmpty() && !amount.toString().isNullOrEmpty() && flag) {
                        loadDebtors(name.toString(), phone.toString(), amount.toString().toLong())
                    }
                }
                flag = false
            }
        }
        database.addValueEventListener(getdata)
      //  database.addListenerForSingleValueEvent(getdata)


    }
    private fun loadDebtors(name: String ,phone: String, amount: Long){
        val debtor = Debtor(id = Types.NULL, name = name, phone = phone, amount = amount)
        val debtorDAO : DebtorDao = DeudoresApp.database.DebtorDao()
        debtorDAO.createDebtor(debtor)
    }


}


