package com.example.lab6.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lab.DatabaseFirebaseLogin
import com.example.lab6.Deudores
import com.example.lab6.DeudoresApp
import com.example.lab6.currentMail
import com.example.lab6.data.dao.DebtorDao
import com.example.lab6.data.entities.Debtor
import com.example.lab6.databinding.FragmentCreateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.sql.Types.NULL

class CreateFragment : Fragment() {

    private lateinit var createViewModel: CreateViewModel
    private var _binding: FragmentCreateBinding? = null
    var database = FirebaseDatabase.getInstance().reference
    private lateinit var database2 : DatabaseReference

    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        createViewModel = ViewModelProvider(this).get(CreateViewModel::class.java)
        _binding = FragmentCreateBinding.inflate(inflater,container,false)
        val root = binding.root
        //val textView: TextView = binding.textDashboard
        createViewModel.text.observe(viewLifecycleOwner, Observer {
         //   textView.text = it
        })

        readDataDebtors()
        binding.createButton.setOnClickListener(){
            val name = binding.nameEditText.text.toString()
            val phone = binding.phoneEditText.text.toString()
            val amount = binding.amountEditText.text.toString().toLong()


            createDebtorFirebase(name, phone, amount)
            createDebtor(name, phone, amount)
        }

        return root
    }

    private fun createDebtorFirebase(name: String, phone: String, amount: Long) {
        val id = Deudores
        database.child("Users").child(currentMail).child("Debtors").child(Deudores).setValue(DatabaseFirebaseLogin(name, phone, amount.toString(),id))
        Deudores = (Deudores.toInt() + 1).toString()
        database.child("Users").child(currentMail).child("numberDebtors").child("nextDebtorId").setValue(Deudores)



        Toast.makeText(requireContext(), "Se agregó un deudor correctamente", Toast.LENGTH_SHORT).show()

        cleanViews()
    }

    private fun createDebtor(name: String, phone: String, amount: Long) {
        val debtor = Debtor(id = NULL, name = name, phone = phone, amount = amount)
        val debtorDAO : DebtorDao = DeudoresApp.database.DebtorDao()
        debtorDAO.createDebtor(debtor)
        cleanViews()
    }

    private fun cleanViews() {
        binding.nameEditText.setText("")
        binding.phoneEditText.setText("")
        binding.amountEditText.setText("")
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null

    }
    private fun readDataDebtors() {
        database2 = FirebaseDatabase.getInstance().getReference("Users")
        database2.child(currentMail).child("numberDebtors").get().addOnSuccessListener {
            if (it.exists()){                Deudores = it.child("nextDebtorId").value.toString()
            }
        }.addOnFailureListener{}
    }
}