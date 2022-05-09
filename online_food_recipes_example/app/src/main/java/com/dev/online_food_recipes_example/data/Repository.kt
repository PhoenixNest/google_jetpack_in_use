package com.dev.online_food_recipes_example.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

// Scope annotation for bindings that should exist for the life of an activity, surviving configuration.
@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDatasource: RemoteDatasource,
    localDatasource: LocalDatasource
) {
    val remote = remoteDatasource
    val local = localDatasource
}