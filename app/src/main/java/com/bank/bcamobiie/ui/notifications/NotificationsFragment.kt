package com.bank.bcamobiie.ui.notifications

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bank.bcamobiie.R
import com.bank.bcamobiie.databinding.FragmentNotificationsBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val indicatorImages = listOf(
        R.drawable.navconngreen,
        R.drawable.navconnblue,
        R.drawable.navconnred,
    )

    private val indicatorChangeDelay = 1000L

    private var indicatorChangeJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        startIndicatorChangeJob()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRefreshNotif.setOnClickListener {
            binding.btnRefreshNotif.visibility = View.INVISIBLE
            binding.messageNotif.visibility = View.INVISIBLE

            lifecycleScope.launch {
                delay(500)
                val progressBar = binding.progressBarNotif
                setProgressBarColor(progressBar, R.color.blue)
                progressBar.visibility = View.VISIBLE

                delay(2000)

                progressBar.visibility = View.INVISIBLE

                binding.btnRefreshNotif.visibility = View.VISIBLE
                binding.messageNotif.visibility = View.VISIBLE
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        indicatorChangeJob?.cancel()
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

    fun setProgressBarColor(progressBar: ProgressBar, color: Int) {
        progressBar.indeterminateDrawable.setColorFilter(ContextCompat.getColor(progressBar.context, color), PorterDuff.Mode.SRC_IN)
    }

}