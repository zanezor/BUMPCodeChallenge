package com.zeina.BUMPCodeChallenge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.zeina.BUMPCodeChallenge.data.GiphyAPI
import com.zeina.BUMPCodeChallenge.data.SearchModel
import com.zeina.BUMPCodeChallenge.search.SearchAdapter
import com.zeina.BUMPCodeChallenge.search.SearchContract
import com.zeina.BUMPCodeChallenge.search.SpacingItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity(), SearchContract.View {

    private var searchLimit = 25

    private var isLoading: Boolean = true
    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var previousTotal: Int = 0
    private var viewThreshold: Int = 15

    private lateinit var listAdapter: SearchAdapter
    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        gridLayoutManager = GridLayoutManager(applicationContext, 2)
        searching_recycler_view.layoutManager = gridLayoutManager

        searchGif(randomWord(words), 0)
    }

    //Hard-coded array
    var words = arrayOf("NYC", "streetwear", "fashion", "art","contemporary")

    //Picks a random word from array
    private fun randomWord(a: Array<String>): String {
        val rnd = Random().nextInt(a.size)
        return a[rnd]
    }

    //Shows progress bar
    override fun showProgress() {
        progress_bar.visible()
    }

    //Hides progress bar, renders searching-recycler-view
    override fun hideProgress() {
        progress_bar.gone()
        searching_recycler_view.visible()
    }

    fun View.gone() = run { this.visibility = View.GONE }
    fun View.visible() = run { this.visibility = View.VISIBLE }

    //If searcing-recycler-view is null (during first API call), call showSearchHelper
    // else addMoreGifs
    private fun showSearch (searching: SearchModel) {
        if (searching_recycler_view.adapter == null) {
            showSearchHelper(searching)
        }
        else {
            listAdapter.addMoreGifs(searching)
        }
    }

    //Helper method to init data
    override fun showSearchHelper(searching: SearchModel) {
        searching_recycler_view.apply {
            addItemDecoration(SpacingItemDecoration(10))
            listAdapter = SearchAdapter(searching, this@MainActivity)
            searching_recycler_view.adapter = listAdapter
        }
    }

    //Makes API call for input
    //If successful, pass data to showSearch
    private fun searchGif(input: String, offset: Int ) {
        showProgress()
        var realOffset = offset

        val call = GiphyAPI.searching().fetchByField(input, searchLimit, realOffset, GiphyAPI.GIPHY_API_KEY)
        call.enqueue(object : Callback<SearchModel> {

            override fun onResponse(call: Call<SearchModel>, response: Response<SearchModel>) {

                if (response.isSuccessful) {
                    hideProgress()
                    response.body()?.let { showSearch(it) }
                } else {
                    Toast.makeText(this@MainActivity, "API call failed!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<SearchModel>, t: Throwable?) {
                Log.e("***** FAILED *****", t?.message)
            }
        })


        //On scroll listener
        //When user scrolls down, increment offset value and call performPagination to call next set of data
        searching_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(searching_recycler_view: RecyclerView, dx: Int, dy: Int) {

                visibleItemCount = gridLayoutManager!!.childCount
                totalItemCount = gridLayoutManager.itemCount
                pastVisibleItems = gridLayoutManager!!.findFirstVisibleItemPosition()

                if (dy > 0) {
                    if (isLoading) {
                        if (totalItemCount>previousTotal) {
                            isLoading = false
                            previousTotal = totalItemCount
                        }
                    }

                    if (!isLoading && (totalItemCount-visibleItemCount)<=pastVisibleItems+viewThreshold) {
                        realOffset+=25
                        performPagination(input, realOffset)
                        isLoading = true

                    }
                }
            }
        })
    }

    //Makes API call to additional gifs
    //If there are no gifs left, displays "No more gifs!" toast message
    private fun performPagination(input: String, searchOffset: Int) {

        val call = GiphyAPI.searching().fetchByField(input, searchLimit, searchOffset, GiphyAPI.GIPHY_API_KEY)
        call.enqueue(object : Callback<SearchModel> {

            override fun onResponse(call: Call<SearchModel>, response: Response<SearchModel>) {

                if (response.body()!!.pagination.total_count > searchOffset ) {

                    response.body()?.let { showSearch(it) }
                }

                else {
                    Toast.makeText(this@MainActivity, "No more gifs!", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<SearchModel>, t: Throwable?) {
                Log.e("***** FAILED *****", t?.message)
            }
        })
    }
}
