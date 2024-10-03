package br.edu.ifpb.pdm.oriymenu.di

import androidx.room.Room
import br.edu.ifpb.pdm.oriymenu.database.OriyMenuDatabase
import br.edu.ifpb.pdm.oriymenu.repository.DishRepository
import br.edu.ifpb.pdm.oriymenu.repository.MenuRepository
import org.koin.dsl.module


val appModule = module {
    single { Room.databaseBuilder(get(), OriyMenuDatabase::class.java, "oriymenu_database").build() }
    single { get<OriyMenuDatabase>().menuDao() }
    single { get<OriyMenuDatabase>().dishDao() }
    single { MenuRepository(get(), get()) }
    single { DishRepository(get(), get()) }
}
