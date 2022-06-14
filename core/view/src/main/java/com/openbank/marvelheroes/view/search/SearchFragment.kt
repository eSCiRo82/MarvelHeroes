package com.openbank.marvelheroes.view.search

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.openbank.marvelheroes.common.di.ComponentProvider
import com.openbank.marvelheroes.common.flow.flowObserverCollector
import com.openbank.marvelheroes.model.CharacterModel
import com.openbank.marvelheroes.view.R
import com.openbank.marvelheroes.view.activity.MainActivity
import com.openbank.marvelheroes.view.databinding.FragmentSearchBinding
import com.openbank.marvelheroes.view.di.CoreViewSubcomponent
import com.openbank.marvelheroes.view.extension.toItem
import com.openbank.marvelheroes.view.list.character.CharacterItem
import com.openbank.marvelheroes.view.listener.OnSafeClickListener
import com.openbank.marvelheroes.viewmodel.SearchViewModel
import com.openbank.marvelheroes.viewmodel.factory.ViewModelFactory
import javax.inject.Inject


/**
 * This is the search screen, where the user can type some characters of the start of the name
 * to search.
 */
class SearchFragment : Fragment(R.layout.fragment_search) {

    @Inject internal lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by viewModels { viewModelFactory }

    private val mapCharacters : (List<CharacterModel>) -> List<CharacterItem> = { it.toItem() }

    private var searchString: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ((context.applicationContext as ComponentProvider)
            .components[CoreViewSubcomponent::class.java] as CoreViewSubcomponent)
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeStates()
        observeEvents()
        setupUi()
    }

    private fun setupUi() {
        with (binding) {
            charactersList.onItemClick = { id ->
                (activity as? MainActivity)?.apply { navigateToDetail(id) }
            }
            charactersList.onLimitReached = {
                if (!viewModel.isLastPage)
                    viewModel.getCharactersByName(searchString,
                        viewModel.currentPage+1,
                        mapCharacters)
            }
            searchButton.setOnClickListener(OnSafeClickListener(500) { view ->
                val inputMethodManager: InputMethodManager? =
                    requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
                inputMethodManager?.hideSoftInputFromWindow(view.applicationWindowToken, 0)
                search(view)
            })
        }
    }

    private fun search(view: View) {
        searchString = binding.filterEdittext.text.toString()
        // The string for searching must have 3 characters at least.
        if (searchString.length < 3) {
            Snackbar.make(requireContext(), binding.charactersList,
                getString(R.string.search_string_length), Snackbar.LENGTH_LONG).show()
        } else {
            view.isEnabled = false
            binding.charactersList.clearList()
            viewModel.getCharactersByName(searchString, 1) { list -> list.toItem() }
        }
    }

    private fun observeStates() {
        viewModel.state.flowObserverCollector(viewLifecycleOwner) { state ->
            when (state) {
                is SearchViewModel.State.CharactersPage<*> -> {
                    binding.charactersList.addCharacters(
                        state.list.map { character -> character as CharacterItem }
                    )
                    binding.charactersList.onLoadFinished()
                    binding.searchButton.isEnabled = true
                }
                SearchViewModel.State.Loading -> {
                    binding.charactersList.clearList()
                    binding.charactersList.showMessage(
                        requireContext().getString(R.string.searching_by_message, searchString))
                }
                SearchViewModel.State.Idle -> Unit
            }
        }
    }

    private fun observeEvents() {
        viewModel.event.flowObserverCollector(viewLifecycleOwner) { event ->
            when (event) {
                SearchViewModel.Event.EmptyCharactersPage -> {
                    binding.charactersList.clearList()
                    binding.charactersList.showMessage(
                        requireContext().getString(R.string.search_characters_not_found, searchString))
                    binding.searchButton.isEnabled = true
                }
            }
        }
    }
}