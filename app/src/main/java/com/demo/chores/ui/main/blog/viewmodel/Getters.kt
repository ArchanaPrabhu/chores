package com.demo.chores.ui.main.blog.viewmodel

import android.net.Uri
import com.demo.chores.models.BlogPost
import com.demo.chores.persistence.BlogQueryUtils.Companion.BLOG_FILTER_DATE_UPDATED
import com.demo.chores.persistence.BlogQueryUtils.Companion.BLOG_ORDER_DESC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
fun BlogViewModel.getFilter(): String {
    return getCurrentViewStateOrNew().let {
        it.blogFields.filter
    }?: BLOG_FILTER_DATE_UPDATED
}

@FlowPreview
fun BlogViewModel.getOrder(): String {
    return getCurrentViewStateOrNew().let {
        it.blogFields.order
    }?: BLOG_ORDER_DESC
}

@FlowPreview
fun BlogViewModel.getSearchQuery(): String {
    return getCurrentViewStateOrNew().let {
        it.blogFields.searchQuery
    }?: return ""
}

@FlowPreview
fun BlogViewModel.getPage(): Int{
    return getCurrentViewStateOrNew().let {
        it.blogFields.page
    }?: return 1
}

@FlowPreview
fun BlogViewModel.getSlug(): String{
    getCurrentViewStateOrNew().let {
        it.viewBlogFields.blogPost?.let {
            return it.slug
        }
    }
    return ""
}

@FlowPreview
fun BlogViewModel.isAuthorOfBlogPost(): Boolean{
    return getCurrentViewStateOrNew().viewBlogFields.isAuthorOfBlogPost ?: false
}

@FlowPreview
fun BlogViewModel.getBlogPost(): BlogPost {
    getCurrentViewStateOrNew().let {
        return it.viewBlogFields.blogPost?.let {
            return it
        }?: getDummyBlogPost()
    }
}

@FlowPreview
fun BlogViewModel.getDummyBlogPost(): BlogPost{
    return BlogPost(-1, "" , "", "", "", 1, "")
}

@FlowPreview
fun BlogViewModel.getUpdatedBlogUri(): Uri? {
    getCurrentViewStateOrNew().let {
        it.updatedBlogFields.updatedImageUri?.let {
            return it
        }
    }
    return null
}








