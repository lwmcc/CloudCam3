package com.mccarty.cloudcam3.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mccarty.cloudcam3.R
import com.mccarty.cloudcam3.adapters.MediaAdapter
import com.mccarty.cloudcam3.db.ImageEntity
import com.mccarty.cloudcam3.dialogs.DeleteConfirmDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_camera_view.*
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val TAG = HomeFragment::class.java.simpleName
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: MediaAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var entityList: MutableList<ImageEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        entityList = mutableListOf()

        homeViewModel.getAllMediaList.observe(requireActivity(), Observer { images ->
            if(images.isNotEmpty()) {
                entityList.clear()
                entityList.addAll(images)
                notifyDataChange() // TODO: is this needed?
            }
        })

        homeViewModel.showDeleteImageDialog.observe(requireActivity(), Observer {
            DeleteConfirmDialog(homeViewModel).show(parentFragmentManager, "DELETE_IMAGE_CONFIRMATION")
        })

        homeViewModel.adapterChanged.observe(requireActivity(), Observer {
            if(it) {
                homeViewModel.getAllMedia()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.media_recycler)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 5)

        adapter = MediaAdapter(homeViewModel, entityList)
        recyclerView.adapter = adapter

        return root
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.getAllMedia()
    }

    private fun notifyDataChange() {
        adapter.notifyDataSetChanged()
    }

}