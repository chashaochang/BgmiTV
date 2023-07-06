package cn.xiaobai.bgmi.di

import android.app.Application
import cn.xiaobai.player.PlayerFactory
import cn.xiaobai.player.domain.TLPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object PlayerModule {

    @Provides
    @ViewModelScoped
    fun provideVideoPlayer(app:Application): TLPlayer {
        return PlayerFactory.create(app)
    }


}