package com.example.lab6.ui.delete

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lab6.Deudores
import com.example.lab6.DeudoresApp
import com.example.lab6.R
import com.example.lab6.currentMail
import com.example.lab6.data.dao.DebtorDao
import com.example.lab6.data.entities.Debtor
import com.example.lab6.databinding.FragmentDeleteBinding
import com.google.firebase.database.*


lateinit var delete : String
var deleteFlag = false
class DeleteFragment : Fragment() {

    companion object {
        fun newInstance() = DeleteFragment()
    }

    private lateinit var viewModel: DeleteViewModel
    private var _binding: FragmentDeleteBinding? = null
    private lateinit var database2 : DatabaseReference
    private lateinit var database : DatabaseReference
    private lateinit var database3 : DatabaseReference

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = FirebaseDatabase.getInstance().getReference("Users/$currentMail/Debtors")

        _binding = FragmentDeleteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.deleteButton.setOnClickListener{
            deleteDebtor(binding.nameEditText.text.toString())

        }
        readDataDebtors()
        return root
    }

    private fun deleteDebtorFirebase(name: String) {

        findToDelete(name)
    }



    private fun deleteDebtor(name: String) {
        val debtorDao : DebtorDao = DeudoresApp.database.DebtorDao()
        val debtor : Debtor = debtorDao.readDebtor(name)

        if(debtor != null){
                val alertDialog: AlertDialog? = activity?.let{
                val builder = AlertDialog.Builder(it)
                builder.apply{
                    setTitle(R.string.title_delete)
                    setMessage("Desea Eliminar a " + debtor.name + ", su deuda es: " + debtor.amount.toString() + "?")
                    setPositiveButton(R.string.accept){ dialog, id ->
                        debtorDao.deleteDebtor(debtor)
                        deleteDebtorFirebase(binding.nameEditText.text.toString())
                        Toast.makeText(requireContext(), "Deudor Eliminado", Toast.LENGTH_SHORT).show()
                        binding.nameEditText.setText("")
                    }
                    setNegativeButton(R.string.cancel){ dialog, id ->
                    }
                }
                builder.create()
            }
            alertDialog?.show()

        }
        else {
            Toast.makeText(requireContext(), "No Existe", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DeleteViewModel::class.java)
        // TODO: Use the ViewModel
    }
    private fun readDataDebtors() {
        database2 = FirebaseDatabase.getInstance().getReference("Users")
        database2.child(currentMail).child("numberDebtors").get().addOnSuccessListener {
            if (it.exists()){                Deudores = it.child("nextDebtorId").value.toString()
            }
        }.addOnFailureListener{}
    }


    private fun findToDelete(name: String) {

        var flag = true
        var getdata = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(i in p0.children){
                    var userName = i.child("name").getValue()
                    var userId = i.child("id").getValue()
                    if(userName.toString() == name && flag == true && userId != ""){
                       deleteDebtorFirebaseUser(userId.toString().toInt())
                        flag = false
                    }

                }
            }
        }
        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)
    }

    private fun deleteDebtorFirebaseUser(j: Int){
            database3 = FirebaseDatabase.getInstance().getReference("Users")
            database3.child(currentMail).child("Debtors").child(j.toString()).removeValue()
            Toast.makeText(requireContext(), "Deudor Eliminado", Toast.LENGTH_SHORT).show()

    }
}