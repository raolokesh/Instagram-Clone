package com.lokesh.instagramclone.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.lokesh.instagramclone.R
import com.lokesh.instagramclone.SignupActivity
import com.lokesh.instagramclone.adapters.ViewPagerAdapter
import com.lokesh.instagramclone.databinding.FragmentProfileBinding
import com.lokesh.instagramclone.models.UserRegisterModel
import com.lokesh.instagramclone.utils.USER_NODE
import com.squareup.picasso.Picasso


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)

        binding.editProfile.setOnClickListener {
            val intent = Intent(activity,SignupActivity::class.java)
            intent.putExtra("MODE",1)
            activity?.startActivity(intent)
            activity?.finish()
        }
        viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragments(MyPostFragment(),"My Post")
        viewPagerAdapter.addFragments(MyReelFragment(),"My Reel")
        binding.viewpager.adapter = viewPagerAdapter
        binding.tablayout.setupWithViewPager(binding.viewpager)
        return binding.root
    }

    companion object {
    }

    override fun onStart() {
        super.onStart()
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                val user:UserRegisterModel = it.toObject<UserRegisterModel>()!!
                binding.name.text = user.name
                binding.profilebio.text = user.email
                if(!user.image.isNullOrEmpty()){
                    Picasso.get().load(user.image).into(binding.profileimage)
                }
            }
    }
}