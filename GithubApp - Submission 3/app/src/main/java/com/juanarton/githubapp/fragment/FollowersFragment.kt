package com.juanarton.githubapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.juanarton.githubapp.MViewModel
import com.juanarton.githubapp.R
import com.juanarton.githubapp.UserProfileActivity
import com.juanarton.githubapp.`interface`.MainView
import com.juanarton.githubapp.adapter.RecyclerAdapter
import com.juanarton.githubapp.databinding.FragmentFollowersBinding
import com.juanarton.githubapp.model.DataParcel
import com.juanarton.githubapp.model.DataProcessing
import com.juanarton.githubapp.model.DetailDataParcel


class FollowersFragment : Fragment(), MainView {

    private lateinit var binding: FragmentFollowersBinding
    private val mViewModel = MViewModel(DataProcessing(this))
    private var listData = ArrayList<DataParcel>()

    companion object {
        const val MODE = "followers"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFollowersBinding.bind(view)

        listData = mViewModel.getSocNetwork(UserProfileActivity.username, MODE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun showRecyclerList(){
        binding.fsPBar.visibility = View.INVISIBLE
        binding.followersRecyclerContainer.layoutManager = LinearLayoutManager(activity)
        val dataAdapter = RecyclerAdapter(listData)
        binding.followersRecyclerContainer.adapter = dataAdapter
    }

    override fun showMessage(messsage: String) {
        Toast.makeText(this.requireContext(), messsage, Toast.LENGTH_SHORT).show()
    }

    override fun showDetail(dataDetail: DetailDataParcel){
    }
}