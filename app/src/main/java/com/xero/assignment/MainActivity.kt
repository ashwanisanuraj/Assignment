package com.xero.assignment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.xero.assignment.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSearch.setOnClickListener {
            val username = binding.userName.text.toString().trim()
            if (username.isNotEmpty()) {
                getUserDetails(username)
            } else {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUserDetails(username: String) {
        GithubClient.instance.getUser(username).enqueue(object : Callback<Github> {
            override fun onResponse(call: Call<Github>, response: Response<Github>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        updateUI(it)
                    }
                } else {
                    binding.tvUsername.text = "No User Found"
                    binding.tvRepositories.text = ""
                    binding.tvFollowers.text = ""
                    binding.tvFollowing.text = ""
                    binding.imageView.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<Github>, t: Throwable) {
                // Handle network errors here
                binding.tvUsername.text = " No Internet "
                binding.tvRepositories.text = ""
                binding.tvFollowers.text = ""
                binding.tvFollowing.text = ""
                binding.imageView.visibility = View.GONE
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(user: Github) {
        Picasso.get().load(user.avatar_url).into(binding.imageView)
        binding.imageView.visibility = View.VISIBLE
        binding.tvUsername.text = user.login
        binding.tvRepositories.text = "Repositories: ${user.public_repos}"
        binding.tvFollowers.text = "Followers: ${user.followers}"
        binding.tvFollowing.text = "Following: ${user.following}"
    }
}
