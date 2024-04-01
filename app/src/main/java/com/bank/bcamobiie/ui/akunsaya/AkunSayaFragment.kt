package com.bank.bcamobiie.ui.akunsaya

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bank.bcamobiie.R
import com.bank.bcamobiie.activity.AboutActivity
import com.bank.bcamobiie.adapter.MenuMyAccAdapter
import com.bank.bcamobiie.databinding.FragmentAkunSayaBinding
import com.bank.bcamobiie.datadummy.dataMenuMyAcc
import com.bank.bcamobiie.utils.Utils.addSpaceEveryFourCharacters
import com.bank.bcamobiie.viewmodel.InputDataViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AkunSayaFragment : Fragment(), MenuMyAccAdapter.OnMenuItemClickListener {

    private var _binding: FragmentAkunSayaBinding? = null
    private val binding get() = _binding!!

    private val indicatorImages = listOf(
        R.drawable.navconngreen,
        R.drawable.navconnblue,
        R.drawable.navconnred,
    )

    private val indicatorChangeDelay = 1000L

    private var indicatorChangeJob: Job? = null
    private val viewModel: InputDataViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentAkunSayaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.rxMenuAkunsaya
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        val adapter = MenuMyAccAdapter(dataMenuMyAcc)
        adapter.setOnMenuItemClickListener(this)
        recyclerView.adapter = adapter
        startIndicatorChangeJob()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(requireActivity()){ datauser ->
            val realRekening = datauser.idKartu.addSpaceEveryFourCharacters()
            binding.tvNomor.text = realRekening
            binding.btnCopy.setOnClickListener {
                val textToCopy = datauser.idKartu
                val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("text", textToCopy)
                clipboardManager.setPrimaryClip(clipData)
            }

            val databaseRef = FirebaseDatabase.getInstance("https://bca-mobiile-default-rtdb.firebaseio.com/").reference
            lifecycleScope.launch {
                databaseRef.child("data_akun").orderByChild("idKartu").equalTo(datauser.idKartu)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {

                                for (dataSnapshotChild in dataSnapshot.children) {
                                    val namaNasabah = dataSnapshotChild.child("nama").getValue(String::class.java)
                                    binding.tvNameNasabah.text = namaNasabah
                                    break
                                }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })


                databaseRef.child("data_rekening").orderByChild("idKartu").equalTo(datauser.idKartu)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {

                                for (dataSnapshotChild in dataSnapshot.children) {
                                    val jenisRekening = dataSnapshotChild.child("jenisRek").getValue(String::class.java)
                                    // Tentukan gambar kartu berdasarkan jenis rekening
                                    when (jenisRekening) {
                                        "PLATINUM" -> {
                                            binding.textView4.text = resources.getString(R.string.platinum)
                                        }
                                        "GOLD" -> {
                                            binding.textView4.text = resources.getString(R.string.gold)
                                        }
                                        "BLUE" -> {
                                            binding.textView4.text = resources.getString(R.string.blue)
                                        }
                                    }

                                    val drawableId = when (jenisRekening) {
                                        "GOLD" -> {
                                            R.drawable.bcacardgoldplain
                                        }
                                        "BLUE" -> {
                                            R.drawable.bcacardblueplain
                                        }
                                        "PLATINUM" -> {
                                            R.drawable.bcacardplatinumplain
                                        }
                                        else -> {R.drawable.bcacardplatinumplain}
                                    }

                                    binding.bcaCard.setImageResource(drawableId)
                                    break
                                }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        indicatorChangeJob?.cancel()
    }

    override fun onItemClick(menuId: Int) {
        when(menuId){
            1 -> {
                startActivity(Intent(requireContext(), AboutActivity::class.java))
            }
        }
    }

    private fun startIndicatorChangeJob() {
        indicatorChangeJob?.cancel()

        indicatorChangeJob = viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                val randomIndex = (0 until indicatorImages.size).random()
                binding.indicatorSignalMyAcc.setImageResource(indicatorImages[randomIndex])
                delay(indicatorChangeDelay)
            }
        }
    }

}