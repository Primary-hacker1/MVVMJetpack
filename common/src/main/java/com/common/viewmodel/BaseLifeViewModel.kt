package com.common.viewmodel

import androidx.lifecycle.ViewModel
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.AutoDisposeConverter
import com.uber.autodispose.ScopeProvider
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleEndedException
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 *create by 2020/9/16
 *@author zt
 */
open class BaseLifeViewModel() : ViewModel(),
    LifecycleScopeProvider<ViewEvent> {
    private val lifecycleEvents = BehaviorSubject.createDefault(ViewEvent.CREATED)

    override fun lifecycle(): Observable<ViewEvent> {
        return lifecycleEvents.hide()
    }

    override fun correspondingEvents(): CorrespondingEventsFunction<ViewEvent> {
        return CORRESPONDING_EVENTS
    }

    /**
     * Emit the [ViewModelEvent.CLEARED] event to
     * dispose off any subscriptions in the ViewModel.
     */
    override fun onCleared() {
        lifecycleEvents.onNext(ViewEvent.DESTROY)
        super.onCleared()
    }

    override fun peekLifecycle(): ViewEvent {
        return lifecycleEvents.value as ViewEvent
    }

    companion object {
        var CORRESPONDING_EVENTS: CorrespondingEventsFunction<ViewEvent> = CorrespondingEventsFunction { event ->
            when (event) {
                ViewEvent.CREATED -> ViewEvent.DESTROY
                else -> throw LifecycleEndedException(
                    "Cannot bind to ViewModel lifecycle after onCleared.")
            }
        }
    }
    fun <T> auto(provider: ScopeProvider): AutoDisposeConverter<T> {
        return AutoDispose.autoDisposable(provider)
    }
}
