package com.example.motus

import com.example.data.game.GameRepository
import com.example.data.word.WordRepositoryImpl
import com.example.domain.MotusGameImpl
import com.example.domain.MotusGameInteractor
import com.example.data.word.WordRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { GameViewModel(get(),get(),get()) }
    single<MotusGameInteractor> { MotusGameImpl() }
    single<WordRepository> { WordRepositoryImpl(get(),get()) }
    single { GameRepository(get()) }
}