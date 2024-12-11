package com.demo.chores.ui.main.blog.state

import android.net.Uri
import android.os.Parcelable
import com.demo.chores.models.BlogPost
import com.demo.chores.persistence.BlogQueryUtils.Companion.BLOG_ORDER_ASC
import com.demo.chores.persistence.BlogQueryUtils.Companion.ORDER_BY_ASC_DATE_UPDATED
import kotlinx.android.parcel.Parcelize

const val BLOG_VIEW_STATE_BUNDLE_KEY = "com.demo.chores.ui.main.blog.state.BlogViewState"

@Parcelize
data class BlogViewState (

    // BlogFragment vars
    var blogFields: BlogFields = BlogFields(),

    // ViewBlogFragment vars
    var viewBlogFields: ViewBlogFields = ViewBlogFields(),

    // UpdateBlogFragment vars
    var updatedBlogFields: UpdatedBlogFields = UpdatedBlogFields()

): Parcelable {

    @Parcelize
    data class BlogFields(
        var blogList: List<BlogPost>? = null,
        var searchQuery: String? = null,
        var page: Int? = null,
//        var isQueryInProgress: Boolean? = null,
        var isQueryExhausted: Boolean? = null,
        var filter: String? = null,
        var order: String? = null,
        var layoutManagerState: Parcelable? = null
    ) : Parcelable

    @Parcelize
    data class ViewBlogFields(
        var blogPost: BlogPost? = null,
        var isAuthorOfBlogPost: Boolean? = null
    ) : Parcelable

    @Parcelize
    data class UpdatedBlogFields(
        var updatedBlogTitle: String? = null,
        var updatedBlogBody: String? = null,
        var updatedImageUri: Uri? = null
    ) : Parcelable
}








