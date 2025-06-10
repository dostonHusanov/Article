package com.doston.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.doston.article.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel:MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       viewModel.articles.observe(viewLifecycleOwner){
           binding.recyclerview.adapter=ArticleAdapter(it)
       }
        viewModel.loadArticles(user ="caf31ffd-cb31-48e6-a67b-cfe794c57486" , key ="com.myapp1" , tab = "money")
    }

}