package com.example.lab6.ui.update

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lab.DatabaseFirebaseLogin
import com.example.lab6.Deudores
import com.example.lab6.DeudoresApp
import com.example.lab6.R
import com.example.lab6.currentMail
import com.example.lab6.data.dao.DebtorDao
import com.example.lab6.data.entities.Debtor
import com.example.lab6.databinding.FragmentUpdateBinding
import com.google.firebase.database.*

class UpdateFragment : Fragment() {

    companion object {
        fun newInstance() = UpdateFragment()
    }

    private lateinit var viewModel: UpdateViewModel
    private var _binding: FragmentUpdateBinding? = null
    private lateinit var database2 : DatabaseReference
    private lateinit var database : DatabaseReference
    private lateinit var database3 : DatabaseReference

    private lateinit var tname : String
    private val binding get() = _binding!!
    private var isSearching = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = FirebaseDatabase.getInstance().getReference("Users/$currentMail/Debtors")
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var idDebtor = 0
        binding.updateButton.setOnClickListener{

            val debtorDao: DebtorDao = DeudoresApp.database.DebtorDao()
            val name = binding.nameEditText.text.toString()

            readDataDebtors()
            if(isSearching){
                var debtor: Debtor = debtorDao.readDebtor(name)
                if(debtor != null){
                    idDebtor = debtor.id
                    binding.amountEditText.setText(debtor.amount.toString())
                    tname = binding.nameEditText.text.toString()
                    binding.phoneEditText.setText(debtor.phone)
                    binding.updateButton.text = getString(R.string.title_update)
                    isSearching = false
                }
                else{
                    Toast.makeText(requireContext(), "No existe", Toast.LENGTH_SHORT).show()
                    cleanWidgets()
                }
            }
            else{
                val debtor: Debtor = Debtor(
                    id = idDebtor,
                    name = binding.nameEditText.text.toString(),
                    amount = binding.amountEditText.text.toString().toLong(),
                    phone  = binding.phoneEditText.text.toString()
                    )
                findToUpdateDebtorFirebaseDatabase(tname, binding.nameEditText.text.toString(), binding.amountEditText.text.toString().toLong(), binding.phoneEditText.text.toString())
                debtorDao.updateDebtor(debtor)

                binding.updateButton.text = getString(R.string.title_read)
                isSearching = true
                cleanWidgets()
                Toast.makeText(requireContext(), "Deudor Actualizado", Toast.LENGTH_LONG).show()
            }
        }

        return root

    }

    private fun findToUpdateDebtorFirebaseDatabase(name: String, nameNew: String, amount: Long, phone:String) {
        var flag = true
        var getdata = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(i in p0.children){
                    var userName = i.child("name").getValue()
                    var userId = i.child("id").getValue()
                    if(userName.toString() == name && flag == true && userId != ""){
                        updateDebtorFirebaseUser(userId.toString().toInt(),nameNew,amount,phone)
                        flag = false
                    }

                }
            }
        }
        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

    }

    private fun updateDebtorFirebaseUser(userId: Int, name: String, amount: Long, phone: String) {
        database3 = FirebaseDatabase.getInstance().getReference("Users")
        database3.child(currentMail).child("Debtors").child(userId.toString()).setValue(
            DatabaseFirebaseLogin(name,amount.toString(),phone,userId.toString())
        )
        Toast.makeText(requireContext(), "Deudor Actualizado", Toast.LENGTH_SHORT).show()

    }

    private fun cleanWidgets() {
        with(binding){
            nameEditText.setText("")
            phoneEditText.setText("")
            amountEditText.setText("")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UpdateViewModel::class.java)
        // TODO: Use the ViewModel
    }
    private fun readDataDebtors() {
        database2 = FirebaseDatabase.getInstance().getReference("Users")
        database2.child(currentMail).child("numberDebtors").get().addOnSuccessListener {
            if (it.exists()){ Deudores = it.child("nextDebtorId").value.toString()       }
        }.addOnFailureListener{}
    }

}