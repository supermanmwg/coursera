package com.utils.apis;

import com.utils.rjos.CategoryCourseRjo;
import com.utils.rjos.CategoryRjo;
import com.utils.rjos.CourseDetailRjo;
import com.utils.rjos.CourseRjo;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by supermanmwg on 15-11-1.
 */
public interface ICourseApi {

    //get all categories
    @GET("/categories")
    void getAllCategory(
            Callback<CategoryRjo> callback
    );

    //get course ids
    @GET("/categories?includes=courses")
    void getCategoryCoursesId(
            @Query("id") int id,
            Callback<CategoryCourseRjo> callback
    );

    //get courses by ids
    @GET("/courses?fields=largeIcon,estimatedClassWorkload&includes=universities")
    void getCoursesByIds(
            @Query(value = "ids", encodeValue = false) String ids,
            Callback<CourseRjo> callback
    );

    //get detail course by id
    @GET("/courses?fields=language,photo,shortDescription,estimatedClassWorkload,instructor,aboutTheCourse")
    void getCourseById(
            @Query("id") int id,
            Callback<CourseDetailRjo> callback
    );

    //search course by key word
    @GET("/courses?q=search&fields=largeIcon,estimatedClassWorkload&includes=universities")
    void getCourseByKeyword(
            @Query("query") String query,
            Callback<CourseRjo> callback
    );
}
