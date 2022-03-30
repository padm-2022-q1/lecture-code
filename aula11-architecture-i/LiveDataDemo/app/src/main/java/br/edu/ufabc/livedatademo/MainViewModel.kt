package br.edu.ufabc.livedatademo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    val counter: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }
}