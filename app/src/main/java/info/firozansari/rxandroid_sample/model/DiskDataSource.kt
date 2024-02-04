package info.firozansari.rxandroid_sample.model

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter

class DiskDataSource {
    val data: Observable<Data?>
        get() =  Observable.create { emitter: ObservableEmitter<Data?> ->
            val data = Data()
            data.source = "Disk"
            emitter.onNext(data)
            emitter.onComplete()
        }
}

