package ru.g000sha256.wallet.common

import kotlinx.coroutines.CoroutineDispatcher

class CoroutineDispatchers(val mainCoroutineDispatcher: CoroutineDispatcher, val ioCoroutineDispatcher: CoroutineDispatcher)