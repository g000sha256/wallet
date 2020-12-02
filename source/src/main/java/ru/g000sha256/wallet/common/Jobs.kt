package ru.g000sha256.wallet.common

import kotlinx.coroutines.Job

class Jobs {

    private val hashMap = HashMap<String, Job>()

    @Synchronized
    fun clear() {
        hashMap.forEach { it.value.cancel() }
        hashMap.clear()
    }

    @Synchronized
    fun contains(key: String): Boolean {
        return hashMap.contains(key)
    }

    @Synchronized
    fun remove(key: String) {
        val job = hashMap.remove(key)
        job?.cancel()
    }

    @Synchronized
    operator fun set(key: String, job: Job) {
        remove(key)
        hashMap[key] = job
    }

}