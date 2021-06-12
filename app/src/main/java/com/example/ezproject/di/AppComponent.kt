package com.example.ezproject.di

import com.example.ezproject.ui.activity.auth.forget.ForgetPasswrodActivity
import com.example.ezproject.ui.activity.auth.login.LoginActivity
import com.example.ezproject.ui.activity.auth.register.RegisterActivity
import com.example.ezproject.ui.activity.auth.splash.SplashActivity
import com.example.ezproject.ui.fragment.AllCorporatesFragment
import com.example.ezproject.ui.fragment.AllFilteredHotelsFragment
import com.example.ezproject.ui.fragment.AllHotelssFragment
import com.example.ezproject.ui.fragment.chat.chatmessages.ChatMessagesFragment
import com.example.ezproject.ui.fragment.chat.chatrooms.ChatRoomsFragment
import com.example.ezproject.ui.fragment.main.MainFragment
import com.example.ezproject.ui.fragment.payment.PayInFragment
import com.example.ezproject.ui.fragment.payment.PayoutFragment
import com.example.ezproject.ui.fragment.profile.*
import com.example.ezproject.ui.fragment.profile.booking.BookingDetailsFragment
import com.example.ezproject.ui.fragment.profile.booking.UserBookingFragment
import com.example.ezproject.ui.fragment.profile.host.BecomeHostFragment
import com.example.ezproject.ui.fragment.profile.notification.NotificationFragment
import com.example.ezproject.ui.fragment.profile.reviews.Cancelation_Policy_Dialog
import com.example.ezproject.ui.fragment.profile.reviews.UserReviewsFragment
import com.example.ezproject.ui.fragment.reviews.add.AddReviewFragment
import com.example.ezproject.ui.fragment.unit.add.*
import com.example.ezproject.ui.fragment.unit.filter.AllUnitsFragment
import com.example.ezproject.ui.fragment.unit.details.UnitDetailsFragment
import com.example.ezproject.ui.fragment.unit.favourite.FavouriteFragment
import com.example.ezproject.ui.fragment.unit.filter.FilterDialogFragment
import com.example.ezproject.ui.fragment.unit.filter.FilterHotelsDialog
import com.example.ezproject.ui.fragment.unit.host.HostProfileFragment
import com.example.ezproject.ui.fragment.unit.reservation.ReservationSummaryBottomDialog
import com.example.ezproject.ui.fragment.unit.reservation.SelectPaymentMethod
import com.example.ezproject.ui.fragment.unit.reviews.UnitReviewFragment
import dagger.Component

@Component(
    modules = [
        NetworkModule::class,
        StorageModule::class,
        ViewModelFactoryModule::class
    ]
)
interface AppComponent {

    fun inject(loginActivity: LoginActivity)
    fun inject(registerActivity: RegisterActivity)
    fun inject(forgetPasswrodActivity: ForgetPasswrodActivity)
    fun inject(mainFragment: MainFragment)
    fun inject(splashActivity: SplashActivity)
    fun inject(unitDetailsFragment: UnitDetailsFragment)
    fun inject(chatMessagesFragment: ChatMessagesFragment)
    fun inject(chatRoomsFragment: ChatRoomsFragment)
    fun inject(allUnitsFragment: AllUnitsFragment)
    fun inject(favouriteFragment: FavouriteFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(notificationFragment: NotificationFragment)
    fun inject(filterDialogFragment: FilterDialogFragment)
    fun inject(editProfileFragment: EditProfileFragment)
    fun inject(editPasswordFragment: EditPasswordFragment)
    fun inject(unitReviewFragment: UnitReviewFragment)
    fun inject(hostProfileFragment: HostProfileFragment)
    fun inject(userUnitsFragment: UserUnitsFragment)
    fun inject(reservationSummaryBottomDialog: ReservationSummaryBottomDialog)
    fun inject(userBookingFragment: UserBookingFragment)
    fun inject(basicInfoFragment: BasicInfoFragment)
    fun inject(legalPaperFragment: AddUnitLegalPaperFragment)
    fun inject(unitAvailabilityFragment: UnitAvailabilityFragment)
    fun inject(unitOptionsFragment: UnitOptionsFragment)
    fun inject(pickUnitLocationFragment: PickUnitLocationFragment)
    fun inject(pickUnitImagesFragment: PickUnitImagesFragment)
    fun inject(unitRulesFragment: UnitRulesFragment)
    fun inject(addUnitPricingFragment: AddUnitPricingFragment)
    fun inject(payInFragment: PayInFragment)
    fun inject(payoutFragment: PayoutFragment)
    fun inject(becomeHostFragment: BecomeHostFragment)
    fun inject(bookingDetailsFragment: BookingDetailsFragment)
    fun inject(validatePhoneVerificationCodeDialog: ValidatePhoneVerificationCodeDialog)
    fun inject(addReviewFragment: AddReviewFragment)
    fun inject(userReviewsFragment: UserReviewsFragment)
    fun inject(cancelationPolicyDialog: Cancelation_Policy_Dialog)
    fun inject(selectpaymentmethod: SelectPaymentMethod)
    fun inject(wallet: Wallet)
    fun inject(allHotelssFragment: AllHotelssFragment)
    fun inject(filterHotelsDialog: FilterHotelsDialog)
    fun inject(allCorporatesFragment: AllCorporatesFragment)
    abstract fun inject(allFilteredHotelsFragment: AllFilteredHotelsFragment)


}