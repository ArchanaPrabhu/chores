package com.demo.chores.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.demo.chores.models.BlogPost
import com.demo.chores.util.Constants.Companion.PAGINATION_PAGE_SIZE

@Dao
interface BlogPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(blogPost: BlogPost): Long

    @Delete
    fun deleteBlogPost(blogPost: BlogPost)

    @Query(
        """
        UPDATE blog_post SET title = :title, body = :body, image = :image 
        WHERE pk = :pk
        """
    )
    fun updateBlogPost(pk: Int, title: String, body: String, image: String)

    @Query(
        """
        SELECT * FROM blog_post 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        OR username LIKE '%' || :query || '%' 
        LIMIT (:page * :pageSize)
        """
    )
    fun getAllBlogPosts(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<BlogPost>

    @Query(
        """
        SELECT * FROM blog_post 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        OR username LIKE '%' || :query || '%' 
        ORDER BY date_updated DESC LIMIT (:page * :pageSize)
        """
    )
    fun searchBlogPostsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<BlogPost>

    @Query(
        """
        SELECT * FROM blog_post 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        OR username LIKE '%' || :query || '%' 
        ORDER BY date_updated  ASC LIMIT (:page * :pageSize)"""
    )
    fun searchBlogPostsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<BlogPost>

    @Query(
        """
        SELECT * FROM blog_post 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        OR username LIKE '%' || :query || '%' 
        ORDER BY username DESC LIMIT (:page * :pageSize)"""
    )
    fun searchBlogPostsOrderByAuthorDESC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<BlogPost>

    @Query(
        """
        SELECT * FROM blog_post 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        OR username LIKE '%' || :query || '%' 
        ORDER BY username  ASC LIMIT (:page * :pageSize)
        """
    )
    fun searchBlogPostsOrderByAuthorASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<BlogPost>


}






