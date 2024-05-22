package info.firozansari.rxjava.model

import io.reactivex.rxjava3.core.Observable

class DataSource(private val memoryDataSource: MemoryDataSource,
                 private val diskDataSource: DiskDataSource,
                 private val networkDataSource: NetworkDataSource) {
    fun dataFromMemory(): Observable<Data?> = memoryDataSource.data
    fun dataFromDisk(): Observable<Data?> = diskDataSource.data
    fun dataFromNetwork(): Observable<Data?> = networkDataSource.data
}