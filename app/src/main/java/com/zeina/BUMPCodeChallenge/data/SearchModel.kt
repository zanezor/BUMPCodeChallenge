package com.zeina.BUMPCodeChallenge.data

import kotlinx.serialization.Serializable

data class SearchModel(var data:List<Data>, val pagination: Pagination)

data class Data(val slug:String, val images: Images)

data class Images(val fixed_width: FixedWidthImage, val fixed_height: FixedHeightImage, val original: OriginalImage)

data class FixedWidthImage(val url:String, val width:Int, val height:Int)

data class FixedHeightImage(val url:String, val width:Int, val height:Int)

data class OriginalImage(val url:String, val width:Int, val height:Int)

@Serializable
data class Pagination(val total_count:Int, val count:Int, val offset:Int)

