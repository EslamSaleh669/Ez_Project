package com.example.ezproject.data.models

import com.google.gson.annotations.SerializedName
import java.io.File


data class User(
	@field:SerializedName("add_by")
	var addBy: String? = null,

	@field:SerializedName("loyalty_account")
	val loyaltyAccount: LoyaltyAccount,

	@field:SerializedName("city")
	var city: String,

	@SerializedName("verify_percent")
	var verifPercent: Int,

	@field:SerializedName("mobile_verified_at")
	val mobileVerifiedAt: String,

	@field:SerializedName("roles")
	val roles: List<Any?>,

	@field:SerializedName("photoid")
	var photoid: String,

	@field:SerializedName("description")
	var description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("percent")
	val percent: Int,

	@field:SerializedName("corporatePercent")
	val corporatePercent: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("contractImage")
	val contractImage: Any,

	@field:SerializedName("taxID")
	val taxID: Int,

	@field:SerializedName("billingCountry")
	val billingCountry: String,

	@field:SerializedName("loyalty_account_user")
	val loyaltyAccountUser: LoyaltyAccountUser? = null,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("billingName")
	val billingName: String,

	@field:SerializedName("lang")
	var lang: String,

	@field:SerializedName("email")
	var email: String,

	@field:SerializedName("billingBank")
	val billingBank: Any,

	@field:SerializedName("crImage")
	val crImage: Any,

	@field:SerializedName("mobile")
	var mobile: String,

	@field:SerializedName("region_id")
	var regionId: Int,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: String,

	@field:SerializedName("avatar")
	val avatar: String,

	@field:SerializedName("time_zone")
	val timeZone: Any,

	@field:SerializedName("rate_score")
	val rateScore: Float,

	@field:SerializedName("accept")
	val accept: Int,

	@field:SerializedName("token")
	var token: String,

	@field:SerializedName("rate_count")
	val rateCount: Int,

	@field:SerializedName("parent_id")
	val parentId: Int,

	@field:SerializedName("device_token")
	val deviceToken: String,

	@field:SerializedName("name")
	var name: String,

	@field:SerializedName("billingBranch")
	val billingBranch: Any,

	@field:SerializedName("photoid_verified_at")
	val photoidVerifiedAt: String,

	@field:SerializedName("billingIBAN")
	val billingIBAN: Any,

	@field:SerializedName("status")
	val status: Int,

	var imageFile: File?
)

data class LoyaltyAccountUser(

	@field:SerializedName("loyalty_account")
	val loyaltyAccount: LoyaltyAccount ,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("max")
	val max: Int? = null,

	@field:SerializedName("level")
	val level: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("loyalty_account_id")
	val loyaltyAccountId: Int? = null,

	@field:SerializedName("wallet_current_balance")
	val walletCurrentBalance: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("loyalty_users_group_id")
	val loyaltyUsersGroupId: Int? = null
)

data class LoyaltyAccount(

	@field:SerializedName("assign_user_id")
	val assignUserId: Any? = null,

	@field:SerializedName("industry_id")
	val industryId: Int? = null,

	@field:SerializedName("level")
	val level: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("active_contract")
	val activeContract: ActiveContract? = null,

	@field:SerializedName("tax")
	val tax: String? = null,

	@field:SerializedName("tax_reviewed")
	val taxReviewed: Int? = null,

	@field:SerializedName("uuid")
	val uuid: String? = null,

	@field:SerializedName("cr")
	val cr: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("logo_reviewed")
	val logoReviewed: Int? = null,

	@field:SerializedName("company_name")
	val companyName: String? = null,

	@field:SerializedName("logo")
	val logo: String? = null,

	@field:SerializedName("company_url")
	val companyUrl: String? = null,

	@field:SerializedName("currency")
	val currency: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("cr_reviewed")
	val crReviewed: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ActiveContract(

	@field:SerializedName("active_program")
	val activeProgram: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("contract_level")
	val contractLevel: Int? = null,

	@field:SerializedName("contract")
	val contract: String? = null,

	@field:SerializedName("contract_status")
	val contractStatus: String? = null,

	@field:SerializedName("level_date")
	val levelDate: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("loyalty_account_id")
	val loyaltyAccountId: Int? = null
)


















//	@SerializedName("accept")
//	var accept: Int,
//	@SerializedName("add_by")
//	var addBy: String,
//	@SerializedName("avatar")
//	var avatar: String,
//	@SerializedName("city")
//	var city: String,
//	@SerializedName("created_at")
//	var createdAt: String,
//	@SerializedName("description")
//	var description: String,
//	@SerializedName("email")
//	var email: String,
//	@SerializedName("email_verified_at")
//	var emailVerifiedAt: String?,
//	@SerializedName("id")
//	var id: Int,
//	@SerializedName("lang")
//	var lang: String,
//	@SerializedName("mobile")
//	var mobile: String,
//	@SerializedName("mobile_verified_at")
//	var mobileVerifiedAt: String?,
//	@SerializedName("name")
//	var name: String,
//	@SerializedName("percent")
//	var percent: Int,
//	@SerializedName("verify_percent")
//	var verifPercent: Int,
//	@SerializedName("photoid")
//	var photoid: String?,
//	@SerializedName("photoid_verified_at")
//	var photoidVerifiedAt: String?,
//	@SerializedName("rate_count")
//	var rateCount: Int,
//	@SerializedName("rate_score")
//	var rateScore: Float,
//	@SerializedName("status")
//	var status: Int,
//	@SerializedName("token")
//	var token: String,
//	@SerializedName("updated_at")
//	var updatedAt: String,
//	var imageFile: File?