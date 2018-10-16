package com.zeina.BUMPCodeChallenge.search

import com.zeina.BUMPCodeChallenge.data.SearchModel

interface SearchContract {

    interface View {
        fun showProgress()
        fun hideProgress()
        fun showSearchHelper(searching: SearchModel)
    }
}