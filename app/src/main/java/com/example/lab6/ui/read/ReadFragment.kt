package com.example.lab6.ui.read

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lab6.Deudores
import com.example.lab6.DeudoresApp
import com.example.lab6.R
import com.example.lab6.currentMail
import com.example.lab6.data.dao.DebtorDao
import com.example.lab6.data.entities.Debtor
import com.example.lab6.databinding.FragmentReadBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ReadFragment : Fragment() {

    private lateinit var readViewModel: ReadViewModel
    private var _binding: FragmentReadBinding? = null
    private val binding get() = _binding!!
    private lateinit var database2 : DatabaseReference


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        readViewModel = ViewModelProvider(this).get(ReadViewModel::class.java)
        _binding = FragmentReadBinding.inflate(inflater,container,false)
        val root: View = binding.root
       // val textView: TextView = binding.textNotifications
        readViewModel.text.observe(viewLifecycleOwner, Observer {
         //   textView.text = it
        })

        binding.readButton.setOnClickListener{
            readDebtors(binding.nameEditText.text.toString())
        }

        return root
    }

    private fun readDebtors(name: String) {
        val debtorDao: DebtorDao = DeudoresApp.database.DebtorDao()
        val debtor: Debtor = debtorDao.readDebtor(name)
        if(debtor != null){
            binding.phoneTextView.text = getString(R.string.phone_value, debtor.phone)
            binding.amountTextView.text = getString(R.string.amount_value, debtor.amount.toString())
        }
        else{
            Toast.makeText(requireContext(), "No Existe", Toast.LENGTH_SHORT).show()
            binding.phoneTextView.setText("")
            binding.amountTextView.setText("")

        }
    }
    private fun readDataDebtors() {
        database2 = FirebaseDatabase.getInstance().getReference("Users")
        database2.child(currentMail).child("numberDebtors").get().addOnSuccessListener {
            if (it.exists()){                Deudores = it.child("nextDebtorId").value.toString()
            }
        }.addOnFailureListener{}
    }

}