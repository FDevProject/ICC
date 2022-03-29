package com.febrian.icc.ui.info

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.febrian.icc.databinding.InfoFragmentBinding
import com.febrian.icc.utils.Constant

class InfoFragment : Fragment(), View.OnClickListener {

    private var _binding: InfoFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.general.setOnClickListener(this)
        binding.peopleAtRisk.setOnClickListener(this)
        binding.vaccines.setOnClickListener(this)
        binding.covidTest.setOnClickListener(this)

        binding.prevention.setOnClickListener(this)
        binding.symptoms.setOnClickListener(this)
        binding.exercise.setOnClickListener(this)
        binding.food.setOnClickListener(this)
    }

    private fun toListInfo(data: String, title: String) {
        val intent = Intent(requireContext(), ListInfoActivity::class.java)
        intent.putExtra(Constant.KEY_INFO, data)
        intent.putExtra(Constant.KEY_TITLE_INFO, title)
        startActivity(intent)
    }

    private fun toDropdownInfo(data: String, title: String) {
        val intent = Intent(requireContext(), DropdownInfoActivity::class.java)
        intent.putExtra(Constant.KEY_INFO, data)
        intent.putExtra(Constant.KEY_TITLE_INFO, title)
        startActivity(intent)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.general -> {
                toDropdownInfo("general", "General")
            }
            binding.peopleAtRisk -> {
                toDropdownInfo("people_risk", "People At Risk")
            }
            binding.covidTest -> {
                toDropdownInfo("covid_test", "Covid Test")
            }
            binding.vaccines -> {
                toDropdownInfo("vaccines", "Vaccines")
            }

            binding.prevention -> {
                toListInfo("prevention", "Prevention")
            }
            binding.symptoms -> {
                toListInfo("symptoms", "Symptoms")
            }
            binding.exercise -> {
                toListInfo("exercise", "Exercise")
            }
            binding.food -> {
                toListInfo("food", "Food")
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}