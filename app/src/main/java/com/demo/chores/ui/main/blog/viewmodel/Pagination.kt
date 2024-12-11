import android.util.Log
import androidx.annotation.experimental.UseExperimental
import com.demo.chores.ui.main.blog.state.BlogStateEvent
import com.demo.chores.ui.main.blog.state.BlogStateEvent.*
import com.demo.chores.ui.main.blog.state.BlogViewState
import com.demo.chores.ui.main.blog.viewmodel.BlogViewModel
import com.demo.chores.ui.main.blog.viewmodel.setBlogListData
import com.demo.chores.ui.main.blog.viewmodel.setQueryExhausted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
fun BlogViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.blogFields.page = 1
    setViewState(update)
}

@FlowPreview
fun BlogViewModel.refreshFromCache(){
    setQueryExhausted(false)
    setStateEvent(RestoreBlogListFromCache())
}

@FlowPreview
fun BlogViewModel.loadFirstPage() {
    setQueryExhausted(false)
    resetPage()
    setStateEvent(BlogSearchEvent())
    Log.e(TAG, "BlogViewModel: loadFirstPage: ${viewState.value!!.blogFields.searchQuery}")
}

@FlowPreview
private fun BlogViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.copy().blogFields.page // get current page
    update.blogFields.page = page?.plus(1)
    setViewState(update)
}

@FlowPreview
fun BlogViewModel.nextPage(){
    if(!isJobAlreadyActive(BlogSearchEvent())
        && !viewState.value!!.blogFields.isQueryExhausted!!
    ){
        Log.d(TAG, "BlogViewModel: Attempting to load next page...")
        incrementPageNumber()
        setStateEvent(BlogSearchEvent())
    }
}

@FlowPreview
fun BlogViewModel.handleIncomingBlogListData(viewState: BlogViewState){
    viewState.blogFields.let { blogFields ->
        blogFields.blogList?.let { setBlogListData(it) }
        blogFields.isQueryExhausted?.let {  setQueryExhausted(it) }
    }
}










