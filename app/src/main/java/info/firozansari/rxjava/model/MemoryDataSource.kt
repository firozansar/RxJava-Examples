package info.firozansari.rxjava.model

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter

class MemoryDataSource {
    val data: Observable<Data?>
        get() =  Observable.create { emitter: ObservableEmitter<Data?> ->
            val data = Data()
            data.source = "Memory"
            emitter.onNext(data)
            emitter.onComplete()
        }
}

