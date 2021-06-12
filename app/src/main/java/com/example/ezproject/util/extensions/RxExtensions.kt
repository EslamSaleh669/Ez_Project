package com.example.ezproject.util.extensions

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


fun Observable<*>.observeOnMainThread(): Observable<*> {
    return this.observeOn(AndroidSchedulers.mainThread())
}

fun Observable<*>.subscribeOnIo(): Observable<*> {
    return this.subscribeOn(Schedulers.io())
}
