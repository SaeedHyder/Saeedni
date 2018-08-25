package com.ingic.saeedni.retrofit;


import com.ingic.saeedni.entities.AllServicesEnt;
import com.ingic.saeedni.entities.CitiesEnt;
import com.ingic.saeedni.entities.JobRequestEnt;
import com.ingic.saeedni.entities.NewJobsEnt;
import com.ingic.saeedni.entities.NotificationEnt;
import com.ingic.saeedni.entities.RegistrationResultEnt;
import com.ingic.saeedni.entities.RequestEnt;
import com.ingic.saeedni.entities.ResponseWrapper;
import com.ingic.saeedni.entities.ServiceEnt;
import com.ingic.saeedni.entities.StaticPageEnt;
import com.ingic.saeedni.entities.TechInProgressEnt;
import com.ingic.saeedni.entities.UserComleteJobsEnt;
import com.ingic.saeedni.entities.UserInProgressEnt;
import com.ingic.saeedni.entities.countEnt;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface WebService {
    @Multipart
    @POST("user/register")
    Call<ResponseWrapper<RegistrationResultEnt>> registerUser(@Part("full_name") RequestBody userName,
                                                              @Part("email") RequestBody email,
                                                              @Part("city_id") RequestBody cityID,
                                                              @Part("phone_no") RequestBody UserPhone,
                                                              @Part("address") RequestBody address,
                                                              @Part("password") RequestBody password,
                                                              @Part("password_confirmation") RequestBody password_confirmation,
                                                              @Part MultipartBody.Part userprofileImage
    );

    @Multipart
    @POST("technician/register")
    Call<ResponseWrapper<RegistrationResultEnt>> registerTechnician(
            @Part("company_name") RequestBody company_name,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("phone_no") RequestBody phone_no,
            @Part("email") RequestBody email,
            @Part("category_id") RequestBody category_id,
            @Part("password") RequestBody password,
            @Part("password_confirmation") RequestBody password_confirmation,
            @Part("registration_type") RequestBody registration_type,
            @Part("registration_date") RequestBody registration_date,
            @Part("address") RequestBody address,
            @Part("city_id") RequestBody city_id,
            @Part("expiry_date") RequestBody expiry_date,
            @Part("device_token") RequestBody device_token,
            @Part("device_type") RequestBody device_type,
            @Part MultipartBody.Part profile_picture,
            @Part MultipartBody.Part trade_license
    );

    @FormUrlEncoded
    @POST("user/login")
    Call<ResponseWrapper<RegistrationResultEnt>> loginUser(@Field("email") String email,
                                                           @Field("password") String password,
                                                           @Field("device_token") String device_token,
                                                           @Field("device_type") String device_type);

    @FormUrlEncoded
    @POST("notification/updatedevicetoken")
    Call<ResponseWrapper> updateToken(@Field("user_id") String userid,
                                      @Field("device_type") String deviceType,
                                      @Field("device_token") String token);

    @FormUrlEncoded
    @POST("user/verifycode")
    Call<ResponseWrapper<RegistrationResultEnt>> verifyCode(@Field("user_id") String UserID,
                                                            @Field("code") String Code);

    @GET("user/getprofile")
    Call<ResponseWrapper<RegistrationResultEnt>> getUserProfile(@Query("user_id") String userID);

    @Multipart
    @POST("user/update")
    Call<ResponseWrapper<RegistrationResultEnt>> updateProfile(@Part("user_id") RequestBody userID,
                                                               @Part("full_name") RequestBody userFullname,
                                                               @Part("email") RequestBody useremail,
                                                               @Part("address") RequestBody useraddress,
                                                               @Part("full_address") RequestBody userfulladdress,
                                                               @Part("phone_no") RequestBody phone_number,
                                                               @Part MultipartBody.Part userprofileImage
    );

    @GET("cms")
    Call<ResponseWrapper<StaticPageEnt>> getTermandAbout(@Query("id") String userID, @Query("type") String type);

    @FormUrlEncoded
    @POST("technician/login")
    Call<ResponseWrapper<RegistrationResultEnt>> loginTechnician(@Field("email") String email,
                                                                 @Field("password") String password);

    @GET("notification/getnotifications")
    Call<ResponseWrapper<ArrayList<NotificationEnt>>> getNotification(@Query("user_id") String userID);

    @GET("allservice")
    Call<ResponseWrapper<ArrayList<ServiceEnt>>> getHomeServices();

    @GET("servicechild")
    Call<ResponseWrapper<ArrayList<ServiceEnt>>> getchildServices(@Query("parent_id") String parent_id);

    @GET("getCategory")
    Call<ResponseWrapper<ArrayList<ServiceEnt>>> getSubServices(@Query("service_id") String parent_id);

    @Multipart
    @POST("request/create")
    Call<ResponseWrapper<RequestEnt>> createRequest(@Part("user_id") RequestBody userID,
                                                    @Part("service_id") RequestBody service_id,
                                                    @Part("category_id") RequestBody category_id,
                                                    @Part("city_id") RequestBody city_id,
                                                    @Part("services_ids") RequestBody services_ids,
                                                    @Part("discription") RequestBody discription,
                                                    @Part("address") RequestBody address,
                                                    @Part("full_address") RequestBody full_address,
                                                    @Part("date") RequestBody date,
                                                    @Part("time") RequestBody time,
                                                    @Part("payment_type") RequestBody payment_type,
                                                    @Part("status") RequestBody status,
                                                    @Part ArrayList<MultipartBody.Part> images

    );

    @GET("request/userinprogress")
    Call<ResponseWrapper<ArrayList<UserInProgressEnt>>> getUserInprogress(@Query("user_id") String userID);

    @GET("request/usercompletejob")
    Call<ResponseWrapper<ArrayList<UserComleteJobsEnt>>> getUserCompleted(@Query("user_id") String userID);

    @Multipart
    @POST("request/editbyuser")
    Call<ResponseWrapper<RequestEnt>> editUserRequest(@Part("user_id") RequestBody userID,
                                                      @Part("request_id") RequestBody request_id,
                                                      @Part("service_id") RequestBody service_id,
                                                      @Part("category_id") RequestBody category_id,
                                                      @Part("services_ids") RequestBody services_ids,
                                                      @Part("discription") RequestBody discription,
                                                      @Part("address") RequestBody address,
                                                      @Part("full_address") RequestBody full_address,
                                                      @Part("date") RequestBody date,
                                                      @Part("time") RequestBody time,
                                                      @Part("payment_type") RequestBody payment_type,
                                                      @Part("status") RequestBody status,
                                                      @Part ArrayList<MultipartBody.Part> images,
                                                      @Part("image_ids") RequestBody deleteImages

    );

    @FormUrlEncoded
    @POST("request/status")
    Call<ResponseWrapper<RequestEnt>> changeStatus(@Field("user_id") String userID,
                                                   @Field("request_id") Integer RequestID,
                                                   @Field("message") String message,
                                                   @Field("status") Integer Status);


    @FormUrlEncoded
    @POST(" request/technicianrequeststatus")
    Call<ResponseWrapper<JobRequestEnt>> acceptJob(@Field("assign_id") Integer assign_id,
                                                   @Field("technician_id") String technician_id,
                                                   @Field("request_id") Integer request_id,
                                                   @Field("status") Integer status
    );

    @FormUrlEncoded
    @POST("request/technicianrequeststatus")
    Call<ResponseWrapper<JobRequestEnt>> rejectJob(@Field("assign_id") Integer assign_id,
                                                   @Field("technician_id") String technician_id,
                                                   @Field("request_id") Integer request_id,
                                                   @Field("status") Integer status,
                                                   @Field("message") String message
    );


    @FormUrlEncoded
    @POST("request/markcomplte")
    Call<ResponseWrapper<JobRequestEnt>> markComplete(@Field("assign_id") String assign_id,
                                                      @Field("technician_id") String technician_id,
                                                      @Field("request_id") String request_id,
                                                      @Field("finish") Integer finish);

    @GET("notification/getnotificationscount")
    Call<ResponseWrapper<countEnt>> getNotificationCount(
            @Query("user_id") String user_id);


    @GET("request/newjobs")
    Call<ResponseWrapper<ArrayList<NewJobsEnt>>> newJobs(
            @Query("technician_id") Integer technician_id);

    @GET("request/technicianinprogress")
    Call<ResponseWrapper<ArrayList<TechInProgressEnt>>> techInProgress(
            @Query("technician_id") Integer technician_id);

    @GET("request/techniciancompletejob")
    Call<ResponseWrapper<ArrayList<TechInProgressEnt>>> techCompleteJobs(
            @Query("technician_id") Integer technician_id);

    @GET("technician/getprofile")
    Call<ResponseWrapper<RegistrationResultEnt>> techProfile(
            @Query("technician_id") Integer technician_id);


    @FormUrlEncoded
    @POST("technician/feedback")
    Call<ResponseWrapper> sendFeedback(@Field("user_id") String user_id,
                                       @Field("request_id") String request_id,
                                       @Field("technician_id") String technician_id,
                                       @Field("rate") Integer rate,
                                       @Field("feedback") String feedback,
                                       @Field("tip_technician") String tip_technician
    );

    @FormUrlEncoded
    @POST("user/feedback")
    Call<ResponseWrapper> sendUserFeedback(@Field("user_id") String user_id,
                                           @Field("request_id") String request_id,
                                           @Field("technician_id") String technician_id,
                                           @Field("rate") Integer rate
    );

    @FormUrlEncoded
    @POST("request/editbytechnician")
    Call<ResponseWrapper> editTechJob(@Field("technician_id") String technician_id,
                                      @Field("service_id") String service_id,
                                      @Field("category_id") String category_id,
                                      @Field("request_id") String request_id,
                                      @Field("services_ids") String services_ids,
                                      @Field("discription") String discription,
                                      @Field("total") String total);

    @FormUrlEncoded
    @POST("request/addbytechnician")
    Call<ResponseWrapper> addTechJob(@Field("technician_id") String technician_id,
                                     @Field("service_id") String service_id,
                                     @Field("category_id") String category_id,
                                     @Field("parent_id") String parent_id,
                                     @Field("services_ids") String services_ids,
                                     @Field("discription") String discription,
                                     @Field("total") String total);

    @FormUrlEncoded
    @POST("user/logout")
    Call<ResponseWrapper> logoutTechnician(@Field("user_id") String userID);

    @FormUrlEncoded
    @POST("user/logout")
    Call<ResponseWrapper> logoutUser(@Field("user_id") String userID);

    @FormUrlEncoded
    @POST("user/forgotpassword")
    Call<ResponseWrapper> forgotPassword(@Field("email") String email);

    @GET("getCities")
    Call<ResponseWrapper<ArrayList<CitiesEnt>>> getAllCities();

    @GET("allservice")
    Call<ResponseWrapper<ArrayList<AllServicesEnt>>> getAllServices();
}