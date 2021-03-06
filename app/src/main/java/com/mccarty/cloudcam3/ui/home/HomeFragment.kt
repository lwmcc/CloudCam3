package com.mccarty.cloudcam3.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.mccarty.cloudcam3.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    //private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        val mediaViewButton: Button = root.findViewById(R.id.button_for_test)

       /* homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        mediaViewButton.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.cameraViewFragment)
        }

        return root
    }
}