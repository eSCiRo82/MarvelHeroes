package com.openbank.marvelheroes.view.detail

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.openbank.marvelheroes.common.di.ComponentProvider
import com.openbank.marvelheroes.common.flow.flowObserverCollector
import com.openbank.marvelheroes.model.CharacterDetailModel
import com.openbank.marvelheroes.view.R
import com.openbank.marvelheroes.view.databinding.FragmentDetailBinding
import com.openbank.marvelheroes.view.di.CoreViewSubcomponent
import com.openbank.marvelheroes.view.extension.toItem
import com.openbank.marvelheroes.viewmodel.DetailViewModel
import com.openbank.marvelheroes.viewmodel.factory.ViewModelFactory
import javax.inject.Inject

/**
 * Character detail screen. In this screen are shown the name, the description, the image and the
 * list of appearances of the character in different formats (comics, series, stories and events)
 */
class DetailFragment: Fragment(R.layout.fragment_detail) {

    @Inject internal lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentDetailBinding

    private val viewModel: DetailViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ((context.applicationContext as ComponentProvider)
            .components[CoreViewSubcomponent::class.java] as CoreViewSubcomponent)
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeEvents()
        setupUi()
        arguments?.getInt("characterId")?.apply {
            viewModel.getCharacterDetail(this)
        }
    }

    private fun setupUi() {
        binding.characterDescription.movementMethod = ScrollingMovementMethod()
    }

    private fun observeEvents() {
        viewModel.event.flowObserverCollector(viewLifecycleOwner) { event ->
            when (event) {
                is DetailViewModel.Event.CharacterDetail -> setDetail(event.detail)
                DetailViewModel.Event.CharacterNotFound -> Snackbar.make(requireContext(),
                    binding.characterAppearancesList,
                    getString(R.string.character_not_found), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setDetail(detail: CharacterDetailModel) {
        with (binding) {
            characterImage.setImageBitmap(detail.image)
            characterName.text = detail.name
            characterDescription.text =
                HtmlCompat.fromHtml(detail.description, HtmlCompat.FROM_HTML_MODE_COMPACT)
            if (detail.comics.isNotEmpty())
                characterAppearancesList.addSection(getString(R.string.comics_section),
                    detail.comics.toItem())

            if (detail.series.isNotEmpty())
                characterAppearancesList.addSection(getString(R.string.series_section),
                    detail.series.toItem())

            if (detail.stories.isNotEmpty())
                characterAppearancesList.addSection(getString(R.string.stories_section),
                    detail.stories.toItem())

            if (detail.events.isNotEmpty())
                characterAppearancesList.addSection(getString(R.string.events_section),
                    detail.events.toItem())
        }
    }
}