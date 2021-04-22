package com.just.news.model

/**
 *create by 2020/6/19
 *@author zt
 */
data class NewResponse(
    val T1467284926140: List<T1467284926140>
)

data class T1467284926140(
    val TAG: String,
    val TAGS: String,
    val alias: String,
    val articleType: String,
    val boardid: String,
    val category: String,
    val cid: String,
    val commentStatus: Int,
    val daynum: String,
    val digest: String,
    val docid: String,
    val editor: List<Any>,
    val ename: String,
    val extraShowFields: String,
    val hasAD: Int,
    val hasCover: Boolean,
    val hasHead: Int,
    val hasIcon: Boolean,
    val hasImg: Int,
    val imgType: Int,
    val imgsrc: String,
    val live_info: LiveInfo,
    val lmodify: String,
    val ltitle: String,
    val mtime: String,
    val order: Int,
    val postid: String,
    val priority: Int,
    val ptime: String,
    val quality: Int,
    val replyCount: Int,
    val skipID: String,
    val skipType: String,
    val source: String,
    val sourceId: String,
    val specialID: String,
    val specialadlogo: String,
    val specialextra: List<Specialextra>,
    val speciallogo: String,
    val specialtip: String,
    val subtitle: String,
    val tagList: List<Tag>,
    val template: String,
    val title: String,
    val tname: String,
    val topic_background: String,
    val topicid: String,
    val unfoldMode: Int,
    val url: String,
    val url_3w: String,
    val votecount: Int
)

data class LiveInfo(
    val end_time: String,
    val mutilVideo: Boolean,
    val pano: Boolean,
    val roomId: Int,
    val roomName: String,
    val start_time: String,
    val type: Int,
    val user_count: Int,
    val video: Boolean
)

data class Specialextra(
    val boardid: String,
    val commentStatus: Int,
    val daynum: String,
    val digest: String,
    val docid: String,
    val editor: List<Any>,
    val extraShowFields: String,
    val imgType: Int,
    val imgextra: List<Imgextra>,
    val imgsrc: String,
    val ltitle: String,
    val mtime: String,
    val postid: String,
    val priority: Int,
    val ptime: String,
    val quality: Int,
    val replyCount: Int,
    val source: String,
    val sourceId: String,
    val subtitle: String,
    val title: String,
    val topicid: String,
    val url: String,
    val url_3w: String,
    val votecount: Int
)

data class Tag(
    val level: Int,
    val text: String,
    val type: String
)

data class Imgextra(
    val imgsrc: String
)