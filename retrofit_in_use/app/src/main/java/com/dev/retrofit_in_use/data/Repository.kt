package com.dev.retrofit_in_use.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDatasource
) {
    val remote = remoteDataSource
    val local = localDataSource
}