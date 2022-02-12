package com.likefirst.btos.utils

import com.likefirst.btos.data.entities.*
import com.likefirst.btos.data.remote.users.response.GetProfileResponse
import com.likefirst.btos.data.remote.users.response.LoginResponse
import com.likefirst.btos.data.remote.BaseResponse
import com.likefirst.btos.data.remote.plant.response.PlantRequest
import com.likefirst.btos.data.remote.plant.response.PlantResponse
import com.likefirst.btos.data.remote.posting.response.*
import com.likefirst.btos.data.remote.posting.response.LetterResponse
import com.likefirst.btos.data.remote.viewer.response.ArchiveCalendar
import com.likefirst.btos.data.remote.viewer.response.ArchiveDiaryResult
import com.likefirst.btos.data.remote.viewer.response.ArchiveList
import com.likefirst.btos.data.remote.viewer.response.UpdateDiaryRequest
import retrofit2.Call
import retrofit2.http.*


interface RetrofitInterface {

    // ------------------- UserAuth -------------------------- //
    @POST("/auth/google")
    fun login(@Body email: String) : Call<LoginResponse>

    @GET("/auth/jwt")
    fun autoLogin() : Call<LoginResponse>
    //@Header("x-access-token") jwt: String
    @POST("/users")
    fun signUp(@Body user: UserSign) : Call<LoginResponse>

    @GET("/users/{useridx}")
    fun getProfile(@Path("useridx") useridx: Int): Call<GetProfileResponse>

    @PATCH("/users/{userIdx}/sad")
    fun updateIsSad(
        @Path("userIdx") userIdx: Int,
        @Body isSad : UserIsSad
    ) : Call<BaseResponse<String>>


    // -------------------Mailbox -------------------------- //
    @GET("/mailboxes/{userId}")
    fun loadMailbox(
        @Path("userId") id: String
    ): Call<MailboxResponse>

    @GET("/mailboxes/mail")
    fun loadDiary(
        @Query("type") type: String,
        @Query("idx")idx: String
    ): Call<DiaryResponse>

    @GET("/mailboxes/mail")
    fun loadLetter(
        @Query("type") type: String,
        @Query("idx")idx: String
    ): Call<LetterResponse>


    @GET("/mailboxes/mail")
    fun loadReply(
        @Query("type") type: String,
        @Query("idx")idx: String
    ): Call<ReplyResponse>

    // -------------------PlantList-------------------------- //

    @GET("/plants/{userId}")
    fun loadPlantList(
        @Path("userId") id: String
    ): Call<PlantResponse>

    @PATCH("/plants/select")
    fun selectPlant(
        @Body PlantSelectRequest : PlantRequest
    ): Call<PlantResponse>

    @POST("/plants/buy")
    fun buyPlant(
        @Body PlantBuyRequest : PlantRequest
    ): Call<PlantResponse>

    @POST("/plants/{userId}/initialize")
    fun initPlant(
        @Path ("userId") userIdx : String
    ): Call<PlantResponse>

    // ---------------- Archive Calendar ------------------ //
    @GET("/archives/calendar/{userIdx}/{date}")
    fun getCalendar(
        @Path("userIdx") userIdx: Int,
        @Path("date") date : String,
        @Query("type") type : String
    ) : Call<BaseResponse<ArrayList<ArchiveCalendar>>>

    @POST("/diaries")
    fun postDiary(
        @Body postDiaryRequest : PostDiaryRequest
    ) : Call<BaseResponse<PostDiaryResponse>>

    @GET("/archives/{diaryIdx}")
    fun getDiary(
        @Path("diaryIdx") diaryIdx : Int
        ) : Call<BaseResponse<ArchiveDiaryResult>>

    // ---------------- Archive List ----------------- //
    @GET("/archives/diaryList/{userIdx}/{pageNum}")
    fun getArchiveList(
        @Path("userIdx") userIdx : Int,
        @Path("pageNum") pageNum : Int,
        @QueryMap search : Map<String, String>?
    ) : Call<ArchiveList>

    @PUT("/diaries")
    fun updateDiary(
        @Body updateRequest : UpdateDiaryRequest
    ) : Call<BaseResponse<String>>
    // ------------------- SettingUser -------------------------- //
    @PATCH("/users/{userIdx}/nickname")
    fun setName(
        @Path("userIdx") userIdx : Int,
        @Body nickName : UserName
    ) : Call<BaseResponse<String>>

    @PATCH("/users/{userIdx}/birth")
    fun setBirth(
        @Path("userIdx") userIdx : Int,
        @Body birth : UserBirth
    ): Call<BaseResponse<String>>

    @PATCH("/users/{userIdx}/receive/others")
    fun setNotificationOther(
        @Path("userIdx") userIdx : Int,
        @Body recOthers : Boolean
    ): Call<BaseResponse<String>>

    @PATCH("/users/{userIdx}/receive/age")
    fun setNotificationAge(
        @Path("userIdx") userIdx : Int,
        @Body recSimilarAge : Boolean
    ): Call<BaseResponse<String>>

    @PATCH("/users/{userIdx}/push-alarm")
    fun setPushAlarm(
        @Path("userIdx") userIdx : Int,
        @Body pushAlarm : Boolean
    ): Call<BaseResponse<String>>

    @PATCH("/users/{userIdx}/font")
    fun setFont(
        @Path("userIdx") userIdx : Int,
        @Body fontIdx : Int
    ): Call<BaseResponse<String>>

    // ------------------- History -------------------------- //
//    @GET("/histories/list/{userIdx}/{pageNum}?filtering=&search=")
//    fun historyListSender(
//        @Path("userIdx") userIdx : Int,
//        @Path("pageNum") pageNum : Int,
//        @Query("filtering") filtering : String,
//        @Query("search") search : String?
//    ) : Call<BaseResponse<SenderHistory>>
//
//    @GET("/histories/list/{userIdx}/{pageNum}?filtering=&search=")
//    fun historyListDiary(
//        @Path("userIdx") userIdx : Int,
//        @Path("pageNum") pageNum : Int,
//        @Query("filtering") filtering : String,
//        @Query("search") search : String?
//    ) : Call<BaseResponse<DiaryHistory>>
//
//    @GET("/histories/list/{userIdx}/{pageNum}?filtering=&search=")
//    fun historyListLetter(
//        @Path("userIdx") userIdx : Int,
//        @Path("pageNum") pageNum : Int,
//        @Query("filtering") filtering : String,
//        @Query("search") search : String?
//    ) : Call<BaseResponse<LetterHistory>>
}