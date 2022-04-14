package com.febrian.icc.ui.setting

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.febrian.icc.R
import com.febrian.icc.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.info.setOnClickListener {
            showInfo()
        }
    }

    private fun showInfo() {
        val builder = AlertDialog.Builder(requireContext())
        val lView =
            LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog_about, null)
        builder.setView(lView)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun setDarkMode() {
        val sharedPref =
            view?.context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val value: String = sharedPref?.getString("KEY", "Follow By System").toString()
        val itemName = setOf(value, "Yes", "No", "Follow By System")
        val list: ArrayList<String> = ArrayList()

        for (i in itemName.indices) {
            list.add(itemName.elementAt(i))
        }

        val adapter =
            view?.let {
                ArrayAdapter(
                    it.context,
                    android.R.layout.simple_spinner_dropdown_item,
                    list
                )
            }
        binding.darkmodeActive.adapter = adapter

        binding.darkmodeActive.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (sharedPref != null) {
                        when {
                            parent?.getItemAtPosition(position).toString() == "Yes" -> {
                                sharedPref.edit().clear().apply()
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                sharedPref.edit().putString("KEY", "Yes").apply()
                            }
                            parent?.getItemAtPosition(position).toString() == "No" -> {
                                sharedPref.edit().clear().apply()
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                                sharedPref.edit().putString("KEY", "No").apply()
                            }
                            parent?.getItemAtPosition(position)
                                .toString() == "Follow By System" -> {
                                sharedPref.edit().clear().apply()
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                                sharedPref.edit().putString("KEY", "Follow By System").apply()
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
    }

    override fun onResume() {
        super.onResume()
        setDarkMode()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}