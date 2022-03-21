package com.likefirst.btos.data.remote.posting.dataSource

import com.likefirst.btos.data.module.PlantApiInterface
import com.likefirst.btos.data.remote.posting.MailboxApiInterface
import com.likefirst.btos.data.remote.posting.response.MailboxResponse
import retrofit2.Response
import javax.inject.Inject

class MailboxRemoteDataSource  @Inject constructor(val MailboxApi :MailboxApiInterface) {

    fun loadMailbox( id: Int) : Response<MailboxResponse>


}