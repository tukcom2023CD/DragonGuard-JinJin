package com.dragonguard.android.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dragonguard.android.R
import com.dragonguard.android.activity.basic.MainActivity
import com.dragonguard.android.activity.compare.RepoCompareActivity
import com.dragonguard.android.activity.menu.MenuActivity
import com.dragonguard.android.adapters.ClientGitOrgAdapter
import com.dragonguard.android.adapters.OthersReposAdapter
import com.dragonguard.android.databinding.FragmentClientProfileBinding
import com.dragonguard.android.model.detail.ClientDetailModel
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ClientProfileFragment(private val token: String, private val viewmodel: Viewmodel, private val userName: String) : Fragment() {
    private lateinit var binding: FragmentClientProfileBinding
    private lateinit var orgAdapter: ClientGitOrgAdapter
    private lateinit var repoAdapter: OthersReposAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClientProfileBinding.inflate(inflater, container, false)
        val main = activity as MainActivity
        setHasOptionsMenu(true)
        main.setSupportActionBar(binding.toolbar)
        main.supportActionBar?.setDisplayShowTitleEnabled(false)
        main.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getClientDetail()
    }



    private fun getClientDetail() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if (!this@ClientProfileFragment.isRemoving) {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.getClientDetails(token)
                }
                val result = resultDeferred.await()
                result?.let {
                    initRecycler(it)
                }

            }
        }
    }

    private fun initRecycler(result: ClientDetailModel) {
        Log.d("결과", "사용자 org: ${result.git_organizations}")
        Log.d("결과", "사용자 repos: ${result.git_repos}")
        if(!this@ClientProfileFragment.isDetached && this@ClientProfileFragment.isAdded) {
            orgAdapter = ClientGitOrgAdapter(result.git_organizations, requireContext(), token)
            binding.memberOrganizaitonList.adapter = orgAdapter
            binding.memberOrganizaitonList.layoutManager = LinearLayoutManager(requireContext())
            orgAdapter.notifyDataSetChanged()

            repoAdapter = OthersReposAdapter(result.git_repos, requireContext(), token, result.member_profile_image, userName)
            binding.memberRepositoryList.adapter = repoAdapter
            binding.memberRepositoryList.layoutManager = LinearLayoutManager(requireContext())
            repoAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.setting, binding.toolbar.menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_button -> {
                val intent = Intent(requireContext(), MenuActivity::class.java)
                intent.putExtra("token", token)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}