package com.example.lab6.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab6.Deudores
import com.example.lab6.DeudoresApp
import com.example.lab6.currentMail
import com.example.lab6.data.dao.DebtorDao
import com.example.lab6.data.entities.Debtor
import com.example.lab6.databinding.FragmentListBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ListFragment : Fragment() {

    private lateinit var listViewModel: ListViewModel
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var  debtorsAdapter: DebtorsAdapter

    private lateinit var database : DatabaseReference
    private lateinit var uid : String
    private lateinit var auth : FirebaseAuth
    private lateinit var database2 : DatabaseReference








    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        _binding = FragmentListBinding.inflate(inflater,container,false)
        val root: View = binding.root


        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        database = FirebaseDatabase.getInstance().getReference("Users/$currentMail/Debtors")
        readDataDebtors()
        var gdata = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                var sb = StringBuilder()
                for(i in p0.children){
                    var name = i.child("name").getValue()
                    var phone = i.child("phone").getValue()
                    var amount = i.child("amount").getValue()
                    sb.append("${i.key}  $name $phone $amount")



                }

            }
        }

        database.addValueEventListener(gdata)
       // database.addListenerForSingleValueEvent(getdata)
        // val textView: TextView = binding.textHome
        listViewModel.text.observe(viewLifecycleOwner, Observer {
           // textView.text = it
        })



        debtorsAdapter = DebtorsAdapter(onItemClicked = { onDebtorItemClicked(it) })
        binding.debtorRecyclerView.apply{
            layoutManager = LinearLayoutManager(this@ListFragment.context)
            adapter = debtorsAdapter
            setHasFixedSize(false)
        }


        val debtorDAO : DebtorDao = DeudoresApp.database.DebtorDao()
        val listDebtors: MutableList<Debtor> = debtorDAO.getDebtors()
       debtorsAdapter.appendItems(listDebtors)

      //  Toast.makeText(requireContext(),database.child("Users").child(currentMail)., Toast.LENGTH_SHORT).show()
        return root
    }



    private fun onDebtorItemClicked(debtor: Debtor) {
        findNavController().navigate(ListFragmentDirections.actionNavigationListToDetailFragment2(debtor = debtor))

    }

    override fun onDestroy() {
        super.onDestroy()
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