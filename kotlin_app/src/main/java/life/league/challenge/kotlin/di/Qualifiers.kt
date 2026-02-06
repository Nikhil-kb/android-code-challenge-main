package life.league.challenge.kotlin.di

import javax.inject.Qualifier

/** Qualifier for injecting IO dispatcher where background work is needed. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher