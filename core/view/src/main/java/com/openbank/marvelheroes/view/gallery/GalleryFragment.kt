package com.openbank.marvelheroes.view.gallery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.openbank.marvelheroes.common.di.ComponentProvider
import com.openbank.marvelheroes.common.flow.flowObserverCollector
import com.openbank.marvelheroes.model.CharacterModel
import com.openbank.marvelheroes.view.R
import com.openbank.marvelheroes.view.activity.MainActivity
import com.openbank.marvelheroes.view.databinding.FragmentGalleryBinding
import com.openbank.marvelheroes.view.di.CoreViewSubcomponent
import com.openbank.marvelheroes.view.extension.toItem
import com.openbank.marvelheroes.view.list.character.CharacterItem
import com.openbank.marvelheroes.viewmodel.GalleryViewModel
import com.openbank.marvelheroes.viewmodel.factory.ViewModelFactory
import javax.inject.Inject

/**
 * Gallery of characters. Each element of this list shows an image and the name of the character.
 * The user can click on one character and the app will lead to the character's detail data screen.
 */
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    @Inject internal lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentGalleryBinding

    private val viewModel: GalleryViewModel by viewModels(ownerProducer = { requireActivity() }) { viewModelFactory }

    private val mapCharacters : (List<CharacterModel>) -> List<CharacterItem> = { it.toItem() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ((context.applicationContext as ComponentProvider)
            .components[CoreViewSubcomponent::class.java] as CoreViewSubcomponent)
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeStates()
        observeEvents()
        setupUi()
        if (viewModel.currentPage == 0)
            viewModel.getCharactersPage(1, mapCharacters)
        else
            viewModel.loadCurrentCharacters(mapCharacters)
    }

    private fun setupUi() {
        with (binding.root) {
            onItemClick = { id ->
                (activity as? MainActivity)?.apply { navigateToDetail(id) }
            }
            onLimitReached = {
                // If the end of the list is reached and there are remaining pages to download,
                // the next page will be requested.
                if (!viewModel.isLastPage)
                    viewModel.getCharactersPage(viewModel.currentPage+1, mapCharacters)
            }
        }
    }

    private fun observeStates() {
        viewModel.state.flowObserverCollector(viewLifecycleOwner) { state ->
            when (state) {
                is GalleryViewModel.State.CharactersPage<*> -> {
                    binding.root.addCharacters(
                        state.list.map { character -> character as CharacterItem }
                    )
                    binding.root.onLoadFinished()
                }
                GalleryViewModel.State.Idle -> Unit
                GalleryViewModel.State.Loading -> {
                    binding.root.showMessage(
                        requireContext().getString(R.string.loading_characters_message)
                    )
                }
            }
        }
    }

    private fun observeEvents() {
        viewModel.event.flowObserverCollector(viewLifecycleOwner) { event ->
            when (event) {
                GalleryViewModel.Event.EmptyCharactersPage -> Snackbar.make(requireContext(),
                    binding.charactersList,
                    getString(R.string.characters_not_found), Snackbar.LENGTH_LONG).show()
            }
        }
    }
}