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
import com.mccarty.cloudcam3.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        val mediaViewButton: Button = root.findViewById(R.id.button_for_test)

        // TODO: use this instead
        //button_for_test.setOnClickListener {
        //    Navigation.createNavigateOnClickListener(R.id.frag_camera_view, null)
        //}

        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        mediaViewButton.setOnClickListener {
            Navigation.createNavigateOnClickListener(R.id.frag_camera_view, null)
        }

        return root
    }
}