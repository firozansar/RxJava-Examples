package info.firozansari.rxjava.model

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter

class NetworkDataSource {
    val data: Observable<Data?>
        get() =  Observable.create { emitter: ObservableEmitter<Data?> ->
            val data = Data()
            data.source = "Network"
            emitter.onNext(data)
            emitter.onComplete()
        }
}

