package com.demo.chores.ui.main.blog

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.demo.chores.R
import com.demo.chores.databinding.FragmentViewBlogBinding
import com.demo.chores.models.BlogPost
import com.demo.chores.ui.AreYouSureCallback
import com.demo.chores.ui.main.blog.state.BLOG_VIEW_STATE_BUNDLE_KEY
import com.demo.chores.ui.main.blog.state.BlogStateEvent.*
import com.demo.chores.ui.main.blog.state.BlogViewState
import com.demo.chores.ui.main.blog.viewmodel.*
import com.demo.chores.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancel
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class ViewBlogFragment
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: RequestManager
): BaseBlogFragment(R.layout.fragment_view_blog)
{

    val viewModel: BlogViewModel by viewModels{
        viewModelFactory
    }

    var _binding : FragmentViewBlogBinding? = null
    val binding: FragmentViewBlogBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Restore state after process death
        savedInstanceState?.let { inState ->
            (inState[BLOG_VIEW_STATE_BUNDLE_KEY] as BlogViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewBlogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    /**
     * !IMPORTANT!
     * Must save ViewState b/c in event of process death the LiveData in ViewModel will be lost
     */
    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value

        //clear the list. Don't want to save a large list to bundle.
        viewState?.blogFields?.blogList = ArrayList()

        outState.putParcelable(
            BLOG_VIEW_STATE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun setupChannel(){
        viewModel.setupChannel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()
        checkIsAuthorOfBlogPost()
        uiCommunicationListener.expandAppBar()

        binding.deleteButton.setOnClickListener {
            confirmDeleteRequest()
        }

    }

    fun confirmDeleteRequest(){
        val callback: AreYouSureCallback = object: AreYouSureCallback {

            override fun proceed() {
                deleteBlogPost()
            }

            override fun cancel() {
                // ignore
            }
        }
        uiCommunicationListener.onResponseReceived(
            response = Response(
                message = getString(R.string.are_you_sure_delete),
                uiComponentType = UIComponentType.AreYouSureDialog(callback),
                messageType = MessageType.Info()
            ),
            stateMessageCallback = object: StateMessageCallback{
                override fun removeMessageFromStack() {
                    viewModel.clearStateMessage()
                }
            }
        )
    }

    fun deleteBlogPost(){
        viewModel.setStateEvent(
            DeleteBlogPostEvent()
        )
    }

    fun checkIsAuthorOfBlogPost(){
        viewModel.setIsAuthorOfBlogPost(false) // reset
        viewModel.setStateEvent(CheckAuthorOfBlogPost())
    }

    fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.viewBlogFields.blogPost?.let{ blogPost ->
                setBlogProperties(blogPost)
            }

            if(viewState.viewBlogFields.isAuthorOfBlogPost == true){
                adaptViewToAuthorMode()
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            stateMessage?.let {
                uiCommunicationListener.onResponseReceived(
                    response = it.response,
                    stateMessageCallback = object: StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })
    }

    fun adaptViewToAuthorMode(){
        activity?.invalidateOptionsMenu()
        binding.deleteButton.visibility = View.VISIBLE
    }

    fun setBlogProperties(blogPost: BlogPost){
        requestManager
            .load(blogPost.image)
            .into(binding.blogImage)
        binding.blogTitle.setText(blogPost.title)
        binding.blogAuthor.setText(blogPost.username)
        binding.blogUpdateDate.setText(DateUtils.convertLongToStringDate(blogPost.date_updated))
        binding.blogBody.setText(blogPost.body)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if(viewModel.isAuthorOfBlogPost()){
            inflater.inflate(R.menu.edit_view_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(viewModel.isAuthorOfBlogPost()){
            when(item.itemId){
                R.id.edit -> {
                    navUpdateBlogFragment()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navUpdateBlogFragment(){
        try{
            // prep for next fragment
            viewModel.setUpdatedTitle(viewModel.getBlogPost().title)
            viewModel.setUpdatedBody(viewModel.getBlogPost().body)
            viewModel.setUpdatedUri(viewModel.getBlogPost().image.toUri())
            findNavController().navigate(R.id.action_viewBlogFragment_to_updateBlogFragment)
        }catch (e: Exception){
            // send error report or something. These fields should never be null. Not possible
            Log.e(TAG, "Exception: ${e.message}")
        }
    }
}













