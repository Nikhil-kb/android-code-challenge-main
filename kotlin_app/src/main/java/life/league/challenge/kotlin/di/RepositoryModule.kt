package life.league.challenge.kotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import life.league.challenge.kotlin.data.auth.CredentialsProvider
import life.league.challenge.kotlin.data.auth.ResourceCredentialsProvider
import life.league.challenge.kotlin.data.posts.repository.NetworkPostsRepository
import life.league.challenge.kotlin.domain.posts.repository.PostsRepository
import javax.inject.Singleton

/** Binding module for interfaces used across layers. */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPostsRepository(impl: NetworkPostsRepository): PostsRepository

    @Binds
    @Singleton
    abstract fun bindCredentialsProvider(impl: ResourceCredentialsProvider): CredentialsProvider
}